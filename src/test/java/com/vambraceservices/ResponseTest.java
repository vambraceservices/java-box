package com.vambraceservices;

import org.junit.Assert;
import org.junit.Test;
import java.util.Collections;

public class ResponseTest {

  @Test
  public void testTo() {

    // given (an empty response)
    Response<String> emptyResponse = Response.empty();

    // when (given a function)
    Response<Integer> integerResponse = emptyResponse.to(Integer::valueOf);

    // then (function not called, returns new empty response instance)
    Assert.assertTrue("response remains empty", integerResponse.isEmpty());

    // given (response with value)
    Response<String> validResponse = Response.from("1");

    // when (given a function)
    Response<Long> longResponse = validResponse.to(Long::valueOf);

    // then (function called and new response returned)
    Assert.assertTrue("response value is valid", longResponse.value() > 0);

    // given (response with value)
    Response<Integer> oneResponse = Response.from(1);

    // when (map to new response but only if passes predicate)
    Response<Integer> bigResponse = oneResponse.to(value -> value > 1, value -> value);

    // then (new response is empty)
    Assert.assertTrue("response is empty if not passing predicate", bigResponse.isEmpty());
  }

  @Test
  public void testAs() {

    // when (response given a function that returns another response)
    Response<Integer> convertedResponse = Response.from(1).as(value -> Response.from(value + 1));

    // then (response returned with new value - not an embedded response)
    convertedResponse.consumeValue(value -> Assert.assertTrue("response can be transformed", value == 2));
  }

  @Test
  public void testUse() {

    // given
    Response<String> thrownResponse =
          Response.failed(new NullPointerException());

    // when
    Integer stringLength = thrownResponse.use(String::length, 0);

    // then
    Assert.assertTrue("string length is zero", stringLength == 0);
  }

  @Test
  public void testFrom() throws Exception {
    // given
    final String value = "some-result";

    // when
    Response<String> response = Response.from(value);
    Response<String> failed = Response.failed(new NullPointerException());

    // then
    response.consume(inner -> Assert.assertEquals("consumed value correct", inner, value),
                     throwable -> Assert.assertNull("no throwable", throwable));

    failed.consume(inner -> Assert.assertNull("empty value", value),
                     throwable -> Assert.assertNotNull("no throwable", throwable));

    Assert.assertEquals("stores value correctly", response.value(), value);
    Assert.assertTrue("shows value is available", response.hasValue());
  }

  @Test
  public void testEmpty() throws Exception {
    // when
    Response emptyResponse = Response.empty();

    // then
    Assert.assertFalse("shows as no value hasValue", emptyResponse.hasValue());
    Assert.assertTrue("shows response is empty", emptyResponse.isEmpty());
  }

}
