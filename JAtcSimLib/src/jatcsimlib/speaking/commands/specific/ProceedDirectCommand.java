/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.speaking.commands.specific;

import jatcsimlib.world.Navaid;

/**
 *
 * @author Marek
 */
public class ProceedDirectCommand extends ToNavaidCommand {

  public ProceedDirectCommand(Navaid navaid) {
    super(navaid);
  }
  
    @Override
  public String toString() {
    return "PD{"+ navaid.getName() + '}';
  }
  
}
