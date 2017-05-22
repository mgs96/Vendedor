package com.movil.mauricio_taborda.vendedor;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView latitudeValue, longitudeValue;
    int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION;
    private BroadcastReceiver broadcastReceiver;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        longitudeValue = (TextView) findViewById(R.id.longitudeValueGPS);
        latitudeValue = (TextView) findViewById(R.id.latitudeValueGPS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MONFA", "hola estoy aqui 1");
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String longitud = (String) intent.getExtras().get("Longitud");
                    String latitud = (String) intent.getExtras().get("Latitud");
                    longitudeValue.setText(longitud);
                    latitudeValue.setText(latitud);
                    Log.i("INFO", "hola estoy aqui 2");
                    Log.i("POS", "lon " + longitud);
                    Log.i("POS", "lat " + latitud);
                    String uid = mAuth.getCurrentUser().getUid();
                    final DatabaseReference dbReference = db.getReference("Recorridos");
                    dbReference.orderByChild("Usuario").equalTo(uid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            System.out.println(dataSnapshot.getValue());
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("Actualización de posicion"));
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Toca otra vez para salir", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startService(View view) {

        // If has permissions, goes to else
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }else {
            Intent i = new Intent(getApplicationContext(), GPSservice.class);
            startService(i);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void stopService(View view) {

        // If has permissions, goes to else
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            Intent i = new Intent(getApplicationContext(), GPSservice.class);
            stopService(i);
        }
    }
}