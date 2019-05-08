package com.example.imdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.imdb.ui.home.HomeAppActivity
import org.junit.Test

class HomeTest: AcceptanceTest<HomeAppActivity>(HomeAppActivity::class.java) {
    @Test
    fun guestIsVisible() {
        onView(withId(R.id.tv))
            .check(matches(isDisplayed()))
    }
}