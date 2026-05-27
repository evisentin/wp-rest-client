#!/usr/bin/env bash

set -euo pipefail

# Get the directory where the script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Ask for version
read -rp "Enter release version (e.g. 1.0.0): " VERSION

if [[ -z "$VERSION" ]]; then
  echo "Version cannot be empty"
  exit 1
fi

# Move to project root
cd "$SCRIPT_DIR/.."

echo "Setting Maven version to $VERSION..."
mvn versions:set -DnewVersion="$VERSION"

echo "Committing release..."
git commit -am "Release $VERSION"

echo "Creating tag v$VERSION..."
git tag "v$VERSION"

echo
echo "Review your changes before pushing if needed."
read -rp "Push to origin? [y/N]: " CONFIRM

if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
  echo "Push cancelled."
  echo
  echo "You can undo changes with:"
  echo "  git reset --hard HEAD~1"
  echo "  git tag -d v$VERSION"
  exit 0
fi

echo "Pushing main branch and tag..."
git push origin main "v$VERSION"

echo "Release $VERSION completed successfully."
