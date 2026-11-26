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

echo "[KMP] Building shared XCFramework for iOS..."
./gradlew :shared:assembleSharedReleaseXCFramework --no-daemon

# 2. Ensure output directory exists for Xcode
FRAMEWORK_DIR="shared/build/XCFrameworks/release"
if [ ! -d "$FRAMEWORK_DIR/Shared.xcframework" ]; then
  echo "[KMP] ERROR: Framework not found at $FRAMEWORK_DIR/Shared.xcframework. Build failed."
  exit 1
fi

# 3. Copy the XCFramework to iosApp/Frameworks for easy Xcode integration
IOS_FRAMEWORKS_DIR="iosApp/Frameworks"
rm -rf "$IOS_FRAMEWORKS_DIR/Shared.xcframework"
mkdir -p "$IOS_FRAMEWORKS_DIR"
cp -R "$FRAMEWORK_DIR/Shared.xcframework" "$IOS_FRAMEWORKS_DIR/"

# 4. Print success message
echo "[KMP] iOS framework is ready at $IOS_FRAMEWORKS_DIR/Shared.xcframework."
echo "[KMP] Open iosApp/kmp_scaffold/kmp_scaffold.xcodeproj in Xcode and build/run."
