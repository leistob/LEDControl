import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class host {

    static BufferedReader bufferedReader;
    static InputStreamReader inputStreamReader;

    public static void main(String srgs[]) throws Exception{

        int count = 0;

        //hard code to use port 8080
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            
            System.out.println("I'm waiting here: " + serverSocket.getLocalPort());
            
            while (true) {
                
                try {
                    Socket socket = serverSocket.accept();
                    

					inputStreamReader = new InputStreamReader(socket.getInputStream());
					bufferedReader = new BufferedReader(inputStreamReader);
            
					String line;
					while((line=bufferedReader.readLine()) != null){
						System.out.println(line);		
						
						String com = "bash -c 433Utils/RPi_utils/./codesend 1200";
						System.out.println(com);				
            	        Process cmdProc = Runtime.getRuntime().exec(com);

						BufferedReader stdoutReader = new BufferedReader(
								 new InputStreamReader(cmdProc.getInputStream()));
						String line2;
						while ((line2 = stdoutReader.readLine()) != null) {
						   // process procs standard output here
						   System.out.println(line2);
						}

						BufferedReader stderrReader = new BufferedReader(
								 new InputStreamReader(cmdProc.getErrorStream()));
						while ((line = stderrReader.readLine()) != null) {
						   // process procs standard error here
						   System.out.println(line);
						}

						int retValue = cmdProc.exitValue();

						System.out.println("REady");

                    }	
                    
                    
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                } finally{
	            if(bufferedReader!=null){
	                try {
	                    bufferedReader.close();
	                } catch (IOException ex) {
	                    System.out.print(ex.toString());
	                }
	            }	            
        	}
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
    
    private static class HostThread extends Thread{
        
        private Socket hostThreadSocket;
        int cnt;
        
        HostThread(Socket socket, int c){
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {

            OutputStream outputStream;
            try {
                outputStream = hostThreadSocket.getOutputStream();
                
                try (PrintStream printStream = new PrintStream(outputStream)) {
                        printStream.print("Hello from Raspberry Pi in background thread, you are #" + cnt);
                }
            } catch (IOException ex) {
                Logger.getLogger(host.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    hostThreadSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(host.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
