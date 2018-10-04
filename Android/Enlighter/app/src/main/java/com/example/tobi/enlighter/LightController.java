package com.example.tobi.enlighter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tobi.enlighter.View.RangeSeekBar;

public class LightController extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private Button applyButton;

    private TextView speedView;

    private RadioButton chooseRadioButton, fadeRadioButton;

    private SeekBar speedSeekbar;
    private RangeSeekBar rangeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_controller);

        applyButton = (Button) findViewById(R.id.applyButton);
        chooseRadioButton = (RadioButton) findViewById(R.id.manualChooserRadioButton);
        fadeRadioButton = (RadioButton) findViewById(R.id.fadeRadioButton);

        speedSeekbar = (SeekBar) findViewById(R.id.speedSeekbar);
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeSeekbar);
        speedView = (TextView) findViewById(R.id.speedView);

        speedSeekbar.setOnSeekBarChangeListener(this);
        chooseRadioButton.setChecked(true);
    }


    public void applyChanges(View view) {

        //TODO:
        //Check, wether connection still exists
        //Send applied data to arduino

        String stringToSend = createDataString();
    }


    private String createDataString() {
        String data = "";

        //Depending on the selected mode, String for transmission is built
        if(fadeRadioButton.isChecked()) {
            int speed = speedSeekbar.getProgress();
            int[] range = {(int)rangeSeekBar.getSelectedMinValue(), (int)rangeSeekBar.getSelectedMaxValue()};
        } else {
            data = "COLORCODE";
        }

        BluetoothConnector connector = new BluetoothConnector();
        try {
            connector.init();
            connector.run();
            //connector.write("test");
        }catch (Exception e) {
            e.printStackTrace();
        }



        return data;
    }

    public void chooseRadioButtonPressed(View view) {
        fadeRadioButton.setChecked(false);
    }
    public void fadeRadioButtonPressed(View view) {
        chooseRadioButton.setChecked(false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String text = (progress==100) ? "" + progress : " " + progress;
        speedView.setText(text);
    }
    public void onStartTrackingTouch(SeekBar seekBar) {}
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
