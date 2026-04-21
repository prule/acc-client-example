# acc-client-example

> This sample application depends on https://github.com/prule/acc-client

To recieve messages from a running ACC installation, you need to check its configuration and make sure you use the same values in `AccClient`.
You'll need to start a driving session in ACC eg be on the track/in the pit for messages to be sent.

> Configuring ACC:
> In `C:\Users\<username>\Documents\Assetto Corsa Competizione\Config\broadcasting.json` you can find the broadcasting configuration - you'll need the port and connection password from here:
> ```json
> {                                                                                                                         
>   "updListenerPort": 9000,
>   "connectionPassword": "asd",
>   "commandPassword": ""
> }
> ```
> For information on ACC broadcasting and the ACC Broadcasting Test Client see https://github.com/prule/acc-messages/blob/main/docs/AccBroadcastingExample/ReadMe.md

## About

This is a small "hello world" example showing how to use [ACC Client](https://github.com/prule/acc-client) to listen to broadcast messages.

## Demonstration

### Start the simulator first

```shell
./gradlew app:runAccSimulator
```

### Start the example application

```shell
./gradlew app:runSampleApp
```

### Simulator output

The simulator starts, waits to receive a registration message, responds with a registration result message, then starts to send events as defined in playback-events.csv.

```text
> Task :app:runAccSimulator
13:39:13.005 [main] DEBUG com.github.prule.acc.client.simulator.AccSimulator -- Starting simulator
13:39:23.230 [main] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 010407004578616d706c650300617364e80300000300617364 {"msgType":"REGISTER_COMMAND_APPLICATION","body":{"protocolVersion":4,"displayName":{"length":7,"data":"Example"},"connectionPassword":{"length":3,"data":"asd"},"msRealtimeUpdateInterval":1000,"commandPassword":{"length":3,"data":"asd"}}}
13:39:23.230 [main] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 010500000001010000
13:39:23.377 [DefaultDispatcher-worker-2] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 02000000000a0504988b49f00bae480d00000008004472697661626c650700436f636b706974090042617369632048554400ba9657471e26000000d96b01000500000003d15b0000bc9f00004b70000000010000
13:39:23.880 [DefaultDispatcher-worker-2] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 030000000001031948d6c2675fb34317642240017800010001000000f623f33d0c00fbffffffff6d01000000000003fe5b0000c8a0000007710000000100004070010000000000035f5c000054a100008d7200000001000032280000000000000000010000
13:39:24.386 [DefaultDispatcher-worker-2] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 0412000000190000000400020005000100070006000a00080009000c000f0010000e0003001300180012000b0014001700150016000d001100
13:39:24.887 [DefaultDispatcher-worker-2] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 05120000000d005265642042756c6c2052696e671a000000de1000000708004472697661626c650705004368617365080046617243686173650600426f6e6e657407004461736850726f0700436f636b706974040044617368060048656c6d6574070048656c6963616d01070048656c6963616d07004f6e626f6172640408004f6e626f6172643008004f6e626f6172643108004f6e626f6172643208004f6e626f6172643307007069746c616e65020c007069746c616e655f43616d310c007069746c616e655f43616d320400736574310d0900536574315f43616d330900536574315f43616d340900536574315f43616d350900536574315f43616d360900536574315f43616d370900536574315f43616d390a00536574315f43616d31300a00536574315f43616d31310a00536574315f43616d31320a00536574315f43616d31330d00536574315f43616d31345f31340900536574315f43616d310900536574315f43616d320400736574320a0900536574325f43616d330900536574325f43616d340900536574325f43616d350900536574325f43616d360900536574325f43616d370900536574325f43616d380900536574325f43616d390a00536574325f43616d31300900536574325f43616d310900536574325f43616d32050073657456520b090043616d657261565231090043616d657261565232090043616d657261565233090043616d657261565234090043616d657261565235090043616d657261565236090043616d657261565237090043616d657261565238090043616d6572615652390a0043616d657261565231300a0043616d65726156523131060500426c616e6b0900426173696320485544040048656c70090054696d655461626c650c0042726f616463617374696e670800547261636b4d6170
13:39:25.388 [DefaultDispatcher-worker-2] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 060000010c00426c61636b2046616c636f6e04000000000002000104004c756361050053746f6c7a030053544f010200
13:39:25.894 [DefaultDispatcher-worker-2] DEBUG com.github.prule.acc.client.MessageSender -- Sending bytes: 0705090030313a33362e373435f360120006000000
```

### Application output

The application uses AccClient to connect to the simulator, with several listeners.
AccClient will send a registration message, then start receiving messages send by the simulator.

The listeners:

- LoggingListener logs every message received
- FilteredMessageListener is set up to only handle Broadcast messages of type Lap Completed. When one arrives, it prints out "Lap Completed ... "

```text
> Task :app:runSampleApp
13:39:22.096 [main] DEBUG com.github.prule.acc.client.AccClient -- Connecting to server
13:39:23.117 [DefaultDispatcher-worker-1] DEBUG com.github.prule.acc.client.AccClient -- Sent register command, listening for data
13:39:23.370 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 010500000001010000 {"msgType":"REGISTRATION_RESULT","body":{"connectionId":5,"connectionSuccess":1,"isReadOnly":1,"errorMessage":{"length":0,"data":""}}}
13:39:23.373 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Unmatched message
13:39:23.387 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 02000000000a0504988b49f00bae480d00000008004472697661626c650700436f636b706974090042617369632048554400ba9657471e26000000d96b01000500000003d15b0000bc9f00004b70000000010000 {"msgType":"REALTIME_UPDATE","body":{"eventIndex":0,"sessionIndex":0,"sessionType":null,"phase":"SESSION","sessionTimeMs":1143552.5,"sessionEndTimeMs":356447.5,"focusedCarIndex":13,"activeCameraSet":{"length":8,"data":"Drivable"},"activeCamera":{"length":7,"data":"Cockpit"},"currentHudPage":{"length":9,"data":"Basic HUD"},"isReplayPlaying":0,"replaySessionTime":null,"replayRemainingTime":null,"timeOfDaySeconds":55190.727,"ambientTemp":30,"trackTemp":38,"clouds":0,"rainLevel":0,"wetness":0,"bestSessionLap":{"lapTimeMs":93145,"carIndex":5,"driverIndex":0,"numSplits":3,"splits":[23505,40892,28747],"isInvalid":0,"isValidForBest":1,"isOutlap":0,"isInlap":0}}}
13:39:23.387 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Unmatched message
13:39:23.884 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 030000000001031948d6c2675fb34317642240017800010001000000f623f33d0c00fbffffffff6d01000000000003fe5b0000c8a0000007710000000100004070010000000000035f5c000054a100008d7200000001000032280000000000000000010000 {"msgType":"REALTIME_CAR_UPDATE","body":{"carIndex":0,"driverIndex":0,"driverCount":1,"gear":3,"worldPosX":-107.140816,"worldPosY":358.74533,"yaw":2.537359,"carLocation":"TRACK","kmh":120,"position":1,"cupPosition":1,"trackPosition":0,"splinePosition":0.118720934,"laps":12,"delta":-5,"bestSessionLap":{"lapTimeMs":93695,"carIndex":0,"driverIndex":0,"numSplits":3,"splits":[23550,41160,28935],"isInvalid":0,"isValidForBest":1,"isOutlap":0,"isInlap":0},"lastLap":{"lapTimeMs":94272,"carIndex":0,"driverIndex":0,"numSplits":3,"splits":[23647,41300,29325],"isInvalid":0,"isValidForBest":1,"isOutlap":0,"isInlap":0},"currentLap":{"lapTimeMs":10290,"carIndex":0,"driverIndex":0,"numSplits":0,"splits":[],"isInvalid":0,"isValidForBest":1,"isOutlap":0,"isInlap":0}}}
13:39:23.884 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Unmatched message
13:39:24.387 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 0412000000190000000400020005000100070006000a00080009000c000f0010000e0003001300180012000b0014001700150016000d001100 {"msgType":"ENTRY_LIST","body":{"connectionId":18,"numCarIndexes":25,"carIndexes":[0,4,2,5,1,7,6,10,8,9,12,15,16,14,3,19,24,18,11,20,23,21,22,13,17]}}
13:39:24.387 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Unmatched message
13:39:24.891 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 05120000000d005265642042756c6c2052696e671a000000de1000000708004472697661626c650705004368617365080046617243686173650600426f6e6e657407004461736850726f0700436f636b706974040044617368060048656c6d6574070048656c6963616d01070048656c6963616d07004f6e626f6172640408004f6e626f6172643008004f6e626f6172643108004f6e626f6172643208004f6e626f6172643307007069746c616e65020c007069746c616e655f43616d310c007069746c616e655f43616d320400736574310d0900536574315f43616d330900536574315f43616d340900536574315f43616d350900536574315f43616d360900536574315f43616d370900536574315f43616d390a00536574315f43616d31300a00536574315f43616d31310a00536574315f43616d31320a00536574315f43616d31330d00536574315f43616d31345f31340900536574315f43616d310900536574315f43616d320400736574320a0900536574325f43616d330900536574325f43616d340900536574325f43616d350900536574325f43616d360900536574325f43616d370900536574325f43616d380900536574325f43616d390a00536574325f43616d31300900536574325f43616d310900536574325f43616d32050073657456520b090043616d657261565231090043616d657261565232090043616d657261565233090043616d657261565234090043616d657261565235090043616d657261565236090043616d657261565237090043616d657261565238090043616d6572615652390a0043616d657261565231300a0043616d65726156523131060500426c616e6b0900426173696320485544040048656c70090054696d655461626c650c0042726f616463617374696e670800547261636b4d6170 {"msgType":"TRACK_DATA","body":{"connectionId":18,"trackName":{"length":13,"data":"Red Bull Ring"},"trackId":26,"trackMeters":4318,"numCameraSets":7,"cameraSets":[{"cameraSetName":{"length":8,"data":"Drivable"},"numCameras":7,"cameras":[{"length":5,"data":"Chase"},{"length":8,"data":"FarChase"},{"length":6,"data":"Bonnet"},{"length":7,"data":"DashPro"},{"length":7,"data":"Cockpit"},{"length":4,"data":"Dash"},{"length":6,"data":"Helmet"}]},{"cameraSetName":{"length":7,"data":"Helicam"},"numCameras":1,"cameras":[{"length":7,"data":"Helicam"}]},{"cameraSetName":{"length":7,"data":"Onboard"},"numCameras":4,"cameras":[{"length":8,"data":"Onboard0"},{"length":8,"data":"Onboard1"},{"length":8,"data":"Onboard2"},{"length":8,"data":"Onboard3"}]},{"cameraSetName":{"length":7,"data":"pitlane"},"numCameras":2,"cameras":[{"length":12,"data":"pitlane_Cam1"},{"length":12,"data":"pitlane_Cam2"}]},{"cameraSetName":{"length":4,"data":"set1"},"numCameras":13,"cameras":[{"length":9,"data":"Set1_Cam3"},{"length":9,"data":"Set1_Cam4"},{"length":9,"data":"Set1_Cam5"},{"length":9,"data":"Set1_Cam6"},{"length":9,"data":"Set1_Cam7"},{"length":9,"data":"Set1_Cam9"},{"length":10,"data":"Set1_Cam10"},{"length":10,"data":"Set1_Cam11"},{"length":10,"data":"Set1_Cam12"},{"length":10,"data":"Set1_Cam13"},{"length":13,"data":"Set1_Cam14_14"},{"length":9,"data":"Set1_Cam1"},{"length":9,"data":"Set1_Cam2"}]},{"cameraSetName":{"length":4,"data":"set2"},"numCameras":10,"cameras":[{"length":9,"data":"Set2_Cam3"},{"length":9,"data":"Set2_Cam4"},{"length":9,"data":"Set2_Cam5"},{"length":9,"data":"Set2_Cam6"},{"length":9,"data":"Set2_Cam7"},{"length":9,"data":"Set2_Cam8"},{"length":9,"data":"Set2_Cam9"},{"length":10,"data":"Set2_Cam10"},{"length":9,"data":"Set2_Cam1"},{"length":9,"data":"Set2_Cam2"}]},{"cameraSetName":{"length":5,"data":"setVR"},"numCameras":11,"cameras":[{"length":9,"data":"CameraVR1"},{"length":9,"data":"CameraVR2"},{"length":9,"data":"CameraVR3"},{"length":9,"data":"CameraVR4"},{"length":9,"data":"CameraVR5"},{"length":9,"data":"CameraVR6"},{"length":9,"data":"CameraVR7"},{"length":9,"data":"CameraVR8"},{"length":9,"data":"CameraVR9"},{"length":10,"data":"CameraVR10"},{"length":10,"data":"CameraVR11"}]}],"numHudPages":6,"hudPages":[{"length":5,"data":"Blank"},{"length":9,"data":"Basic HUD"},{"length":4,"data":"Help"},{"length":9,"data":"TimeTable"},{"length":12,"data":"Broadcasting"},{"length":8,"data":"TrackMap"}]}}
13:39:24.891 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Unmatched message
13:39:25.392 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 060000010c00426c61636b2046616c636f6e04000000000002000104004c756361050053746f6c7a030053544f010200 {"msgType":"ENTRY_LIST_CAR","body":{"carId":0,"carModelType":1,"teamName":{"length":12,"data":"Black Falcon"},"raceNumber":4,"cupCategory":"OVERALL_PRO","driverIndex":0,"nationality":2,"numDrivers":1,"drivers":[{"firstName":{"length":4,"data":"Luca"},"lastName":{"length":5,"data":"Stolz"},"shortName":{"length":3,"data":"STO"},"category":"SILVER","nationality":2}]}}
13:39:25.392 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Unmatched message
13:39:25.896 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.LoggingListener -- Received bytes: 0705090030313a33362e373435f360120006000000 {"msgType":"BROADCASTING_EVENT","body":{"type":"LAPCOMPLETED","msg":{"length":9,"data":"01:36.745"},"timeMs":1204467,"carId":6}}
13:39:25.896 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.FilteredMessageListener -- Matched message
Lap completed {"type":"LAPCOMPLETED","msg":{"length":9,"data":"01:36.745"},"timeMs":1204467,"carId":6}
13:39:27.897 [DefaultDispatcher-worker-3] DEBUG com.github.prule.acc.client.MessageReceiver -- Socket timed out. Session ended.
```

## Usage

Simply create an instance of AccClient with an appropriate configuration, and provide a list of listeners when connecting.
The client will send a registration message to ACC which will reply with a response, and then the client should start recieving messages.

To see this in action, you can either use the AccSimulator from the `acc-client` library, or use ACC itself.

```kotlin
import com.github.prule.acc.client.AccClient
import com.github.prule.acc.client.AccClientConfiguration
import com.github.prule.acc.client.FilteredMessageListener
import com.github.prule.acc.client.LoggingListener
import com.github.prule.acc.messages.AccBroadcastingInbound
import kotlinx.coroutines.runBlocking

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
}
```

A simulator can be programmatically started using

```kotlin
    AccSimulator(
    AccSimulatorConfiguration(
        port = 9000,
        connectionPassword = "asd",
        playbackEventsFile = ClasspathSource("com/github/prule/acc/client/simulator/playback-events.csv"),
    ),
).start()
```

## Alternative

To run the simulator and the client from the one program - although logs from the simulator and the client will be mixed:

```shell
./gradlew app:run --args="--simulator --events=./playback-events.csv"
```

# Native image

## Set up the JVM

Install SDKMAN so you can manage JVMs.

```shell
sdk install java 25.0.2-graalce
sdk use java 25.0.2-graalce
```

## Build a native image

```shell
./gradlew nativeCompile
```

## Run the native image

```shell
./app/build/native/nativeCompile/acc-client-example
```

```shell
./app/build/native/nativeCompile/acc-client-example --simulator --events=./app/playback-events.csv
```

https://jitpack.io/#prule/acc-client-example
