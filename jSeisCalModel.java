

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.StringTokenizer;

import calc.*;
import jlib330.*;


public class jSeisCalModel {
	//... Constants
	private static final String INITIAL_Q330SERIAL = "0100000123456789";

	private static final String INITIAL_Q330ADDRESS = "192.168.1.10";
	private static final String INITIAL_Q330DATAPORT = "3";
	private static final String INITIAL_Q330AUTHCODE = "0000000000000000";
	private static final int Q330BASEPORT = 5330;

	private static final String INITIAL_TABLEADDRESS = "192.168.1.20";
	private static final String INITIAL_TABLEDISPLACEMENT = "0.893";
	private static final String INITIAL_TABLEFREEPERIOD = "360";
	private static final String INITIAL_TABLEDAMPING = "0.707";
	private static final String INITIAL_TABLEBITS = "26";
	private static final String INITIAL_TABLEVOLTS = "40";
	private static final String INITIAL_TABLECHANNEL = "HHZ";

	private static final String INITIAL_METROZETADDRESS = "192.168.1.30";

	private String q330serial;
	private String q330address;
	private String q330dataport;
	private String q330authcode;

	private String tableAddress;
	private String tableDisplacement;
	private String tableFreePeriod;
	private String tableDamping;
	private String tableBits;
	private String tableVolts;
	private String tableChannel;
	public boolean tableMode = false;
	public ModBus tableMB;
	public TableBangs tableControlVertical;
	public TableBangsValued tableControlHorizontal;

	private String metrozetAddress;

	public UDPClient q330controlsocket = null;;
	public UDPClient q330datasocket = null;
	private InetAddress q330inetaaddress = null;
	UdpServer q330UdpServer = null; 

	private int calcdatastartindex;
	private int calcdatastopindex;
	private int calcdatalenght;
	public long ecalibstart;
	public long ecalibstop;

	public CalcDataset dataset = null;
	public ECalibDataset edataset = null;
	public Data buffers = null;
	public q330 q330 = null;

	public MetrozetClient metrozetClient;


	//============================================================== constructor
	/** Constructor */
	jSeisCalModel() {
		reset();
	}

