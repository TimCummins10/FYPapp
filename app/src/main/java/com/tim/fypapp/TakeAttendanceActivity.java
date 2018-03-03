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

import java.util.ArrayList;

public class TakeAttendanceActivity extends AppCompatActivity {


    private ArrayList<String> allStudentsList = new ArrayList<String>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference dbAllRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child("Database Systems").child("AllStudents");
    private DatabaseReference dbPresentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Classes").child("Database Systems").child("PresentStudents");

    ArrayList<String> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String name = ds.getKey();
                    allStudentsList.add(name);
                }

                ListView listView = findViewById(R.id.checkable_list);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
               // String[] students = {"Tim", "Bobby", "Frank", "Zack", "Chris"};

                ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout, R.id.checkList, allStudentsList);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem = ((TextView)view).getText().toString();
                        if(selectedItems.contains(selectedItem)){
                            selectedItems.remove(selectedItem);
                        }

                        else selectedItems.add(selectedItem);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
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

        switch(item.getItemId()){

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, SignInActivity.class));

                break;

        }

        return true;
    }

    public void showSelectedItems(View view){

        FirebaseUser user = mAuth.getCurrentUser();
        String presentStudents = "";

            for (int i = 0; i < selectedItems.size(); i++)
            {
                //dbPresentRef.child(selectedItems.get(i)).child("studentName").setValue(selectedItems.get(i));
                dbPresentRef.child(selectedItems.get(i)).push().setValue("Present");
                presentStudents += "-" + selectedItems.get(i) + "\n";

            }

        Toast.makeText(this,"Students Present: \n" + presentStudents, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, HomeActivity.class));
    }


}
