/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsim.frmPacks.oneWindow;

import jatcsimdraw.painting.Settings;
import jatcsimlib.Simulation;

/**
 *
 * @author Marek
 */
public class Pack extends jatcsim.frmPacks.Pack {

  @Override
  public void initPack(Simulation sim, Settings displaySettings) {
    FrmMain f = new FrmMain();
    f.init(sim, displaySettings);
  }

  @Override
  public void startPack() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}