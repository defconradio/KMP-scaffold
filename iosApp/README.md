# iOS App (`kmp_scaffold`)

The Xcode project lives in `iosApp/kmp_scaffold/kmp_scaffold.xcodeproj` and links the static `Shared.xcframework` located at `iosApp/Frameworks/Shared.xcframework`.

## Build & Run (macOS)

1. Build and copy the XCFramework from the repository root:
   ```bash
   ./iosApp/kmp-ios-build.sh
   ```
2. Open `iosApp/kmp_scaffold/kmp_scaffold.xcodeproj` in Xcode and run on an iOS simulator or device.
