import java.net.*;
import java.io.*;


public class ModBus  {
	
	Socket echoSocket = new Socket();
	InetSocketAddress soaddr;
	InetAddress target;
	DataOutputStream out;
	DataInputStream in;
	String myHost;
	byte ModBusRTUCmd[];
	byte ModBusID;
	boolean IsRuning = false;

	public boolean open(String ip, int i) {
		boolean state = false;	
		echoSocket = null;
		out = null;
		in = null;
		ModBusRTUCmd = new byte[128];
		if (IsRuning)
			Close();
		IsRuning = true;
		myHost = chkHostIP(ip);
		ModBusID = (byte) i;
		try {
			state = TCPInit();
		} catch (Exception exception) {
			System.out.println("Error in TCPInit()");
			exception.printStackTrace();
		}
		return state;
	}

	public boolean IsModBusAlive() {
		return IsRuning;
	}

	public boolean connect(String s) {
		
		boolean connected = false;
		System.out.println("Sending Modbus Request To : " + s );
		ModBusRTUCmd = new byte[128];
		if (IsRuning)
			Close();
		IsRuning = true;
		myHost = chkHostIP(s);
		ModBusID = 1;
		try {
			if (TCPInit()) {
				System.out.println("Connected to " + s);
				connected = true;
				
			}
			else {
				System.out.println("Not Connected. Check address : " + s);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		return connected; 
	}

	public String GetHostIP() {
		return myHost;
	}

	public boolean TCPInit() throws Exception {
		try {
			soaddr = new InetSocketAddress(myHost, 502);
			if (echoSocket.isConnected()) {
				echoSocket.close();
				}
			echoSocket = new Socket();
			echoSocket.setSoTimeout(500);
			echoSocket.connect(soaddr, 500);
			echoSocket.setSoTimeout(500);
			echoSocket.setSoLinger(false, 0);
			echoSocket.setKeepAlive(true);
						
			out = new DataOutputStream(echoSocket.getOutputStream());
			in = new DataInputStream(echoSocket.getInputStream());

		} catch (UnknownHostException unknownhostexception) {
			System.err.println("Don't know about host.");
			return false;
		} catch (IOException ioexception) {
			System.err.println("Couldn't get I/O for the connection to: Adam.");
			return false;
		}
		return true;
	}

	public void ForceCoil(int i, boolean flag) {
		if (IsRuning) {
			ModBusRTUCmd[0] = 0;
			ModBusRTUCmd[1] = 0;
			ModBusRTUCmd[2] = 0;
			ModBusRTUCmd[3] = 0;
			ModBusRTUCmd[4] = 0;
			ModBusRTUCmd[5] = (byte) 6;
			ModBusRTUCmd[6] = (byte) ModBusID;
			ModBusRTUCmd[7] = (byte) 5;
			ModBusRTUCmd[8] = 0;
	
			if (i == 0) 
				ModBusRTUCmd[9] = (byte) 16;
			else 
				ModBusRTUCmd[9] = (byte) 17;
	
			if (flag == true) 
				ModBusRTUCmd[10] = (byte) 0xFF;
			else 
				ModBusRTUCmd[10] = 0;
	
			ModBusRTUCmd[11] = 0;
	
		    SendCmd2Adam(ModBusRTUCmd, 12);
		}
		else {
			System.out.println("Not Connected");
		}
	}

	public boolean ReadCoil(int i, int j, byte abyte0[]) {
		if (i <= 1)
			i = 0;
		else
			i--;
		ModBusRTUCmd[0] = 0;
		ModBusRTUCmd[1] = 0;
		ModBusRTUCmd[2] = 0;
		ModBusRTUCmd[3] = 0;
		ModBusRTUCmd[4] = 0;
		ModBusRTUCmd[5] = 6;
		ModBusRTUCmd[6] = ModBusID;
		ModBusRTUCmd[7] = 1;
		ModBusRTUCmd[8] = 0;
		ModBusRTUCmd[9] = (byte) i;
		ModBusRTUCmd[10] = 0;
		ModBusRTUCmd[11] = (byte) j;
		if (SendCmd2Adam(ModBusRTUCmd, 12)) {
			for (int k = 0; k < ModBusRTUCmd[8]; k++) {
				for (int l = 0; l < 8; l++)
					if ((ModBusRTUCmd[9 + k] >> l & 1) > 0)
						abyte0[k * 8 + l] = 1;
					else
						abyte0[k * 8 + l] = 0;

			}

		} else {
			return false;
		}
		return true;
	}

	public boolean PresetRegister(int i, int j) {
		if (i <= 1)
			i = 0;
		else
			i--;
		ModBusRTUCmd[0] = 0;
		ModBusRTUCmd[1] = 0;
		ModBusRTUCmd[2] = 0;
		ModBusRTUCmd[3] = 0;
		ModBusRTUCmd[4] = 0;
		ModBusRTUCmd[5] = 6;
		ModBusRTUCmd[6] = ModBusID;
		ModBusRTUCmd[7] = 6;
		ModBusRTUCmd[8] = 0;
		ModBusRTUCmd[9] = (byte) i;
		ModBusRTUCmd[10] = (byte) ((j & 0xff) >> 8);
		ModBusRTUCmd[11] = (byte) (j & 0xff);
		return SendCmd2Adam(ModBusRTUCmd, 12);
	}

	public boolean ReadRegister(int i, int j, byte abyte0[]) {
		if (i <= 1)
			i = 0;
		else
			i--;
		ModBusRTUCmd[0] = 0;
		ModBusRTUCmd[1] = 0;
		ModBusRTUCmd[2] = 0;
		ModBusRTUCmd[3] = 0;
		ModBusRTUCmd[4] = 0;
		ModBusRTUCmd[5] = 6;
		ModBusRTUCmd[6] = ModBusID;
		ModBusRTUCmd[7] = 3;
		ModBusRTUCmd[8] = 0;
		ModBusRTUCmd[9] = (byte) i;
		ModBusRTUCmd[10] = 0;
		ModBusRTUCmd[11] = (byte) j;
		if (SendCmd2Adam(ModBusRTUCmd, 12)) {
			for (int k = 0; k < ModBusRTUCmd[8]; k++)
				abyte0[k] = ModBusRTUCmd[9 + k];

		} else {
			return false;
		}
		return true;
	}

	public boolean Adam5kASCCmd(byte abyte0[], int i) {
		ModBusRTUCmd[0] = 0;
		ModBusRTUCmd[1] = 0;
		ModBusRTUCmd[2] = 0;
		ModBusRTUCmd[3] = 0;
		ModBusRTUCmd[4] = 0;
		ModBusRTUCmd[6] = ModBusID;
		ModBusRTUCmd[7] = 16;
		ModBusRTUCmd[8] = 39;
		ModBusRTUCmd[9] = 15;
		if (i % 2 == 1)
			i++;
		ModBusRTUCmd[5] = (byte) (7 + i);
		ModBusRTUCmd[10] = 0;
		ModBusRTUCmd[11] = (byte) (i / 2);
		ModBusRTUCmd[12] = (byte) i;
		for (int j = 0; j < i; j++)
			ModBusRTUCmd[13 + j] = abyte0[j];

		if (!SendCmd2Adam(ModBusRTUCmd, 13 + i))
			return false;
		ModBusRTUCmd[0] = 0;
		ModBusRTUCmd[1] = 0;
		ModBusRTUCmd[2] = 0;
		ModBusRTUCmd[3] = 0;
		ModBusRTUCmd[4] = 0;
		ModBusRTUCmd[5] = 6;
		ModBusRTUCmd[6] = ModBusID;
		ModBusRTUCmd[7] = 3;
		ModBusRTUCmd[8] = 39;
		ModBusRTUCmd[9] = 15;
		ModBusRTUCmd[10] = 0;
		ModBusRTUCmd[11] = 50;
		if (SendCmd2Adam(ModBusRTUCmd, 12)) {
			for (int k = 0; k < 100; k++) {
				abyte0[k] = ModBusRTUCmd[9 + k];
				if (abyte0[k] == 13)
					return true;
			}

			return true;
		} else {
			return false;
		}
	}

	public void Close() {
		try {
			echoSocket.close();
			out.close();
			in.close();
		} catch (IOException ioexception) {
			IsRuning = false;
		} catch (NullPointerException nex) {
			IsRuning = false;
		}
	}

	private String chkHostIP(String s) {
		String s1 = "";
		if (s.length() < 15)
			return s;
		for (int i = 0; i < 16; i += 4)
			s1 = s1 + String.valueOf(Long.valueOf(s.substring(i, i + 3), 16)) + '.';

		return s1.substring(0, s1.length() - 1);
	}

	private boolean SendCmd2Adam(byte abyte0[], int i) {
		try {
			out.write(ModBusRTUCmd, 0, i);
			int j = in.read(abyte0, 0, 128);
			if (j <= 0)
				return false;
		} catch (IOException ioexception) {
			return false;
		} catch (NullPointerException nex) {
			return false;
		}
		return true;
	}
	
	public void push() {
		ForceCoil(0, true );
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ForceCoil(0, false );
	}
	



	

}