	//==================================================================== reset
	/** Reset to initial value. */
	public void reset() {



		FileInputStream fstream;
		if (new File("jSeisCal.ini").exists()) {
			try {

				Properties p = new Properties();
				p.loadFromXML(new FileInputStream("jSeisCal.ini"));
				p.list(System.out);
				q330address = p.getProperty("q330address");
				q330serial = p.getProperty("q330serial");
				q330dataport = p.getProperty("q330dataport");
				q330authcode = p.getProperty("q330authcode");

				tableAddress = p.getProperty("tableAddress");
				tableDisplacement = p.getProperty("tableDisplacement");
				tableFreePeriod = p.getProperty("tableFreePeriod");
				tableDamping = p.getProperty("tableDamping");
				tableBits = p.getProperty("tableBits");
				tableVolts = p.getProperty("tableVolts");
				tableChannel = p.getProperty("tableChannel");

				metrozetAddress = p.getProperty("metrozetAddress");

			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			System.out.println ("jSeisCal.ini not found - using defaults");
			q330serial = INITIAL_Q330SERIAL;
			q330address = INITIAL_Q330ADDRESS;
			q330dataport = INITIAL_Q330DATAPORT;
			q330authcode = INITIAL_Q330AUTHCODE;

			tableAddress = INITIAL_TABLEADDRESS;
			tableDisplacement = INITIAL_TABLEDISPLACEMENT;
			tableFreePeriod = INITIAL_TABLEFREEPERIOD;
			tableDamping = INITIAL_TABLEDAMPING;
			tableBits = INITIAL_TABLEBITS;
			tableVolts = INITIAL_TABLEVOLTS;
			tableChannel = INITIAL_TABLECHANNEL;

			metrozetAddress = INITIAL_METROZETADDRESS;

			WriteProperties();    		
		}

		dataset = new CalcDataset();
		buffers = new Data();
		q330 = new q330(buffers);
		tableMB = new ModBus();


	}


	public void WriteProperties() {

		Properties p = new Properties();
		p.setProperty("q330address", q330address);
		p.setProperty("q330serial", q330serial);
		p.setProperty("q330authcode", q330authcode);
		p.setProperty("q330dataport", q330dataport);
		p.setProperty("tableAddress", tableAddress);
		p.setProperty("tableDisplacement", tableDisplacement);
		p.setProperty("tableFreePeriod", tableFreePeriod);
		p.setProperty("tableDamping", tableDamping);
		p.setProperty("tableBits", tableBits);
		p.setProperty("tableVolts", tableVolts);
		p.setProperty("tableChannel", tableChannel);
		p.setProperty("metrozetAddress", metrozetAddress);

		try {
			FileOutputStream out = new FileOutputStream("jSeisCal.ini");
			p.storeToXML(out, "jSeisCal Configuration File");
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//================================================================= set

	public void setQ330Serial(String serial) {
		q330serial = serial;
	}
	public void setQ330Address(String address) {
		q330address = address;
	}
	public void setQ330DataPort(String dp) {
		q330dataport = dp;
	}
	public void setQ330AuthCode(String code) {
		q330authcode = code;
	}
	public void setDataStartIndex(){
		if (dataset.axis == "HHZ") {
			calcdatastartindex = buffers.hhz.index;	
		}else if (dataset.axis == "HHN") {
			calcdatastartindex = buffers.hhn.index;	
		}else if (dataset.axis == "HHE") {
			calcdatastartindex = buffers.hhe.index;	
		} else if (dataset.axis == "BHZ") {
			calcdatastartindex = buffers.bhz.index;	
		}else if (dataset.axis == "BHN") {
			calcdatastartindex = buffers.bhn.index;	
		}else if (dataset.axis == "BHE") {
			calcdatastartindex = buffers.bhe.index;	
		}

	}
	public void setDataStopIndex(){
		if (dataset.axis == "HHZ") {
			calcdatastopindex = buffers.hhz.index;	
		}else if (dataset.axis == "HHN") {
			calcdatastopindex = buffers.hhn.index;	
		}else if (dataset.axis == "HHE") {
			calcdatastopindex = buffers.hhe.index;	
		}
		if (dataset.axis == "BHZ") {
			calcdatastopindex = buffers.bhz.index;	
		}else if (dataset.axis == "BHN") {
			calcdatastopindex = buffers.bhn.index;	
		}else if (dataset.axis == "BHE") {
			calcdatastopindex = buffers.bhe.index;	
		}
	}

	public void setECalibTimeStampStart(){
		buffers.setStart();		
	}
	public void setECalibTimeStampStop(){
		buffers.setStop();
	}

	public void setTableAddress(String address) {
		tableAddress = address;
	}
	public void setTableDisplacement(String dis) {
		tableDisplacement = dis;
		dataset.displacement = Double.valueOf(dis);
	}
	public void setTableFreePeriod(String s) {
		tableFreePeriod = s;
		dataset.freeperiod = Double.valueOf(s);
	}
	public void setTableDamping(String s) {
		tableDamping = s;
		dataset.damping = Double.valueOf(s);
	}
	public void setTableBits(String s) {
		tableBits = s;
		dataset.bits = Double.valueOf(s);
	}
	public void setTableVolts(String s) {
		tableVolts = s;
		dataset.volts = Double.valueOf(s);
	}
	public void setTableChannel(String s) {
		tableChannel = s;
		dataset.axis = s;
	}
	public void setMetrozetAddress(String address) {
		metrozetAddress = address;
	}
	//================================================================= get

	public String getQ330Serial() {
		return q330serial;
	}
	public String getQ330Address() {
		return q330address;
	}
	public String getQ330DataPort() {
		return q330dataport;
	}
	public String getQ330AuthCode() {
		return q330authcode;
	}

	public int getDataStartIndex(){
		return calcdatastartindex;
	}
	public int getDataStopIndex(){
		return calcdatastopindex;
	}
	public String getTableAddress() {
		return tableAddress;
	}
	public String getTableDisplacement() {
		return tableDisplacement;
	}
	public String getTableFreePeriod() {
		return tableFreePeriod;
	}
	public String getTableDamping() {
		return tableDamping;
	}
	public String getTableBits() {
		return tableBits;
	}
	public String getTableVolts() {
		return tableVolts;
	}
	public String getTableChannel() {
		return tableChannel;
	}
	public String getMetrozetAddress() {
		return metrozetAddress;
	}

	public UDPClient newQ330ControlSocket() {
		int port = Integer.valueOf(getQ330DataPort()) * 2 + Q330BASEPORT;
		System.out.println(port);
		try {
			q330inetaaddress = InetAddress.getByName(getQ330Address());
			q330controlsocket = new UDPClient(q330inetaaddress, port, 5500);
			q330controlsocket.setTimeOut(5000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return q330controlsocket;
	}

	public UDPClient newQ330DataSocket() {
		int port = Integer.valueOf(getQ330DataPort()) * 2 + Q330BASEPORT + 1;
		System.out.println(port);
		try {
			q330inetaaddress = InetAddress.getByName(getQ330Address());
			q330datasocket = new UDPClient(q330inetaaddress, port, 5501);
			q330datasocket.setTimeOut(5000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return q330datasocket;
	}

	public UDPClient getQ330DataSocket() {
		return q330datasocket;
	}    

	public UDPClient getQ330ControlSocket() {
		return q330controlsocket;
	}

	public void setQ330DataSocket(UDPClient socket) {
		q330datasocket = socket;
	}    

	public void setQ330ControlSocket(UDPClient socket) {
		q330controlsocket = socket;
	}


	public void setCalcDataset() {
		System.out.println("calcdatastartindex = " + calcdatastartindex);
		System.out.println("calcdatastopindex = " + calcdatastopindex);
		calcdatalenght = calcdatastopindex - calcdatastartindex;
		System.out.println("calcdatalenght = " + calcdatalenght);  
		dataset.bits = Double.valueOf(tableBits);
		dataset.volts = Double.valueOf(tableVolts);
		dataset.freeperiod = Double.valueOf(tableFreePeriod);
		dataset.damping = Double.valueOf(tableDamping);
		dataset.displacement = Double.valueOf(tableDisplacement);


		if (dataset.axis == "HHZ") {
			dataset.samplerate = 200; // temporaire
			dataset.Init(calcdatalenght);
			System.arraycopy(buffers.hhz.samples, calcdatastartindex, dataset.samples, 0, calcdatalenght);
			System.arraycopy(buffers.hhz.times, calcdatastartindex, dataset.times, 0, calcdatalenght);
		}else if (dataset.axis == "HHN") {
			dataset.samplerate = 200; // temporaire
			dataset.Init(calcdatalenght);
			System.arraycopy(buffers.hhn.samples, calcdatastartindex, dataset.samples, 0, calcdatalenght);
			System.arraycopy(buffers.hhn.times, calcdatastartindex, dataset.times, 0, calcdatalenght);
		}else if (dataset.axis == "HHE") {
			dataset.samplerate = 200; // temporaire
			dataset.Init(calcdatalenght);
			System.arraycopy(buffers.hhe.samples, calcdatastartindex, dataset.samples, 0, calcdatalenght);
			System.arraycopy(buffers.hhe.times, calcdatastartindex, dataset.times, 0, calcdatalenght);
		}else if (dataset.axis == "BHZ") {
			dataset.samplerate = 20; // temporaire
			dataset.Init(calcdatalenght);
			System.arraycopy(buffers.bhz.samples, calcdatastartindex, dataset.samples, 0, calcdatalenght);
			System.arraycopy(buffers.bhz.times, calcdatastartindex, dataset.times, 0, calcdatalenght);
		}else if (dataset.axis == "BHN") {    		
			dataset.samplerate = 20; // temporaire
			dataset.Init(calcdatalenght);
			System.arraycopy(buffers.bhn.samples, calcdatastartindex, dataset.samples, 0, calcdatalenght);
			System.arraycopy(buffers.bhn.times, calcdatastartindex, dataset.times, 0, calcdatalenght);
		}else if (dataset.axis == "BHE") {
			dataset.samplerate = 20; // temporaire
			dataset.Init(calcdatalenght);
			System.arraycopy(buffers.bhe.samples, calcdatastartindex, dataset.samples, 0, calcdatalenght);
			System.arraycopy(buffers.bhe.times, calcdatastartindex, dataset.times, 0, calcdatalenght);
		}


	}


	public void setECalibDataset(boolean typeH, boolean displayFrame) {    	
		if (typeH) {
			edataset = new ECalibDataset(buffers, true);
			edataset.saveDatasetToFile();	
		} else {
			edataset = new ECalibDataset(buffers, false);
			edataset.saveDatasetToFile();	
		}
		
		if (displayFrame) {
			ECalibResult cr = new ECalibResult(edataset, true);
		}
	}

	public static void main(String[] args) {

		jSeisCalModel test = new jSeisCalModel();
	}
}
