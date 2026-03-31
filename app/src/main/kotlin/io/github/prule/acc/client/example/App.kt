package io.github.prule.acc.client.example

import io.github.prule.acc.client.AccClient
import io.github.prule.acc.client.AccClientConfiguration
import io.github.prule.acc.client.CsvWriterListener
import io.github.prule.acc.client.FilteredMessageListener
import io.github.prule.acc.client.JsonFormatter
import io.github.prule.acc.client.LoggingListener
import io.github.prule.acc.client.MessageListener
import io.github.prule.acc.client.MessageSender
import io.github.prule.acc.client.RegistrationResultListener
import io.github.prule.acc.client.simulator.AccSimulator
import io.github.prule.acc.client.simulator.AccSimulatorConfiguration
import io.github.prule.acc.client.simulator.ClasspathSource
import io.github.prule.acc.client.simulator.FileSource
import io.github.prule.acc.client.simulator.Source
import io.github.prule.acc.messages.AccBroadcastingInbound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

/**
 * A sample "hello world" equivalent demonstrating how to use the acc-client library.
 *
 * Ensure you have the simulator running (or real ACC running) and change the AccClientConfiguration
 * to use the appropriate port, server IP, connectionPassword.
 *
 * For information on setting up the simulator or ACC, see the acc-client
 * [ReadMe.md](https://github.com/prule/acc-client/blob/main/ReadMe.md) file.
 */
fun main(args: Array<String>) = runBlocking {
  val simulator = args.contains("--simulator")
  if (simulator) {
    println("Starting ACC Simulator...")
    val eventsArg = args.find { it.startsWith("--events=") }
    val source: Source =
        if (eventsArg != null) {
          val path = eventsArg.substringAfter("=")
          println("Using custom events file: $path")
          FileSource(path)
        } else {
          println("Using default classpath events file.")
          ClasspathSource("io/github/prule/acc/client/simulator/playback-events.csv")
        }

    launch(Dispatchers.IO) { runAccSimulator(source) }
    // Give the simulator a moment to start up before the client tries to connect
    delay(1000)
  }

  println("Starting ACC Client Example...")
  // record if we aren't using the simulator
  runAccClientExample(!simulator)
}

private fun runAccSimulator(source: Source) {
  AccSimulator(
          AccSimulatorConfiguration(
              port = 9000,
              connectionPassword = "asd",
              playbackEventsFile = source,
          ),
      )
      .start()
}

private suspend fun runAccClientExample(record: Boolean) {
  AccClient(
          AccClientConfiguration(
              name = "Example",
              port = 9000,
              //            serverIp = "127.0.0.1",
              serverIp = "192.168.86.50",
              updateMillis = 1000,
              connectionPassword = "asd",
          ),
      )
      .connect(
          listOfNotNull(
              // log everything
              LoggingListener(),
              RegistrationResultListener(),
              if (record)
                  CsvWriterListener(
                      Path.of("./recordings"),
                  )
              else null,
              // filter to only broadcast messages of type "lap completed" and print them
              FilteredMessageListener(
                  AccBroadcastingInbound.BroadcastingEvent::class,
                  { message ->
                    message.type() == AccBroadcastingInbound.BroadcastType.LAPCOMPLETED
                  },
                  // listeners to apply to filtered messages
                  listOf(
                      object : MessageListener<AccBroadcastingInbound.BroadcastingEvent> {
                        override fun onMessage(
                            bytes: ByteArray,
                            message: AccBroadcastingInbound.BroadcastingEvent,
                            messageSender: MessageSender,
                        ) {
                          println("Lap completed ${JsonFormatter.toJsonString(message as Any)}")
                        }
                      },
                  ),
              ),
          )
      )
}
