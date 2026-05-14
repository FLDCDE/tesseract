package com.fieldcode.tesseract.matrix.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Listener<T> {

  private final List<Consumer<T>> callbacks = new ArrayList<>();

  private Listener() {
  }

  public static <T> Listener<T> of() {
    return new Listener<>();
  }

  public void register(Consumer<T> callback) {
    callbacks.add(callback);
  }

  public void callback(T value) {
    callbacks.forEach(a -> a.accept(value));
  }

}
