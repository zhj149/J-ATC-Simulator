/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.world;

import jatcsimlib.coordinates.Coordinate;
import jatcsimlib.coordinates.Coordinates;
import jatcsimlib.global.KeyItem;
import jatcsimlib.global.KeyList;
import jatcsimlib.global.MustBeBinded;
import jatcsimlib.global.XmlOptional;

/**
 *
 * @author Marek
 */
public class RunwayThreshold extends MustBeBinded implements KeyItem<String> {

  private String name;
  private Coordinate coordinate;
  private final KeyList<Approach, Approach.eType> approaches = new KeyList();
  private final KeyList<Route, String> routes = new KeyList();
  private Runway parent;
  private double _course;
  private int initialDepartureAltitude;
  private RunwayThreshold _other;
  @XmlOptional
  private boolean preferred = false;
  @XmlOptional
  private Coordinate fafCross;

  public int getInitialDepartureAltitude() {
    return initialDepartureAltitude;
  }

  public boolean isPreferred() {
    return preferred;
  }
  
  public String getName() {
    return name;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Coordinate getFafCross() {
    return fafCross;
  }
  
  public KeyList<Approach, Approach.eType> getApproaches() {
    return approaches;
  }

  public KeyList<Route, String> getRoutes() {
    return routes;
  }

  @Override
  public String getKey() {
    return getName();
  }

  public Runway getParent() {
    return parent;
  }

  public void setParent(Runway parent) {
    this.parent = parent;
  }

  public double getCourse() {
    checkBinded();

    return this._course;
  }

  public Approach tryGetApproachByTypeWithILSDerived(Approach.eType type) {
    Approach ret = getApproaches().tryGet(type);
    if (ret == null) {
      switch (type) {
        case ILS_I:
          ret = tryGetApproachByTypeWithILSDerived(Approach.eType.ILS_II);
          break;
        case ILS_II:
          ret = tryGetApproachByTypeWithILSDerived(Approach.eType.ILS_III);
      }
    }
    return ret;
  }

  public Approach getHighestApproach() {
    Approach ret;

    ret = tryGetApproachByTypeWithILSDerived(Approach.eType.ILS_I);
    if (ret == null) {
      ret = approaches.tryGet(Approach.eType.GNSS);
    }
    if (ret == null) {
      ret = approaches.tryGet(Approach.eType.VORDME);
    }
    if (ret == null) {
      ret = approaches.tryGet(Approach.eType.NDB);
    }

    return ret;
  }

  @Override
  protected void _bind() {
    this._other
        = this.getParent().getThresholdA().equals(this)
        ? this.getParent().getThresholdB()
        : this.getParent().getThresholdA();
    this._course
        = Coordinates.getBearing(this.coordinate, _other.coordinate);
  }

  public RunwayThreshold getOtherThreshold() {
    return _other;
  }

}
