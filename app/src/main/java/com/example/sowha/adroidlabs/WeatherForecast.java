package com.example.sowha.adroidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Yongdan Wang on 2017-12-05.
 */

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeaterForecastActivity";
    private TextView mimnTempView;
    private TextView maxTempView;
    private TextView currentTempView;
    private ImageView imageView;
    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);

        imageView = (ImageView) findViewById(R.id.tempImageView);
        mimnTempView = (TextView) findViewById(R.id.mimnTempView);
        maxTempView = (TextView) findViewById(R.id.maxTempView);
        currentTempView = (TextView) findViewById(R.id.currentTempView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap picture;
        String iconName;

        @Override
        protected String doInBackground(String... params) {
            XmlPullParser parser = Xml.newPullParser();

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                InputStream stream = conn.getInputStream();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {

                    if (parser.getEventType() == XmlPullParser.START_TAG ) {
                        if (parser.getName().equalsIgnoreCase("temperature")) {
                            currentTemp = parser.getAttributeValue(null, "value") + "°C";
                            this.publishProgress(25);
                            Thread.sleep(1000);
                            minTemp = parser.getAttributeValue(null, "min") + "°C";
                            this.publishProgress(50);
                            Thread.sleep(1000);
                            maxTemp = parser.getAttributeValue(null, "max") + "°C";
                            this.publishProgress(75);
                            Thread.sleep(1000);

                        } else if (parser.getName().equalsIgnoreCase("weather")) {
                            iconName = parser.getAttributeValue(null, "icon");
                        }
                    }

                }

                if (fileExistance(iconName + ".png")) {
                    picture = this.readImage(iconName + ".png");
                }

                else {
                    String bitmapURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                    picture = getImage(new URL(bitmapURL));
                }
                this.publishProgress(100);

                conn.disconnect();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mimnTempView.setText(this.minTemp);
            maxTempView.setText(this.maxTemp);
            currentTempView.setText(this.currentTemp);
            imageView.setImageBitmap(this.picture);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            Bitmap bitmap = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());

                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            Log.i(ACTIVITY_NAME, "Download image from the website");
            return bitmap;
        }

        public Bitmap readImage(String imagefile) {
            Bitmap bitm = null;
            try {
                FileInputStream fis = openFileInput(imagefile);

                bitm = BitmapFactory.decodeStream(fis);
                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i(ACTIVITY_NAME, "Read the image from local directory");
            return bitm;
        }
    }
}
