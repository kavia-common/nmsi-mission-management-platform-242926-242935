#!/bin/bash
cd /home/kavia/workspace/code-generation/nmsi-mission-management-platform-242926-242935/nmsi_frontend_web
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

