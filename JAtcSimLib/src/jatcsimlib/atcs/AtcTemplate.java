/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.atcs;

import jatcsimlib.global.KeyItem;

/**
 *
 * @author Marek
 */
public class AtcTemplate implements KeyItem<Atc.eType> {
  private Atc.eType type;
  private String name;
  private double frequency;
  private int acceptAltitude;
  private int releaseAltitude;
  private int orderedAltitude;

  @Override
  public Atc.eType getKey() {
    return type;
  }

  public Atc.eType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public double getFrequency() {
    return frequency;
  }

  public int getAcceptAltitude() {
    return acceptAltitude;
  }

  public int getReleaseAltitude() {
    return releaseAltitude;
  }

  public int getOrderedAltitude() {
    return orderedAltitude;
  }
  
}
