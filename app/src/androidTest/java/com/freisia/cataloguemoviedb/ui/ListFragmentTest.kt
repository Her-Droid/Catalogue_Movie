package com.freisia.cataloguemoviedb.ui

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.freisia.cataloguemoviedb.App
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.di.component.TestListMovieComponent
import com.freisia.cataloguemoviedb.utils.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ListFragmentTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val mockWebServer: MockWebServer by lazy {
        MockWebServer()
    }
    private lateinit var context: Context

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
        val instrument = InstrumentationRegistry.getInstrumentation()
        val app = instrument.targetContext.applicationContext as App
        context = instrument.context
        val component = app.provideListComponent() as TestListMovieComponent
        component.inject(this)
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
        mockWebServer.shutdown()
    }

    @Test
    fun loadData(){
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(
            DataConverter.readStringFromFile("list_success_response.json")
        ))
        val count = 20
        onView(allOf(isDisplayed(), withId(R.id.rv_movie_tv)))
        onView(allOf(isDisplayed(), withId(R.id.rv_movie_tv)))
            .check(RecyclerViewItemCountAssertion(count))
        SwipeUtils.swipeUp(R.id.rv_movie_tv)
    }

    @Test
    fun filterData(){
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(
            DataConverter.readStringFromFile("list_success_response.json")
        ))
        onView(allOf(isDisplayed(), withId(R.id.fab)))
        onView(allOf(isDisplayed(), withId(R.id.fab))).perform(click())
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(
            DataConverter.readStringFromFile("genre_response.json")
        ))
        // error chip not detected in view
        onView(allOf(isDisplayed(), withTagValue(`is`("Action" as Any))))
        onView(allOf(isDisplayed(), withTagValue(`is`("Action" as Any)))).perform(click())
        onView(allOf(isDisplayed(), withId(R.id.button)))
        onView(allOf(isDisplayed(), withId(R.id.button))).perform(click())
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(
            DataConverter.readStringFromFile("filter_success_response.json")
        ))
        onView(allOf(isDisplayed(), withId(R.id.rv_movie_tv)))
        SwipeUtils.swipeUp(R.id.rv_movie_tv)
    }

    @Test
    fun errorData(){
        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(
            DataConverter.readStringFromFile("error_response.json")
        ))
        onView(allOf(isDisplayed(), withId(R.id.layout_empty)))
        onView(allOf(isDisplayed(), withId(R.id.message_title)))
        onView(allOf(isDisplayed(), withId(R.id.message_value)))
    }

}