package com.example.tobi.enlighter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;


/**
 * Created by tobi on 16.02.16.
 */
public class BluetoothCommunication {

    private BluetoothAdapter blueAdapter;
    private BluetoothDevice bluetoothDevice;

    public BluetoothCommunication() {

        blueAdapter = BluetoothAdapter.getDefaultAdapter();

        Set pairedDevices = blueAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for(Object device : pairedDevices) {
                    bluetoothDevice = (BluetoothDevice) device;
                    break;
            }
        }

        ParcelUuid[] uuids = bluetoothDevice.getUuids();


        BluetoothSocket mmSocket = null;
        try {
            mmSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            mmSocket.connect();
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            InputStream mmInputStream = mmSocket.getInputStream();

            String test = "Test123";
            mmOutputStream.write(test.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
