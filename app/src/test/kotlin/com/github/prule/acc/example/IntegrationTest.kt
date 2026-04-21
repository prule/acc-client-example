package com.github.prule.acc.example

import com.github.prule.acc.client.AccClient
import com.github.prule.acc.client.AccClientConfiguration
import com.github.prule.acc.client.MessageListener
import com.github.prule.acc.client.MessageSender
import com.github.prule.acc.client.simulator.AccSimulator
import com.github.prule.acc.client.simulator.AccSimulatorConfiguration
import com.github.prule.acc.client.simulator.ClasspathSource
import com.github.prule.acc.messages.AccBroadcastingInbound
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class IntegrationTest {
  private val logger = LoggerFactory.getLogger(javaClass)

  @Test
  fun `should connect and receive messages from simulator`() = runBlocking {
    val receivedMessage = AtomicBoolean(false)
    val port = 9006 // Use a unique port

    val simulator =
      AccSimulator(
        AccSimulatorConfiguration(
          port = port,
          connectionPassword = "test",
          playbackEventsFile =
            ClasspathSource("com/github/prule/acc/client/simulator/playback-events.csv"),
        )
      )

    val simulatorJob =
      launch(Dispatchers.IO) {
        try {
          simulator.start()
        } catch (e: Exception) {
          logger.debug("Simulator stopped: ${e.message}")
        }
      }

    delay(1000.milliseconds)

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
          logger.debug("Received message: ${message.javaClass.simpleName}")
          // Accept any inbound message as proof of connection and communication
          receivedMessage.set(true)
        }
      }

    val clientJob =
      launch(Dispatchers.IO) {
        try {
          client.connect(listOf(listener))
        } catch (e: Exception) {
          logger.debug("Client stopped: ${e.message}")
        }
      }

    try {
      withTimeout(15.seconds) {
        while (!receivedMessage.get()) {
          delay(100.milliseconds)
        }
      }
    } finally {
      clientJob.cancel()
      simulatorJob.cancel()

      try {
        val socketField = simulator.javaClass.getDeclaredField("socket")
        socketField.isAccessible = true
        val socket = socketField.get(simulator) as? java.net.DatagramSocket
        socket?.close()
      } catch (_: Exception) {}
    }

    assertThat(receivedMessage.get()).isTrue
    Unit
  }
}
