package tobi.com.ledcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import tobi.com.ledcontrol.fragments.EmptyFragment;
import tobi.com.ledcontrol.fragments.Mode1Fragment;
import tobi.com.ledcontrol.fragments.Mode2Fragment;
import tobi.com.ledcontrol.fragments.Mode3Fragment;
import tobi.com.ledcontrol.fragments.Mode4Fragment;

public class MainActivity extends Activity {

    public static final String testAddress = "192.168.178.197";
    public static final int testPort = 8080;

    private List<Integer> ids = Arrays.asList(R.id.id_01, R.id.id_02, R.id.id_03, R.id.id_04, R.id.id_05);
    private List<Integer> modes = Arrays.asList(R.id.mode_01, R.id.mode_02, R.id.mode_03, R.id.mode_04);

    private static final int NO_MODE = 123456789;

    private int mode;
    private Fragment currentFragment;

    private List<String> selectedIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedIds = new ArrayList<>();

        for(Integer integer : ids) {
            Button tmpButton = (Button) findViewById(integer);
            tmpButton.setBackgroundColor(Color.WHITE);
            tmpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String pressedId = (String)((Button)view).getText();

                    if (selectedIds.contains(pressedId)) {
                        selectedIds.remove(pressedId);
                        view.setBackgroundColor(Color.WHITE);
                    } else {
                        selectedIds.add(pressedId);
                        view.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.colorAccent));
                        if(selectedIds.size()!=1) {
                            changeMode(mode = NO_MODE);
                        } else{
                            //TODO: Fetch data for this LED stripe and update mode
                            System.out.println("One selected");
                        }
                    }
                }
            });
        }

        for(Integer integer : modes) {
            Button tmpButton = (Button) findViewById(integer);
            tmpButton.setBackgroundColor(Color.WHITE);
            tmpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mode != view.getId())
                        changeMode(mode = view.getId());
                }
            });
        }
    }

    private void disableAllModeButtons() {
        for(Integer integer : modes) {
            Button tmpButton = (Button) findViewById(integer);
            tmpButton.setBackgroundColor(Color.WHITE);
        }
    }

    public void sendMessage(View view) {

        for(String id : selectedIds) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(Commands.LED_ID, id);

                switch(mode) {
                    case R.id.mode_01:
                        obj.put(Commands.CMD, Commands.MODE_FADE);
                        int fadeSpeed = ((Mode1Fragment)currentFragment).getSeekBarProgress();
                        obj.put(Commands.FADE_SPEED, fadeSpeed);
                        break;
                    case R.id.mode_02:
                        obj.put(Commands.CMD, Commands.MODE_COLOR);
                        int hexColor = ((Mode2Fragment)currentFragment).getColorHex();
                        obj.put(Commands.RED, (hexColor & 0xFF0000) >> 16);
                        obj.put(Commands.GREEN, (hexColor & 0xFF00) >> 8);
                        obj.put(Commands.BLUE, (hexColor & 0xFF));

                        //int rgb = Color.rgb((hexColor & 0xFF0000) >> 16, (hexColor & 0xFF00) >> 8, (hexColor & 0xFF));
                        break;
                    case R.id.mode_03:
                        obj.put(Commands.CMD, Commands.MODE_CHILL);

                        break;
                    case R.id.mode_04:
                        obj.put(Commands.CMD, Commands.MODE_POLICE);

                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String sendMessage = obj.toString() + "\n";
            System.out.println(sendMessage);
            sendMessage("kobold");//42[\"messageType\",{\"greeting\":\"hello\"}]");
            System.out.println("----------------------kobold");
        }
    }

    private void sendMessage(String message) {
        ClientTask myClientTask = new ClientTask(testAddress, testPort, message);
        myClientTask.execute();
    }

    /*
    public void onClick(View view) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "set_mode");
            obj.put("mode", "fade");
            obj.put("led_id", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sendMessage = obj.toString() + "\n";
        System.out.println(sendMessage);
        sendMessage(sendMessage);
    }*/

    private void changeMode(int newMode) {
        Fragment newFragment = null;

        for(Integer id : modes) {
            Button tmp = (Button) findViewById(id);
            tmp.setBackgroundColor(Color.WHITE);
        }
        Button tmpButton = (Button) findViewById(newMode);

        switch (newMode) {
            case R.id.mode_01:
                newFragment = new Mode1Fragment();
                tmpButton.setBackgroundColor(ContextCompat.getColor(tmpButton.getContext(),R.color.colorAccent));
                break;
            case R.id.mode_02:
                newFragment = new Mode2Fragment();
                tmpButton.setBackgroundColor(ContextCompat.getColor(tmpButton.getContext(),R.color.colorAccent));
                break;
            case R.id.mode_03:
                newFragment = new Mode3Fragment();
                tmpButton.setBackgroundColor(ContextCompat.getColor(tmpButton.getContext(),R.color.colorAccent));
                break;
            case R.id.mode_04:
                newFragment = new Mode4Fragment();
                tmpButton.setBackgroundColor(ContextCompat.getColor(tmpButton.getContext(),R.color.colorAccent));
                break;
            case NO_MODE:
                newFragment = new EmptyFragment();
                disableAllModeButtons();
                break;
        }

        if (newFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, newFragment).commit();
            currentFragment = newFragment;
        }
    }
}