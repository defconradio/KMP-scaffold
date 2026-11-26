//
//  kmp_scaffoldApp.swift
//  kmp_scaffold
//
//  Created by Ricardo Bessil on 6/2/26.
//

import SwiftUI
import Shared

@main
struct kmp_scaffoldApp: App {
    init() {
        KoinModuleKt.doInitLogger()
        KoinModuleKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
