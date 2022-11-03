package dk.bollsjen.wantedcats

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dk.bollsjen.wantedcats", appContext.packageName)

        onView(ViewMatchers.withId(R.id.show_filter_chip)).perform(ViewActions.click())

        pause(2000)

        onView(ViewMatchers.withId(R.id.filter_cats_by_rewards)).perform(ViewActions.click())



        onView(ViewMatchers.withId(R.id.filter_reward_lower_limit)).perform(ViewActions.typeText("150"))
        onView(ViewMatchers.withId(R.id.filter_reward_form_submit)).perform(ViewActions.click())

        onView(ViewMatchers.withId(R.id.show_filter_chip)).perform(ViewActions.click())

        pause(5000)
    }

    private fun pause(millis: Long) {
        try {
            Thread.sleep(millis)
            // https://www.repeato.app/android-espresso-why-to-avoid-thread-sleep/
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


}