package com.fieldcode.tesseract.collection;

import java.util.Iterator;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Signal;

import static com.fieldcode.tesseract.moment.Moments.ninf;
import static com.fieldcode.tesseract.signal.Signals.signal;

/**
 * This class represents an iterator that limits the boundaries in a forward direction. It extends the BoundaryLimiterBaseIterator class. <br/>
 * <b> NOTE: This implementation assumes that the signals are ordered in a forward direction by keys. Wrong order can lead to unexpected behavior. </b>
 * <br/> For boundary limiting explanation see the {@link BoundaryLimiterIterator} class.
 */

class BoundaryLimiterForwardIterator extends BoundaryLimiterBaseIterator {

  private static final Signal<Moment, Boolean> DEFAULT_KNOW_SIGNAL = signal(ninf(), false);

  private BoundaryLimiterForwardIterator(Iterator<Signal<Moment, Boolean>> input, Interval window) {
    super(input, window, DEFAULT_KNOW_SIGNAL);
    state(this::head);
  }

  static Iterator<Signal<Moment, Boolean>> of(Iterator<Signal<Moment, Boolean>> delegate, Interval window) {
    return new BoundaryLimiterForwardIterator(delegate, window);
  }

  private void head() {

    if (hasNoNextSignal()) {
      emitKnownAt(lower, upper);
      end();
      return;
    }

    var next = peekNextSignal();
    var nextAt = next.getKey();

    var arrangement = arrangement(nextAt);

    switch (arrangement) {
      case -2:
        memorize(next);
        ignoreNextSignal();
        break;
      case -1:
        state(this::tail);
        break;
      case 0:
        emitKnownAt(lower);
        state(this::tail);
        break;
      case 1:
        emitKnownAt(lower);
        emit(next);
        end();
        break;
      case 2:
        emitKnownAt(lower, upper);
        end();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + arrangement);
    }
  }

  private void tail() {

    if (hasNoNextSignal()) {
      emitKnownAt(upper);
      end();
      return;
    }

    var current = popNextSignal();
    var at = current.getKey();
    var arrangement = arrangement(at);

    switch (arrangement) {
      case -1:
      case 0:
        emit(current);
        memorize(current);
        break;
      case 1:
        emit(current);
        end();
        break;
      case 2:
        emitKnownAt(upper);
        end();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + arrangement);
    }

  }

}
