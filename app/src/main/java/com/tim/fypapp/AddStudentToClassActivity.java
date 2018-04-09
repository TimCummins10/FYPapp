package com.tim.fypapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentToClassActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAddStudent;
    private TextView tvEnterNameOfStudent;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference dbRef, dbRefTEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_to_class);

        etAddStudent = (EditText) findViewById(R.id.etAddStudent);

        tvEnterNameOfStudent = (TextView) findViewById(R.id.tvEnterNameOfStudent);
        tvEnterNameOfStudent.setText("What Student would you like to add?");


        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Classes").child("Maths").child("AllStudents");

        findViewById(R.id.addStudentButton).setOnClickListener(this);


    }

    private void addNewStudent() {

        String newString;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("classSelected");
        }

        String newStudent = etAddStudent.getText().toString().trim();

        if (newStudent.isEmpty()) {
            etAddStudent.setError("Please fill in a student you would like to add.");
            etAddStudent.requestFocus();
            return;
        }
        dbRefTEST = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Classes").child(newString).child("AllStudents");

        dbRefTEST.child(newStudent).setValue(newStudent);
        Toast.makeText(getApplicationContext(), "New Student Added!", Toast.LENGTH_LONG).show();

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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.addStudentButton:
                finish();
                addNewStudent();
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

}
