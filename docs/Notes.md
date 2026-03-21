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

