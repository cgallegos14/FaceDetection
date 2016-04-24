package com.google.android.gms.samples.vision.face.facetracker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AntiFatigueActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_fatigue);

        Button stopAlertButton = (Button) findViewById(R.id.stopAlert);
        stopAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(AntiFatigueActivity.this,FaceTrackerActivity.class);
                startActivity(in);
                finish();
            }
        });
    }

}
