package com.freisia.cataloguemoviedb.ui

import android.content.Context
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.freisia.cataloguemoviedb.App
import com.freisia.cataloguemoviedb.di.component.TestDetailComponent
import com.freisia.cataloguemoviedb.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

class DetailFragmentTest {

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
        val component = app.provideDetailComponent() as TestDetailComponent
        component.inject(this)
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
        mockWebServer.shutdown()
    }
}