package com.example.studentchatapss;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ActiveUserAdapter extends ArrayAdapter<ActiveUser> {

    private Context userContext;
    private List<ActiveUser> activeUserList = new ArrayList<>();

    public ActiveUserAdapter(@NonNull Context context,  ArrayList<ActiveUser> list) {
        super(context, 0 , list);
        userContext = context;
        activeUserList = list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(userContext).inflate(R.layout.list_item,parent,false);

        ActiveUser activeUser = activeUserList.get(position);
        String info=activeUser.getUserName() +"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+activeUser.getLastActiveTime()+"\t\t\t\t\t\t" +activeUser.getActive();
        //String info=String.format("%-22s%-22s%-22s\n",activeUser.getUserName() ,activeUser.getLastActiveTime(),activeUser.getActive());

        TextView userInfo = (TextView) listItem.findViewById(R.id.active_user_info);
        userInfo.setText(info);
        //
//        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
//        image.setImageResource(currentMovie.getmImageDrawable());
      //  System.out.println(activeUser.getGroupName());
//        TextView name = (TextView) listItem.findViewById(R.id.active_user_user_name);
//        TextView time = (TextView) listItem.findViewById(R.id.active_user_time);
//        TextView status = (TextView) listItem.findViewById(R.id.active_user_status);
     //   TextView name = (TextView) listItem.findViewById(R.id.active_user_user_name);
//        name.setText(activeUser.getUserID());
//        time.setText(activeUser.getLastActiveTime());
//        status.setText(activeUser.getActive());
//        TextView release = (TextView) listItem.findViewById(R.id.textView_release);
//        release.setText(currentMovie.getmRelease());

        return listItem;
    }

}
