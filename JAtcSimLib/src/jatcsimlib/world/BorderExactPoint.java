/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsimlib.world;

import jatcsimlib.coordinates.Coordinate;
import java.util.Objects;

/**
 *
 * @author Marek
 */
public class BorderExactPoint extends BorderPoint {
  private Coordinate coordinate;

  public Coordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.coordinate);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final BorderExactPoint other = (BorderExactPoint) obj;
    if (!Objects.equals(this.coordinate, other.coordinate)) {
      return false;
    }
    return true;
  }
}
