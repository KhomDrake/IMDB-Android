package com.example.imdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {
    @Test
    fun guestIsVisible() {
        onView(withId(R.id.guest)).check(matches(isDisplayed()))
    }

    @Test
    fun homeActivityIsLoading() {
        onView(withId(R.id.login_edit_text)).check(matches(isDisplayed()))
    }
}
