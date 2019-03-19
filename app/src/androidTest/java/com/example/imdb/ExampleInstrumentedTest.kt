package com.example.imdb

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith


/**
 * Instrumented requestResponse, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule = IntentsTestRule(MainActivity::class.java, false)

}
