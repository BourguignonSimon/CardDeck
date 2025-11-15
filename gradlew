#!/bin/sh

set -e

DIRNAME=$(cd "$(dirname "$0")" && pwd)
APP_BASE_NAME=$(basename "$0")
GRADLE_WRAPPER_DIR="$DIRNAME/gradle/wrapper"

if [ -d "$DIRNAME/.gradle" ]; then
  GRADLE_USER_HOME="$DIRNAME/.gradle"
fi

JAVA_OPTS=${JAVA_OPTS:-""}

exec "${JAVA_HOME:+$JAVA_HOME/bin/}java" $JAVA_OPTS \
  -jar "$GRADLE_WRAPPER_DIR/gradle-wrapper.jar" "$@"
