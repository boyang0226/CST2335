package com.example.bo.androidlabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {


    protected static final String ACTIVITY_Name = "ListItemsActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_Name,"In onCreate()");


        ImageButton buttonImage = (ImageButton) findViewById(R.id.imageButton);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (picture.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(picture, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setSelected(true);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence text;
                int duration;
                if (compoundButton.isChecked()) {
                    text = "Switch is On";
                    duration = Toast.LENGTH_SHORT;
                } else {
                    text = "Switch is Off";
                    duration = Toast.LENGTH_SHORT;
                }

                Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                toast.show();
            }
        });

        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
        cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics

                builder.setMessage(R.string.dialog_title) ;//Add a dialog message to strings.xml

                     builder.setTitle(R.string.dialog_title);
                       builder .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                Intent resultIntent = new Intent(  );
                                resultIntent.putExtra("Response", "My information to share");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        });
                       builder .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                       builder .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton imageBt = (ImageButton) findViewById(R.id.imageButton);
            imageBt.setBackground(new BitmapDrawable(imageBitmap));
        }
    }


    @Override

    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_Name,"In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_Name,"In onStart()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_Name,"In onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_Name,"In onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_Name,"In onDestroy()");
    }
}
