/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.commands;

/**
 *
 * @author Marek
 */
public class AfterAltitudeCommand extends Command {
  private final int altitudeInFt;

  public AfterAltitudeCommand(int altitudeInFt) {
    this.altitudeInFt = altitudeInFt;
  }

  public int getAltitudeInFt() {
    return altitudeInFt;
  }
  
  
}