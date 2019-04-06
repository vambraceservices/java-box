package com.vambraceservices;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BuilderTest {

  public static class TestClass {
    private TestInnerClass testValue;

    public TestInnerClass getTestValue() {
      return testValue;
    }

    public void setTestValue(TestInnerClass testValue) {
      this.testValue = testValue;
    }
  }

  public static class TestInnerClass {
    private TestInnerInnerClass innerValue;

    public TestInnerInnerClass getInnerValue() {
      return innerValue;
    }

    public void setInnerValue(TestInnerInnerClass innerValue) {
      this.innerValue = innerValue;
    }
  }

  public static class TestInnerInnerClass {
    private List<String> innerInnerValue;

    public List<String> getInnerInnerValue() {
      return innerInnerValue;
    }

    public void setInnerInnerValue(List<String> innerInnerValue) {
      this.innerInnerValue = innerInnerValue;
    }
  }

  @Test
  public void done() throws Exception {

    // given
    TestClass testClass = new TestClass();

    // when
    List<String> innerInner = Builder.of(testClass)
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
