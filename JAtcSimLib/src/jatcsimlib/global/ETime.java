/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.global;

import java.util.Calendar;

/**
 *
 * @author Marek
 */
public class ETime implements Comparable<ETime> {

  private final int DAY_SECONDS = 60 * 60 * 24;
  private int value = 0;

  public ETime(int value) {
    this.value = value;
  }

  public ETime(int hour, int minute, int second) {
    set(hour, minute, second);
  }

  public ETime(Calendar c) {
    int h = c.get(Calendar.HOUR_OF_DAY);
    int m = c.get(Calendar.MINUTE);
    int s = c.get(Calendar.SECOND);

    set(h, m, s);
  }

  // TODO remove sets or make them private, ensure that constructor rounds value around midnight.
  public final void set(int hours, int minutes, int seconds) {
    this.value = hours * 60 * 60 + minutes * 60 + seconds;
  }

  public final void set(int day, int hours, int minutes, int seconds) {
    this.value = day * DAY_SECONDS + hours * 60 * 60 + minutes * 60 + seconds;
  }

  public int getSeconds() {
    return value % 60;
  }

  public int getTotalSeconds() {
    return value;
  }

  public int getMinutes() {
    return ((int) (Math.floor(value / 60))) % 60;
  }

  public int getHours() {
    return ((int) (Math.floor(value / 60 / 60)));
  }
  
  public int getDays(){
    return ((int) (Math.floor(value / DAY_SECONDS)));
  }

  @Override
  public String toString() {
    return String.format("%d.%02d:%02d:%02d", getDays(), getHours(), getMinutes(), getSeconds());
  }
  
  public String toTimeString() {
    return String.format("%02d:%02d:%02d", getHours(), getMinutes(), getSeconds());
  }

  public ETime addSeconds(int amount) {
    return new ETime(value + amount);
  }

  @Override
  public ETime clone() {
    return new ETime(this.value);
  }

  @Override
  public int compareTo(ETime o) {
    return Integer.compare(this.value, o.value);
  }

  public boolean isBefore(ETime otherTime) {
    return this.value < otherTime.value;
  }

  public boolean isAfter(ETime otherTime) {
    return this.value > otherTime.value;
  }

  public boolean isBeforeOrEq(ETime otherTime) {
    return isBefore(otherTime) || this.value == otherTime.value;
  }

  public boolean isAfterOrEq(ETime otherTime) {
    return isAfter(otherTime) || this.value == otherTime.value;
  }

  public boolean isBetween(ETime before, ETime after) {
    return this.isAfterOrEq(before) && this.isBeforeOrEq(after);
  }

  public boolean isBetweenStrictly(ETime before, ETime after) {
    return this.isAfter(before) && this.isBefore(after);
  }

  public void increaseSecond() {
    this.value++;
  }

  /**
   * Checks if minute fraction is zero.
   *
   * @return true if seconds == 0, false otherwise.
   */
  public boolean isIntegralMinute() {
    return getSeconds() == 0;
  }

  /**
   * Returns clone of this time extended by minutes
   * @param minutes Number of minutes to send.
   * @return This time extended by number of minutes.
   */
  public ETime addMinutes(int minutes) {
    // TODO check for midnight
    return new ETime(this.getTotalSeconds() + minutes*60);
  }
}
