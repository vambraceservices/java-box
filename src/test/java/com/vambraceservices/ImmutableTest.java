package com.vambraceservices;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.spy;

public class ImmutableTest {

  @Test
  public void setSucceeds() throws AssertionError {

    // given
    Immutable<String> testContainer = spy(new Immutable<>());

    // when
    testContainer.set("will succeed");

    // then
    testContainer.get(innerValue -> {
      Assert.assertEquals("values match", "will succeed", innerValue);
    });
  }

  @Test(expected = AssertionError.class)
  public void setFails() throws AssertionError {

    // given
    Immutable<String> testContainer = spy(new Immutable<>());

    // when
    testContainer.set("will succeed");

    // then
    testContainer.set("will fail");
  }

  @Test
  public void hasValue() throws AssertionError {

    // given
    Immutable<String> testContainer = spy(new Immutable<>());

    // then
    Assert.assertFalse("knows there is no value", testContainer.hasValue());

    // when
    testContainer.set("with a value");

    // then
    Assert.assertTrue("knows there has a value", testContainer.hasValue());
  }

}
