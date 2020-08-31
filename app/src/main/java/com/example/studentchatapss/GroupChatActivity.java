package com.example.studentchatapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mrecyclerView;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    EditText messageText;
    private String s;

    String currentGroupName;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentGroupName = getIntent().getStringExtra("GroupName");
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_group_chat);
        toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        mdatabase = FirebaseDatabase.getInstance();
        mreference = mdatabase.getReference("Groups");
        messageText = (EditText) findViewById(R.id.etMessage);
        mrecyclerView = (RecyclerView) findViewById(R.id.rvMessage);
        activeOnline(auth.getCurrentUser().getEmail(),currentGroupName,auth.getCurrentUser().getEmail());
        new FirebaseDatabaseHelper(currentGroupName).readMessages(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Message> messages, List<String> keys) {
                new RecyclerView_Config().setConfig(mrecyclerView, GroupChatActivity.this, messages, keys);
            }

            @Override
            public void DataIsInserted() {

            }
        });

    }
    @Override
    public void onBackPressed() {
        offline(auth.getCurrentUser().getEmail(),currentGroupName,auth.getCurrentUser().getEmail());
        Intent backIntent = new Intent(GroupChatActivity.this, ViewGroupsActivity.class);
        startActivity(backIntent);
    }

    //http://54.84.136.251:8080/CS370_FinalProject/ActiveUserServlet?UserID=cs1&GroupName=cs99&UserName=test
    private void activeOnline(String userID, String groupName, String userName) {
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nothing","haha")
                .build();

        String URL = "http://54.84.136.251:8080/CS370_FinalProject/ActiveUserServlet";
        URL = URL + "?UserID=" + userID + "&GroupName=" + groupName + "&UserName=" + userName;


        System.out.println(URL);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).post(formBody).build();

        System.out.println(request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                }
            }
        });
    }
    private void offline(String userID, String groupName, String userName) {
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nothing","haha")
                .build();

        String URL = "http://54.84.136.251:8080/CS370_FinalProject/OfflineUserServlet";
        URL = URL + "?UserID=" + userID + "&GroupName=" + groupName + "&UserName=" + userName;


        System.out.println(URL);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).post(formBody).build();

        System.out.println(request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.in_chat_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout_option) {
            auth.signOut();
            offline(auth.getCurrentUser().getEmail(),currentGroupName,auth.getCurrentUser().getEmail());
            SendUserToLoginActivity();
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.main_settings_option) {
        //    offline(auth.getCurrentUser().getEmail(),currentGroupName,auth.getCurrentUser().getEmail());
            SendUserToSettingActivity();
        }

        if (item.getItemId() == R.id.top_posts_option) {
            Intent topPostIntent = new Intent(GroupChatActivity.this, TopPostsActivity.class);
            topPostIntent.putExtra("GroupName", currentGroupName);
      //      offline(auth.getCurrentUser().getEmail(),currentGroupName,auth.getCurrentUser().getEmail());

            startActivity(topPostIntent);
        }
        if (item.getItemId() == R.id.active_user) {
            Intent activeUserIntent = new Intent(GroupChatActivity.this, ActiveUserActivity.class);
            activeUserIntent.putExtra("GroupName", currentGroupName);
         //   offline(auth.getCurrentUser().getEmail(),currentGroupName,auth.getCurrentUser().getEmail());

            startActivity(activeUserIntent);
        }

        return true;
    }


    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(GroupChatActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToSettingActivity() {
        Intent settingIntent = new Intent(GroupChatActivity.this, SettingActivity.class);
        startActivity(settingIntent);
    }

    @Override
    public void onClick(View v) {

        String sentMessage;
        String messageSender;
        String mdate;
        String mtime;

        if (!TextUtils.isEmpty(messageText.getText().toString())) {

            sentMessage = (messageText.getText().toString());
            messageSender = auth.getCurrentUser().getEmail();
            mdate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
            mtime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            Message message = new Message(mdate, sentMessage, mtime, messageSender);
            messageText.setText("");
            mreference.child(currentGroupName).push().setValue(message);

        }


    }
}
