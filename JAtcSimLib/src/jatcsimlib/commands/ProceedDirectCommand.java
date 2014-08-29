/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.commands;

import jatcsimlib.world.Navaid;

/**
 *
 * @author Marek
 */
public class ProceedDirectCommand extends Command {
  private final Navaid navaid;

  public ProceedDirectCommand(Navaid navaid) {
    this.navaid = navaid;
  }

  public Navaid getNavaid() {
    return navaid;
  }
  
  
}