package net.sinclairstudios.android.dumplings;

import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity(R.layout.choices_and_ratios_activity)
public class ChoicesAndRatiosActivity extends Activity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
