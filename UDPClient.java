import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.RegularTimePeriod;
import javax.swing.Timer;

import jlib330.*;


public class UDPClient extends KeyAdapter {

	private InetAddress remote;
	private int port;
	private DatagramSocket ds;
	private static boolean stop = false;
	private static byte[] network;
	private static byte[] station;

	/**
	 * Creates a new UDPClient.
	 * 
	 * @param remoteHost
	 *            The address of the remote host to which data will be sent
	 * @param port
	 *            The port on the remote host to which data will be sent
	 * @param timeout
	 *            The number of milliseconds to wait to receive a packet before
	 *            timing out
	 * @throws SocketException
	 */
	public UDPClient(InetAddress remoteHost, int remotePort, int localPort)
			throws SocketException {

		this.remote = remoteHost;
		this.port = remotePort;
		ds = new DatagramSocket(localPort);
		// the next lines require Java 2
		ds.connect(remote, port);

	}
	
	public void setTimeOut(int timeout) throws SocketException {
		ds.setSoTimeout(timeout);
	}
	

	public UDPClient(InetAddress remoteHost, int remotePort)
			throws SocketException {

		this.remote = remoteHost;
		this.port = remotePort;
		ds = new DatagramSocket();
		// the next line requires Java 2
		ds.connect(remote, port);

	}

	public UDPClient(String hostname, int port) throws UnknownHostException,
			SocketException {
		this(InetAddress.getByName(hostname), port);
	}

	/**
	 * This method sends data to the remote host via UDP. If the array is longer
	 * than the maximum reliable length of a UDP Datagram (8192) bytes then an
	 * IOException is thrown
	 * 
	 * @param data
	 *            A byte array containing the data to be sent
	 * @throws IOException
	 */
	public void send(byte[] data) throws IOException {

		if (data.length > 8192)
			throw new IOException("Too much data");
		DatagramPacket dp = new DatagramPacket(data, data.length, remote, port);
		ds.send(dp);

	}

	/**
	 * This method sends an empty datagram to the remote host via UDP.
	 * 
	 * @throws IOException
	 */
	public void send() throws IOException {
		byte[] b = new byte[1];
		this.send(b);
	}
	
	public void close() throws IOException {
		this.ds.close();
		this.ds.disconnect();
	}
	
	public boolean isClosed() throws IOException {
		return ds.isClosed();
	}

	private byte[] receiveBuffer = new byte[65507];

	/**
	 * This method blocks until a UDP Datagram is received. This can be an
	 * indefinite amount of time if the host is unreachable so calls to this
	 * method should be placed in a separate thread from the main program.
	 * 
	 * @return the data received as a byte array
	 * @throws IOException
	 */
	public byte[] receive() throws IOException {

		DatagramPacket incoming = new DatagramPacket(receiveBuffer,
				receiveBuffer.length);
		ds.receive(incoming);
		byte[] result = new byte[incoming.getLength()];
		System.arraycopy(incoming.getData(), 0, result, 0, incoming.getLength());
		return result;

	}

	/**
	 * @return the port which this object sends data to
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @return the port which this client is bound to
	 */
	public int getLocalPort() {
		return this.ds.getLocalPort();
	}

	/**
	 * @return the InetAddress which this client sends data to
	 */
	public InetAddress getAddress() {
		return this.remote;
	}

	/**
	 * @return a String showing the remote host and port which this client sends
	 *         data to
	 */
	public String toString() {
		return "[UDPClient:address=" + remote + ";port=" + port + "]";
	}



}
