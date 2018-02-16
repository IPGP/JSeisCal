import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import Console.Console;


public class SenaTCPListener implements Runnable  {

	BufferedReader inFromServer;
	String buffer;
	Console console;
	
	SenaTCPListener(Socket clientSocket, Console output) {
		try {
			console = output;
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void run() {
		while(true) {
			try {			
				buffer = inFromServer.readLine();
				if (!buffer.startsWith("Ticks") && !buffer.startsWith("Seconds") && !buffer.equals("")) {
					System.out.println(buffer);
					console.write(buffer);			
				}							
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}


}
