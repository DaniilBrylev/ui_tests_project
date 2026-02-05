package org.openqa.selenium.html5;

import java.util.Objects;

public class Location {
  private final double latitude;
  private final double longitude;
  private final double altitude;

  public Location(double latitude, double longitude, double altitude) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getAltitude() {
    return altitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Location)) return false;
    Location that = (Location) o;
    return Double.compare(that.latitude, latitude) == 0
        && Double.compare(that.longitude, longitude) == 0
        && Double.compare(that.altitude, altitude) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude, altitude);
  }

  @Override
  public String toString() {
    return "Location{" +
        "latitude=" + latitude +
        ", longitude=" + longitude +
        ", altitude=" + altitude +
        '}';
  }
}