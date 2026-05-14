package com.fieldcode.tesseract;

import java.util.function.Supplier;

public enum QueryDirection {

  FORWARD {
    @Override
    public <T> T select(T onForward, T onBackward) {
      return onForward;
    }

    @Override
    public <T> T get(Supplier<T> onForward, Supplier<T> onBackward) {
      return onForward.get();
    }

  },
  BACKWARD {
    @Override
    public <T> T select(T onForward, T onBackward) {
      return onBackward;
    }

    @Override
    public <T> T get(Supplier<T> onForward, Supplier<T> onBackward) {
      return onBackward.get();
    }

  };

  public abstract <T> T select(T onForward, T onBackward);

  public abstract <T> T get(Supplier<T> onForward, Supplier<T> onBackward);

}
