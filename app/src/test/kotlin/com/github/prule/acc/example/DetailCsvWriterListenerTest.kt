package com.github.prule.acc.example

import com.github.prule.acc.client.MessageSender
import com.github.prule.acc.messages.AccBroadcastingInbound
import io.mockk.every
import io.mockk.mockk
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class DetailCsvWriterListenerTest {

  @TempDir lateinit var tempDir: Path

  @Test
  fun `should create csv file and write header on init`() {
    DetailCsvWriterListener(tempDir)

    val files = Files.list(tempDir).toList()
    assertThat(files).hasSize(1)
    assertThat(files[0].name).startsWith("simulator-recording-").endsWith(".csv")

    val lines = Files.readAllLines(files[0])
    assertThat(lines).isNotEmpty
    assertThat(lines[0]).isEqualTo("date,splinePosition,type,hex,json")
  }

  @Test
  fun `should write data row when message received`() {
    val listener = DetailCsvWriterListener(tempDir)
    val message =
      mockk<AccBroadcastingInbound.RealtimeCarUpdate> {
        every { splinePosition() } returns 0.5f
        every { kmh() } returns 200
        every { worldPosX() } returns 10.0f
        every { worldPosY() } returns 20.0f
        every { gear() } returns 4
        every { delta() } returns 123
        every { laps() } returns 10
        every { carIndex() } returns 1
      }
    val sender = mockk<MessageSender>()

    listener.onMessage(byteArrayOf(), message, sender)
    listener.onStop()

    val file = Files.list(tempDir).toList()[0]
    val lines = Files.readAllLines(file)

    assertThat(lines).hasSize(2)
    val dataRow = lines[1]
    assertThat(dataRow).contains("0.5")
    assertThat(dataRow).contains("200")
    assertThat(dataRow).contains("10.0")
    assertThat(dataRow).contains("20.0")
    assertThat(dataRow).contains(",4,")
    assertThat(dataRow).contains(",10,")
    // carIndex is at the end of the line, so it won't be followed by a comma
    assertThat(dataRow).endsWith(",1")
  }
}
