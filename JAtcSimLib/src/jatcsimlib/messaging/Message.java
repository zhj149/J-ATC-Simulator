/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.messaging;

import jatcsimlib.Acc;
import jatcsimlib.airplanes.Airplane;
import jatcsimlib.atcs.Atc;
import jatcsimlib.atcs.UserAtc;
import jatcsimlib.exceptions.ENotSupportedException;
import jatcsimlib.global.ERandom;
import jatcsimlib.global.ETime;

/**
 *
 * @author Marek
 */
public class Message implements Comparable<Message> {

  public static int planeVisibleTimeInSeconds = 10;
  public static int atcVisibleTimeInSeconds = 10;
  public static int systemVisibleTimeInSeconds = 10;
  
  public static int minPlaneDelayInSeconds = 3;
  public static int maxPlaneDelayInSeconds = 10;
  public static int minAtcDelayInSeconds = 3;
  public static int maxAtcDelayInSeconds = 10;
  
  public static int minSystemDelayInSeconds = 0;
  public static int maxSystemDelayInSeconds = 0;
  
  private static final ERandom rnd = new ERandom();
  
  public final ETime creationTime;
  public final ETime displayFromTime;
  public final ETime displayToTime;
  public final Object source;
  public final Object target;
  public final Object content;

  public Message(Object source, Object target, Object content) {
    this (Acc.now(), source, target, content);
  }
  
  public Message(ETime creationTime, Object source, Object target, Object content) {
    if (content == null) {
      throw new IllegalArgumentException("Argument \"content\" cannot be null.");
    }
    if (target == null) {
      throw new IllegalArgumentException("Argument \"target\" cannot be null.");
    }
    
    this.creationTime = creationTime.clone();
    this.displayFromTime = this.creationTime.addSeconds(generateDelay(source));
    this.displayToTime = this.displayFromTime.addSeconds(generateVisible(source));
    this.source = source;
    this.target = target;
    this.content = content;
  }
  
  public boolean isPlaneMessage(){
    return source != null && source instanceof Airplane;
  }
  public boolean isAtcMessage(){
    return source != null && source instanceof Atc;
  }
  public boolean isSystemMessage(){
    return source == null;
  }

  private int generateDelay(Object source) {
    if (source == null)
      return rnd.getInt(minSystemDelayInSeconds, maxSystemDelayInSeconds);
    else if (source instanceof Airplane)
      return rnd.getInt(minPlaneDelayInSeconds, maxPlaneDelayInSeconds);
    else if (source instanceof UserAtc)
      return 0;
    else if (source instanceof Atc)
      return rnd.getInt(minAtcDelayInSeconds, maxAtcDelayInSeconds);
    else
      throw new ENotSupportedException();
  }

  private int generateVisible(Object source) {
    if (source == null)
      return systemVisibleTimeInSeconds;
    else if (source instanceof Airplane)
      return planeVisibleTimeInSeconds;
    else if (source instanceof UserAtc)
      return 0;
    else if (source instanceof Atc)
      return atcVisibleTimeInSeconds;
    else
      throw new ENotSupportedException();
  }

  @Override
  public int compareTo(Message o) {
    return this.displayFromTime.compareTo(o.displayFromTime);
  }

  public Object getSource() {
    return source;
  }

  public String getText() {
    return (String) content;
  }
  
  public String tryGetText(){
    String ret;
    try{
      ret = getText();
    } catch (Exception ex){
      ret = null;
    }
    return ret;
  }
  
  public Object getContent(){
    return content;
  }
}
