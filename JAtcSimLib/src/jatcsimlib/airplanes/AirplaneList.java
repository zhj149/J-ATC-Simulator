/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.airplanes;

import jatcsimlib.global.KeyList;
import java.util.Arrays;

/**
 *
 * @author Marek
 */
public class AirplaneList extends KeyList<Airplane, Callsign> {
  
  public Airplane tryGetByCallsign(String callsign){
    Callsign cs = new Callsign(callsign);
    return this.tryGet(cs);
  }
  
  public Airplane tryGetBySqwk (String sqwk){
    char [] s = sqwk.toCharArray();
    return this.tryGet(s);
  }
  
  public Airplane tryGet(char [] sqwk){
    for (Airplane p : this){
      if (Arrays.equals(p.getSqwk(), sqwk))
        return p;
    }
    return null;
  }
}
