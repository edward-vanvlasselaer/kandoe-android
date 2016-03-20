package be.kdg.kandoe.kandoe;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import be.kdg.kandoe.kandoe.activity.LandingActivity;
import be.kdg.kandoe.kandoe.activity.LoginActivity;
import be.kdg.kandoe.kandoe.dom.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@LargeTest
public class AuthenticationTest {
    private final String realUsername = "jacky";
    private final String realPassword = "jacky";
    private String fakeUsername = "";
    private String fakePassword = "";
    private User testUser;

    @Rule
    public ActivityTestRule<LandingActivity> mActivityRule = new ActivityTestRule<>(LandingActivity.class);

    @Before
    public final void setup(){
        fakeUsername = randomiseString();
        fakePassword = randomiseString();
        testUser = new User();
            testUser.setEmail("test@mail.com");
            testUser.setUsername("test123");
            testUser.setFirstName("testFirstname");
            testUser.setLastName("testLastname");
            testUser.setPassword("testPassword");
    }

    @After
    public final void reset(){
        goBackN();
    }

    @Test(expected = NoMatchingViewException.class)
    public void testLoginWithFakeCredentials(){
        //LandingActivity
        onView(withId(R.id.landing_btn_login)).perform(click());
        //LoginActivity
        onView(withId(R.id.login_input_username)).perform(typeText(fakeUsername));
        onView(withId(R.id.login_input_password)).perform(click());
        onView(withId(R.id.login_input_password)).perform(typeText(fakePassword));
        onView(withId(R.id.login_btn_login)).perform(click());
        sleep(3000);
        //OrganisationActivity
        onView(withId(R.id.organisation_base)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithRealCredentials(){
        //LandingActivity
        onView(withId(R.id.landing_btn_login)).perform(click());
        //LoginActivity
        onView(withId(R.id.login_input_username)).perform(typeText(realUsername));
        onView(withId(R.id.login_input_password)).perform(click());
        onView(withId(R.id.login_input_password)).perform(typeText(realPassword));
        onView(withId(R.id.login_btn_login)).perform(click());
        sleep(3000);
        //OrganisationActivity
        onView(withId(R.id.organisation_base)).check(matches(isDisplayed()));
    }


    @Test
    public void testRegisterUser(){
        //LandingActivity
        onView(withId(R.id.landing_btn_register)).perform(click());
        //RegisterActivity
        onView(withId(R.id.register_input_username)).perform(typeText(testUser.getUsername()));
        onView(withId(R.id.register_input_firstname)).perform(typeText(testUser.getFirstName()));
        onView(withId(R.id.register_input_lastname)).perform(typeText(testUser.getLastName()));
        onView(withId(R.id.register_input_email)).perform(typeText(testUser.getEmail()));
        onView(withId(R.id.register_input_password)).perform(typeText(testUser.getPassword()));
        onView(withId(R.id.register_input_confirm)).perform(typeText(testUser.getPassword()));
        onView(withId(R.id.register_btn_register)).perform(click());

        sleep(3000);
        onView(withId(R.id.organisation_base)).check(matches(isDisplayed()));
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void goBackN() {
        final int N = 10; // how many times to hit back button
        try {
            for (int i = 0; i < N; i++)
                Espresso.pressBack();
        } catch (NoActivityResumedException e) {
            Log.e(this.getClass().getSimpleName(), "Closed all activities", e);
        }
    }


    private String randomiseString(){
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVW".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
         return sb.toString();
    }
}
