package com.example.tobi.enlighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main extends AppCompatActivity {

    private Button connectButton;
    private EditText macAdresEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = (Button) findViewById(R.id.connectButton);
        macAdresEditText = (EditText) findViewById(R.id.macEditText);
    }


    public void startLightController(View view) {

        //TODO:
        //Read out MAC Adress
        //Check if connection managable
        //Build up connection to arduino
        //start color activity


        Intent intent = new Intent(this, blueActivity.class);
        startActivity(intent);
    }

}
