/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.traffic;

import jatcsimlib.Acc;
import static jatcsimlib.Simulation.rnd;
import jatcsimlib.airplanes.Airplane;
import jatcsimlib.airplanes.AirplaneType;
import jatcsimlib.airplanes.Callsign;
import jatcsimlib.airplanes.Squawk;
import jatcsimlib.atcs.Atc;
import jatcsimlib.coordinates.Coordinate;
import jatcsimlib.coordinates.Coordinates;
import jatcsimlib.global.Global;
import jatcsimlib.speaking.SpeechList;
import jatcsimlib.speaking.fromAtc.IAtcCommand;
import jatcsimlib.speaking.fromAtc.commands.afters.AfterAltitudeCommand;
import jatcsimlib.speaking.fromAtc.commands.ChangeAltitudeCommand;
import jatcsimlib.speaking.fromAtc.commands.ContactCommand;
import jatcsimlib.world.Route;
import jatcsimlib.world.Routes;

import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TestTrafficOneDeparture extends TestTraffic {

  Airplane a = null;
  boolean done = false;

  private Airplane generatePlane() {

    Airplane ret;

    Callsign cs;
    cs = new Callsign("CSA", "001");
    AirplaneType pt = Acc.sim().getPlaneTypes().getRandomByTraffic(Acc.airport().getTrafficCategories(), true);

    Route r;
    Iterable<Route> rts = Acc.threshold().getRoutes();
    List<Route> avails = Routes.getByFilter(rts, false, pt.category);
    if (avails.isEmpty()) {
      return null; // if no route, return null
    }
    r = eng.eSystem.Lists.getRandom(avails);

    Coordinate coord = Acc.threshold().getCoordinate();
    Squawk sqwk = Squawk.create("0001");

    int heading = (int) Acc.threshold().getCourse();
    int alt = Acc.threshold().getParent().getParent().getAltitude();
    int spd = 0;

    SpeechList<IAtcCommand> routeCmds;
    if (r != null) {
      routeCmds = r.getCommandsListClone();
    } else {
      routeCmds = new SpeechList<>();
    }

    int indx = 0;
    // added command to contact after departure
    routeCmds.add(indx++, new ContactCommand(Atc.eType.twr));

    routeCmds.add(indx++, new ChangeAltitudeCommand(
        ChangeAltitudeCommand.eDirection.climb, Acc.threshold().getInitialDepartureAltitude()));

    // -- po vysce+300 ma kontaktovat APP
    routeCmds.add(indx++,
        new AfterAltitudeCommand(Acc.threshold().getParent().getParent().getAltitude() + Acc.rnd().nextInt(150, 450)));
    routeCmds.add(indx++, new ContactCommand(Atc.eType.app));

    String routeName;
    if (r != null) {
      routeName = r.getName();
    } else {
      routeName = "(vfr)";
    }
    ret = new Airplane(
        cs, coord, sqwk, pt, heading, alt, spd, true,
        routeName, routeCmds);

    return ret;
  }

  private Route tryGetRandomRoute(boolean arrival, AirplaneType planeType) {

    Iterable<Route> rts = Acc.threshold().getRoutes();
    List<Route> avails = Routes.getByFilter(rts, arrival, planeType.category);

    if (avails.isEmpty()) {
      return null; // if no route, return null
    }
    int index = rnd.nextInt(avails.size());

    Route ret = avails.get(index);

    return ret;
  }

  private int generateArrivingPlaneAltitude(Route r) {
    double thousandsFeetPerMile = 0.30;

    double dist = r.getRouteLength();
    if (dist < 0) {
      dist = Coordinates.getDistanceInNM(r.getMainFix().getCoordinate(), Acc.airport().getLocation());
    }

    int ret = (int) (dist * thousandsFeetPerMile) + rnd.nextInt(1, 5); //5, 12);
    ret = ret * 1000;
    return ret;
  }

  private Coordinate generateArrivalCoordinate(Coordinate navFix, Coordinate aipFix) {
    double radial = Coordinates.getBearing(aipFix, navFix);
    radial += rnd.nextDouble() * 50 - 25; // nahodne zatoceni priletoveho radialu
    double dist = rnd.nextDouble() * Global.MAX_ARRIVING_PLANE_DISTANCE; // vzdalenost od prvniho bodu STARu
    Coordinate ret = null;
    while (ret == null) {

      ret = Coordinates.getCoordinate(navFix, (int) radial, dist);
      for (Airplane p : Acc.planes()) {
        double delta = Coordinates.getDistanceInNM(ret, p.getCoordinate());
        if (delta < 5d) {
          ret = null;
          break;
        }
      }
      dist += rnd.nextDouble() * 10;
    }
    return ret;
  }

  @Override
  public Airplane[] getNewAirplanes() {
    if (a != null) {
      Airplane[] ret = new Airplane[]{a};
      a = null;
      return ret;
    } else {
      return new Airplane[0];
    }
  }

  @Override
  public void generateNewMovementsIfRequired() {
    if (!done) {
      a = generatePlane();
      done = true;
    }
  }

  @Override
  public Movement[] getScheduledMovements() {
    return new Movement[0];
  }

}
