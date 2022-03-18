package com.freisia.cataloguemoviedb.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId

object SwipeUtils {
    fun swipeUp(id : Int){
        val recyclerView = onView(withId(id)).check(matches(isDisplayed()))
        recyclerView.perform(ViewActions.swipeUp())
    }
}