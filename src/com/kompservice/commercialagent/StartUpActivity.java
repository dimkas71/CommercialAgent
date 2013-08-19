package com.kompservice.commercialagent;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class StartUpActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Toast.makeText(this, deviceId, Toast.LENGTH_LONG).show();


    }
}
