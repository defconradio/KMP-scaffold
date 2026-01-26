iOS integration notes

Use the generated framework in Xcode. Build one of the targets and point Xcode to the produced .framework under
`shared/build/bin/`.

For production, use XCFramework creation by combining multiple architectures.
