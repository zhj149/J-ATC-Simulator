/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.atcs;

import jatcsimlib.airplanes.Airplane;

/**
 *
 * @author Marek
 */
public class TowerAtc extends ComputerAtc {

  public TowerAtc(String airportIcao) {
    super(Atc.eType.twr, airportIcao);
  }

  @Override
  protected void _registerNewPlane(Airplane plane) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
