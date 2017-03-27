package com.example.bo.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_fragment);

        MessageFragment frag = new MessageFragment(null);
        Bundle bun = getIntent().getExtras();
        frag.setArguments( bun );
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, frag).commit();
    }
}
