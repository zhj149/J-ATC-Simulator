/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib;

import jatcsimlib.airplanes.Airplane;
import jatcsimlib.atcs.*;
import jatcsimlib.exceptions.ENotSupportedException;
import jatcsimlib.global.ERandom;
import jatcsimlib.global.ETime;
import jatcsimlib.global.ReadOnlyList;
import jatcsimlib.weathers.Weather;
import jatcsimlib.world.Airport;
import jatcsimlib.world.Area;
import jatcsimlib.world.RunwayThreshold;

/**
 * @author Marek
 */
public class Acc {

  private static Area area;
  private static Simulation sim;
  private static Airport aip;

  public static void setArea(Area area) {
    Acc.area = area;
  }

  public static void setAirport(Airport aip) {
    Acc.aip = aip;
  }

  public static void setSimulation(Simulation simulation) {
    Acc.sim = simulation;
    Acc.setAirport(sim.getActiveAirport());
  }

  public static Simulation sim() {
    return sim;
  }

  public static ReadOnlyList<Airplane> planes() {
    return prm().getAll();
  }

  public static PlaneResponsibilityManager prm() {
    return PlaneResponsibilityManager.getInstance();
  }

  public static ETime now() {
    return sim.getNow();
  }

  public static Airport airport() {
    return Acc.aip;
  }

  public static jatcsimlib.messaging.Messenger messenger() {
    return sim().getMessenger();
  }

  public static Weather weather() {
    return sim.getWeather();
  }

  public static RunwayThreshold threshold() {
    return atcTwr().getRunwayThresholdInUse();
  }

  public static Area area() {
    return area;
  }

  public static UserAtc atcApp() {
    return sim.getAppAtc();
  }

  public static TowerAtc atcTwr() {
    return sim.getTwrAtc();
  }

  public static CenterAtc atcCtr() {
    return sim.getCtrAtc();
  }

  public static String toAltS(int altitudeInFt, boolean appendFt) {
    return Acc.sim().toAltitudeString(altitudeInFt, appendFt);
  }

  public static Atc atc(Atc.eType type) {
    switch (type) {
      case app:
        return atcApp();
      case ctr:
        return atcCtr();
      case twr:
        return atcTwr();
      default:
        throw new ENotSupportedException();
    }
  }

  public static ERandom rnd() {
    return Simulation.rnd;
  }
}
