package com.tim.fypapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StudentStatsActivity extends AppCompatActivity {

    private ArrayList<String> allStudentsList = new ArrayList<String>();
    private ArrayList<Integer> allAbsent = new ArrayList<Integer>();
    private ArrayList<Integer> allPresent = new ArrayList<Integer>();
    private ArrayList<Integer> totalClasses = new ArrayList<Integer>();
    private ArrayList<String> percentage = new ArrayList<String>();
    private ArrayList<String> allNames = new ArrayList<String>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference dbAllRef, absentNumber, presentNumber;
    private TextView statsResult;
    private int presentClasses;
    private int absentClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_stats);

        statsResult = (TextView) findViewById(R.id.statsResult);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    allNames.add(name);

                    String selectedClass;
                    Bundle extras = getIntent().getExtras();
                    if (extras == null) {
                        selectedClass = null;
                    } else {
                        selectedClass = extras.getString("classSelected");
                    }

                    absentNumber = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("AbsentStudents").child(name);
                    presentNumber = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("PresentStudents").child(name);

                    ValueEventListener eventListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            absentClasses = 0;

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                absentClasses = absentClasses + 1;

                            }
                            allAbsent.add(absentClasses);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    absentNumber.addListenerForSingleValueEvent(eventListener2);


                    ValueEventListener eventListener3 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            presentClasses = 0;

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                presentClasses = presentClasses + 1;

                            }
                            System.out.println("PRESENT" + presentClasses);
                            allPresent.add(presentClasses);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    presentNumber.addListenerForSingleValueEvent(eventListener3);


                }

                for (int i = 0; i < allPresent.size(); i++) {
                    totalClasses.add(allPresent.get(i) + allAbsent.get(i));
                }

                ValueEventListener getTotalClasses = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < allPresent.size(); i++) {
                            totalClasses.add(allPresent.get(i) + allAbsent.get(i));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                presentNumber.addListenerForSingleValueEvent(getTotalClasses);


                ValueEventListener attendancePercentage = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < totalClasses.size(); i++) {

                            double present = (double) allPresent.get(i);
                            double total = (double) totalClasses.get(i);
                            double stat = (present / total) * 100;

                            DecimalFormat df = new DecimalFormat("#0.00");
                            String finalPercentage = df.format(stat);

                            percentage.add(finalPercentage);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                presentNumber.addListenerForSingleValueEvent(attendancePercentage);

                ValueEventListener AttendanceRecord = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < totalClasses.size(); i++) {

                            allStudentsList.add(allNames.get(i) + ": \t" + percentage.get(i) + "%");
                            final ListView listView = findViewById(R.id.studentList);
                            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, allStudentsList);
                            listView.setAdapter(adapter);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                dbAllRef.addListenerForSingleValueEvent(AttendanceRecord);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        };

        String selectedClass;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            selectedClass = null;
        } else {
            selectedClass = extras.getString("classSelected");
        }


        dbAllRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("AllStudents");
        dbAllRef.addListenerForSingleValueEvent(eventListener);

    }


}


