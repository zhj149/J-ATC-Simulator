/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.messaging;

/**
 *
 * @author Marek
 */
public class StringMessage implements IContent{
  public final String text;

  public StringMessage(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "Msg{" + text + '}';
  }
}