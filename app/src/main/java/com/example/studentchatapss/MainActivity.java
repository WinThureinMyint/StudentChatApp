package com.example.studentchatapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button view_group;
    private Toolbar mToolbar;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RootRef = FirebaseDatabase.getInstance().getReference();
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Chat App");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        view_group = (Button)findViewById(R.id.view_group);

        view_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGroup();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser==null)
        {
            SendUserToLoginActivity();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.main_logout_option)
        {
            mAuth.signOut();
            SendUserToLoginActivity();
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId()== R.id.main_settings_option)
        {
            SendUserToSettingActivity();
        }
        if (item.getItemId()== R.id.main_create_group_option)
        {
            RequestNewGroup();
        }
        if (item.getItemId() == R.id.main_find_friends_option)
        {

        }

        return true;
    }
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
    private void SendUserToSettingActivity() {
        Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(settingIntent);
    }
    private void RequestNewGroup(){
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name");

        final EditText groupNameField=new EditText(MainActivity.this);
        groupNameField.setHint("eg. CS335");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this,"please write Group Name",Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
    private void CreateNewGroup(final String groupName){
        RootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,groupName+"is Created Successfully...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void viewGroup(){
        Intent groupIntent = new Intent(MainActivity.this, ViewGroupsActivity.class);
        startActivity(groupIntent);
    }
}
