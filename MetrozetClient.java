import java.io.*;
import java.net.*;

import Console.Console;

public class MetrozetClient 
{
	
	Socket clientSocket = new Socket();
	InetSocketAddress soaddr;
	SenaTCPListener sena;
	Thread t;
	String command;
	DataOutputStream outToServer;
	Console console;
	
	MetrozetClient(String metrozetAddress, Console cons) {
		
		console = cons;

		try {
			soaddr = new InetSocketAddress(metrozetAddress, 6001);
			if (clientSocket.isConnected()) {
				clientSocket.close();
			}
			clientSocket = new Socket();
			//clientSocket.setSoTimeout(5000);
			//clientSocket.connect(soaddr, 5000);
			clientSocket.connect(soaddr);
			//clientSocket.setSoTimeout(500);
			//clientSocket.setSoLinger(false, 0);
			clientSocket.setKeepAlive(true);

			
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			sena = new SenaTCPListener(clientSocket, console);
			t = new Thread(sena);
			t.start();			
			command = "enableabcdef\r";
			outToServer.writeBytes(command);		
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		//t.stop();
	}
	
	
	public void send(String cmd) {
		command = cmd + "\r";
		if (clientSocket.isConnected()) {
		//if (clientSocket.isBound()) {
			try {
				outToServer.writeBytes(command);
				console.write(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}

	}
	
	public boolean isOpen() {
		return clientSocket.isConnected();
	}
	  
	public static void main(String argv[]) throws Exception
	 {
		MetrozetClient cl = new MetrozetClient("192.168.18.8", new Console());
	 }
}
