/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.atcs;

/**
 *
 * @author Marek
 */
public abstract class Atc {
  public enum eType{
    gnd,
    twr,
    app,
    ctr
  }
  
  private final eType type;

  public Atc(eType type) {
    this.type = type;
  }
  
  public abstract boolean isHuman();

  public eType getType() {
    return type;
  }
}
