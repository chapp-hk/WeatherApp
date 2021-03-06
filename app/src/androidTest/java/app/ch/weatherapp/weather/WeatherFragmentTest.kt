package app.ch.weatherapp.weather

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.ch.base.test.rule.DisableAnimationsRule
import app.ch.base.test.data.local.populateWeatherData
import app.ch.base.test.hasItemAtPosition
import app.ch.data.base.local.DaoProvider
import app.ch.weatherapp.R
import app.ch.weatherapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class WeatherFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    @Inject
    lateinit var daoProvider: DaoProvider

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun empty_history() {
        launchFragmentInHiltContainer<WeatherFragment>()

        onView(withId(R.id.tvWelcome)).check(matches(isDisplayed()))
    }

    @Test
    fun show_latest_history() {
        daoProvider.populateWeatherData()

        launchFragmentInHiltContainer<WeatherFragment>()

        onView(withText("Hong Kong")).check(matches(isDisplayed()))
        onView(withText(R.string.weather_feels_like)).check(matches(isDisplayed()))
        onView(withText(R.string.weather_max_temp)).check(matches(isDisplayed()))
        onView(withText(R.string.weather_min_temp)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView)).check(
            matches(
                hasItemAtPosition(
                    0, allOf(
                        hasDescendant(withText("Clear sky")),
                    )
                )
            )
        )
    }
}