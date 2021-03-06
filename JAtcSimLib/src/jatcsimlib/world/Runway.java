/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.world;

import jatcsimlib.global.KeyItem;
import jatcsimlib.global.KeyList;

/**
 *
 * @author Marek
 */
public class Runway implements KeyItem<String> {
  private final KeyList<RunwayThreshold, String> thresholds = new KeyList();
  private boolean active;
  
  private Airport parent;

  public RunwayThreshold get(int index){
    return thresholds.get(index);
  }
  
  public KeyList<RunwayThreshold,String> getThresholds(){
    return this.thresholds;
  }
  
  public RunwayThreshold getThresholdA(){
    return thresholds.get(0);
  }
  
  public RunwayThreshold getThresholdB(){
    return thresholds.get(1);
  }

  public boolean isActive() {
    return active;
  }
  
  public String getName(){
    return getThresholdA().getName() + "-" + getThresholdB().getName();
  }
  
  @Override
  public String getKey() {
    return getName();
  }

  public Airport getParent() {
    return parent;
  }

  public void setParent(Airport parent) {
    this.parent = parent;
  }
  
}
