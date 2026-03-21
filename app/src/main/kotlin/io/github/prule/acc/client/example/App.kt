package io.github.prule.acc.client.example

import io.github.prule.acc.client.AccClient
import io.github.prule.acc.client.AccClientConfiguration
import io.github.prule.acc.client.FilteredMessageListener
import io.github.prule.acc.client.JsonFormatter
import io.github.prule.acc.client.LoggingListener
import io.github.prule.acc.client.simulator.AccSimulator
import io.github.prule.acc.client.simulator.AccSimulatorConfiguration
import io.github.prule.acc.client.simulator.ClasspathSource
import io.github.prule.acc.messages.AccBroadcastingInbound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * A sample "hello world" equivalent demonstrating how to use the acc-client library.
 *
 * Ensure you have the simulator running (or real ACC running) and change the AccClientConfiguration
 * to use the appropriate port, server IP, connectionPassword.
 *
 * For information on setting up the simulator or ACC, see the acc-client [ReadMe.md](https://github.com/prule/acc-client/blob/main/ReadMe.md) file.
 */
fun main(args: Array<String>) =
    runBlocking {
        if (args.contains("--simulator")) {
            println("Starting ACC Simulator...")
            launch(Dispatchers.IO) {
                runAccSimulator()
            }
            // Give the simulator a moment to start up before the client tries to connect
            delay(1000)
        }

        println("Starting ACC Client Example...")
        runAccClientExample()
    }

private fun runAccSimulator() {
    AccSimulator(
        AccSimulatorConfiguration(
            port = 9000,
            connectionPassword = "asd",
            playbackEventsFile = ClasspathSource("io/github/prule/acc/client/simulator/playback-events.csv"),
        ),
    ).start()
}

private suspend fun runAccClientExample() {
    AccClient(
        AccClientConfiguration(
            name = "Example",
            port = 9000,
            serverIp = "127.0.0.1",
            updateMillis = 1000,
            connectionPassword = "asd",
        ),
    ).connect(
        listOf(
            // log everything
            LoggingListener(),
            // filter to only broadcast messages of type "lap completed" and print them
            FilteredMessageListener(
                AccBroadcastingInbound.BroadcastingEvent::class,
                { message -> message.type() == AccBroadcastingInbound.BroadcastType.LAPCOMPLETED },
                { message -> println("Lap completed ${JsonFormatter.toJsonString(message as Any)}") },
            ),
        ),
    )
}
