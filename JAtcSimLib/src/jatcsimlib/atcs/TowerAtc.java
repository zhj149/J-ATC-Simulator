/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.atcs;

import jatcsimlib.Acc;
import jatcsimlib.airplanes.Airplane;
import jatcsimlib.airplanes.AirplaneList;
import jatcsimlib.coordinates.Coordinates;
import jatcsimlib.exceptions.ENotSupportedException;
import jatcsimlib.exceptions.ERuntimeException;
import jatcsimlib.global.ETime;
import jatcsimlib.global.Headings;
import jatcsimlib.messaging.Message;
import jatcsimlib.messaging.StringMessageContent;
import jatcsimlib.speaking.fromAirplane.notifications.GoingAroundNotification;
import jatcsimlib.speaking.fromAirplane.notifications.GoodDayNotification;
import jatcsimlib.speaking.fromAtc.commands.ClearedForTakeoffCommand;
import jatcsimlib.speaking.fromAtc.notifications.RadarContactConfirmationNotification;
import jatcsimlib.weathers.Weather;
import jatcsimlib.world.Runway;
import jatcsimlib.world.RunwayThreshold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marek
 */
public class TowerAtc extends ComputerAtc {

  private static final int RUNWAY_CHANGE_INFO_UPDATE_INTERVAL = 10 * 60;
  private static final int MAXIMAL_SPEED_FOR_PREFERRED_RUNWAY = 5;
  private final AirplaneList readyForTakeoff = new AirplaneList();
  private final AirplaneList departings = new AirplaneList();
  private final LastDepartures lastDepartures = new LastDepartures();
  private RunwayChangeInfo runwayChangeInfo = null;
  private RunwayThreshold runwayThresholdInUse = null;

  public TowerAtc(AtcTemplate template) {
    super(template);
  }

  public static RunwayThreshold getSuggestedThreshold() {
    Weather w = Acc.weather();

    RunwayThreshold rt = null;

    if (w.getWindSpeetInKts() <= MAXIMAL_SPEED_FOR_PREFERRED_RUNWAY) {
      for (Runway r : Acc.airport().getRunways()) {
        if (r.isActive() == false) {
          continue; // skip inactive runways
        }
        for (RunwayThreshold t : r.getThresholds()) {
          if (t.isPreferred()) {
            rt = t;
            break;
          }
        }
        if (rt != null) {
          break;
        }
      }
    }

    int diff = Integer.MAX_VALUE;
    if (rt == null) {
      // select runway according to wind
      for (Runway r : Acc.airport().getRunways()) {
        for (RunwayThreshold t : r.getThresholds()) {
          int localDiff = Headings.getDifference(w.getWindHeading(), (int) t.getCourse(), true);
          if (localDiff < diff) {
            diff = localDiff;
            rt = t;
          }
        }
      }
    }

    return rt;
  }

  public RunwayThreshold getRunwayThresholdInUse() {
    return runwayThresholdInUse;
  }

  @Override
  protected void _elapseSecond() {
    List<Message> msgs = Acc.messenger().getByTarget(this, true);

    esRequestPlaneSwitchFromApp();

    for (Message m : msgs) {
      recorder.logMessage(m); // incoming speech

      if (m.isSourceOfType(Airplane.class)) {
        if (m.getContent() instanceof GoingAroundNotification) {
          // predavame na APP
          Airplane plane = m.getSource();
          super.requestSwitch(plane);
          waitingRequestsList.add(plane);
        } else if (m.getContent() instanceof GoodDayNotification) {
          Message msg = new Message(
              this,
              m.<Airplane>getSource(),
              new RadarContactConfirmationNotification());
          Acc.messenger().send(msg);
          recorder.logMessage(msg);
        }
        continue;
      } else if (m.getSource() != Acc.atcApp()) {
        continue;
      }

      Airplane p = m.<PlaneSwitchMessage>getContent().plane;
      if (waitingRequestsList.contains(p) == false) {
        p = null;
      }

      if (p == null) {
        // APP -> TWR, muze?
        p = m.<PlaneSwitchMessage>getContent().plane;
        if (canIAcceptFromApp(p)) {
          super.confirmSwitch(p);
          super.approveSwitch(p);
        } else {
          super.refuseSwitch(p);
        }

      } else {
        // potvrzene od APP, TWR predava na APP
        waitingRequestsList.remove(p);
        readyForTakeoff.add(p);
      }
    }

    processReadyForTakeoff();
    switchDepartings();

    // runway change
    checkForRunwayChange();
  }

