package com.vambraceservices;

import com.wsi.services.core.exceptions.InternalServerErrorException;
import com.wsi.services.core.validators.ServiceResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

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

    // given (failed response)
    Response<String> errorResponse =
        Response.failed(
            new InternalServerErrorException(
                "error",
                new ServiceResponse().withHttpStatus(HttpStatus.BAD_REQUEST)));

    // when (map to new response)
    Response<String> stringResponse = errorResponse.to(r -> r);

    // then (throwable is persisted)
    Assert.assertTrue("response is still a failure", stringResponse.hasError());
    Assert.assertNotEquals("response is not original failure response", errorResponse, stringResponse);

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
          Response.failed(
              new InternalServerErrorException(
                  "error",
                  new ServiceResponse().withHttpStatus(HttpStatus.BAD_REQUEST)));

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
    Response<String> failed = Response.failed(new InternalServerErrorException("message", Collections.emptyList()));

    // then
    response.consume(inner -> Assert.assertEquals("consumed value correct", inner, value),
                     throwable -> Assert.assertNull("no throwable", throwable));

    failed.consume(inner -> Assert.assertNull("empty value", value),
                     throwable -> Assert.assertNotNull("no throwable", throwable));

    Assert.assertEquals("stores value correctly", response.value(), value);
    Assert.assertTrue("shows value is available", response.hasValue());
  }

  @Test
  public void testFailed() throws Exception {
    // given
    String errorCode = "1234";

    // when
    Response failedResponse =
        Response.failed(
            new InternalServerErrorException(
                "error",
                new ServiceResponse().withHttpStatus(HttpStatus.BAD_REQUEST)));

    // then
    Assert.assertEquals("record throwable",
                        ((InternalServerErrorException)failedResponse.error())
                            .getErrors()
                            .get(0)
                            .getHttpStatus(), HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testEmpty() throws Exception {
    // when
    Response emptyResponse = Response.empty();

    // then
    Assert.assertFalse("shows as no value hasValue", emptyResponse.hasValue());
    Assert.assertTrue("shows response is empty", emptyResponse.isEmpty());
  }

  @Test(expected = InternalServerErrorException.class)
  public void elseThrowFunction() {
    // given
    Response<String> hasValue = Response.from("hello");
    // when
    hasValue.elseThrow(f -> new InternalServerErrorException("message", Collections.emptyList()));
    // given
    Response<String> failed = Response.failed(new NullPointerException());
    // when
    failed.elseThrow(f -> new InternalServerErrorException("message", Collections.emptyList()));
  }

  @Test(expected = InternalServerErrorException.class)
  public void elseThrowSupplier() {
    // given
    Response<String> hasValue = Response.from("hello");
    // when
    hasValue.elseThrow(() -> new InternalServerErrorException("message", Collections.emptyList()));
    // given
    Response<String> failed = Response.failed(new NullPointerException());
    // when
    failed.elseThrow(() -> new InternalServerErrorException("message", Collections.emptyList()));
  }

  @Test
  public void testToString() {

    // when
    Response<String> empty = Response.empty();
    // then
    Assert.assertTrue("empty", "".equals(empty.toString()));
    // when
    Response<String> failed = Response.failed(new NullPointerException());
    // then
    Assert.assertTrue("has error", failed.toString().length() > 0);
    // when
    Response<String> value = Response.from("hello");
    // then
    Assert.assertTrue("empty", "hello".equalsIgnoreCase(value.toString()));
  }

  @Test
  public void testElseReturnsWithEmptyValue() {
    // when
    String alternativeValue = "alternativeValue";
    Response<String> empty = Response.empty();

    //when
    String returnedValue = empty.elseReturn(alternativeValue);

    // then
    Assert.assertEquals("alternative value must be returned", alternativeValue, returnedValue);
  }

  @Test
  public void testElseReturnsWithValue() {
    // when
    String originalValue = "originalValue";
    String alternativeValue = "alternativeValue";
    Response<String> response = Response.from(originalValue);

    //when
    String returnedValue = response.elseReturn(alternativeValue);

    // then
    Assert.assertEquals("original value must be returned", originalValue, returnedValue);
  }

  @Test
  public void testHttpStatusCode() {
    // when
    Response<String> response = Response.from("value").withHttpStatus(HttpStatus.BAD_GATEWAY);
    // then
    Assert.assertEquals("stores the http status correctly", HttpStatus.BAD_GATEWAY, response.getHttpStatus());
  }

}
