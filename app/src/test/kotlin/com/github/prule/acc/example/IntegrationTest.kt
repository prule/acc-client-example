package com.github.prule.acc.example

import com.github.prule.acc.client.AccClient
import com.github.prule.acc.client.AccClientConfiguration
import com.github.prule.acc.client.MessageListener
import com.github.prule.acc.client.MessageSender
import com.github.prule.acc.client.simulator.AccSimulator
import com.github.prule.acc.client.simulator.AccSimulatorConfiguration
import com.github.prule.acc.client.simulator.ClasspathSource
import com.github.prule.acc.messages.AccBroadcastingInbound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds

class IntegrationTest {

  @Test
  fun `should connect and receive messages from simulator`() = runBlocking {
    val receivedMessage = AtomicBoolean(false)
    val port = 9005 // Use a different port for tests

    // 1. Start simulator
    val simulator =
        AccSimulator(
            AccSimulatorConfiguration(
                port = port,
                connectionPassword = "test",
                playbackEventsFile =
                    ClasspathSource("com/github/prule/acc/client/simulator/playback-events.csv"),
            )
        )

    val simulatorJob = launch(Dispatchers.IO) { simulator.start() }

    try {
      // 2. Start client
      val client =
          AccClient(
              AccClientConfiguration(
                  name = "TestClient",
                  port = port,
                  serverIp = "127.0.0.1",
                  updateMillis = 100,
                  connectionPassword = "test",
              )
          )

      val listener =
          object : MessageListener<AccBroadcastingInbound> {
            override fun onMessage(
                bytes: ByteArray,
                message: AccBroadcastingInbound,
                messageSender: MessageSender,
            ) {
              if (message is AccBroadcastingInbound.RealtimeUpdate) {
                receivedMessage.set(true)
              }
            }
          }

      launch { client.connect(listOf(listener)) }

      // 3. Wait for message with timeout
      withTimeout(5000.milliseconds) {
        while (!receivedMessage.get()) {
          delay(100)
        }
      }

      assertThat(receivedMessage.get()).isTrue
    } finally {
      simulatorJob.cancel()
    }
    Unit
  }
}
