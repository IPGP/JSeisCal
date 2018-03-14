
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;

import jlib330.DataChannel;


import calc.*;

public class jSeisCalController {
	// ... The Controller needs to interact with both the Model and View.
	private jSeisCalModel m_model;
	private jSeisCalView m_view;

	// ========================================================== constructor
	/** Constructor */
	jSeisCalController(jSeisCalModel model, jSeisCalView view) {
		m_model = model;
		m_view = view;

		// ... Add listeners to the view.
		view.addQ330AddressTfListener(new Q330AddressListener());
		view.addQ330SerialTfListener(new Q330SerialListener());
		view.addQ330DataPortTfListener(new Q330DataPortListener());
		view.addQ330DataPortLstListener(new Q330DataPortListener());
		view.addQ330AuthCodeTfListener(new Q330AuthCodeListener());
		
		view.addTableDisplacementTfListener(new TableDisplacementListener());
		view.addTableFreePeriodTfListener(new TableFreePeriodListener());
		view.addTableDampingTfListener(new TableDampingListener());
		view.addTableBitsTfListener(new TableBitsListener());
		view.addTableVoltsTfListener(new TableVoltsListener());
		view.addTableChannelLstListener(new TableChannelListener());

		view.addMetrozetAddressTfListener(new MetrozetAddressListener());
		view.addMetrozetCommandTfListener(new MetrozetCommandListener());
		view.addMetrozetListener(new MetrozetListener());

		view.addElectricalCalibListener(new ElectricalCalibListener());
		
		view.addRegisterListener(new RegisterListener());
		view.addDeRegisterListener(new DeRegisterListener());
		view.addStartListener(new StartListener());
		
		view.addMenuItemSaveListener(new MenuItemSaveListener());
		view.addMenuItemOpenListener(new MenuItemOpenListener());
		view.addMenuItemAboutListener(new MenuItemAboutListener());
		view.addMenuCalibListener(new MenuCalibListener());
		view.addMenuQuitListener(new MenuQuitListener());
		
		view.over.addChannelOverviewListener(new ChannelOverviewListener());

	}


