/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.speaking.fromAtc.commands.afters;

/**
 *
 * @author Marek
 */
public class AfterSpeedCommand extends AfterCommand {

  private final int speedInKts;

  public AfterSpeedCommand(int speedInKts) {
    this.speedInKts = speedInKts;
  }

  public int getSpeedInKts() {
    return speedInKts;
  }
  
    @Override
  public String toString() {
    return "AS{"+ speedInKts + '}';
  }

}
