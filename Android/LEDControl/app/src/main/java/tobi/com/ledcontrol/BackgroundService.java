package tobi.com.ledcontrol;

import android.app.IntentService;
import android.content.Intent;


public class BackgroundService extends IntentService {


    public BackgroundService() {
        super("Test");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

        System.out.println("Test");

    }
}