	class Q330AddressListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330Address(m_view.getQ330Address());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330Address(m_view.getQ330Address());
        }

	}
	
	class Q330SerialListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330Serial(m_view.getQ330Serial());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330Serial(m_view.getQ330Serial());
        }

	}
	
	class Q330DataPortListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330DataPort(m_view.getQ330DataPort());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330DataPort(m_view.getQ330DataPort());
        }

	}
	
	class Q330AuthCodeListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330AuthCode(m_view.getQ330AuthCode());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330AuthCode(m_view.getQ330AuthCode());
        }

	}
	
	class TableDisplacementListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableDisplacement(m_view.getTableDisplacement());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableDisplacement(m_view.getTableDisplacement());
        }

	}
	
	class TableFreePeriodListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableFreePeriod(m_view.getTableFreePeriod());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableFreePeriod(m_view.getTableFreePeriod());
        }

	}
	
	class TableDampingListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableDamping(m_view.getTableDamping());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableDamping(m_view.getTableDamping());
        }

	}
	class TableBitsListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableBits(m_view.getTableBits());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableBits(m_view.getTableBits());
        }

	}
	
	class TableVoltsListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableVolts(m_view.getTableVolts());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableVolts(m_view.getTableVolts());
        }

	}
	
	class TableChannelListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableChannel(m_view.getTableChannel());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableChannel(m_view.getTableChannel());
        }

	}
	
	class MetrozetAddressListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setMetrozetAddress(m_view.getMetrozetAddress());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setMetrozetAddress(m_view.getMetrozetAddress());
        }

	}
	
	class MetrozetCommandListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.metrozetClient.send(m_view.getMetrozetCommand());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			//m_model.setMetrozetAddress(m_view.getMetrozetAddress());
        }

	}
	

	
	class MetrozetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			//System.out.println(cmd);
			if (cmd.equals("ENABLE")) {
				System.out.println("Enable Metrozet");
				if (m_model.metrozetClient == null) {
					m_model.metrozetClient = new MetrozetClient(m_model.getMetrozetAddress(), m_view.console);
				} else {
					m_model.metrozetClient.send("ENABLEABCDEF");
				}
			} else if ( (m_model.metrozetClient != null) && m_model.metrozetClient.isOpen() ) {
				m_model.metrozetClient.send(cmd);
			}
		}	    
	}

    class ElectricalCalibListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (m_model.metrozetClient != null) {
                                  m_model.metrozetClient.send(cmd);
                } 
        }           
}
	
	
	public void OutConsole(String s) {
        m_view.console.write(s);
	}
	
	public void RegisterQ330() {
		System.out.println("Start Registration with ");
		System.out.println("Serial   : " + m_model.getQ330Serial());
		System.out.println("Address  : " + m_model.getQ330Address());
		System.out.println("DataPort : " + m_model.getQ330DataPort());
		System.out.println("AuthCode : " + m_model.getQ330AuthCode());


		byte[] result = new byte[1500];
		
		try {
			// Get sockets
			UDPClient client = m_model.getQ330ControlSocket();
			if (client == null){
				client = m_model.newQ330ControlSocket();
			}

			// Send Deregistration
			byte[] dereg = m_model.q330.SetDeregPacket(m_model.getQ330Serial());
			
			client.send(dereg);
			result = client.receive();
			
			// Send Server Request
			byte[] rqsrv = m_model.q330.SetServerRequestPacket(m_model.getQ330Serial());
			client.send(rqsrv);
			result = client.receive();

			// Send Server Response
			byte[] srvrsp = m_model.q330.SetServerResponsePacket(rqsrv, result,
					m_model.getQ330AuthCode());
			client.send(srvrsp);
			result = client.receive();
			if (result[4] == (byte) 0xa0) {
				System.out.println("Registration OK");
			}
			
			// Send Request Log
			byte[] rqlog = m_model.q330.SetRequestLogPacket(Integer.valueOf(m_model.getQ330DataPort()));
			client.send(rqlog);
			result = client.receive();

			// Send Flush DP
			byte[] flushDP = m_model.q330.SetFlushDpPacket(result);
			client.send(flushDP);
			result = client.receive();		
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Send Data Open packet
			byte[] dtopen = m_model.q330.SetDataOpenPacket();
			InetAddress addr = InetAddress.getByName(m_model.getQ330Address());
			int port = Integer.valueOf(m_model.getQ330DataPort())*2+5331;
			DatagramPacket dp = new DatagramPacket(dtopen, dtopen.length, addr , port);
			
			m_model.q330UdpServer = new UdpServer();                             // Create the server
			m_model.q330UdpServer.setPort( 5501 );                                         // Set the port
			m_model.q330UdpServer.addUdpServerListener( new UdpServer.Listener() {         // Add listener
	            //@Override
	            public void packetReceived( UdpServer.Event evt ) {     // Packet received
	            	m_model.q330.ParsePacket(evt.getPacketAsBytes());
	            	
					// Acknowledge data
					byte[] dtack = new byte[36];
					dtack = m_model.q330.SetDataAckPacket(evt.getPacketAsBytes()[8], evt.getPacketAsBytes()[9]);
		                try {
		                	int port = Integer.valueOf(m_model.getQ330DataPort())*2+5331;
							DatagramPacket dp2 = new DatagramPacket(dtack, dtack.length, InetAddress.getByName(m_model.getQ330Address()), port);
							evt.send(dp2);
	               
		                } catch( java.io.IOException ex ) {
	                    ex.printStackTrace(); // Please don't use printStackTrace in production code
	                }   // end ctach
	            }   // end packetReceived
	        }); // end Listener
	        //
			m_model.q330UdpServer.start();
	        OutConsole("Registered");
	        System.out.print("Starting UDP Server");
	        while (!(m_model.q330UdpServer.getState() == UdpServer.State.STARTED)) { }
	        System.out.print("UDP Server Started");
	        
	        m_model.q330UdpServer.send(dp);

		} catch (NumberFormatException nfex) {
			m_view.showError("Bad input !");
		} catch (UnknownHostException d) {
			d.printStackTrace();
		} catch (SocketTimeoutException t) {
			System.out.println("Q330 packet time out - Check network configuration");
		} catch (SocketException s) {
			s.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (NoSuchAlgorithmException a) {
			a.printStackTrace();
		}

	}
	
	public void UnRegisterQ330() {
		System.out.println("Start DeRegistration with ");
    	System.out.println(m_view.getQ330Serial());
    	System.out.println(m_view.getQ330Address());
    	System.out.println(m_view.getQ330DataPort());
    	
    	m_model.setQ330Serial(m_view.getQ330Serial());
    	m_model.setQ330Address(m_view.getQ330Address());
    	m_model.setQ330DataPort(m_view.getQ330DataPort());
    	m_model.setQ330AuthCode(m_view.getQ330AuthCode());
    	

		byte[] result = new byte[1500];

		try {
			// Get sockets
			UDPClient deregclient = m_model.getQ330ControlSocket();
			if (deregclient == null){
				deregclient = m_model.newQ330ControlSocket();
			}

			// Send Deregistration
			byte[] dereg = m_model.q330.SetDeregPacket(m_model.getQ330Serial());
			deregclient.send(dereg);
			result = deregclient.receive();

			if (result[4] == (byte) 0xa0) {
				System.out.println("DeRegistration OK");
			}
			
			m_model.q330controlsocket.close();
			m_model.q330controlsocket = null;

			if (m_model.q330UdpServer != null ) { 
				m_model.q330UdpServer.stop();
			}
		
		} catch (UnknownHostException d) {
			d.printStackTrace();
		} catch (SocketException s) {
			s.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}		
	}
	
	class RegisterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			RegisterQ330();
		}
	}
	class DeRegisterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			UnRegisterQ330();
		}	
	}
	
	class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			double result = 0;
			DataChannel calibChannel = null;
			System.out.println(e.getActionCommand());			
			m_view.ToggleStartStop();
			if (e.getActionCommand() == "Start") {
				m_model.setDataStartIndex();				
				m_view.getChannel().addMarker("Start");	
				String str = (String) m_view.m_tableChannelLst.getSelectedItem();
				System.out.println(str);	
				if ((str.equals("HHZ") || str.equals("BHZ"))) {
					// Vertical calibration					
					//m_model.tableControlVertical = new TableBangs(m_model.tableMB, 10, 10);
					
				} else {
					// Horizontal calibration
					if (str.equals("HHN")){
						calibChannel = m_model.buffers.hhn;
					} else if (str.equals("HHE")){
						calibChannel = m_model.buffers.hhe;
					} else if (str.equals("BHN")){
						//calibChannel = m_model.buffers.bhn;
						// Trigger always on HH due to latency on BH
						calibChannel = m_model.buffers.hhn;
					} else if (str.equals("BHE")){
						//calibChannel = m_model.buffers.bhe;
						// Trigger always on HH due to latency on BH
						calibChannel = m_model.buffers.hhe;
					}
				}
				
			} else if (e.getActionCommand() == "Stop") {
				m_view.getChannel().addMarker("Stop");
				m_model.setDataStopIndex();
				m_model.setCalcDataset();
				String s;
				if ( m_model.dataset.axis.equals("HHZ") || m_model.dataset.axis.equals("BHZ") ){
					CalcResult resultFrame = new CalcResult(m_model.dataset, false);
					result = resultFrame.CalcVertical();
					s = String.format( "%.3f V", result );
					System.out.println("Vertical Sensitivity = " + s);
					OutConsole("Vertical Sensitivity = " + s);
				}
				else {
					CalcResult resultFrame = new CalcResult(m_model.dataset, false);
					result = resultFrame.CalcHorizontal();
					s = String.format( "%.3f V", result );
					System.out.println("Horizontal Sensitivity = " + s);
					OutConsole("Horizontal Sensitivity = " + s);
				}
			}
		}
	}

	
	class ChannelOverviewListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "Clear All Buffers") {
				System.out.println("Clear All Buffers");
				m_model.buffers.clear();
				m_view.clearChannels();	
			} else if (e.getActionCommand() == "hhzCb") {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				m_model.buffers.hhz.channelOn = abstractButton.isSelected();								        
			} else if (e.getActionCommand() == "hhnCb") {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				m_model.buffers.hhn.channelOn = abstractButton.isSelected();
			} else if (e.getActionCommand() == "hheCb") {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				m_model.buffers.hhe.channelOn = abstractButton.isSelected();
			} else if (e.getActionCommand() == "bmzCb") {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				m_model.buffers.bmz.channelOn = abstractButton.isSelected();								        
			} else if (e.getActionCommand() == "bmnCb") {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				m_model.buffers.bmn.channelOn = abstractButton.isSelected();
			} else if (e.getActionCommand() == "bmeCb") {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				m_model.buffers.bme.channelOn = abstractButton.isSelected();
			} else {
				System.out.println(e.getActionCommand());				
			}
			
		}
	}
	
	class MenuItemSaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Save properties");
			m_model.WriteProperties();			
		}
	}
	
	class MenuItemOpenListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Reset");
			m_view.setVisible(false);
			m_model = null;
			jSeisCal.reset();
		}
	}
	
	class MenuItemAboutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("GEOSCOPE - N.LEROY");
			m_view.ShowAbout();
		}
	}
	
	class MenuCalibListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Calibration View");
	    	CalcResult result = new CalcResult(m_model.dataset, false);
	    	result.setVisible(true);
		}
	}
	
	class MenuQuitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Exit with Q330 Deregistration");
			UnRegisterQ330();
			System.exit(0);
		}
	}
	
	
}
