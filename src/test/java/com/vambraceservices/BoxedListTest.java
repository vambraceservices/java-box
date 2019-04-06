package com.vambraceservices;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BoxedListTest {

  TestListContainer<String> container = new TestListContainer<>();

  @Before
  public void setUp() {
    container.setContainedList(Arrays.asList("one", "two", "three"));
  }

  @Test
  public void of() {

    // given
    BoxedList<String> list = BoxedList.of(container.getContainedList());

    // then
    Assert.assertTrue("has copy of list",
                      list.size() == container.getContainedList().size());
  }
  @Test
  public void get() {

    // when (get list)

    List<String> valid = Boxed
        .of(container)
        .mapList(TestListContainer::getContainedList)
        .get();

    // then (list has values)

    Assert.assertFalse("has values", valid.isEmpty());

    // when (get invalid list)

    List<String> invalid = Boxed
        .of(container)
        .mapList(TestListContainer::getNullList)
        .get();

    // then (list has values)

    Assert.assertTrue("has values", invalid.isEmpty());

    // when (get list of any kind)

    List<String> linkedValid = Boxed
        .of(container)
        .mapList(TestListContainer::getContainedList)
        .get(LinkedList::new);

    // then (list has values)

    Assert.assertFalse("has values", linkedValid.isEmpty());
  }

  @Test
  public void first() throws Exception {

    // when (first in valid list)

    Optional<String> firstItem = Boxed
        .of(container)
        .mapList(TestListContainer::getContainedList)
        .first();

    // then
    Assert.assertTrue("gets first item safely", firstItem.isPresent());
    Assert.assertTrue("gets first item", firstItem.get().equalsIgnoreCase("one"));

    // when (get first)

    Optional<String> noItem = Boxed
        .of(container)
        .mapList(TestListContainer::getNullList)
        .first();

    // then
    Assert.assertFalse("gets first item safely", noItem.isPresent());

  }

  @Test
  public void size() throws Exception {

    int size = Boxed
        .of(container)
        .mapList(TestListContainer::getContainedList)
        .size();

    // then
    Assert.assertTrue("gets size safely", size == 3);

  }

  @Test
  public void stream() throws Exception {

    List<Integer> empty = Boxed
        .of(container)
        .mapList(TestListContainer::getNullList)
        .stream()
        .map(String::length)
        .collect(Collectors.toList());

    // then

    Assert.assertTrue(empty.size() == 0);

    // when (stream from a null list)

    List<Integer> valid = Boxed
        .of(container)
        .mapList(TestListContainer::getContainedList)
        .stream()
        .map(String::length)
        .collect(Collectors.toList());

    Assert.assertTrue(valid.size() == 3);

  }

  private class TestListContainer<T> {
    private List<T> containedList;
    private List<T> nullList;
    private String containedValue;

    public List<T> getContainedList() {
      return containedList;
    }

    public void setContainedList(List<T> containedList) {
      this.containedList = containedList;
    }

    public List<T> getNullList() {
      return nullList;
    }
  }

}
