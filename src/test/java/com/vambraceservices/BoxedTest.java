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
    boxed.peek(v -> Assert.assertEquals("stores value", "test", v));
    // when (request to convert stored value to length of stored value)
    Boxed<Integer> length = boxed.map(String::length);
    // then (new box stores length of original string)
    length.peek(v -> Assert.assertTrue("converts to length", v == 4));
    // when (empty box)
    Boxed<String> empty = Boxed.of(null);
    // then (box is empty)
    Assert.assertTrue("shows is empty", empty.isEmpty());
    // when (request to convert stored value to length of stored value)
    Boxed<Integer> safeLength = empty.map(String::length);
    // then (no error occurs. only a new empty box returned)
    Assert.assertTrue("safe length is empty", safeLength.isEmpty());
    // when (request to check stored value is longer than 10)
    Boxed<Boolean> longLength = safeLength.map(Objects::isNull, v -> v > 10);
    // then (no error occurs. only a new empty box returned)
    Assert.assertTrue("still empty", longLength.isEmpty());
    // when (request to replace value with true, but only if current value fails predicate)
  }



}
