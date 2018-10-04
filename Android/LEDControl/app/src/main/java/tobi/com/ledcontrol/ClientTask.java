package tobi.com.ledcontrol;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

class ClientTask extends AsyncTask<Void, Void, Void> {

    private String address;
    private String message;
    private int port;

    ClientTask(String address, int port, String  message){

        this.address = address;
        this.port = port;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            final Socket socket = new Socket(address, port);

            //Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            //String number = "adsfasdfasdfasdfasdfasdfasdfasdf";

            bw.write(message);
            bw.flush();
            System.out.println("Message sent to the server : " + message);


            new Thread( new Runnable() {
                @Override public void run() {
                    try {
                        System.out.println("Gleich hängt er!");
                        //Get the return message from the server
                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String message = br.readLine();
                        System.out.println("Message received from the server : " +message);
                        System.out.println("Hier hängt er!");
                    }
                    catch ( IOException e ) {
                        System.out.println( "Blockierung gelöst" );
                    }
                }
            } ).start();

            try {
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            socket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}