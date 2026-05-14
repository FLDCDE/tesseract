package com.fieldcode.tesseract.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.Iterators;
import com.fieldcode.tesseract.utils.iterator.StateIterator;

public class EventCounterIterator extends StateIterator<Signal<Moment, Integer>> {

  private final Moment lower;
  private final Moment upper;
  private final FluentIterator<List<Signal<Moment, Boolean>>> bounds;

  private int counter;

  private EventCounterIterator(List<? extends Iterator<Signal<Moment, Boolean>>> sources, Interval window, QueryDirection direction) {
    this.lower = window.getLower();
    this.upper = window.getUpper();
    this.bounds = Iterators.multiplex(sources, IntervalBoundComparator.of(direction));
  }

  public static FluentIterator<Signal<Moment, Integer>> of(List<? extends IntervalCollection> collections, Interval window, QueryDirection direction) {
    var sources = new ArrayList<Iterator<Signal<Moment, Boolean>>>(collections.size());
    for (IntervalCollection collection : collections) {
      sources.add(collection.bounds(window, direction));
    }

    return Iterators.fluent(new EventCounterIterator(sources, window, direction));
  }

  @Override
  protected void start() {
    var keyBounds = getNextKeyBounds();
    if (keyBounds.isEmpty()) {
      end();
      return;
    }
    count(keyBounds);
    var key = keyBounds.get(0).getKey();
    emit(Signals.signal(key, counter));
  }

  private List<Signal<Moment, Boolean>> getNextKeyBounds() {
    return bounds.hasNext()
        ? bounds.next()
        : List.of();
  }

  private void count(List<Signal<Moment, Boolean>> keyBounds) {
    for (Signal<Moment, Boolean> keyBound : keyBounds) {
      count(keyBound);
    }
  }

  private void count(Signal<Moment, Boolean> signal) {
    var key = signal.getKey();
    var value = signal.getValue();
    if ((key.eq(lower) || key.eq(upper))) {
      counter = counter + (value ? 1 : 0);
    } else {
      counter = counter + (value ? 1 : -1);
    }
  }
}
