/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.airplanes;

import jatcsimlib.Acc;
import jatcsimlib.airplanes.pilots.Pilot;
import jatcsimlib.atcs.Atc;
import jatcsimlib.coordinates.Coordinate;
import jatcsimlib.coordinates.Coordinates;
import jatcsimlib.exceptions.ERuntimeException;
import jatcsimlib.global.Headings;
import jatcsimlib.global.KeyItem;
import jatcsimlib.messaging.IMessageContent;
import jatcsimlib.messaging.IMessageParticipant;
import jatcsimlib.messaging.Message;
import jatcsimlib.speaking.IFromAtc;
import jatcsimlib.speaking.ISpeech;
import jatcsimlib.speaking.SpeechList;
import jatcsimlib.speaking.fromAtc.IAtcCommand;
import jatcsimlib.speaking.fromAtc.commands.ChangeHeadingCommand;
import jatcsimlib.world.Navaid;

import java.util.List;

/**
 * @author Marek
 */
public class Airplane implements KeyItem<Callsign>, IMessageParticipant {

  public class AirplaneInfo {

    private boolean airprox = false;

    public Coordinate coordinate() {
      return Airplane.this.coordinate;
    }

    public Callsign callsign() {
      return Airplane.this.callsign;
    }

    public String callsignS() {
      return Airplane.this.callsign.toString();
    }

    public String callsignCompany() {
      return Airplane.this.callsign.getCompany();
    }

    public String callsignNumber() {
      return Airplane.this.callsign.getNumber();
    }

    public String sqwkS() {
      return Airplane.this.sqwk.toString();
    }

    public Atc tunedAtc() {
      return pilot.getTunedAtc();
    }

    public Atc responsibleAtc() {
      return Acc.prm().getResponsibleAtc(Airplane.this);
    }

    public int altitude() {
      return Airplane.this.altitude;
    }

    public String altitudeSLong() {
      return Acc.toAltS(Airplane.this.altitude, true);
    }

    public String altitudeSInFtShort() {
      return Integer.toString(Airplane.this.altitude);
    }

    public String targetAltitudeSInFtShort() {
      return Integer.toString(Airplane.this.targetAltitude);
    }

    public String altitudeSShort() {
      return Integer.toString(Airplane.this.altitude / 100);
    }

    public String altitudeSFixed() {
      return String.format("%1$03d", Airplane.this.altitude / 100);
    }

    public String targetAltitudeSFixed() {
      return String.format("%1$03d", Airplane.this.targetAltitude / 100);
    }

    public String targetAltitudeSLong() {
      return Acc.toAltS(Airplane.this.targetAltitude, true);
    }

    public String targetAltitudeSShort() {
      return Integer.toString(Airplane.this.targetAltitude / 100);
    }

    public String climbDescendChar() {
      if (Airplane.this.targetAltitude > Airplane.this.altitude) {
        return "↑"; //"▲";
      } else if (Airplane.this.targetAltitude < Airplane.this.altitude) {
        return "↓"; // "▼";
      } else {
        return "=";
      }
    }

    public String departureArrivalChar() {
      if (isDeparture()) {
        return "▲"; //"↑"; //
      } else {
        return "▼"; // "↓";
      }
    }

    public int heading() {
      return Airplane.this.heading;
    }

    public String headingSLong() {
      return String.format("%1$03d", Airplane.this.heading);
    }

    public String headingSShort() {
      return Integer.toString(Airplane.this.heading);
    }

    public String targetHeadingSLong() {
      return String.format("%1$03d", Airplane.this.targetHeading);
    }

    public String targetHeadingSShort() {
      return Integer.toString(Airplane.this.targetHeading);
    }

    public int speed() {
      return Airplane.this.speed;
    }

    public String speedSLong() {
      return Airplane.this.speed + " kt";
    }

    public String speedSShort() {
      return Integer.toString(Airplane.this.speed);
    }

    public String speedSShortAligned() {
      return String.format("%1# 3d", Airplane.this.speed);
    }

    public int targetSpeed() {
      return Airplane.this.targetSpeed;
    }

    public String targetSpeedSLong() {
      return Airplane.this.targetSpeed + " kt";
    }

    public String targetSpeedSShort() {
      return Integer.toString(Airplane.this.targetSpeed);
    }

    public String planeAtcName() {
      return tunedAtc().getName();
    }

    public Atc.eType planeAtcType() {
      return tunedAtc().getType();
    }

    public String responsibleAtcName() {
      return responsibleAtc().getName();
    }

    public Atc.eType responsibleAtcType() {
      return responsibleAtc().getType();
    }

