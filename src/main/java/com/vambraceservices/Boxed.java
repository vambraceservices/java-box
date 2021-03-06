package com.vambraceservices;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Boxed<T> {

  private T value;

  private Boxed(T seed) {
    this.value = seed;
  }

  /**
   * @param consumer - accepts inner value to be called if inner value is valid
   * @return itself
   */

  public Boxed<T> peek(Consumer<T> consumer) {
    if (this.isNotEmpty()) {
      consumer.accept(this.value);
    }
    return this;
  }

  /**
   * Return inner value as optional
   * @return inner value wrapped in Optional
   */

  public Optional<T> get() {
    return Optional.ofNullable(this.value);
  }

  /**
   * @param <T> - the stored type
   * @param seed - the supplied value boxed
   * @return new boxed instance containing value
   */

  public static <T> Boxed<T> of(T seed) {
    return new Boxed<>(seed);
  }

  /**
   * @param <T> - the stored type
   * @param supplier - provides inner value to be stored
   * @return the supplier's returned value boxed.
   */

  public static <T> Boxed<T> from(Supplier<T> supplier) {
    if (Objects.isNull(supplier)) {
      return new Boxed<>(null);
    }
    return new Boxed<>(supplier.get());
  }

  /**
   * @return if this box is empty
   */

  public boolean isEmpty() {
    return Objects.isNull(value);
  }

  /**
   * @return if this box is not empty
   */

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  /**
   * @param selector - predicate to determine if inner value should be retained
   * @return new instance of Boxed with either new value or empty
   */

  public Boxed<T> filter(Predicate<T> selector) {
    return this.map(selector, v -> v);
  }

  /**
   * @param <R> - the returned inner type
   * @param function - to execute on inner value,
   * @param alternative - value to retain if no current valid inner value
   * @return new instance of Boxed with either new value or empty
   */

  public <R> Boxed<R> map(Function<T, R> function, Supplier<R> alternative) {
    return this.map(v -> true, function, alternative);
  }

  /**
   * @param <R> - the returned inner type
   * @param function - to execute on the inner value
   * @return new instance of Boxed with either new value or empty
   */

  public <R> Boxed<R> map(Function<T, R> function) {
    return this.map(v -> true, function);
  }

  /**
   * @param <R> - the returned inner type
   * @param function to execute on inner value
   * @return new instance of Boxed with either new value or empty
   */

  public <R> Boxed<R> omap(Function<T, Optional<R>> function) {
    return this.map(function).map(Optional::isPresent, Optional::get);
  }

  /**
   * @param <R> - the returned inner type
   * @param function to execute on inner value
   * @return new instance of Boxed with either new value or empty
   */

  public <R> Boxed<R> flatmap(Function<T, Boxed<R>> function) {
    return this.map(function).map(Boxed::isNotEmpty, Boxed::get).omap(optional -> optional);
  }

  /**
   * @param <R> - the returned inner type
   * @param selector - to determine if current inner value valid,
   * @param function - to execute on that inner value
   * @return new instance of Boxed with either new value or empty
   */

  public <R> Boxed<R> map(Predicate<T> selector, Function<T, R> function) {
    return this.map(selector, function, () -> null);
  }

  /**
   * @param <R> - the returned inner type
   * @param selector - to determine if current inner value valid,
   * @param function - to execute on that inner value
   * @param supplier - of alternative if method cannot becalled
   * @return new instance of Boxed with either new value or empty
   */

  public <R> Boxed<R> map(Predicate<T> selector, Function<T, R> function, Supplier<R> supplier) {
    if (!this.isEmpty() && selector.test(this.value)) {
      return new Boxed<>(function.apply(this.value));
    } else if (Objects.nonNull(supplier)) {
      return new Boxed<>(supplier.get());
    }
    return new Boxed<>(null);
  }

  /**
   * @param selector - to determine if current inner value is valid
   * @param alternative - to supply allternative value if it is not valid
   * @return new instance of Boxed with either new value or empty
   */

  public Boxed<T> or(Predicate<T> selector, Supplier<T> alternative) {
    if (selector.test(this.value)) {
      return this;
    }
    return new Boxed<>(alternative.get());
  }

  /**
   * @param <R> - the returned inner type
   * @param function - to call on valid inner value. Will return list
   * @return new instance of Boxed with either new value or empty
   */

  public <R> BoxedList<R> mapList(Function<T, Collection<R>> function) {
    return this.mapList(v -> true, function);
  }

  /**
   * @param <R> - the returned inner type
   * @param selector - to determine if inner value is valid for use
   * @param function - to call on valid inner value. Will return list
   * @return new instance of Boxed with either new value or empty
   */

  public <R> BoxedList<R> mapList(Predicate<T> selector, Function<T, Collection<R>> function) {
    if (this.isNotEmpty() && selector.test(this.value)) {
      return new BoxedList<>(function.apply(this.value));
    }
    return new BoxedList<>(null);
  }

}
