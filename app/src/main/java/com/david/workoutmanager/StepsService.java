package com.david.workoutmanager;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class StepsService extends Service implements SensorEventListener {
    Context context;

    TextView mEditText;

    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;


    public void onCreate(Bundle savedInstance){
        super.onCreate();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        mEditText.setText(String.valueOf(sensorEvent.values[0]));

        Log.i("Info", String.valueOf(sensorEvent.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int
            startId) {

        mEditText = (TextView) ((Activity)context).findViewById(R.id.showStepsTextView);

        mSensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null)
        {
            mStepDetectorSensor =
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        return Service.START_STICKY;

    }
}
