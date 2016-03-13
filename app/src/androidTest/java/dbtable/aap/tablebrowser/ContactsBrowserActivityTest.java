package dbtable.aap.tablebrowser;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.LoaderManager;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ContactsBrowserActivityTest {

    @Rule
    public ActivityTestRule<ContactsBrowserActivity> activityRule = new ActivityTestRule<ContactsBrowserActivity>(ContactsBrowserActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Intent intent = super.getActivityIntent();
            intent.putExtra(ContactsBrowserActivity.EXTRA_SKIP_INIT_LOADER, true);
            return intent;
        }

        @Override
        protected void afterActivityLaunched() {
            mockLoaderManager = mock(LoaderManager.class);
            getActivity().injectLoaderManager(mockLoaderManager);
        }
    };

    LoaderManager mockLoaderManager;


    @Test
    public void testActivity() {
        ContactsBrowserActivity contactsBrowserActivity = activityRule.getActivity();
        assertNotNull(contactsBrowserActivity.viewSwitcher);
        contactsBrowserActivity.callInitLoader();
        verify(mockLoaderManager, times(1)).restartLoader(contactsBrowserActivity.uriArrayOffset,
                                                       null,
                                                       contactsBrowserActivity);
    }

    @Test
    public void testClickFloatingButtonClick() {
        ContactsBrowserActivity contactsBrowserActivity = activityRule.getActivity();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertNotNull(contactsBrowserActivity.viewSwitcher);
        contactsBrowserActivity.callInitLoader();
        int oldOffset = contactsBrowserActivity.uriArrayOffset;
        onView(withId(R.id.fab)).perform(click());
        verify(mockLoaderManager, times(1)).restartLoader(oldOffset+1,
                                                          null,
                                                          contactsBrowserActivity);
    }

}
