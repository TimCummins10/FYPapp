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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoveClassActivity extends AppCompatActivity {

    private DatabaseReference dbRefTEST, removeClass;
    private ArrayList<String> allClassNames = new ArrayList<String>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference dbAllClasses = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes");
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_classes_new_student);


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    allClassNames.add(name);
                }

                Spinner allClasses = findViewById(R.id.allClasses);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, allClassNames);
                allClasses.setAdapter(adapter);
                allClasses.setPrompt("Please Select A Class");
                allClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (++check > 1) {

                            Toast.makeText(adapterView.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                            //String selectedClass = adapterView.getItemAtPosition(i).toString();
                            dbRefTEST = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Classes").child(adapterView.getItemAtPosition(i).toString()).child("AllStudents");

                            removeClass = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Classes").child(adapterView.getItemAtPosition(i).toString());
                            removeClass.removeValue();

                            Intent backToHome = new Intent(getApplicationContext(), HomeActivity.class);
                            //allClassNames.remove(adapterView.getItemAtPosition(i));

                            startActivity(backToHome);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        dbAllClasses.addListenerForSingleValueEvent(eventListener);
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

}

