package com.example.bo.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.progress;
import static com.example.bo.androidlabs.R.id.progressBar;
import static com.example.bo.androidlabs.R.id.textView;

public class WeatherForecast extends AppCompatActivity {

    String value,min,max;
    Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery thread = new ForecastQuery();
        thread.execute();

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>

    {
        protected String doInBackground(String... args) {

            String in = "";
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric"); //("http://www.google.com/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                Log.i("XML parsing:", "" + xpp.getEventType());
                int type;
                while ((type = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    if (xpp.getEventType() == XmlPullParser.START_TAG)
                    {
                        if (xpp.getName().equals("temperature"))
                        {
                            value = xpp.getAttributeValue(null, "value");
                           // Thread.sleep(1000);
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                           // Thread.sleep(1000);
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            //Thread.sleep(1000);
                            publishProgress(75);

                        } else if (xpp.getName().equals("weather")) {

                            String icon = xpp.getAttributeValue(null, "icon");

                            if (!fileExistance(icon + ".png")) {

                                URL ImageURL = new URL("http://openweathermap.org/img/w/" + icon + ".png");
                                Bitmap image = getImage(ImageURL);
                                FileOutputStream outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();

                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(icon + ".png");

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                bm = BitmapFactory.decodeStream(fis);
                             } else {
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(icon + ".png");

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                bm = BitmapFactory.decodeStream(fis);

                                }
                           // Thread.sleep(1000);
                            publishProgress(100);

                        }


                    }
                    xpp.next();
                }

            } catch (Exception me) {
                Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
            }

                 return in;
        }

        public void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(progress[0]);




        }

        public void onPostExecute(String work) {

            ImageView  imageView =(ImageView)findViewById(R.id.imageView3) ;
                 imageView.setImageBitmap(bm);

            TextView tx = (TextView)findViewById(R.id.textView);
            tx.setText("current Temperature is : "+value);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);


            TextView tx2 = (TextView)findViewById(R.id.textView3);
            tx2.setText("min Temperature is : "+min);

            TextView tx3 = (TextView)findViewById(R.id.textView5);
            tx3.setText("max Temperature is : "+max);
        }


        public boolean fileExistance(String fname) {

            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public  Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

    }


}
