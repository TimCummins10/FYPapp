package com.tim.fypapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TakeAttendanceActivity extends AppCompatActivity {


    private ArrayList<String> allStudentsList = new ArrayList<String>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    //private DatabaseReference dbAllRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child("Maths").child("AllStudents");
    //private DatabaseReference dbPresentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child("Maths").child("PresentStudents");
    //private DatabaseReference dbAbsentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child("Maths").child("AbsentStudents");
    private DatabaseReference dbAllRef, dbPresentRef, dbAbsentRef;
    TextView dateAndTime;


    private ArrayList<String> selectedItems = new ArrayList<>();
    private ArrayList<String> unselectedItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    allStudentsList.add(name);
                    unselectedItems.add(name);
                }

                final ListView listView = findViewById(R.id.checkable_list);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout, R.id.checkList, allStudentsList);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem = ((TextView) view).getText().toString();
                        if (!selectedItems.contains(selectedItem)) {
                            selectedItems.add(selectedItem);
                        }

                        if (unselectedItems.contains(selectedItem)) {
                            unselectedItems.remove(selectedItem);
                        }

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        String newString;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("testing");
        }

        dbAllRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(newString).child("AllStudents");
        dbAllRef.addListenerForSingleValueEvent(eventListener);


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

    public void showSelectedItems(View view) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
       // dateAndTime.setText(formattedDate);

        String selectedClass;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            selectedClass = null;
        } else {
            selectedClass = extras.getString("testing");
        }

        FirebaseUser user = mAuth.getCurrentUser();
        dbAbsentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("AbsentStudents");
        String presentStudents = "";

        for (int i = 0; i < selectedItems.size(); i++) {
            dbPresentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child(selectedClass).child("PresentStudents").child(selectedItems.get(i)).child(formattedDate);

            dbPresentRef.setValue("Present");
            presentStudents += "-" + selectedItems.get(i) + "\n";
        }

        for (int i = 0; i < unselectedItems.size(); i++) {
            dbAbsentRef.child(unselectedItems.get(i)).push().setValue("Absent");
        }

        Toast.makeText(this, "Students Present: \n" + presentStudents, Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, HomeActivity.class));
    }


}
