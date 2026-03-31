package io.github.prule.acc.client.example

import com.github.doyaaaaaken.kotlincsv.client.CsvFileWriter
import com.github.doyaaaaaken.kotlincsv.client.KotlinCsvExperimental
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import io.github.prule.acc.client.MessageListener
import io.github.prule.acc.client.MessageSender
import io.github.prule.acc.messages.AccBroadcastingInbound
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import java.time.LocalDateTime

@OptIn(KotlinCsvExperimental::class)
class DetailCsvWriterListener(
    directory: Path?,
) : MessageListener<AccBroadcastingInbound.RealtimeCarUpdate> {
  private val logger = LoggerFactory.getLogger(javaClass)
  private lateinit var writer: CsvFileWriter

  init {
    if (directory != null) {
      val targetDir = directory.toFile()
      targetDir.mkdirs()
      val filename = "simulator-recording-${dateToFilename()}.csv"
      writer = csvWriter().openAndGetRawWriter(File(targetDir, filename))
      writer.writeRow("date", "splinePosition", "type", "hex", "json")
      logger.debug("Writing $filename")
    } else {
      logger.debug("Csv Writer NOT enabled")
    }
  }

  override fun onStop() {
    writer.close()
  }

  private fun dateToFilename(): String = LocalDateTime.now().toString().replace(":", "-")

  override fun onMessage(
      bytes: ByteArray,
      message: AccBroadcastingInbound.RealtimeCarUpdate,
      messageSender: MessageSender,
  ) {
    writer.writeRow(
        listOf(
            LocalDateTime.now(),
            message.splinePosition(),
            message.kmh(),
            message.worldPosX(),
            message.worldPosY(),
            message.gear(),
            message.delta(),
            message.laps(),
            message.carIndex(),
        ),
    )
  }
}
