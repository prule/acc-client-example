package io.github.prule.acc.client.example

import io.github.prule.acc.client.AccClient
import io.github.prule.acc.client.AccClientConfiguration
import io.github.prule.acc.client.FilteredMessageListener
import io.github.prule.acc.client.LoggingListener
import io.github.prule.acc.messages.AccBroadcastingInbound
import kotlinx.coroutines.runBlocking

/**
 * A sample "hello world" equivalent demonstrating how to use the acc-client library.
 *
 * Ensure you have the simulator running (or real ACC running) and change the AccClientConfiguration
 * to use the appropriate port, server IP, connectionPassword.
 *
 * For
 */
fun main() {
    runBlocking {
        AccClient(
            AccClientConfiguration(
                name = "Example",
                port = 9996,
                serverIp = "127.0.0.1",
                updateMillis = 1000,
                connectionPassword = "asd",
            ),
        ).connect(
            listOf(
                LoggingListener(),
                FilteredMessageListener(
                    AccBroadcastingInbound.BroadcastingEvent::class,
                    { message -> message.type() == AccBroadcastingInbound.BroadcastType.LAPCOMPLETED },
                    { println("Lap completed $it") },
                ),
            ),
        )
    }
}
