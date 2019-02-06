package com.vambraceservices;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class BoxedTest {

  @Test
  public void testBoxing() throws Exception {
    // when (supplier returned a valid string)
    Boxed<String> boxed = Boxed.from(() -> "test");
    // then (box stores value)
    Assert.assertEquals("stores value", "test", boxed.peek());
    // when (request to convert stored value to length of stored value)
    Boxed<Integer> length = boxed.to(String::length);
    // then (new box stores length of original string)
    Assert.assertTrue("converts to length", length.peek() == 4);
    // when (empty box)
    Boxed<String> empty = Boxed.of(null);
    // then (box is empty)
    Assert.assertTrue("shows is empty", empty.isEmpty());
    // when (request to convert stored value to length of stored value)
    Boxed<Integer> safeLength = empty.to(String::length);
    // then (no error occurs. only a new empty box returned)
    Assert.assertTrue("safe length is empty", safeLength.isEmpty());
    // when (request to check stored value is longer than 10)
    Boxed<Boolean> longLength = safeLength.to(Objects::isNull, v -> v > 10);
    // then (no error occurs. only a new empty box returned)
    Assert.assertTrue("still empty", longLength.isEmpty());
    // when (request to replace value with true, but only if current value fails predicate)
    Boxed<Boolean> booleanBoxed = longLength.unless(() -> true);
    // then (new box created with that true value stored)
    Assert.assertTrue("now has true value", booleanBoxed.peek());
    // when (repeat request to replace value with true, but only if current fails predicate)
    Boxed<Boolean> falseBox = booleanBoxed.unless(value -> value, () -> false);
    // then (original box returned because the stored value was not null)
    Assert.assertTrue("box still has true value", falseBox.peek());
    Assert.assertEquals("box the same as before", falseBox, booleanBoxed);
    // when (request with both function and fall back supplier)
    Boxed<Integer> finalBox = falseBox.to(value -> !value, value -> 20, () -> 10);
    // then
    Assert.assertTrue("stored alternative value", 10 == finalBox.peek());
    // when (request with both function and fall back supplier)
    Boxed<Integer> extraBox = finalBox.to(value -> (value == 10), value -> 20, () -> 10);
    // then
    Assert.assertTrue("stored requested value", 20 == extraBox.peek());
    // then
    Assert.assertTrue("optional present", extraBox.get().isPresent());
  }



}
