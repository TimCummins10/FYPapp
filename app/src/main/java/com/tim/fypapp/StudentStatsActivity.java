package com.tim.fypapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class StudentStatsActivity extends AppCompatActivity {

    private ArrayList<String> allStudentsList = new ArrayList<String>();
    private ArrayList<String> attendancePercentage = new ArrayList<String>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference dbAllRef, absentNumber, presentNumber;
    private TextView statsResult;
    private long  total, attendanceRecord;

    long present = -1;
    long absent = -1;



    public void checkAttendanceRecord(long present, long absent) {
        if (present >= 0 && absent >= 0) {
            total = present + absent;
            attendanceRecord = present / total;

            allStudentsList.add( "\t" + attendanceRecord + "%");
        }
    }

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


                    String selectedClass;
                    Bundle extras = getIntent().getExtras();
                    if (extras == null) {
                        selectedClass = null;
                    } else {
                        selectedClass = extras.getString("classSelected");
                    }
                    //absentNumber = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("AbsentStudents").child(name);
                    //presentNumber = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("PresentStudents").child(name);




                    presentNumber.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                present = snap.getChildrenCount() ;
                            }
                            checkAttendanceRecord(present, absent);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });

                    absentNumber.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                 absent = snap.getChildrenCount() ;
                            }
                            checkAttendanceRecord(present, absent);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });

               //     int presentTest = (int) present;
                //    int absentTest = (int) absent;
                  //  int totalTest = presentTest + absentTest;
                   // double attendanceRecordTest = presentTest / totalTest;
                   // total = present + absent;
                   // attendanceRecord = present / total;

                   // allStudentsList.add(name + "\t" + attendanceRecordTest + "%");

                }


                final ListView listView = findViewById(R.id.studentList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, allStudentsList);
                listView.setAdapter(adapter);


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

     //   dbAllRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("AllStudents");
     //   dbAllRef.addListenerForSingleValueEvent(eventListener);
        }



    }


