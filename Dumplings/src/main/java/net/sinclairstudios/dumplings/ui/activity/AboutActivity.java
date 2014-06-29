package net.sinclairstudios.dumplings.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import com.googlecode.androidannotations.annotations.EActivity;
import net.sinclairstudios.dumplings.R;

@EActivity(R.layout.about_layout)
public class AboutActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity_.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openBrowserToSinclairStudiosWebsite(View button) {

        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.sinclairStudiosUrl))));
    }
}
