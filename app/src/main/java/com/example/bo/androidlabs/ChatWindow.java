package com.example.bo.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    protected  ArrayList<String> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatWindow";
    Cursor results;
    protected ChatDatabaseHelper dbHelper;
    ChatAdapter messageAdapter;
    private boolean isTablet;
    MessageFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_chat_window);

        dbHelper=new ChatDatabaseHelper(this);
        final   SQLiteDatabase db = dbHelper.getWritableDatabase();


        isTablet = (findViewById(R.id.fragment) != null);


         results = db.query(false, ChatDatabaseHelper.databaseName,
                new String[] { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE },
                null, null, null, null, null, null);

        int rows = results.getCount() ; //number of rows returned
        results.moveToFirst(); //move to first result


        while(!results.isAfterLast() ) {
            chatMessage.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            results.moveToNext();
        }

        chatList = (ListView) findViewById(R.id.listview);
        edit = (EditText) findViewById(R.id.editText3);
        send = (Button) findViewById(R.id.send);


        //in this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter =new ChatAdapter( this );
         chatList.setAdapter (messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage.add(edit.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView();
                ContentValues newValues = new ContentValues();
                newValues.put(ChatDatabaseHelper.KEY_MESSAGE, edit.getText().toString());
                db.insert(ChatDatabaseHelper.databaseName, "", newValues);

                edit.setText("");

                results = db.query(false, ChatDatabaseHelper.databaseName,
                        new String[] { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE }, null, null, null, null, null, null);


            }
           });


        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("chatListView", "onItemClick: " + i + " " + l);

                String message = messageAdapter.getItem(i);
                Bundle bun = new Bundle();
                bun.putLong("ID", l );//l is the database ID of selected item
                bun.putString("message", message);

                //step 2, if a tablet, insert fragment into FrameLayout, pass data
                if(isTablet) {
                    frag = new MessageFragment(ChatWindow.this);

                    frag.setArguments(bun);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, frag).commit();

                }
                //step 3 if a phone, transition to empty Activity that has FrameLayout
                else //isPhone
                {
                    Intent intnt = new Intent(ChatWindow.this, MessageDetails.class);
                    intnt.putExtra("ID" , l); //pass the Database ID to next activity
                    intnt.putExtra( "message", message);
                    startActivityForResult(intnt,5); //go to view fragment details
                }
            }
        });

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + results.getColumnCount() );
        for (int i=0; i< results.getColumnCount(); i++){
            System.out.println(results.getColumnName(i));
        }





 }
        protected class ChatAdapter extends ArrayAdapter<String>  {

           public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

           public  int getCount(){
                return chatMessage.size();
            }



            public long getItemId(int position) {
                results.moveToPosition(position);
                return results.getLong( results.getColumnIndex(ChatDatabaseHelper.KEY_ID) );
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


    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if ((requestCode == 5)&&(resultCode == Activity.RESULT_OK))
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        Long deleteId = data.getLongExtra("DeleteID", -1);
        deleteListMessage(deleteId);
    }

    public void deleteListMessage(Long id)
    {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(ChatDatabaseHelper.databaseName, "_id=" + id , null);
        chatMessage.clear();

        results = db.query(false, ChatDatabaseHelper.databaseName,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},null, null, null, null, null, null);
        int rows = results.getCount();
        results.moveToFirst();
        while(!results.isAfterLast()) {
            chatMessage.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            results.moveToNext();
        }

        messageAdapter.notifyDataSetChanged();
    }

    public void removeFragment()
    {
        getSupportFragmentManager().beginTransaction().remove(frag).commit();
    }


    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}
