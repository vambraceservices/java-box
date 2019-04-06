package com.vambraceservices;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The BoxedList class
 *
 * @param <L> original stored type
 */

public class BoxedList<L> {

  private Collection<L> listValue;

  /**
   * @param lv - value to be stored
   */

  BoxedList(Collection<L> lv) {
    if (Objects.nonNull(lv)) {
      this.listValue = Collections.unmodifiableCollection(lv);
    }
  }

  /**
   * @param <L> - type of stored value
   * @param lv - value to be stored
   * @return provided value stored in Boxed
   */

  public static <L> BoxedList<L> of(List<L> lv) {
    return new BoxedList<>(lv);
  }

  /**
   * @return if this list is instantiated, and has items
   */

  public boolean isEmpty() {
    return Objects.isNull(listValue) || listValue.isEmpty();
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  /**
   * @return either the first item in the list, or an empty optional
   */

  public Optional<L> first() {
    if (this.isNotEmpty()) {
      return this.listValue.stream().findFirst();
    }
    return Optional.empty();
  }

  /**
   * @return either a copy of the list, or an empty list
   */

  public List<L> get() {
    return this.get(ArrayList::new);
  }

  public List<L> get(Supplier<List<L>> builder) {
    if (this.isNotEmpty()) {
      List<L> list = builder.get();
      list.addAll(this.listValue);
      return list;
    }
    return Collections.emptyList();
  }

  /**
   * @return either the size of the list, or zero
   */

  public int size() {
    if (this.isNotEmpty()) {
      return this.listValue.size();
    }
    return 0;
  }

  /**
   * @return : either the stream of the list, or an empty stream
   */

  public Stream<L> stream() {
    if (this.isNotEmpty()) {
      return this.listValue.stream();
    }
    return Stream.empty();
  }

}
