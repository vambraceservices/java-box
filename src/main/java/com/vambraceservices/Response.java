package com.vambraceservices;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Response<T> {

  private static final String EMPTY_STRING = "";
  private T      inner;
  private Throwable error;
  private Response() {
  }

  private Response(T seed) {
    this.inner = seed;
  }

  public static <T> Response<T> from(T item) {
    Response<T> response = new Response<>();
    response.inner = item;
    return response;
  }

  public <N> Response<N> to(Predicate<T> predicate, Function<T, N> fn) {
    if (this.hasError()) {
      return Response.failed(this.error);
    }
    if (this.hasValue() && predicate.test(this.inner)) {
      return Response.from(fn.apply(this.inner));
    }
    return Response.empty();
  }

  public <N> Response<N> to(Function<T, N> fn) {
    if (this.hasError()) {
      return Response.failed(this.error);
    }
    if (this.hasValue()) {
      return Response.from(fn.apply(this.inner));
    }
    return Response.empty();
  }

  public <N> N use(Function<T, N> fn, N fallback) {
    if (this.hasValue()) {
      return fn.apply(this.inner);
    }
    return fallback;
  }

  public <N> Response<N> as(Function<T, Response<N>> fn) {
    if (this.hasError()) {
      return Response.failed(this.error);
    }
    if (this.hasValue()) {
      return fn.apply(this.inner);
    }
    return Response.empty();
  }

  public static <T> Response<T> failed(Throwable exception) {
    Response<T> response = new Response<>();
    response.error = exception;
    return response;
  }

  public static <N> Response<N> failed(N value, Response original) {
    Response<N> response = new Response<>();
    response.inner = value;
    response.error = original.error;
    return response;
  }

  public static <T> Response<T> empty() {
    return new Response<>(null);
  }

  public T value() {
    return this.inner;
  }

  public Boolean hasValue() {
    return !hasNoValue();
  }

  public Boolean hasNoValue() {
    return Objects.isNull(this.inner);
  }

  public Boolean hasError() {
    return !hasNoError();
  }

  public Boolean hasNoError() {
    return Objects.isNull(this.error);
  }

  public Throwable error() {
    return this.error;
  }

  public Boolean isEmpty() {
    return Objects.isNull(this.inner);
  }

  public Response<T> consumeValue(Consumer<T> consumer) {
    if (consumer != null && this.hasValue()) {
      consumer.accept(this.inner);
    }
    return this;
  }

  public Response<T> consumeError(Consumer<Throwable> errorConsumer) {
    if (errorConsumer != null && this.hasError()) {
      errorConsumer.accept(this.error);
    }
    return this;
  }

  public Response<T> consume(Consumer<T> consumer, Consumer<Throwable> errorConsumer) {
    this.consumeValue(consumer);
    this.consumeError(errorConsumer);
    return this;
  }

  public <X extends Throwable> T elseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (this.hasValue()) {
      return this.inner;
    } else {
      throw exceptionSupplier.get();
    }
  }

  public <X extends Throwable> T elseThrow(Function<Throwable, ? extends X> exceptionSupplier) throws X {
    if (this.hasValue()) {
      return this.inner;
    } else {
      throw exceptionSupplier.apply(this.error);
    }
  }

  public T elseReturn(T alternative) {
    if (this.hasValue()) {
      return this.inner;
    } else {
      return alternative;
    }
  }

  @Override
  public String toString() {
    if (this.hasValue()) {
      return this.value().toString();

    } else if (this.hasError()) {
      return this.error.toString();
    }
    return EMPTY_STRING;
  }
}
