#!/usr/bin/env bash

set -euo pipefail

IGNORE_REGEX='(?i).*[.-](M[0-9]|alpha|beta|rc|cr|ea|snapshot).*'

echo "========================================="
echo " Maven Dependency Updates"
echo "========================================="
mvn versions:display-dependency-updates "-Dmaven.version.ignore=${IGNORE_REGEX}"

echo
echo "Done."
