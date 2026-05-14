package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;

import static com.fieldcode.tesseract.moment.Moments.inf;
import static com.fieldcode.tesseract.signal.Signals.signal;

/**
 * This class represents an iterator that limits the boundaries in a backward direction. It extends the BoundaryLimiterBaseIterator class. <br/>
 * <b> NOTE: This implementation assumes that the signals are ordered in a backward direction by keys. Wrong order can lead to unexpected behavior. </b>
 * <br/> For boundary limiting explanation see the {@link BoundaryLimiterIterator} class.
 */
class BoundaryLimiterBackwardIterator extends BoundaryLimiterBaseIterator {

  private static final Signal<Moment, Boolean> DEFAULT_KNOWN_SIGNAL = signal(inf(), false);

  private BoundaryLimiterBackwardIterator(Iterator<Signal<Moment, Boolean>> delegate, Interval window) {
    super(delegate, window, DEFAULT_KNOWN_SIGNAL);
    state(this::head);
  }

  static Iterator<Signal<Moment, Boolean>> of(Iterator<Signal<Moment, Boolean>> delegate, Interval window) {
    return new BoundaryLimiterBackwardIterator(delegate, window);
  }

  private void head() {
    if (hasNoNextSignal()) {
      emitKnownAt(upper, lower);
      end();
      return;
    }

    var next = peekNextSignal();
    var nextAt = next.getKey();

    var arrangement = arrangement(nextAt);

    switch (arrangement) {
      case 2:
        memorize(next);
        ignoreNextSignal();
        break;
      case 1:
        state(this::tail);
        break;
      case 0:
        emitKnownAt(upper);
        state(this::tail);
        break;
      case -1:
        emitKnownAt(upper);
        emit(next);
        end();
        break;
      case -2:
        emitKnownAt(upper, lower);
        end();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + arrangement);
    }
  }

  private void tail() {

    if (hasNoNextSignal()) {
      emitKnownAt(lower);
      end();
      return;
    }

    var current = popNextSignal();
    var at = current.getKey();
    var arrangement = arrangement(at);

    switch (arrangement) {
      case 1:
      case 0:
        emit(current);
        memorize(current);
        break;
      case -1:
        emit(current);
        end();
        break;
      case -2:
        emitKnownAt(lower);
        end();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + arrangement);
    }

  }

}
