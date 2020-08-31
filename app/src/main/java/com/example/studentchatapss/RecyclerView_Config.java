package com.example.studentchatapss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//for servlet


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecyclerView_Config extends AppCompatActivity {

    private Context mcontext;
    private Messagedapter mMessageAdapter;
    private boolean vote;

    public void setConfig(RecyclerView recyclerView, Context context, List<Message> messages, List<String> keys) {
        mcontext = context;
        mMessageAdapter = new Messagedapter(messages, keys);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mMessageAdapter);

    }


    class MessageItemView extends RecyclerView.ViewHolder {
        ImageButton idVoteUp;
        ImageButton idVoteDown;
        private TextView mDate;
        private TextView mTime;
        private TextView muserName;
        private TextView mMessage;
        private String key;
        String userID;
        String groupID;
        String messageID;

        public MessageItemView(ViewGroup parent) {
            super(LayoutInflater.from(mcontext).
                    inflate(R.layout.item_message, parent, false));

            mDate = (TextView) itemView.findViewById(R.id.viewdate);
            mTime = (TextView) itemView.findViewById(R.id.viewtime);
            mMessage = (TextView) itemView.findViewById(R.id.viewmessage);
            muserName = (TextView) itemView.findViewById(R.id.viewuserName);
            idVoteUp = (ImageButton) itemView.findViewById(R.id.voteUp);
            idVoteDown = (ImageButton) itemView.findViewById(R.id.voteDown);
            idVoteUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, "You voted", Toast.LENGTH_SHORT).show();
                    //?UserID=U123&GroupID=G002&MessageID=M001
//                    String userID = "User123";
//                    String groupID = "G013";
//                    String messageID = "001";

                    RequestBody formBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("UserID", userID)
                            .addFormDataPart("GroupID", groupID)
                            .addFormDataPart("MessageID", messageID)
                            .build();
                    String URL = "http://54.84.136.251:8080/CS370_FinalProject/UserServlet";
                    URL = URL + "?UserID=" + userID + "&GroupID=" + groupID + "&MessageID=" + messageID;


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
                    idVoteUp.setClickable(false);
                }
            });

            idVoteDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
        //http://54.84.136.251:8080/CS370_FinalProject/UserServlet?UserID=d@a.com&GroupID=cs99&MessageID=-Lvsjqjchbyius8n86eP

        boolean voted(String UserID, String GroupID, String MessageID) {

            String URL = "http://54.84.136.251:8080/CS370_FinalProject/UserServlet";
            URL = URL + "?UserID=" + userID + "&GroupID=" + groupID + "&MessageID=" + messageID;


            System.out.println(URL);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(URL).build();

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

                        try {
                            JSONObject jsnobject = new JSONObject(myResponse);

                            if (jsnobject.get("vote") == "true") {
                                vote = true;
                               // idVoteUp.setClickable(false);
                                System.out.println(vote);
                            } else {
                                vote = false;
                              //  idVoteUp.setClickable(true);
                                System.out.println(vote);
                            }
                        } catch (JSONException e) {
                            vote = false;
                        }
                    }
                }
            });
          //  System.out.println(vote);
            return vote;
        }

        public void bind(Message message, String key) {

            mDate.setText(message.getDate());
            mTime.setText(message.getTime());
            mMessage.setText(message.getMessage());
            muserName.setText(message.getUserName());
            this.key = key;
            //this.userID = "c6fee7b";
            this.userID = message.getUserName();
            //this.groupID = message.getGroupID();
            this.groupID = message.getGroupID();
            this.messageID = message.getMessageID();

            if(voted(message.getUserID(), message.getGroupID(), messageID) == true){
                idVoteUp.setClickable(false);
            }


        }
        // http://54.84.136.251:8080/CS370_FinalProject/UserServlet?UserID=com.google.firebase.auth.internal.zzl@c6fee7b&GroupID=cs111&MessageID=-LvsiiVG8CnYBNHIQxWz
    }

    class Messagedapter extends RecyclerView.Adapter<MessageItemView> {
        private List<Message> mmessagesList;
        private List<String> mkeys;

        public Messagedapter(List<Message> mmessagesList, List<String> mkeys) {
            this.mmessagesList = mmessagesList;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public MessageItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MessageItemView(parent);

        }

        @Override
        public void onBindViewHolder(@NonNull MessageItemView holder, int position) {
            holder.bind(mmessagesList.get(position), mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mmessagesList.size();
        }
    }
}
