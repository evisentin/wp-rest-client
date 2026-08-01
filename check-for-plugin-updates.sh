#!/usr/bin/env bash

set -euo pipefail

IGNORE_REGEX='.*-(M|alpha|beta|rc|CR|EA|SNAPSHOT).*'

echo
echo "========================================="
echo " Maven Plugin Updates"
echo "========================================="
mvn versions:display-plugin-updates "-Dmaven.version.ignore=${IGNORE_REGEX}"

echo
echo "Done."
