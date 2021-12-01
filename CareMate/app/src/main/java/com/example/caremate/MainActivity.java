package com.example.caremate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caremate.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    BluetoothDevice CareMate;
    BluetoothAdapter mBluetoothAdapter;
    public static ConnectThread conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sent test command to CareMate", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                conn.sendData("test send");
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_medication, R.id.nav_checkin, R.id.nav_alarm, R.id.nav_notification, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //Start Bluetooth connection
        CareMate = getIntent().getExtras().getParcelable("CareMate");
        Log.w("Bluetooth", CareMate.getAddress());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        conn = new ConnectThread(CareMate,true);
        conn.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void saveSettings(View v){
        conn.sendData("{wifi_ssid, network_name, wifi_pass, network_password}");
        Log.w("click", "WiFi Changes Saved");
    }

    public void saveMedication(View v){
        conn.sendData("{bin1,monday-1159,bin2,tuesday-0800,wednesday-0830}");
        Log.w("click", "Medication save Button Pressed");
    }

    public void saveAlarm(View v){
        conn.sendData("{alarm1,monday-1159,alarm2,tuesday-0800,alarm3,wednesday-0830}");
        Log.w("click", "Alarm Save Button Pressed");
    }

    public void saveNotification(View v){
        conn.sendData("{email,labbloe@gmail.com,phone,3147661588}");
        Log.w("click", "Notification Save Button Pressed");
    }

    /*
        Bluetooth Setup
     */

    public class ConnectThread extends Thread {
        BluetoothDevice cDevice;
        BluetoothSocket socket;
        ConnectedThread ct;

        ConnectThread(BluetoothDevice device, boolean insecureConnection) {
            cDevice = device;
            //ParcelUuid list[] = device.getUuids();
            //Log.d("UUID", list[0].toString());
            /*
            NOTE: This is the default serial BT UUID that many devices default to
                  The ESP32 library we are using does not allow for custom UUID allocation.
                  However, it does default to the address below.
             */
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            try {
                if (insecureConnection) {
                    socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                } else {
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                }
            } catch (IOException e) {
                e.getMessage();
            }
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();

            try {
                Log.d("BT", "Socket ready to connect");
                socket.connect();
                Log.d("BT", "Socket connected");
                // out = socket.getOutputStream();
                // input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            } catch (final IOException e) {
                Log.d("ERROR","Msg: ");
                e.getMessage();
            }
            catch(Exception e){
                Log.d("ERROR","Full Msg: ");
                e.getMessage();
            }

            ct = new ConnectedThread(socket);

            //ct.write("Q-smart".getBytes());
            /*try {
                socket.close();
            } catch (final IOException closeException) {
                closeException.getMessage();
            }*/
        }

        public void sendData(String message) {
            Log.d(TAG, message);
            if (socket != null) {
                ct.write(message.getBytes());
            } else {
                Toast.makeText(MainActivity.this,"Please connect to bluetooth first", Toast.LENGTH_LONG).show();
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    Log.w("READ", "message received");
                    // Send the obtained bytes to the UI activity.
               /* Message readMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_READ, numBytes, -1,
                        mmBuffer);
                readMsg.sendToTarget();*/
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
           /* Message writtenMsg = mHandler.obtainMessage(
                    MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
            writtenMsg.sendToTarget();*/
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
           /* Message writeErrorMsg =
                    mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
            Bundle bundle = new Bundle();
            bundle.putString("toast",
                    "Couldn't send data to the other device");
            writeErrorMsg.setData(bundle);
            mHandler.sendMessage(writeErrorMsg);*/
            }
        }


    }
}


