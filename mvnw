#!/usr/bin/env sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements. See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License. You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ----------------------------------------------------------------------------

PRG="$0"
while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="$(dirname "$PRG")/$link"
  fi
done

PRG_DIR=$(dirname "$PRG")
MAVEN_HOME=

if [ -f "$PRG_DIR/.mvn/wrapper/maven-wrapper.properties" ]; then
  APP_NAME="mvnw"
  MAVEN_PROJECTBASEDIR="$PRG_DIR"
  MAVEN_OPTS=""
  MAVEN_WRAPPER_LAUNCHER="$PRG_DIR/.mvn/wrapper/maven-wrapper.jar"
  MAVEN_WRAPPER_PROPERTIES="$PRG_DIR/.mvn/wrapper/maven-wrapper.properties"

  if [ -f "$MAVEN_WRAPPER_LAUNCHER" ]; then
    if [ -n "$JAVA_HOME" ]; then
      JAVA_CMD="$JAVA_HOME/bin/java"
    else
      JAVA_CMD=java
    fi
    exec "$JAVA_CMD" -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" -classpath "$MAVEN_WRAPPER_LAUNCHER" org.apache.maven.wrapper.MavenWrapperMain "$@"
  else
    echo "Maven wrapper jar not found. Please download .mvn/wrapper/maven-wrapper.jar or install Maven and run 'mvn clean install'."
    exit 1
  fi
else
  echo "Maven wrapper properties not found."
  exit 1
fi