    public String planeType() {
      return Airplane.this.airplaneType.name;
    }

    public int verticalSpeed() {
      return Airplane.this.lastVerticalSpeed;
    }

    public String verticalSpeedSLong() {
      return Airplane.this.lastVerticalSpeed + " ft/m";
    }

    public String verticalSpeedSShort() {
      return Integer.toString(Airplane.this.lastVerticalSpeed);
    }

    public String format(String pattern) {
      StringBuilder sb = new StringBuilder(pattern);
      int[] p = new int[2];

      while (true) {
        updatePair(sb, p);
        if (p[0] < 0) {
          break;
        }

        String tmp = sb.substring(p[0] + 1, p[1]);
        int index = Integer.parseInt(tmp);
        sb.replace(p[0], p[1] + 1, getFormatValueByIndex(index));
      }

      return sb.toString();
    }

    public String typeName() {
      return Airplane.this.airplaneType.name;
    }

    public String typeCategory() {
      return Character.toString(Airplane.this.airplaneType.category);
    }

    private void updatePair(StringBuilder ret, int[] p) {
      int start = ret.indexOf("{");
      if (start < 0) {
        p[0] = -1;
        return;
      }
      p[0] = start;
      int end = ret.indexOf("}", start);
      p[1] = end;
    }

    private String getFormatValueByIndex(int index) {
      switch (index) {
        case 1:
          return this.callsignS();
        case 2:
          return this.callsignCompany();
        case 3:
          return this.callsignNumber();
        case 4:
          return this.typeName();
        case 5:
          return this.typeCategory();
        case 8:
          return this.sqwkS();
        case 11:
          return this.headingSLong();
        case 12:
          return this.headingSShort();
        case 15:
          return this.targetHeadingSLong();
        case 16:
          return this.targetHeadingSShort();
        case 21:
          return this.speedSLong();
        case 22:
          return this.speedSShort();
        case 23:
          return this.speedSShortAligned();
        case 31:
          return this.targetSpeedSLong();
        case 32:
          return this.targetSpeedSShort();
        case 33:
          return this.altitudeSLong();
        case 34:
          return this.altitudeSShort();
        case 35:
          return this.altitudeSInFtShort();
        case 36:
          return this.targetAltitudeSLong();
        case 37:
          return this.targetAltitudeSShort();
        case 38:
          return this.targetAltitudeSInFtShort();
        case 41:
          return this.verticalSpeedSLong();
        case 42:
          return this.verticalSpeedSShort();
        case 43:
          return this.climbDescendChar();
        default:
          return "???";
      }
    }

    public boolean isAirprox() {
      return airprox;
    }

    void setAirprox(boolean airprox) {
      this.airprox = airprox;
    }

    public boolean isDeparture() {
      return Airplane.this.departure;
    }

    public String routeNameOrFix() {
      return Airplane.this.pilot.getRouteName();
    }

  }

  private final static double GROUND_MULTIPLIER = 1.0; //1.5; //3.0;
  private static final double secondFraction = 1 / 60d / 60d;
  private final static int MAX_HEADING_CHANGE_DERIVATIVE_STEP = 1;
  // <editor-fold defaultstate="collapsed" desc=" variables ">
  private final Callsign callsign;
  private final Squawk sqwk;
  private final boolean departure;
  private final Pilot pilot;
  private final AirplaneType airplaneType;
  private final AirplaneInfo info;
  private int lastHeadingChange = 0;
  private int targetHeading;
  private boolean targetHeadingLeftTurn;
  private int heading;
  private int targetAltitude;
  private int altitude;
  private int targetSpeed;
  private int speed;
  private Coordinate coordinate;

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=" .ctors ">
  private int lastVerticalSpeed;
  private FlightRecorder flightRecorder = null;

  public Airplane(Callsign callsign, Coordinate coordinate, Squawk sqwk, AirplaneType airplaneSpecification,
                  int heading, int altitude, int speed, boolean isDeparture,
                  String routeName, SpeechList<IAtcCommand> routeCommandQueue) {

    this.info = this.new AirplaneInfo();

    this.callsign = callsign;
    this.coordinate = coordinate;
    this.sqwk = sqwk;
    this.airplaneType = airplaneSpecification;

    this.departure = isDeparture;

    this.heading = heading;
    this.altitude = altitude;
    this.speed = speed;
    this.ensureSanity();
    this.targetAltitude = this.altitude;
    this.targetHeading = this.heading;
    this.targetSpeed = this.speed;

    this.pilot = new Pilot(this, routeName, routeCommandQueue);

    // flight recorders on
    this.flightRecorder = FlightRecorder.create(this.callsign, false, true);
  }

