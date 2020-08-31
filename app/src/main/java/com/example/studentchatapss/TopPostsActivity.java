package com.example.studentchatapss;

import android.os.Bundle;
import android.text.TextPaint;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TopPostsActivity extends AppCompatActivity {
    // private TextView tp;
    private ListView tp;
    private String s;
    private String currentGroupName;
    private DatabaseReference GroupRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_post);
        // tp = (TextView) findViewById(R.id.top_post);
        tp = (ListView) findViewById(R.id.top_post);
        currentGroupName = getIntent().getStringExtra("GroupName");
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        topPost();
        //tp.setText(s);
    }

    String topPost() {

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserID", "")
                .build();

        String URL = "http://54.84.136.251:8080/CS370_FinalProject/GetMessagesServlet";

        URL = URL + "?GroupID=" + currentGroupName;

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
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                    //tp.setText(myResponse);
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        // JSONObject jsnobject = new JSONObject(myResponse);
                        //   JSONArray jsonArray = jsnobject.getJSONArray();
                      //  List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        ArrayList<String> arrayList=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject singleUser = jsonArray.getJSONObject(i);
                            String MID = GroupRef.child(singleUser.getString("MessageID")).toString();
                            String votes = singleUser.getString("TotalVotes");
                            String s = MID + " \t \t \t \t " + votes;
                            arrayList.add(s);
                        }
                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(TopPostsActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        tp.setAdapter(itemsAdapter);
                        s = myResponse;
                    } catch (JSONException e) {

                    }

                }
            }
        });
        return s;
    }
}
