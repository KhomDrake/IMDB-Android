package com.example.imdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class MainActivityTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {
    @Test
    fun guestIsVisible() {
        onView(withId(R.id.guest)).check(matches(isDisplayed()))
    }
}