  private void ensureSanity() {
    heading = Headings.to(heading);

    if (speed < 0) {
      speed = 0;
    }

    if (altitude < 0) {
      altitude = 0;
    }
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=" getters / setters ">
  public AirplaneInfo getInfo() {
    return info;
  }

  public boolean isDeparture() {
    return departure;
  }

  public boolean isArrival() {
    return !departure;
  }

  public int getVerticalSpeed() {
    return lastVerticalSpeed;
  }

  public Callsign getCallsign() {
    return callsign;
  }

  public int getHeading() {
    return heading;
  }

  public String getHeadingS() {
    return String.format("%1$03d", this.heading);
  }

  public int getAltitude() {
    return altitude;
  }

  public boolean isFlying() {
    return altitude > Acc.airport().getAltitude();
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Squawk getSqwk() {
    return sqwk;
  }

  public AirplaneType getType() {
    return airplaneType;
  }

  public int getSpeed() {
    return speed;
  }

  public FlightRecorder getFlightRecorder() {
    return flightRecorder;
  }

  public String getTargetHeadingS() {
    return String.format("%1$03d", this.targetHeading);
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=" public methods ">
  @Override
  public Callsign getKey() {
    return this.callsign;
  }

  public void elapseSecond() {

    processMessages();
    drivePlane();
    updateSHABySecond();
    updateCoordinates();

    flightRecorder.logFDR(this, this.pilot);
  }

  @Override
  public String toString() {
    return this.callsign.toString();
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=" private methods ">
  private void drivePlane() {
    pilot.elapseSecond();
  }

  private void processMessages() {
    List<Message> msgs = Acc.messenger().getByTarget(this, true); //Acc.messenger().getMy(this, true);

    for (Message m : msgs) {
      processMessage(m);
    }
  }

  private void processMessage(Message msg) {
    // if speech from non-tuned ATC, then is ignored
    if (msg.getSource() != this.pilot.getTunedAtc()) {
      return;
    }

    SpeechList cmds;
    IMessageContent s = msg.getContent();
    if (isValidMessageForAirplane(s)) {
      if (s instanceof SpeechList)
        cmds = (SpeechList) s;
      else {
        cmds = new SpeechList();
        cmds.add(s);
      }
    } else {
      throw new ERuntimeException("Airplane can only deal with messages containing \"IFromAtc\" or \"List<IFromAtc>\".");
    }

    processCommands(cmds);
  }

  private boolean isValidMessageForAirplane(IMessageContent msg) {
    if (msg instanceof IFromAtc)
      return true;
    else if (msg instanceof SpeechList) {
      for (ISpeech o : (SpeechList<ISpeech>) msg) {
        if (!(o instanceof IFromAtc)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private void processCommands(SpeechList speeches) {
    this.pilot.addNewSpeeches(speeches);
  }

  private void updateSHABySecond() {
    double energy = 1;
    // when climb, first energy goes for altitude, then for speed
    // when descend, first energy goes for speed, then for altitude
    boolean isSpeedPreffered = getVerticalSpeed() > 0 || speed < this.getType().vDep;

    if (lastVerticalSpeed != 0 && targetAltitude == altitude) {
      lastVerticalSpeed = 0;
    }

    if (isSpeedPreffered) {
      if (targetSpeed != speed) {
        energy = adjustSpeed(energy);
      }
      energy = Math.max(energy, 0.2);
      if (targetAltitude != altitude) {
        adjustAltitude(energy);
      }
    } else {
      if (targetAltitude != altitude) {
        energy = adjustAltitude(energy);
      }
      energy = Math.max(energy, 0.2);
      if (targetSpeed != speed) {
        energy = adjustSpeed(energy);
      }
    }

    if (targetHeading != heading) {
      adjustHeading();
    }

  }

  private double adjustSpeed(double energyLeft) {
    // this is faster:
    boolean onGround = speed < airplaneType.vMinApp && speed > 20;
    // in flight
    if (targetSpeed > speed) {
      int step = airplaneType.speedIncreaseRate;
      if (onGround) {
        step = (int) Math.ceil(step * GROUND_MULTIPLIER);
      } else {
        step = (int) Math.ceil(step * energyLeft);
      }
      speed += step;
      if (targetSpeed < speed) {
        double diff = speed - targetSpeed;
        energyLeft = energyLeft - diff / step;
        speed = targetSpeed;
      } else {
        energyLeft = 0;
      }
    } else if (targetSpeed < speed) {
      int step = airplaneType.speedDecreaseRate;
      if (onGround) {
        step = (int) Math.ceil(step * GROUND_MULTIPLIER);
      } else {
        step = (int) Math.ceil(step * energyLeft);
      }
      speed -= step;
      if (targetSpeed > speed) {
        double diff = targetSpeed - speed;
        energyLeft = energyLeft - diff / step;
        speed = targetSpeed;
      } else {
        energyLeft = 0;
      }
    } // else if (targetSpeed < speed)
    return energyLeft;
  }

  private void adjustHeading() {
    // old
//    int newHeading
//        = Headings.turn(heading, airplaneType.headingChangeRate, targetHeadingLeftTurn, targetHeading);
//
//
//    this.heading = newHeading;

    // new implementation containing first derivative of heading change

    // newer
//    int changeSuggest = Headings.getSuggestedHeadingChangeForTurn(
//        heading, airplaneType.headingChangeRate, targetHeadingLeftTurn, targetHeading);
//
//    int realChange = adjustHeadingWithDerivative(heading, changeSuggest, lastHeadingChange);
//    this.heading = Headings.add(heading, realChange);
//    this.lastHeadingChange = realChange;

    // newest
    int diff = Headings.getDifference(heading, targetHeading, true);
    //TODO potential problem as "diff" function calculates difference in the shortest arc

    if (diff > lastHeadingChange) {
      lastHeadingChange += MAX_HEADING_CHANGE_DERIVATIVE_STEP;
      if (lastHeadingChange > airplaneType.headingChangeRate) {
        lastHeadingChange = airplaneType.headingChangeRate;
      }
    } else {
      // this is to make slight adjustment, but at least equal to 1 degree
      lastHeadingChange = Math.max(diff - MAX_HEADING_CHANGE_DERIVATIVE_STEP, 1);
    }

    if (targetHeadingLeftTurn)
      this.heading = Headings.add(heading, -lastHeadingChange);
    else
      this.heading = Headings.add(heading, lastHeadingChange);
  }

  private double adjustAltitude(double energyLeft) {
    if (speed < airplaneType.vR) {
      if (altitude == Acc.airport().getAltitude()) {
        return energyLeft;
      }
    }

    int origAlt = altitude;

    double step;
    if (targetAltitude > altitude) {
      step = airplaneType.getClimbRateForAltitude(this.altitude);
    } else {
      step = -airplaneType.getDescendRateForAltitude(this.altitude);
    }
    step = step * energyLeft;
    double neededStep = targetAltitude - altitude;

    if (Math.abs(step) < Math.abs(neededStep)) {
      energyLeft = 0;
      altitude += step;
    } else {
      energyLeft = energyLeft - neededStep / step;
      altitude = targetAltitude;
    }

    this.lastVerticalSpeed = (altitude - origAlt) * 60;

    return energyLeft;
  }

  public Atc getTunedAtc() {
    return pilot.getTunedAtc();
  }

  public int getTAS() {
    double m = 1 + this.altitude / 100000d;
    int ret = (int) (this.speed * m);
    return ret;
  }

  public int getGS() {
    return getTAS();
  }

  private void updateCoordinates() {
    double dist = this.getGS() * secondFraction;
    Coordinate newC
        = Coordinates.getCoordinate(coordinate, heading, dist);
    this.coordinate = newC;
  }

  public void setTargetHeading(int targetHeading, boolean useLeftTurn) {
    this.targetHeading = targetHeading;
    this.targetHeadingLeftTurn = useLeftTurn;
  }

  public int getTargetHeading() {
    return targetHeading;
  }

  public void setTargetHeading(int targetHeading) {
    boolean useLeft
        = Headings.getBetterDirectionToTurn(heading, targetHeading) == ChangeHeadingCommand.eDirection.left;
    setTargetHeading(targetHeading, useLeft);
  }

  public int getTargetAltitude() {
    return targetAltitude;
  }

  public void setTargetAltitude(int targetAltitude) {
    this.targetAltitude = targetAltitude;
  }

  public int getTargetSpeed() {
    return targetSpeed;
  }

  public void setTargetSpeed(int targetSpeed) {
    this.targetSpeed = targetSpeed;
  }

  @Override
  public String getName() {
    return this.getCallsign().toString();
  }

  public Navaid getDepartureLastNavaid() {
    if (isDeparture() == false)
      throw new ERuntimeException("This method should not be called on departure aircraft %s.", this.getCallsign().toString());

    String routeName = this.getInfo().routeNameOrFix();
    if (routeName.length() > 2 && Character.isDigit(routeName.charAt(routeName.length()-2)))
      routeName = routeName.substring(0, routeName.length() - 2);
    Navaid ret = Acc.area().getNavaids().tryGet(routeName);
    return ret;
  }

  // </editor-fold>
}
