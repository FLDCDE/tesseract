package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Listener")
class ListenerTest {

  @Test
  @DisplayName("should notify every registered callback with the given value")
  void callback_MultipleRegistrations_NotifiesAll() {

    // Arrange: a listener with two callbacks registered
    var listener = Listener.<String>of();
    var firstCalls = new ArrayList<String>();
    var secondCalls = new ArrayList<String>();
    listener.register(firstCalls::add);
    listener.register(secondCalls::add);

    // Act: fire a callback
    listener.callback("hash-1");

    // Assert: both callbacks received the value
    assertThat(firstCalls).containsExactly("hash-1");
    assertThat(secondCalls).containsExactly("hash-1");
  }

  @Test
  @DisplayName("should do nothing when no callback is registered")
  void callback_NoRegistrations_NoOp() {

    // Arrange: a listener with no callbacks registered
    var listener = Listener.<String>of();

    // Act & Assert: firing a callback doesn't throw
    assertThatCode(() -> listener.callback("hash-1")).doesNotThrowAnyException();
  }

}
