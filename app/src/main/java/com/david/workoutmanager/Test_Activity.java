package com.david.workoutmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test_Activity extends AppCompatActivity implements SensorEventListener {

    private TextView mCaloriesTextView;

    private BarChart mBarChart;

    int today;
    int lastTimeStarted;

    String accID;

    String user;
    SensorManager sManager;
    Sensor stepSensor;
    Button mButton;
    TextView mEditText;
    private FirebaseAuth mAuth;
    private static final String USER = "USER";
    private long steps;
    boolean running = false;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    TextView mDistanceTextView;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance();

        ref = db.getReference("User");

        mBarChart = (BarChart) findViewById(R.id.Barchart);



        mCaloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        mDistanceTextView = (TextView) findViewById(R.id.distanceTextView);

        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        steps = 0;
        setContentView(R.layout.activity_test_);
        mButton = (Button) findViewById(R.id.showButton);
        mEditText = (TextView) findViewById(R.id.showStepsTextView);
        mAuth = FirebaseAuth.getInstance();

        //mEditText.setText(mAuth.getCurrentUser().toString());
        loadBar();
        getDate();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent i = new Intent(Test_Activity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        };



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                System.out.println("split(String regex):");
                String user = mAuth.getCurrentUser().toString();
                String[] array1= user.split("\\.");
                //ref.setValue(array1[5]);
                accID = "123";

                ref.child(accID).child(String.valueOf(today-3));
                ref.child(accID).child(String.valueOf(today-3)).child("Steps").setValue("4000");
                ref.child(accID).child(String.valueOf(today-2));
                ref.child(accID).child(String.valueOf(today-2)).child("Steps").setValue("3500");
                ref.child(accID).child(String.valueOf(today-1));
                ref.child(accID).child(String.valueOf(today-1)).child("Steps").setValue("4500");
                ref.push();

                mAuth.signOut();
                Toast.makeText(Test_Activity.this,"Signed Out", Toast.LENGTH_LONG).show();


            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
 //               Log.d("TEST", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TEST", "Failed to read value.", error.toException());
            }
        });

    }

    private void getDate() {

        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_YEAR);
        String dateStr = "04/05/2010";
        lastTimeStarted = today;

        System.out.println("split(String regex):");
        String user = mAuth.getCurrentUser().toString();
        String[] array1= user.split("\\.");
        //ref.setValue(array1[5]);
        user = array1[5];
        ref.child(array1[5]).child(String.valueOf(today));
        String newDateString;

        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateObj = curFormater.parse(dateStr);
            SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");

            newDateString = postFormater.format(dateObj);
            //ref.child(array1[5]).setValue(newDateString);


        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }




    }

    private void loadBar() {

        mBarChart = (BarChart) findViewById(R.id.Barchart);


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f,40f));
        barEntries.add(new BarEntry(1f,50f));
        barEntries.add(new BarEntry(2f,60f));
        barEntries.add(new BarEntry(3f,0f));
        barEntries.add(new BarEntry(4f,0f));
        barEntries.add(new BarEntry(5f,0f));
        barEntries.add(new BarEntry(6f,0f));

        BarDataSet barDataSet = new BarDataSet(barEntries,"Steps");

        List<Entry> entries = new ArrayList<Entry>();

        final ArrayList<String> days = new ArrayList<>();
        days.add("Su");
        days.add("M");
        days.add("T");
        days.add("W");
        days.add("Th");
        days.add("F");
        days.add("Sa");

        BarData data = new BarData(barDataSet);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return days.get((int)value);
            }
        });

        data.setBarWidth(0.9f); // set custom bar width
        mBarChart.setData(data);

        mBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        mBarChart.invalidate();





    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void goBack(){
        Intent data = new Intent();
        data.putExtra("Test Suc", "test");
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onResume() {

        super.onResume();

        running = true;

        Sensor countSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_YEAR);

        if (today != lastTimeStarted) {
            //startSomethingOnce();
            getDate();

        }

        if (countSensor != null) {
            sManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);

        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }

        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sManager.unregisterListener(this, stepSensor);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        mCaloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        mDistanceTextView = (TextView) findViewById(R.id.distanceTextView);
        mEditText.setText(String.valueOf(event.values[0]));
        float distance = (78 * event.values[0]) / 2000;
        String dis = String.format("%.02f",distance);
        mDistanceTextView.setText(dis);
        double calories = distance / 99.75;
        String cal = String.format("%.02f",calories);
        mCaloriesTextView.setText(cal);
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        // nobody knows what happens here: step value might magically decrease
        // when this method is called...
        //if (BuildConfig.DEBUG) Logger.log(sensor.getName() + " accuracy changed: " + accuracy);
    }




}
