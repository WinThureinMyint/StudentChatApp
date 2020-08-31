package com.example.studentchatapss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ViewGroupsActivity extends AppCompatActivity {
    private View groupView;
    private FirebaseAuth mauth;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<String>();
    private Toolbar toolbar;
    private DatabaseReference GroupRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(ViewGroupsActivity.this,"is Created Successfully...",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_view_group);
        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        mauth = FirebaseAuth.getInstance();
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        InitializeFields();

        RetrieveAndDisplayGroup();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();

                Intent groupChatIntent= new Intent(ViewGroupsActivity.this, GroupChatActivity.class);
                groupChatIntent.putExtra("GroupName",currentGroupName);
                startActivity(groupChatIntent);
            }
        });
    }

    private void InitializeFields(){
        list_view = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(ViewGroupsActivity.this, android.R.layout.simple_list_item_1,list_of_groups);
        list_view.setAdapter(arrayAdapter);
    }

    private void RetrieveAndDisplayGroup(){
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
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
            mauth.signOut();
            SendUserToLoginActivity();
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId()== R.id.main_create_group_option)
        {
            ;
        }

        if (item.getItemId() == R.id.top_posts_option)
        {

        }

        return true;
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(ViewGroupsActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
    private void SendUserToSettingActivity() {
        Intent settingIntent = new Intent(ViewGroupsActivity.this,SettingActivity.class);
        startActivity(settingIntent);
    }


}
