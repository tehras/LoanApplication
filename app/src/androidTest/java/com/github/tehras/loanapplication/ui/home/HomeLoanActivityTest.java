package com.github.tehras.loanapplication.ui.home;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.github.tehras.loanapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeLoanActivityTest {

    @Rule
    public ActivityTestRule<HomeLoanActivity> mActivityTestRule = new ActivityTestRule<>(HomeLoanActivity.class);

    @Test
    public void homeLoanActivityTest() {

        ViewInteraction textView2 = onView(
                allOf(withText("Current Balance"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.appbar_layout),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Current Balance")));

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.line_chart_layout),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.home_payment_chart_layout),
                                        0),
                                0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.home_add_button),
                        childAtPosition(
                                allOf(withId(R.id.listCoordinatorLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                2),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withText("Loan Name"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Loan Name")));

        ViewInteraction textView4 = onView(
                allOf(withText("Loan Amount"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Loan Amount")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.home_loan_amount), withText("$12,000.00"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.home_loan_name), withText("Loan 1"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.home_loan_provider), withText("A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listRecyclerView),
                                        0),
                                0),
                        isDisplayed()));
        textView7.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
