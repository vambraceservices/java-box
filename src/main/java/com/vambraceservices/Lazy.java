package com.vambraceservices;

import java.util.Objects;
import java.util.function.Supplier;

public class Lazy<V> {

  private V           value;
  private Supplier<V> supplier;

  /**
   * @param loader -  supplier that can be called to load the value when needed
   * @param <V> - type of returned value
   * @return a container for a value that will be lazy loaded
   */

  public static <V> Lazy<V> init(final Supplier<V> loader) {
    return new Lazy<>(loader);
  }

  /**
   * @param s - value to store
   */

  private Lazy(final Supplier<V> s) {
    this.supplier = s;
  }

  /**
   * @return - inner value after callings supplier if not there
   */

  public synchronized V get() {
    if (Objects.isNull(value)) {
      value = this.supplier.get();
    }
    return value;
  }

}
