package com.vambraceservices;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Immutable<V> {

  private V value;

  /**
   * Usage
   * when you want to have the option of setting a value after the constructor
   * but you want to ensure after it is set, it remains immutable
   *
   * Throws
   * AssertionError if the set has been called before
   *
   * @param v - value to store
   */

  public void set(V v) {
    assert Objects.isNull(this.value);
    this.value = v;
  }

  /**
   * @param consumer - that accepts a final version of the inner value
   *
   * Usage
   * when you want to access the inner value.
   * by only allowing the value to be read in a consumer, we enforce immutability
   */

  public void get(final Consumer<V> consumer) {
    consumer.accept(this.value);
  }

  /**
   * @return Whether container has a value already set.
   */

  public Boolean hasValue() {
    return Objects.nonNull(this.value);
  }

  /**
   * @param predicate - test that will test the value
   * @return If that test was successful
   */

  public Boolean test(Predicate<V> predicate) {
    return predicate.test(this.value);
  }


}
