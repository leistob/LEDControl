package com.example.tobi.enlighter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class blueActivity extends Activity {

    /*
     * Arduino Bluetooth-Modul. Android Test-Applikation www.frag-duino.de
     */

    // UUID fuer Kommunikation mit Seriellen Modulen
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String LOG_TAG = "FRAGDUINO";

    // Variablen
    private BluetoothAdapter adapter = null;
    private BluetoothSocket socket = null;
    private OutputStream stream_out = null;
    private InputStream stream_in = null;
    private boolean is_connected = false;
    private static String mac_adresse; // MAC Adresse des Bluetooth Adapters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        Log.d(LOG_TAG, "Bluetest: OnCreate");

        // Textfeld setzen
        ((TextView) findViewById(R.id.text_uuid)).setText("UUID: " + uuid);

        // Verbindung mit Bluetooth-Adapter herstellen
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            Toast.makeText(this, "Bitte Bluetooth aktivieren",
                    Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG,
                    "onCreate: Bluetooth Fehler: Deaktiviert oder nicht vorhanden");
            finish();
            return;
        } else
            Log.d(LOG_TAG, "onCreate: Bluetooth-Adapter ist bereit");
    }

    public void verbinden(View v) {
        mac_adresse = ((EditText) findViewById(R.id.text_adresse)).getText()
                .toString();
        Log.d(LOG_TAG, "Verbinde mit " + mac_adresse);

        BluetoothDevice remote_device = adapter.getRemoteDevice(mac_adresse);

        // Socket erstellen
        try {
            socket = remote_device
                    .createInsecureRfcommSocketToServiceRecord(uuid);
            Log.d(LOG_TAG, "Socket erstellt");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Socket Erstellung fehlgeschlagen: " + e.toString());
        }

        adapter.cancelDiscovery();

        // Socket verbinden
        try {
            socket.connect();
            Log.d(LOG_TAG, "Socket verbunden");
            is_connected = true;
        } catch (IOException e) {
            is_connected = false;
            Log.e(LOG_TAG, "Socket kann nicht verbinden: " + e.toString());
        }

        // Socket beenden, falls nicht verbunden werden konnte
        if (!is_connected) {
            try {
                socket.close();
            } catch (Exception e) {
                Log.e(LOG_TAG,
                        "Socket kann nicht beendet werden: " + e.toString());
            }
        }

        // Outputstream erstellen:
        try {
            stream_out = socket.getOutputStream();
            Log.d(LOG_TAG, "OutputStream erstellt");
        } catch (IOException e) {
            Log.e(LOG_TAG, "OutputStream Fehler: " + e.toString());
            is_connected = false;
        }

        // Inputstream erstellen
        try {
            stream_in = socket.getInputStream();
            Log.d(LOG_TAG, "InputStream erstellt");
        } catch (IOException e) {
            Log.e(LOG_TAG, "InputStream Fehler: " + e.toString());
            is_connected = false;
        }

        if (is_connected) {
            Toast.makeText(this, "Verbunden mit " + mac_adresse,
                    Toast.LENGTH_LONG).show();
            ((Button) findViewById(R.id.bt_verbinden))
                    .setBackgroundColor(Color.GREEN);
        } else {
            Toast.makeText(this, "Verbindungsfehler mit " + mac_adresse,
                    Toast.LENGTH_LONG).show();
            ((Button) findViewById(R.id.bt_verbinden))
                    .setBackgroundColor(Color.RED);
        }
    }

    public void senden(View v) {
        String message = ((EditText) findViewById(R.id.text_eingabe)).getText()
                .toString();
        message += "\n";
        byte[] msgBuffer = message.getBytes();
        if (is_connected) {
            Log.d(LOG_TAG, "Sende Nachricht: " + message);
            try {
                stream_out.write(msgBuffer);
            } catch (IOException e) {
                Log.e(LOG_TAG,
                        "Bluetest: Exception beim Senden: " + e.toString());
            }
        }
    }

    public void empfangen(View v) {
        byte[] buffer = new byte[1024]; // Puffer
        int laenge; // Anzahl empf. Bytes
        String msg = "";
        try {
            if (stream_in.available() > 0) {
                laenge = stream_in.read(buffer);
                Log.d(LOG_TAG,
                        "Anzahl empfangender Bytes: " + String.valueOf(laenge));

                // Message zusammensetzen:
                for (int i = 0; i < laenge; i++)
                    msg += (char) buffer[i];

                Log.d(LOG_TAG, "Message: " + msg);
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

            } else
                Toast.makeText(this, "Nichts empfangen", Toast.LENGTH_LONG)
                        .show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Fehler beim Empfangen: " + e.toString());
        }
    }

    public void trennen(View v) {
        if (is_connected && stream_out != null) {
            is_connected = false;
            ((Button) findViewById(R.id.bt_verbinden))
                    .setBackgroundColor(Color.RED);
            Log.d(LOG_TAG, "Trennen: Beende Verbindung");
            try {
                stream_out.flush();
                socket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG,
                        "Fehler beim beenden des Streams und schliessen des Sockets: "
                                + e.toString());
            }
        } else
            Log.d(LOG_TAG, "Trennen: Keine Verbindung zum beenden");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy. Trenne Verbindung, falls vorhanden");
        trennen(null);
    }
}