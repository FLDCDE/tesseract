package com.fieldcode.tesseract.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.fieldcode.tesseract.FluentIterator;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.QueryDirection;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.moment.MomentPredicates;
import com.fieldcode.tesseract.signal.Signals;
import com.fieldcode.tesseract.utils.iterator.MultiEmmitStateIterator;
import com.google.common.collect.PeekingIterator;

import static com.fieldcode.tesseract.utils.iterator.Iterators.fluent;
import static com.google.common.collect.Iterators.peekingIterator;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;

public class SignalExpanderIterator extends MultiEmmitStateIterator<Signal<Moment, Boolean>> {

  private final Comparator<Moment> comparator;
  private final PeekingIterator<Moment> moments;
  private final PeekingIterator<Signal<Moment, Boolean>> signals;

  private boolean previousValue = false;

  public SignalExpanderIterator(Iterator<Signal<Moment, Boolean>> signals, Collection<Moment> moments, QueryDirection direction) {
    this.comparator = direction.select(naturalOrder(), reverseOrder());

    this.signals = peekingIterator(signals);
    this.moments = peekingIterator(orderedMomentsIterator(moments, comparator));

    state(this::loop);
  }

  static FluentIterator<Signal<Moment, Boolean>> of(Iterator<Signal<Moment, Boolean>> delegate, Collection<Moment> moments, QueryDirection direction) {
    return fluent(new SignalExpanderIterator(delegate, moments, direction));
  }

  public static FluentIterator<Signal<Moment, Boolean>> crop(Iterator<Signal<Moment, Boolean>> signals, Interval window, QueryDirection direction) {

    var iterator = SignalExpanderIterator.of(
        signals,
        List.of(window.getLower(), window.getUpper()),
        direction
    );

    var momentPredicate = MomentPredicates.between(window.getLower(), true, window.getUpper(), true);

    return iterator.filter(signal -> momentPredicate.test(signal.getKey()));
  }

  private static Iterator<Moment> orderedMomentsIterator(Collection<Moment> moments, Comparator<Moment> comparator) {
    return moments
        .stream()
        .sorted(comparator)
        .distinct()
        .iterator();
  }

  private void loop() {

    // If there are no more signals, we emit the remaining moments and then end.
    if (!signals.hasNext()) {
      emitRemainingThan(moments, this::signal, this::end);
      return;
    }

    // If there are no more moments, we emit the remaining signals and then end.
    if (!moments.hasNext()) {
      emitRemainingThan(signals, this::end);
      return;
    }

    var signal = signals.peek();
    var moment = moments.peek();
    var momentSignalRelation = comparator.compare(moment, signal.getKey());

    // If the moment is equal to the signal, we emit only the signal and the moment can be skipped.
    if (momentSignalRelation == 0) {
      emitSignalAndSetPrevious(signal);
      moments.next();
      signals.next();
      return;
    }

    // If the moment is before the signal, we emit the moment and leave the signal for the next iteration.
    if (momentSignalRelation < 0) {
      emitSignalAndSetPrevious(signal(moment));
      moments.next();
      return;
    }

    // If the moment is after the signal, we emit the signal and leave the moment for the next iteration.
    emitSignalAndSetPrevious(signal);
    signals.next();
  }

  private Signal<Moment, Boolean> signal(Moment at) {
    return Signals.signal(at, previousValue);
  }

  private void emitSignalAndSetPrevious(Signal<Moment, Boolean> signal) {
    emit(signal);
    previousValue = signal.getValue();
  }

}
