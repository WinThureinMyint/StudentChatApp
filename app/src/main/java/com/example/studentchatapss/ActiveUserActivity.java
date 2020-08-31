package com.example.studentchatapss;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.widget.ProgressBar;

public class ActiveUserActivity extends AppCompatActivity {
    private TextView tp;
    private String s;
    private String currentGroupName;
    private ProgressBar loader;
    private JSONObject reader;
    private ListView listView;
    private ArrayList<HashMap<String, String>> list_of_groups = new ArrayList<HashMap<String, String>>();
    private ActiveUserAdapter activeUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user);
        //tp = (TextView) findViewById(R.id.active_user_TV);
        // loader = (ProgressBar) findViewById(R.id.loader_PB);
        currentGroupName = getIntent().getStringExtra("GroupName");
        listView = (ListView) findViewById(R.id.active_user_list);
        topPost();
        //  tp.setText(topPost());
    }

    String topPost() {
//        loader.setVisibility(View.VISIBLE);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserID", "")
                .build();

        String URL = "http://54.84.136.251:8080/CS370_FinalProject/ActiveUserServlet";

        URL = URL + "?GroupName=" + currentGroupName;

        System.out.println(URL);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).build();

        System.out.println(request.toString());
        //String myResponse1 = "";
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    System.out.println(myResponse);
                    ActiveUserActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<ActiveUser> userArrayList = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        // JSONObject jsnobject = new JSONObject(myResponse);
                        //   JSONArray jsonArray = jsnobject.getJSONArray();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleUser = jsonArray.getJSONObject(i);
                            System.out.println(singleUser);
                            ActiveUser activeUser = new ActiveUser(singleUser.getString("GroupName"), singleUser.getString("UserName"), singleUser.getString("lastActiveTime"), singleUser.getString("UserID"), singleUser.getString("active"));
                            userArrayList.add(activeUser);
                            //  JSONObject  = jsonArray.getJSONObject(i);
//                            HashMap<String, String> map = new HashMap<String, String>();
//                            map.put("GroupName", singleUser.getString("GroupName"));
//                            map.put("UserName", singleUser.getString("UserName"));
//                            map.put("lastActiveTime", singleUser.getString("lastActiveTime"));
//                            map.put("UserID", singleUser.getString("UserID"));
//                            list_of_groups.add(map);
                        }
                                activeUserAdapter = new ActiveUserAdapter(ActiveUserActivity.this, userArrayList);
                                listView.setAdapter(activeUserAdapter);
                    } catch (JSONException e) {

                    }
                        }
                    });
                }
            }
        });
        return s;
    }
}
