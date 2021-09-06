package com.example.caremate;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class StartupActivity extends AppCompatActivity{

    boolean deviceConnected = false;
    LinearLayout logoPage;
    LinearLayout connectPage;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        logoPage = (LinearLayout) findViewById(R.id.startupLogo);
        connectPage = (LinearLayout) findViewById(R.id.findDevice);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(this,"Device Doesn't Support Bluetooth",Toast.LENGTH_LONG).show();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);


    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.w("Discovery", "Name: " + deviceName + "  MAC: " + deviceHardwareAddress);

                if(deviceHardwareAddress == "78:4f:43:4f:67:d4"){
                    Toast.makeText(StartupActivity.this, "Device FOUND using discovery!", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public void onClick(View v){
        /*
        Bluetooth devices will be scanned for and detected on the connect page.
        The CareMate device will advertise the same general name, and likewise
        the application will only bond with devices matching that name.

        For security purposes, all provisioning changes will need to be signed by the
        CareMate to take effect. Additionally, the CareMate will require a signed
        initial connect statement. If the signature fails, the connected device will
        be disconnected.
         */
        if(deviceConnected == false){
            logoPage.setVisibility(View.GONE);
            connectPage.setVisibility(View.VISIBLE);

            //First, check for paired devices before re-scanning to see if CareMate is already
            //saved on the device.
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0){
                //paired devices exist
                for (BluetoothDevice device : pairedDevices){
                    String deviceName = device.getName();
                    String deviceMAC = device.getAddress();
                    Log.w("Device", "Name: " + deviceName + "  MAC: " + deviceMAC);
                    if(deviceName == "CareMate"){
                        Toast.makeText(this, "Device FOUND!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this, "no saved device match", Toast.LENGTH_LONG).show();
                    }
                }
            }
            //If no paired device scan and find device
            Log.w("Bluetooth", "Starting device discovery");
            bluetoothAdapter.startDiscovery();



            deviceConnected = true;

        }
        else{
            Intent mainActivity = new Intent(StartupActivity.this, MainActivity.class);
            //mainActivity.putExtra("key", value); //pass parameters
            StartupActivity.this.startActivity(mainActivity);
        }
    }
}
