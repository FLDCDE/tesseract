package com.fieldcode.tesseract;

public interface MomentMapper<T> {

  T map(FiniteMoment finite);

  T map(InfiniteMoment infinite);

}
