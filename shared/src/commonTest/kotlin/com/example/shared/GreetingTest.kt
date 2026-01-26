package com.example.shared

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTest {
    @Test
    fun testGreeting() {
        val g = Greeting()
        assertTrue(g.greeting().contains("Hello"))
    }
}
