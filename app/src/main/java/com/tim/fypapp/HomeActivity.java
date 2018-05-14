package com.tim.fypapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tim.fypapp.Model.UserInformation;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase database;
    private DatabaseReference users;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference username = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
    private int check = 0;
    private ArrayList<String> userInfo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();
        users = database.getReference().child("fullName");
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.takeAttendance).setOnClickListener(this);
        findViewById(R.id.viewAllClasses).setOnClickListener(this);
        findViewById(R.id.addStudent).setOnClickListener(this);
        findViewById(R.id.addClass).setOnClickListener(this);
        findViewById(R.id.removeClass).setOnClickListener(this);
        findViewById(R.id.viewStats).setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, SignInActivity.class));

                break;

        }

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.takeAttendance:
                startActivity(new Intent(this, ViewClassesTakeAttendanceActivity.class));
                break;

            case R.id.viewAllClasses:
                startActivity(new Intent(this, ViewClassesActivity.class));
                break;

            case R.id.addClass:
                startActivity(new Intent(this, AddNewClassActivity.class));
                break;

            case R.id.removeClass:
                startActivity(new Intent(this, RemoveClassActivity.class));
                break;

            case R.id.addStudent:
                startActivity(new Intent(this, ViewClassesNewStudentActivity.class));
                break;

            case R.id.viewStats:
                startActivity(new Intent(this, ViewClassesStudentStatsActivity.class));
                break;


        }
    }
}
