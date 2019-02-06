package com.vambraceservices;

import lombok.Data;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BuilderTest {

  @Data
  public static class TestClass {
    private TestInnerClass testValue;
  }

  @Data
  public static class TestInnerClass {
    private TestInnerInnerClass innerValue;
  }

  @Data
  public static class TestInnerInnerClass {
    private List<String> innerInnerValue;
  }

  @Test
  public void done() throws Exception {

    // given
    TestClass testClass = new TestClass();

    // when
    val innerInner = Builder.of(testClass, TestClass::new)
        .with(TestClass::getTestValue, t -> t::setTestValue, TestInnerClass::new)
        .with(TestInnerClass::getInnerValue, t-> t::setInnerValue, TestInnerInnerClass::new)
        .with(TestInnerInnerClass::getInnerInnerValue, t-> t::setInnerInnerValue, ArrayList::new)
        .done();

    // then
    Assert.assertNotNull(innerInner);
    Assert.assertNotNull(testClass.getTestValue());
    Assert.assertNotNull(testClass.getTestValue().getInnerValue());
    Assert.assertNotNull(testClass.getTestValue().getInnerValue().getInnerInnerValue());
    Assert.assertEquals(innerInner, testClass.getTestValue().getInnerValue().getInnerInnerValue());

  }

}
