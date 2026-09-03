package com.fieldcode.tesseract.location;

import com.fieldcode.tesseract.Location;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class ProximitySort {

  private static final Comparator<ItemWithDistance<?>> COMPARATOR = Comparator.comparingDouble(i -> i.distance);
  private static final Comparator<ItemWithDistance<?>> REVERSED_COMPARATOR = Comparator.<ItemWithDistance<?>>comparingDouble(i -> i.distance).reversed();

  private ProximitySort() {}

  static <T> Stream<T> sort(Iterable<T> input, Location reference, Function<T, ? extends Location> mapper, ToDoubleBiFunction<Location, Location> resolver, boolean reverse) {

    var comparator = reverse
        ? REVERSED_COMPARATOR
        : COMPARATOR;

    return StreamSupport.stream(input.spliterator(), false)
        .map(item -> new ItemWithDistance<>(item, resolver.applyAsDouble(reference, mapper.apply(item))))
        .sorted(comparator)
        .map(itemWithDistance -> itemWithDistance.item);
  }

  private static class ItemWithDistance<T> {

    private final T item;
    private final double distance;

    private ItemWithDistance(T item, double distance) {
      this.item = item;
      this.distance = distance;
    }

  }

}
