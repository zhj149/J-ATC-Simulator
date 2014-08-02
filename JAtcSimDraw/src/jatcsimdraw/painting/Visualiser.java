/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimdraw.painting;

import jatcsimlib.world.Border;
import jatcsimlib.world.Navaid;
import jatcsimlib.world.Runway;

/**
 *
 * @author Marek
 */
public abstract class Visualiser {
  protected final Painter p;
  protected final Settings sett;

  public Visualiser(Painter p, Settings sett) {
    this.p = p;
    this.sett = sett;
  }
  
  public abstract void drawBorder(Border border);
  public abstract void drawRunway(Runway runway);
  public abstract void drawNavaid (Navaid navaid);
  public abstract void clear();
}
