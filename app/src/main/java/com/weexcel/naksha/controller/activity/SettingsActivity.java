package com.weexcel.naksha.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;
import com.weexcel.naksha.R;


public class SettingsActivity extends Activity
{
    Slider sliderRadius;
    TextView showRadius;
    ButtonRectangle btnSelect;
    long gotRadius = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sliderRadius = (Slider) findViewById(R.id.sliderMain);
        showRadius = (TextView) findViewById(R.id.showSelectedRadius);
        btnSelect = (ButtonRectangle) findViewById(R.id.btnSelect);
        sliderRadius.setMin(0);
        sliderRadius.setMax(50);
        sliderRadius.setShowNumberIndicator(true);

        sliderRadius.setOnValueChangedListener(new Slider.OnValueChangedListener()
        {
            @Override
            public void onValueChanged(int i)
            {
                showRadius.setText(sliderRadius.getValue()+" km");
                gotRadius = sliderRadius.getValue() * 1000;
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putLong("RADIUS", gotRadius).commit();
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
