#!/bin/bash
# KMP iOS build script for iosApp target
# This script builds the shared KMP framework and prepares all files/folders needed for Xcode integration.
# Usage: ./kmp-ios-build.sh

set -e

# 1. Go to project root
dir=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
cd "$dir"

# Clean build before building framework
echo "[KMP] Cleaning build..."
./gradlew clean --no-daemon

echo "[KMP] Building shared framework for iOS..."
./gradlew :shared:syncFramework --no-daemon

# 2. Ensure output directory exists for Xcode
FRAMEWORK_DIR="shared/build/XCFrameworks/release"
if [ ! -d "$FRAMEWORK_DIR" ]; then
  echo "[KMP] ERROR: Framework not found at $FRAMEWORK_DIR. Build failed."
  exit 1
fi

# 3. Copy the XCFramework to iosApp/Frameworks for easy Xcode integration
IOS_FRAMEWORKS_DIR="iosApp/Frameworks"
mkdir -p "$IOS_FRAMEWORKS_DIR"
cp -R "$FRAMEWORK_DIR/Shared.xcframework" "$IOS_FRAMEWORKS_DIR/"

# 4. Print success message
echo "[KMP] iOS framework is ready at $IOS_FRAMEWORKS_DIR/Shared.xcframework."
echo "[KMP] Open iosApp/iOSApp.xcodeproj in Xcode, add the framework from $IOS_FRAMEWORKS_DIR, and build/run."
