package app.ch.base.test.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedDiagnosingMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun hasItemAtPosition(position: Int, matcher: Matcher<View>): Matcher<View> {
    return object : BoundedDiagnosingMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun matchesSafely(item: RecyclerView, description: Description): Boolean {
            val viewHolder = item.findViewHolderForAdapterPosition(position)
            return matcher.matches(viewHolder?.itemView)
        }

        override fun describeMoreTo(description: Description) {
            description.appendText("has item at position $position : ")
            matcher.describeTo(description)
        }
    }
}

fun hasItemCount(itemCount: Int): Matcher<View> {
    return object : BoundedDiagnosingMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun matchesSafely(item: RecyclerView, mismatchDescription: Description?): Boolean {
            return item.adapter?.itemCount == itemCount
        }

        override fun describeMoreTo(description: Description) {
            description.appendText("has $itemCount items")
        }
    }
}

