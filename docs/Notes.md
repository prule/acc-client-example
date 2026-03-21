Notes

----
```text
Execution failed for task ':app:nativeCompile'.
> /Users/paulrule/.sdkman/candidates/java/current/bin/native-image wasn't found. This probably means that JDK isn't a GraalVM distribution.
Make sure to declare the GRAALVM_HOME or JAVA_HOME environment variable or install GraalVM withnative-image in a standard location recognized by Gradle Java toolchain support
```

The error message ".sdkman/candidates/java/current/bin/native-image wasn't found" indicates that your currently selected JDK is a standard OpenJDK distribution, which does not include the native-image tool by default. To resolve this, you need to install a GraalVM distribution using SDKMAN! and, in some cases, install the native-image component separately.

1. Check available GraalVM versions
   First, list the available GraalVM distributions that can be installed via SDKMAN!:
   sdk list java
   Look for identifiers ending with -graal (Oracle GraalVM) or -graalce (GraalVM Community Edition).
2. Install a GraalVM JDK
   Choose a specific GraalVM version from the list and install it. For example, to install a recent version of GraalVM Community Edition for Java 22, you would use:
   sdk install java 22.0.2-graalce
   (Replace 22.0.2-graalce with the specific identifier you found in the list).
   +2
3. Switch to the installed GraalVM JDK
   Set the newly installed GraalVM as your default or use it for the current shell session:
   To set as the default for all new shells:
   sdk default java 22.0.2-graalce
   To use only in the current terminal session:
   sdk use java 22.0.2-graalce
   You can verify the current version with sdk current java.
4. Install the native-image component (if necessary)
   For older GraalVM versions (prior to some recent updates where it's included by default), you might need to install the native-image component using the gu (GraalVM Updater) tool.
   Ensure the gu command is in your PATH, which it should be if you followed the SDKMAN! installation correctly.
   Run the installation command:
   gu install native-image
   This command must be run using a GraalVM JDK.
   +3
5. Verify the installation
   After installation and switching versions, run java -version to confirm that it shows a GraalVM distribution in the output. You can also try running the native-image command to see if it is now found:
   native-image --version
6. System Prerequisites
   Ensure you have the necessary system dependencies for building native images (e.g., build-essential, zlib1g-dev, gcc on Linux; Xcode command line tools on macOS) installed on your system.



sdk install java 25.0.2-graalce

Pauls-MacBook-Air acc-client-example % sdk use java 25.0.2-graalce

Using java version 25.0.2-graalce in this shell.

Pauls-MacBook-Air acc-client-example % java -version
openjdk version "25.0.2" 2026-01-20
OpenJDK Runtime Environment GraalVM CE 25.0.2+10.1 (build 25.0.2+10-jvmci-b01)
OpenJDK 64-Bit Server VM GraalVM CE 25.0.2+10.1 (build 25.0.2+10-jvmci-b01, mixed mode, sharing)

Pauls-MacBook-Air acc-client-example % native-image --version
native-image 25.0.2 2026-01-20
GraalVM Runtime Environment GraalVM CE 25.0.2+10.1 (build 25.0.2+10-jvmci-b01)
Substrate VM GraalVM CE 25.0.2+10.1 (build 25.0.2+10, serial gc)

----

Other issues seemed to be resolved when I upgraded to gradle 9.4 (from 9.0) and JDK 25 (from 21) 

- Execution failed for task ':app:nativeCompile'.
> 'other' has different root


- Produced artifacts:
  /Users/runner/work/acc-client-example/acc-client-example/app/build/native/nativeCompile/svm_err_b_20260321T065838.183_pid14941.md (build_info)
  ========================================================================================================================
  Failed generating 'acc-client-example' after 2.5s.

The build process encountered an unexpected error:

> com.oracle.svm.core.util.VMError$HostedError: java.nio.file.NoSuchFileException: "/Users/runner/work/acc-client-example/acc-client-example/app/build/native-temp"/SVM-1189774245107620780

Please inspect the generated error report at:
/Users/runner/work/acc-client-example/acc-client-example/app/build/native/nativeCompile/svm_err_b_20260321T065838.183_pid14941.md
- e: file:///Users/paulrule/IdeaProjects/acc-client-example/app/build.gradle.kts:47:5: Unresolved reference 'buildArgs'.


----

graalvm Apple could not verify is free of malware that may harm your Mac or compromise your privacy.

This error occurs because macOS Gatekeeper cannot verify the GraalVM signature, usually because it was downloaded outside the App Store. It is generally safe if downloaded from the official GraalVM site or GitHub. To open it, go to System Settings > Privacy & Security, scroll down, and click "Open Anyway".

Steps to Resolve
Try to open: Attempt to open the GraalVM binary/app to trigger the error.
Open Settings: Click the Apple logo, then go to System Settings > Privacy & Security.
Allow Access: Scroll down to the "Security" section. You will see a message about "java" or "GraalVM." Click "Open Anyway".
Enter Credentials: Enter your administrator password and click "Open".

Alternative Methods
Context Menu: Right-click (or Control-click) the application file and select "Open".
Terminal: To remove the quarantine attribute, use xattr -d com.apple.quarantine <path_to_graalvm>

Note: Only bypass this warning if you have directly downloaded the software from official Oracle or authorized sources. 

----

https://docs.gradle.org/current/userguide/best_practices_dependencies.html

----

the .github/workflows/release-native.yml workflow for you .
Here's how it works:
1.
Trigger: It runs whenever you push a tag that starts with v (e.g., git tag v1.0.0 and git push --tags).
2.
Platform: Currently set to build on macos-latest (just like your build-native.yml), but you can add windows-latest or ubuntu-latest to the matrix if you want multi-platform releases.
3.
Build: It uses Java 25 and GraalVM to build the native executable.
4.
Release: It automatically creates a GitHub Release for that tag and uploads the executable (renamed to acc-client-example-macOS) as an asset.
Now, whenever you release a new version, users can download the binary directly from the Releases page of your repository, and the URL will be permanent for that version (e.g., .../releases/download/v1.0.0/acc-client-example-macOS).

----

jitpack to release libraries

