#!/bin/bash
# KMP iOS build script for iosApp target
# This script builds the shared KMP framework and prepares all files/folders needed for Xcode integration.
# Usage: ./kmp-ios-build.sh

set -e

# 1. Go to project root
dir=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
cd "$dir"

echo "[KMP] Building shared framework for iOS..."
./gradlew :shared:syncFramework --no-daemon

# 2. Ensure output directory exists for Xcode
FRAMEWORK_DIR="shared/build/XCFrameworks/release"
if [ ! -d "$FRAMEWORK_DIR" ]; then
  echo "[KMP] ERROR: Framework not found at $FRAMEWORK_DIR. Build failed."
  exit 1
fi

# 3. Copy the framework to iosApp if needed (optional, Xcode can reference directly)
# cp -R "$FRAMEWORK_DIR/Shared.framework" iosApp/

# 4. Print success message
echo "[KMP] iOS framework is ready at $FRAMEWORK_DIR."
echo "[KMP] Open iosApp/iOSApp.xcodeproj in Xcode, add the framework from $FRAMEWORK_DIR, and build/run."
