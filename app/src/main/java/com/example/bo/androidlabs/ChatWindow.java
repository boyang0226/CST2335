package com.example.bo.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {


    ListView chatList;
    EditText edit;
    Button send;
    protected  ArrayList<String> chatMessage = new ArrayList<String>();
    protected static final String ACTIVITY_Name = "ChatWindow";

    protected ChatDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_chat_window);

        dbHelper=new ChatDatabaseHelper(this);
        final   SQLiteDatabase db = dbHelper.getWritableDatabase();



        Cursor results = db.query(false, ChatDatabaseHelper.databaseName,
                new String[] { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE },
                null, null, null, null, null, null);

        int rows = results.getCount() ; //number of rows returned
        results.moveToFirst(); //move to first result


        while(!results.isAfterLast() ) {
            chatMessage.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            Log.i(ACTIVITY_Name, "SQL MESSAGE:" + results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            results.moveToNext();
        }

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

                ContentValues newValues = new ContentValues();
                newValues.put(ChatDatabaseHelper.KEY_MESSAGE, edit.getText().toString());
                db.insert(ChatDatabaseHelper.databaseName, "", newValues);

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
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        Log.i(ACTIVITY_Name,"In onDestroy()");
    }

}
