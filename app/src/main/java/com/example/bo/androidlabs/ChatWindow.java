package com.example.bo.androidlabs;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {


      ListView chatList;
    EditText edit;
    Button send;
    protected  ArrayList<String> chatMessage = new ArrayList<String>();
    protected static final String ACTIVITY_Name = "ChatWindow";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatList = (ListView) findViewById(R.id.listview);
        edit = (EditText) findViewById(R.id.editText3);
        send = (Button) findViewById(R.id.send);


        //in this case, “this” is the ChatWindow, which is-A Context object
         final   ChatAdapter messageAdapter =new ChatAdapter( this );
        chatList.setAdapter (messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage.add(edit.getText().toString());
                edit.setText("");
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView();

            }
           });





    }
        protected class ChatAdapter extends ArrayAdapter<String>  {

           public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

           public  int getCount(){
                return chatMessage.size();
            }

            public String getItem(int position){
                return chatMessage.get(position);
            }
            public View getView(int position, View convertView, ViewGroup parent){

                LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

                View result = null ;

                if(position%2 == 0)
                    result = inflater.inflate(R.layout.chat_row_outgoing, null);
                else
                    result = inflater.inflate(R.layout.chat_row_incoming, null);

                TextView message = (TextView)result.findViewById(R.id.message_text);

                message.setText(getItem(position)); // get the string at position

                return result;


            }

        }


}
