package com.tim.fypapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewClassActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAddClass;
    private TextView tvEnterNameOfClass;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_class);

        etAddClass = (EditText) findViewById(R.id.etAddClass);
        tvEnterNameOfClass = (TextView) findViewById(R.id.tvEnterNameOfClass);
        tvEnterNameOfClass.setText("What class would you like to create?");

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Classes");

        findViewById(R.id.addClassButton).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.addClassButton:
                finish();
                addNewClass();
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }


    private void addNewClass() {
        String newClass = etAddClass.getText().toString().trim();

        if(newClass.isEmpty()){
            etAddClass.setError("Please fill in a student you would like to add.");
            etAddClass.requestFocus();
            return;
        }

        //dbRef.child(newClass).push().setValue(newClass);
        dbRef.child(newClass).child("AllStudents").setValue("List");
        Toast.makeText(getApplicationContext(), "New Class Added!", Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, SignInActivity.class));

                break;

        }

        return true;
    }

}
