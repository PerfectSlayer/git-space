#!/bin/bash
set -x

if [ -n "$JAVA_17_HOME" ]; then
  JAVA_BINARY=$JAVA_17_HOME/bin/java
elif [ -n "$JAVA_HOME" ]; then
  JAVA_BINARY=$JAVA_HOME/bin/java
else
  JAVA_BINARY=java
fi

TARGET_JAR_FILES=(target/git-space*.jar)
JAR_FILES=(git-space*.jar)
if [ -n "${TARGET_JAR_FILES[0]}" ]; then
  JAR_FILE=${TARGET_JAR_FILES[0]}
elif [ -n "${JAR_FILES[0]}" ]; then
  JAR_FILE=${JAR_FILES[0]}
else
  JAR_FILE=git-space.jar
fi

LOG_FILE_PATH=$(dirname -- "$0")/git-space.log $JAVA_BINARY -jar $JAR_FILE
