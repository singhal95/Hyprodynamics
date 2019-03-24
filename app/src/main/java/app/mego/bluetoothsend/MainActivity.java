/*
 * Copyright (c) 2017 Ahmed-Abdelmeged
 *
 * github: https://github.com/Ahmed-Abdelmeged
 * email: ahmed.abdelmeged.vm@gamil.com
 * Facebook: https://www.facebook.com/ven.rto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.mego.bluetoothsend;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static app.mego.bluetoothsend.BluetoothDevices.REQUEST_ENABLE_BT;

public class MainActivity extends AppCompatActivity {

    /**
     * UI Element
     */

    private Button READING,GRAPH;
    private TextView HUMIDITY,TEMPERATURE,WINDSPEED,WATERLEVEL;
    private Button SPRINKLER,LIGHT,SOLUTIONS;

    private DatabaseReference mDatabase;

    /**
     * Tag for the log (Debugging)
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    /**
     * the MAC address for the chosen device
     */
    String address = null;

    private ProgressDialog progressDialog;
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it'
    //This the SPP for the arduino(AVR)
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private int newConnectionFlag = 0;
    volatile boolean stopWorker;
    int readBufferPosition;
    InputStream mmInputStream;
    byte[] readBuffer;
    Thread workerThread;
    DatabaseReference myRef;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the MAC address from the Bluetooth Devices Activity
        Intent newIntent = getIntent();
        Log.e("nitin123","nitin");
        address = newIntent.getStringExtra(BluetoothDevices.EXTRA_DEVICE_ADDRESS);
        setContentView(R.layout.activity_main);
        initializeScreen();
        database = FirebaseDatabase.getInstance();
        myRef =  database.getReference("user");
        //check if the device has a bluetooth or not
        //and show Toast message if it does't have
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableIntentBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntentBluetooth, REQUEST_ENABLE_BT);
        }




        SPRINKLER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("A",btSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        LIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("B",btSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        SOLUTIONS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("C",btSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        GRAPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GraphActivity.class));
            }
        });
    }

  void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    receiveData(btSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        workerThread.start();
    }



    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {

        HUMIDITY= (TextView) findViewById(R.id.humidity);
        TEMPERATURE= (TextView) findViewById(R.id.temperature);
        WINDSPEED= (TextView) findViewById(R.id.windspeed);
        WATERLEVEL=findViewById(R.id.waterlevel);
        SPRINKLER=findViewById(R.id.sprinker);
        LIGHT=findViewById(R.id.light);
        SOLUTIONS=findViewById(R.id.solution);
        GRAPH=findViewById(R.id.graph);
        READING=findViewById(R.id.readings);
        READING.setEnabled(false);
    }

    /**
     * used to send data to the micro controller
     *
     * @param data the data that will send prefer to be one char
     */
    private void sendData(String data) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(data.getBytes());
            } catch (IOException e) {
                makeToast("Error");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        newConnectionFlag++;
        if (address != null) {
            //call the class to connect to bluetooth
            if (newConnectionFlag == 1) {
                new ConnectBT().execute();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    //    Disconnect();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_disconnect:
                Disconnect();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * An AysncTask to connect to Bluetooth socket
     */
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {

            //show a progress dialog
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Connecting...", "Please wait!!!");
        }

        //while the progress dialog is shown, the connection is done in background
        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (btSocket == null || !isBtConnected) {
                    //get the mobile bluetooth device
                    myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    //connects to the device's address and checks if it's available
                    BluetoothDevice bluetoothDevice = myBluetoothAdapter.getRemoteDevice(address);

                    //create a RFCOMM (SPP) connection
                    btSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);

                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    //start connection
                    btSocket.connect();

                }

            } catch (IOException e) {
                //if the try failed, you can check the exception here
                connectSuccess = false;
            }

            return null;
        }

        //after the doInBackground, it checks if everything went fine
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(LOG_TAG, connectSuccess + "");
            if (!connectSuccess) {
                makeToast("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                isBtConnected = true;
                makeToast("Connected");
                beginListenForData();
            /*  try {
                  // receiveData(btSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


            }
            progressDialog.dismiss();
        }
    }

    /**
     * fast way to call Toast
     */
    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * to disconnect the bluetooth connection
     */
    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                makeToast("Error");
            }
        }
    }

    public void receiveData(BluetoothSocket socket) throws IOException {
        InputStream socketInputStream = socket.getInputStream();
        byte[] buffer = new byte[2056];
        String s1 = "";
        int k=0;
        int bytes;
        while (true) {
            bytes = socketInputStream.read(buffer);
            final String s = new String(buffer, 0, bytes);
            s1=s1+s;
            final String finalS = s1;

            if(s1.contains("$")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int l=finalS.indexOf("T");
                        int u=finalS.indexOf("W");
                        TEMPERATURE.setText(finalS.substring(l+1,u-2));
                        Constants.RT.add(Float.valueOf(finalS.substring(l+1,u-2)));
                        l=u;
                        u=finalS.indexOf("P");
                        HUMIDITY.setText(finalS.substring(l+1,u-2));
                        Constants.wt.add(Float.valueOf(finalS.substring(l+1,u-2)));
                        l=u;
                        u=finalS.indexOf("L");
                        WINDSPEED.setText(finalS.substring(l+1,u-2));
                        Constants.ph.add(Float.valueOf(finalS.substring(l+1,u-2)));
                        l=u;
                        WATERLEVEL.setText(finalS.substring(l+1,finalS.length()-2));
                        Constants.WL.add(Float.valueOf(finalS.substring(l+1,finalS.length()-2)));
                        Log.i("nitin", s);
                        if(Constants.ph.size()==5){
                            Constants.ph.clear();
                            Constants.RT.clear();
                            Constants.WL.clear();
                            Constants.wt.clear();
                        }
                    }
                });
                s1="";
            }
          /*  if(s.contains("$")){
                s1=s1+s;
                final String finalS = s1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int l=finalS.indexOf("T");
                        int u=finalS.indexOf("W");
                        TEMPERATURE.setText(finalS.substring(l+1,u-2));
                        Constants.RT.add(Float.valueOf(finalS.substring(l+1,u-2)));
                        l=u;
                        u=finalS.indexOf("P");
                        HUMIDITY.setText(finalS.substring(l+1,u-2));
                        Constants.wt.add(Float.valueOf(finalS.substring(l+1,u-2)));
                        l=u;
                        u=finalS.indexOf("L");
                       WINDSPEED.setText(finalS.substring(l+1,u-2));
                       Constants.ph.add(Float.valueOf(finalS.substring(l+1,u-2)));
                        l=u;
                        WATERLEVEL.setText(finalS.substring(l+1,finalS.length()-1));
                        Constants.WL.add(Float.valueOf(finalS.substring(l+1,finalS.length()-1)));
                        Log.i("nitin", s);
                        if(Constants.ph.size()==5){
                            Constants.ph.clear();
                            Constants.RT.clear();
                            Constants.WL.clear();
                            Constants.wt.clear();
                        }


                if(Float.valueOf(TEMPERATURE.getText().toString())<27.00){
                          String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                       //  NotificationModel notificationModel=new NotificationModel("TEMPERATURE IS LOW",mydate);
                  //    myRef.setValue(Constants.USERID);
                     //myRef.child(Constants.USERID).setValue(mydate);
                     //myRef.push();
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("default",
                                        "YOUR_CHANNEL_NAME",
                                        NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("TEMPERATURE IS LOW");
                                mNotificationManager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                    .setContentTitle("Hello") // title for notification
                                    .setContentText("bdsjkkjsdn")// message for notification
                                    // set alarm sound for notification
                                    .setAutoCancel(true); // clear notification after click
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pi);
                            mNotificationManager.notify(0, mBuilder.build());

                        }
                        if(Float.valueOf(TEMPERATURE.getText().toString())>32.00){
                        /*    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                            NotificationModel notificationModel=new NotificationModel("TEMPERATURE IS LOW",mydate);
                            mDatabase.child("users").child(Constants.USERID).setValue(notificationModel);*/
                       /*     NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("default",
                                        "YOUR_CHANNEL_NAME",
                                        NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("TEMPERATURE IS HIGH");
                                mNotificationManager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                    .setContentTitle("Hello") // title for notification
                                    .setContentText("bdsjkkjsdn")// message for notification
                                    // set alarm sound for notification
                                    .setAutoCancel(true); // clear notification after click
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pi);
                            mNotificationManager.notify(0, mBuilder.build());
                        }
                        if(Float.valueOf(WINDSPEED.getText().toString())>6.00){
                          /* String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                            NotificationModel notificationModel=new NotificationModel("TEMPERATURE IS LOW",mydate);
                            mDatabase.child("users").child(Constants.USERID).setValue(notificationModel);*/
                          /*  NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("default",
                                        "YOUR_CHANNEL_NAME",
                                        NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("PH IS ABOVE THE RANGE");
                                mNotificationManager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                    .setContentTitle("Hello") // title for notification
                                    .setContentText("bdsjkkjsdn")// message for notification
                                    // set alarm sound for notification
                                    .setAutoCancel(true); // clear notification after click
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pi);
                            mNotificationManager.notify(0, mBuilder.build());
                        }
                    }
                });
                s1="";

            }*/
          /*  else {
                s1=s1+s;
            }*/




        }
    }






    public void write(String input,BluetoothSocket socket) throws IOException {
     OutputStream socketInputStream =  socket.getOutputStream();
        byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
        try {
            socketInputStream.write(msgBuffer);                //write bytes over BT connection via outstream
        } catch (IOException e) {
            //if you cannot write, close the application
            Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            finish();

        }
    }



}



