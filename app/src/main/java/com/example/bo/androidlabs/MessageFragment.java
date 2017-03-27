package com.example.bo.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {



    Long id;
    String mes;


    ChatWindow ChatWindow = null;


    public MessageFragment(){}
    public MessageFragment (ChatWindow c){
        ChatWindow= c;
    }

    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        mes=bun.getString("message");
        id = bun.getLong("ID");


    }




    @Override
    public View onCreateView(LayoutInflater inflater,  @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View gui = inflater.inflate(R.layout.activity_message_details, null);

        TextView message = (TextView)gui.findViewById(R.id.textView8);
        TextView ID =(TextView)gui.findViewById(R.id.textView9);
        message.setText( mes);
        ID.setText("ID"+id);


        Button btnDelete = (Button)gui.findViewById(R.id.button4);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ChatWindow == null) {               // called from phone
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("DeleteID", id);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
                else
                {
                    ChatWindow.deleteListMessage(id);
                    ChatWindow.removeFragment();
                }
            }
        });

        return gui;
    }

}