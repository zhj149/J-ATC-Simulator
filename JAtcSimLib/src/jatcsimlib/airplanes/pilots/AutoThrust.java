/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.airplanes.pilots;

import jatcsimlib.airplanes.Airplane;
import jatcsimlib.exceptions.ENotSupportedException;
import jatcsimlib.global.EStringBuilder;
import jatcsimlib.global.SpeedRestriction;

/**
 *
 * @author Marek Vajgl
 */
public class AutoThrust {

  public enum Mode {

    /**
     * Used when no speed is commanded
     */
    idle,
    /**
     * Used at plane takeoff until acceleration altitude
     */
    takeOff,
    /**
     * Used for planes cleared to approach, but without captured descend slope yet
     */
    approach,
    /**
     * Used for planes with clearence to approach, on descend sclope
     */
    atFinal,
    /**
     * Normal flight above FL100
     */
    normalHigh,
    /**
     * Normal flight below FL100
     */
    normalLow
  }

  private final Airplane parent;
  private SpeedRestriction orderedSpeed;
  private Mode mode;

  public AutoThrust(Airplane parentPlane, Mode mode) {
    this.parent = parentPlane;
    this.mode = mode;
    this.orderedSpeed = null;
    updateTargetSpeed();
  }

  public void setMode(Mode mode, boolean cleanOrderedSpeed) {
    this.mode = mode;
    if (cleanOrderedSpeed) {
      orderedSpeed = null;
    }
    updateTargetSpeed();
  }

  public void setOrderedSpeed(SpeedRestriction speedRestriction) {
    this.orderedSpeed = speedRestriction;
    updateTargetSpeed();
  }

  public void cleanOrderedSpeed() {
    orderedSpeed = null;
    updateTargetSpeed();
  }

  private void updateTargetSpeed() {
    int s = getBestSpeedByMode();
    s = updateSpeedBySpeedRestriction(s);
    parent.setTargetSpeed(s);
  }

  private int getBestSpeedByMode() {
    int ret;
    switch (mode) {
      case idle:
        ret = 0;
        break;
      case normalHigh:
        ret = parent.getType().vCruise;
        break;
      case normalLow:
        ret = Math.min(parent.getType().vCruise, 250);
        break;
      case approach:
        ret = parent.getType().vMinClean;
        break;
      case atFinal:
        ret = parent.getType().vApp;
        break;
      case takeOff:
        ret = parent.getType().vDep;
        break;
      default:
        throw new ENotSupportedException();
    }
    return ret;
  }

  private int updateSpeedBySpeedRestriction(int speedInKts) {
    if (this.orderedSpeed == null) {
      return speedInKts;
    } else {
      switch (this.mode) {
        case atFinal:
        case takeOff:
        case idle:
          return speedInKts;
        default:
          switch (this.orderedSpeed.direction) {
            case exactly:
              return this.orderedSpeed.speedInKts;
            case atMost:
              return Math.min(this.orderedSpeed.speedInKts, speedInKts);
            case atLeast:
              return Math.max(this.orderedSpeed.speedInKts, speedInKts);
            default:
              throw new ENotSupportedException();
          }
      }
    }
  }

  public String toLogString() {
    EStringBuilder sb = new EStringBuilder();

    sb.appendFormat("%s", mode.toString());
    if (orderedSpeed != null) {
      sb.appendFormat(" (%s %4d)",
        orderedSpeed.direction.toString(),
        orderedSpeed.speedInKts);
    }

    return sb.toString();
  }
}
