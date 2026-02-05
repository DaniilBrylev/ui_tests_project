package org.openqa.selenium.html5;

import org.openqa.selenium.WebDriver;

public class RemoteLocationContext implements LocationContext {
  public RemoteLocationContext(WebDriver driver) {
    // shim only, no-op
  }

  @Override
  public Location location() {
    throw new UnsupportedOperationException("LocationContext shim: not supported");
  }

  @Override
  public void setLocation(Location location) {
    throw new UnsupportedOperationException("LocationContext shim: not supported");
  }
}