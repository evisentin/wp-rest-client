#!/usr/bin/env bash

set -euo pipefail

IGNORE_REGEX='.*-(M|alpha|beta|rc|CR|EA|SNAPSHOT).*'

echo "========================================="
echo " Maven Dependency Updates"
echo "========================================="
mvn versions:display-dependency-updates "-Dmaven.version.ignore=${IGNORE_REGEX}"

echo
echo "Done."