  private void processReadyForTakeoff() {
    if (readyForTakeoff.isEmpty() == false && canTakeOffNextPlane()) {
      Airplane p = readyForTakeoff.get(0);
      readyForTakeoff.remove(0);
      departings.add(p);
      lastDepartures.set(runwayThresholdInUse, p, Acc.now());

      Message m;

      m = new Message(this, p,
          new RadarContactConfirmationNotification());
      Acc.messenger().send(m);

      m = new Message(this, p,
          new ClearedForTakeoffCommand(runwayThresholdInUse));
      Acc.messenger().send(m);
      recorder.logMessage(m);
    }
  }

  private boolean canTakeOffNextPlane() {
    for (Airplane p : getPrm().getPlanes(this)) {
      double dst = Coordinates.getDistanceInNM(p.getCoordinate(), Acc.threshold().getCoordinate());
      if (p.isArrival() && dst < 5) { // ... with distance closer than 5nm
        return false;
      }
    } // for

    LastDeparture ld = lastDepartures.get(runwayThresholdInUse);
    if (ld == null) {
      return true;
    }

    TakeOffSeparation tos = TakeOffSeparation.create(
        ld.plane.getType().category,
        this.readyForTakeoff.get(0).getType().category);

    if (ld.time.addSeconds(tos.seconds).isAfter(Acc.now())) {
      return false; // there are too close due to time
    }
    if (Coordinates.getDistanceInNM(ld.plane.getCoordinate(), this.readyForTakeoff.get(0).getCoordinate()) < tos.nm) {
      return false; // they are too close due to distance
    }

    final int MIN_ALTITUDE_FOR_NEXT = 300;
    if (Acc.airport().getAltitude() + MIN_ALTITUDE_FOR_NEXT > ld.plane.getAltitude()) {
      return false; // the first one is not high enough
    }

    return true;
  }

  private boolean canIAcceptFromApp(Airplane p) {
    if (p.isDeparture()) {
      return false;
    }
    if (Acc.atcApp().isControllingAirplane(p) == false) {
      return false;
    }
    if (p.getAltitude() > super.acceptAltitude) {
      return false;
    }
    double dist = Coordinates.getDistanceInNM(p.getCoordinate(), Acc.airport().getLocation());
    if (dist > 15) {
      return false;
    }

    return true;
  }

  private void esRequestPlaneSwitchFromApp() {
    Message m;

    // TWR -> APP, zadost na APP
    AirplaneList plns = getPlanesReadyForApp();
    for (Airplane p : plns) {
      // todo PlaneSwitchMessage are only several types, some enum should be used instead of fixed string message
      m = new Message(this, Acc.atcApp(),
          new PlaneSwitchMessage(p, " to you"));
      Acc.messenger().send(m);
      recorder.logMessage(m);

      getPrm().requestSwitch(this, Acc.atcApp(), p);

      waitingRequestsList.add(p);
    }
    // opakovani starych zadosti
    List<Airplane> awaitings = waitingRequestsList.getAwaitings();
    for (Airplane p : awaitings) {
      m = new Message(this, Acc.atcApp(),
          new PlaneSwitchMessage(p, " to you (repeated)"));
      Acc.messenger().send(m);
      recorder.logMessage(m);
    }
  }

  private AirplaneList getPlanesReadyForApp() {
    AirplaneList ret = new AirplaneList();

    for (Airplane p : getPrm().getPlanes(this)) {
      if (p.isArrival()) {
        continue;
      }
      if (getPrm().isAskedToSwitch(p)) {
        continue;
      }

      ret.add(p);
    }

    return ret;
  }

  private void switchDepartings() {
    AirplaneList tmp = new AirplaneList();
    for (Airplane p : departings) {
      if (p.getAltitude() > Acc.airport().getAltitude()) {
        tmp.add(p);
      }
    }

    for (Airplane p : tmp) {
      super.approveSwitch(p);
      departings.remove(p);
    }
  }

