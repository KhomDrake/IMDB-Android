package com.example.imdb

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.imdb.ui.home.HomeAppActivity
import org.junit.Test

class HomeTest: AcceptanceTest<HomeAppActivity>(HomeAppActivity::class.java) {

    @Test
    fun guestIsVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.tv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}