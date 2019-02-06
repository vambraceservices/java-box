package com.vambraceservices;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Builder<T> {

  private T value;

  /**
   * @param seed - value to store
   */

  private Builder(T seed) {
    this.value = seed;
  }

  /**
   * @param seed - the seed value
   * @param <T> - the type of the stored value
   * @return returns new instance of Builder with stored value
   */

  public static <T> Builder<T> of(T seed) {
    return new Builder<>(seed);
  }

  /**
   * @param <R> - the type of the returned value
   * @param function - to retrieve the objects inner attribute's value
   * @param consumer - to set the object inner attribute's value (if req'd)
   * @param supplier - to create a value for the object inner attribute (if req'd)
   * @return a builder with the value of the objects inner attribute's value
   */
  public <R> Builder<R> with(final Function<T, R> function,
                             final Function<T, Consumer<R>> consumer,
                             final Supplier<R> supplier) {
    if (Objects.nonNull(value)) {
      R next = function.apply(this.value);
      if (Objects.isNull(next)) {
        next = supplier.get();
        consumer.apply(this.value).accept(next);
        return new Builder<>(next);
      }
      return new Builder<>(next);
    }
    return new Builder<>(null);
  }

  public T done() {
    return this.value;
  }

}
