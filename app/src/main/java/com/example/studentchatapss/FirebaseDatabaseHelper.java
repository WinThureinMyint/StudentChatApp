package com.example.studentchatapss;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Message> messages = new ArrayList<>();
    public static List<Message> messagesAll = new ArrayList<>();
    private String groupName;
    public FirebaseDatabaseHelper(String group) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Groups").child(group);
        groupName = group;
    }

    public interface DataStatus {

        void DataIsLoaded(List<Message> messages, List<String> keys);
      //  boolean voted(String UserID, String GroupID, String MessageID);
        void DataIsInserted();

    }

    public void readMessages(final DataStatus dataStatus) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Message message = keyNode.getValue(Message.class);
                    message.setMessageID(keyNode.getKey());
                    message.setGroupID(groupName);
                    System.out.println(message.getGroupID());
                    messages.add(message);

                }
                messagesAll = messages;
                dataStatus.DataIsLoaded(messages, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