  private void checkForRunwayChange() {
    if (Acc.now().getTotalSeconds() % RUNWAY_CHANGE_INFO_UPDATE_INTERVAL == 0) {
      RunwayThreshold suggestedThreshold = getSuggestedThreshold();
      if (suggestedThreshold != Acc.threshold()) {
        RunwayChangeInfo rci = new RunwayChangeInfo(suggestedThreshold, Acc.now().addSeconds(15 * 60)); // change in 15 minutes
        if (this.runwayChangeInfo == null || this.runwayChangeInfo.newRunwayThreshold != suggestedThreshold) {
          this.runwayChangeInfo = rci;
          Message m = new Message(
              this,
              Acc.atcApp(),
              new StringMessageContent(
                  "Expect change to runway %s at %s.", rci.newRunwayThreshold.getName(), rci.changeTime.toString()));
          Acc.messenger().send(m);
          recorder.logMessage(m);
        }
      }
    } else if (this.runwayChangeInfo != null && this.runwayChangeInfo.changeTime.isBefore(Acc.now())) {
      changeRunwayInUse(this.runwayChangeInfo.newRunwayThreshold);
      this.runwayChangeInfo = null;
    }
  }

  @Override
  public void init() {
    RunwayThreshold suggestedThreshold = getSuggestedThreshold();
    changeRunwayInUse(suggestedThreshold);
  }

  @Override
  protected void _registerNewPlane(Airplane plane) {
    if (plane.isArrival()) {
      throw new ERuntimeException("Arriving plane cannot be registered using this method.");
    }
  }

  private void changeRunwayInUse(RunwayThreshold newRunwayInUseThreshold) {
    Message m = new Message(
        this,
        Acc.atcApp(),
        new StringMessageContent("Runway in use %s from now.", newRunwayInUseThreshold.getName()));
    Acc.messenger().send(m);
    recorder.logMessage(m);
    this.runwayThresholdInUse = newRunwayInUseThreshold;
  }

}

class RunwayChangeInfo {

  public final RunwayThreshold newRunwayThreshold;
  public final ETime changeTime;

  public RunwayChangeInfo(RunwayThreshold newRunwayThreshold, ETime changeTime) {
    this.newRunwayThreshold = newRunwayThreshold;
    this.changeTime = changeTime;
  }
}

class TakeOffSeparation {

  private final static int[][] sepDistanceNm = new int[][]{
      new int[]{4, 5, 6, 6}, // A
      new int[]{0, 3, 3, 5}, // B
      new int[]{0, 0, 3, 4}, // C
      new int[]{0, 0, 0, 0}}; // D
  private final static int[][] sepTimeSeconds = new int[][]{
      new int[]{120, 180, 180, 180}, // A
      new int[]{0, 120, 120, 120}, // B
      new int[]{0, 0, 120, 120}, // C
      new int[]{0, 0, 0, 120}}; // D
  /**
   * Increase frequency of departures. Higher means higher frequency of take-offs.
   */
  private final static double DEPARTURE_ACCELERATOR_DIVIDER = 1.3;
  public final int nm;
  public final int seconds;

  private TakeOffSeparation(int nm, int seconds) {
    this.nm = nm;
    this.seconds = seconds;
  }

  private static int c2i(char c) {
    switch (c) {
      case 'A':
        return 0;
      case 'B':
        return 1;
      case 'C':
        return 2;
      case 'D':
        return 3;
      default:
        throw new ENotSupportedException("Unknown plane type category " + c);
    }
  }

  public static TakeOffSeparation create(char leadingTypeCategory, char followingTypeCategory) {
    int a = c2i(leadingTypeCategory);
    int b = c2i(followingTypeCategory);
    int time = sepTimeSeconds[a][b];
    int dist = sepDistanceNm[a][b];
    time = (int) (time * DEPARTURE_ACCELERATOR_DIVIDER);
    dist = (int) (dist * DEPARTURE_ACCELERATOR_DIVIDER);

    return new TakeOffSeparation(dist, time);
  }
}

class LastDeparture {

  public final Airplane plane;
  public final ETime time;

  public LastDeparture(Airplane plane, ETime time) {
    this.plane = plane;
    this.time = time;
  }
}

class LastDepartures {

  private final Map<RunwayThreshold, LastDeparture> inner = new HashMap<>();

  public void set(RunwayThreshold runwayThreshold, LastDeparture departure) {
    inner.put(runwayThreshold, departure);
  }

  public void set(RunwayThreshold runwayThreshold, Airplane plane, ETime time) {
    this.set(runwayThreshold, new LastDeparture(plane, time.clone()));
  }

  public LastDeparture get(RunwayThreshold runwayThreshold) {
    if (inner.containsKey(runwayThreshold)) {
      return inner.get(runwayThreshold);
    } else {
      return null;
    }
  }
}
