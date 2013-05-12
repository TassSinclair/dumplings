package net.sinclairstudios.android.dumplings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.googlecode.androidannotations.annotations.*;

@EActivity(R.layout.how_many_people_activity)
public class HowManyPeopleActivity extends Activity {

    @ViewById
    protected Spinner howManyPeopleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @AfterViews
    protected void initPeopleCounterSpinnerAdapter() {

        int maxPeopleCount = 12;
        Integer[] peopleCounts = new Integer[12];
        for (int i = 0; i < maxPeopleCount; ++i) {
            peopleCounts[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter =
                new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, peopleCounts);
        howManyPeopleSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.how_many_people_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.customiseMenuItem:
                navigateToChoicesAndRatiosActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToChoicesAndRatiosActivity() {
        Intent intent = new Intent(this, ChoicesAndRatiosActivity_.class);
        startActivity(intent);
    }
}
