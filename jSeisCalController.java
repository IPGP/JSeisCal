
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
		
		view.addTableAddressTfListener(new TableAddressListener());
		view.addTableDisplacementTfListener(new TableDisplacementListener());
		view.addTableFreePeriodTfListener(new TableFreePeriodListener());
		view.addTableDampingTfListener(new TableDampingListener());
		view.addTableBitsTfListener(new TableBitsListener());
		view.addTableVoltsTfListener(new TableVoltsListener());
		view.addTableChannelLstListener(new TableChannelListener());
		view.addTableConnectListener(new TableConnectListener());
		view.addTableModeListener(new TableModeListener());
		view.addTablePushListener(new TablePushListener());

		view.addMetrozetAddressTfListener(new MetrozetAddressListener());
		view.addMetrozetCommandTfListener(new MetrozetCommandListener());
		view.addMetrozetListener(new MetrozetListener());
		
		view.addElectricalCalibListener(new ElectricalCalibListener());
		
		view.addRegisterListener(new RegisterListener());
		view.addDeRegisterListener(new DeRegisterListener());
		view.addStartListener(new StartListener());
		
		view.addMenuItemSTS1Listener(new MenuItemSTS1Listener());
		view.addMenuItemSTS2Listener(new MenuItemSTS2Listener());
		view.addMenuItemSaveListener(new MenuItemSaveListener());
		view.addMenuItemOpenListener(new MenuItemOpenListener());
		view.addMenuItemAboutListener(new MenuItemAboutListener());
		view.addMenuItemSetupListener(new MenuItemSetupListener());
		view.addMenuItemAbsoluteCalibListener(new MenuItemAbsoluteCalibListener());
		view.addMenuItemElectricalCalibListener(new MenuItemElectricalCalibListener());
		view.addMenuCalibListener(new MenuCalibListener());
		view.addMenuQuitListener(new MenuQuitListener());
		
		view.over.addChannelOverviewListener(new ChannelOverviewListener());

		
		// Add listener to the model


	}


	class Q330AddressListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330Address(m_view.getQ330Address());
			//System.out.println("Q330 address set to : " + m_model.getQ330Address());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330Address(m_view.getQ330Address());
			//System.out.println(m_model.getQ330Address());
        }

	}
	
	class Q330SerialListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330Serial(m_view.getQ330Serial());
			//System.out.println(m_model.getQ330Serial());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330Serial(m_view.getQ330Serial());
			//System.out.println(m_model.getQ330Serial());
        }

	}
	
	class Q330DataPortListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330DataPort(m_view.getQ330DataPort());
			//System.out.println(m_model.getQ330DataPort());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330DataPort(m_view.getQ330DataPort());
			//System.out.println(m_model.getQ330DataPort());
        }

	}
	
	class Q330AuthCodeListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setQ330AuthCode(m_view.getQ330AuthCode());
			//System.out.println(m_model.getQ330AuthCode());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setQ330AuthCode(m_view.getQ330AuthCode());
			//System.out.println(m_model.getQ330AuthCode());
        }

	}
	
	class TableAddressListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableAddress(m_view.getTableAddress());
			//System.out.println(m_model.getTableAddress());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableAddress(m_view.getTableAddress());
			//System.out.println(m_model.getTableAddress());
        }

	}
	
	class TableDisplacementListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableDisplacement(m_view.getTableDisplacement());
			//System.out.println(m_model.getTableDisplacement());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableDisplacement(m_view.getTableDisplacement());
			//System.out.println(m_model.getTableDisplacement());
        }

	}
	
	class TableFreePeriodListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableFreePeriod(m_view.getTableFreePeriod());
			//System.out.println(m_model.getTableFreePeriod());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableFreePeriod(m_view.getTableFreePeriod());
			//System.out.println(m_model.getTableFreePeriod());
        }

	}
	
	class TableDampingListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableDamping(m_view.getTableDamping());
			//System.out.println(m_model.getTableDamping());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableDamping(m_view.getTableDamping());
			//System.out.println(m_model.getTableDamping());
        }

	}
	class TableBitsListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableBits(m_view.getTableBits());
			//System.out.println(m_model.getTableBits());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableBits(m_view.getTableBits());
			//System.out.println(m_model.getTableBits());
        }

	}
	
	class TableVoltsListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableVolts(m_view.getTableVolts());
			//System.out.println(m_model.getTableVolts());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableVolts(m_view.getTableVolts());
			//System.out.println(m_model.getTableVolts());
        }

	}
	
	class TableChannelListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setTableChannel(m_view.getTableChannel());
			//System.out.println(m_model.getTableChannel());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setTableChannel(m_view.getTableChannel());
			//System.out.println(m_model.getTableChannel());
        }

	}
	
	class MetrozetAddressListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			m_model.setMetrozetAddress(m_view.getMetrozetAddress());
			//System.out.println(m_model.getMetrozetAddress());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			m_model.setMetrozetAddress(m_view.getMetrozetAddress());
			//System.out.println(m_model.getMetrozetAddress());
        }

	}
	
	class MetrozetCommandListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			//System.out.println(m_view.getMetrozetCommand());
			m_model.metrozetClient.send(m_view.getMetrozetCommand());
		}
	    public void focusGained(FocusEvent e) {
	    }

        public void focusLost(FocusEvent e) {
			//m_model.setMetrozetAddress(m_view.getMetrozetAddress());
			//System.out.println(m_model.getMetrozetAddress());
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
			//System.out.println(cmd);
			if (m_model.metrozetClient != null) {
				System.out.println("STS1");
				if (cmd.equals("Start") ) {
					m_model.metrozetClient.send(m_view.m_metrozetSigLst.getSelectedItem().toString());
					OutConsole(m_view.m_metrozetSigLst.getSelectedItem().toString());
					m_model.setECalibTimeStampStart();
					m_view.bhz.addMarker("Start");
					m_view.bhn.addMarker("Start");
					m_view.bhe.addMarker("Start");
					m_view.ToggleECalibStartStop();
				} else if (cmd.equals("Stop") ) {
					m_model.metrozetClient.send("STOP");
					m_view.ToggleECalibStartStop();
					m_view.bhz.addMarker("Stop");
					m_view.bhn.addMarker("Stop");
					m_view.bhe.addMarker("Stop");
					m_model.setECalibTimeStampStop();
					m_model.setECalibDataset(m_view.getChType(), false);
				} else {
					m_model.metrozetClient.send(cmd);
				}
			} else {
				System.out.println("STS2");
				if (cmd.equals("Start") ) {
					m_model.setECalibTimeStampStart();
					m_view.bhz.addMarker("Start");
					m_view.bhn.addMarker("Start");
					m_view.bhe.addMarker("Start");
					m_view.ToggleECalibStartStop();
				} else if (cmd.equals("Stop") ) {
					m_view.ToggleECalibStartStop();
					m_view.bhz.addMarker("Stop");
					m_view.bhn.addMarker("Stop");
					m_view.bhe.addMarker("Stop");
					m_model.setECalibTimeStampStop();
					m_model.setECalibDataset(m_view.getChType(), true);
				}
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
			
			/*if (result[4] == (byte)0xa2) {
				System.out.println("Q330 Error");
			}*/
			
			// Send Server Request
			byte[] rqsrv = m_model.q330.SetServerRequestPacket(m_model.getQ330Serial());
			client.send(rqsrv);
			result = client.receive();

			// Send Server Response
			byte[] srvrsp = m_model.q330.SetServerResponsePacket(rqsrv, result,
					m_model.getQ330AuthCode());
			client.send(srvrsp);
			result = client.receive();
			//System.out.println("Reg : " + result[4]);
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
			//clientdata.send(dtopen);
			
			m_model.q330UdpServer = new UdpServer();                             // Create the server
			m_model.q330UdpServer.setPort( 5501 );                                         // Set the port
			m_model.q330UdpServer.addUdpServerListener( new UdpServer.Listener() {         // Add listener
	            //@Override
	            public void packetReceived( UdpServer.Event evt ) {     // Packet received
	            	m_model.q330.ParsePacket(evt.getPacketAsBytes());
	            	//System.out.println(Integer.toString(evt.getPacketAsBytes()[8]) + Integer.toString(evt.getPacketAsBytes()[9]));

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
    	//System.out.println(m_view.getQ330AuthCode());
    	
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
					m_model.tableControlVertical = new TableBangs(m_model.tableMB, 10, 10);
					
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
					double threshold = Math.pow(2,Integer.valueOf(m_model.getTableBits())) / 4.;  //Half posivite range
					m_model.tableControlHorizontal = new TableBangsValued(m_model.tableMB, 10, 10, calibChannel, threshold);
				}
					
				
			} else if (e.getActionCommand() == "Stop") {
				m_view.getChannel().addMarker("Stop");
				m_model.setDataStopIndex();
				m_model.setCalcDataset();
				//m_model.tableControl.stop();
				String s;
				if ( m_model.dataset.axis.equals("HHZ") || m_model.dataset.axis.equals("BHZ") ){
					m_model.tableControlVertical.stop();
					CalcResult resultFrame = new CalcResult(m_model.dataset, false);
					result = resultFrame.CalcVertical();
					s = String.format( "%.3f V", result );
					System.out.println("Vertical Sensitivity = " + s);
					OutConsole("Vertical Sensitivity = " + s);
					//resultFrame.setVisible(true);
				}
				else {
					m_model.tableControlHorizontal.stop();
					CalcResult resultFrame = new CalcResult(m_model.dataset, false);
					result = resultFrame.CalcHorizontal();
					s = String.format( "%.3f V", result );
					System.out.println("Horizontal Sensitivity = " + s);
					OutConsole("Horizontal Sensitivity = " + s);
					//resultFrame.setVisible(true);
				}
				//m_view.ShowResult(s);
			}
		}
	}

	
	class TableConnectListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Try to connect to : " + m_model.getTableAddress());
			if (m_model.tableMB.connect(m_model.getTableAddress())) {
				OutConsole("Connected to table.");
			}
		}
	}
	
	class TableModeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			m_model.tableMB.ForceCoil(1, m_model.tableMode );
			if (m_model.tableMode) {
				m_model.tableMode = false;
				m_view.m_tableModeBtn.setText("Mode : V");
				System.out.println("Mode Vertical");
				OutConsole("Mode Vertical");
			} else {
				m_model.tableMode = true;
				m_view.m_tableModeBtn.setText("Mode : H");
				System.out.println("Mode Horizontal");
				OutConsole("Mode Horizontal");
			}
				
			
		}
	}
	
	class TablePushListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Push");
			m_model.tableMB.push();
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
	
	
	
	class MenuItemSTS1Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("STS1 View");
			m_view.STS1View();
		}
	}
	class MenuItemSTS2Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("STS2 View");	
			m_view.STS2View();
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
			//m_model.LoadProperties();
		}

	}
	class MenuItemAboutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("GEOSCOPE - N.LEROY");
			m_view.ShowAbout();

		}

	}
	
	class MenuItemSetupListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Setup");
			String s; 
			s = m_view.ShowSetup1();
			if (s == null) {return;}
			m_model.setQ330Address(s);
			m_view.setQ330Address(s);
			
			s = m_view.ShowSetup2();
			if (s == null) {return;}
			m_model.setQ330Serial(s);
			m_view.setQ330Serial(s);
			
			s = m_view.ShowSetup3();
			if (s == null) {return;}
			m_model.setQ330AuthCode(s);
			m_view.setQ330AuthCode(s);
			
			s = m_view.ShowSetup4();
			if (s == null) {return;}
			m_model.setQ330DataPort(s);
			m_view.setQ330DataPort(s);


		}
	}
	class MenuItemAbsoluteCalibListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String s; 
			System.out.println("Absolute Calibration");
			s = m_view.ShowCalib1();
			if (s == null) {return;}
			if (s == "Q330HR") {
				m_model.setTableBits("26");
				m_view.setTableBits("26");
				m_model.dataset.bits = 26.;
				m_model.setTableVolts("40");
				m_view.setTableVolts("40");
				m_model.dataset.volts = 40.;				
				m_model.dataset.uVperCount = 40. / Math.pow(2, 26) * 1000000.;
				
				System.out.println("Set uV/c to " + m_model.dataset.uVperCount);				
				
			}
			else if (s == "Q330SR") {
				m_model.setTableBits("24");
				m_view.setTableBits("24");
				m_model.dataset.bits = 26.;
				m_model.setTableVolts("40");
				m_view.setTableVolts("40");
				m_model.dataset.volts = 40.;
				m_model.dataset.uVperCount = 40. / Math.pow(2, 24) * 1000000.;
				System.out.println("Set uV/c to " + m_model.dataset.uVperCount);
			}
			else {
				System.out.println("Not supported ...");
				return;
			}
			
			s = m_view.ShowCalib2();
			if (s == null) {return;}
			if (s == "STS1") {
				m_model.dataset.freeperiod = 360.;
				m_model.setTableFreePeriod("360");
				m_view.setTableFreePeriod("360");
				m_model.dataset.damping = 0.707;
				m_model.setTableDamping("0.707");
				m_view.setTableDamping("0.707");
				System.out.println("Set Free Period to 360 s and damping to 0.707");
			}
			else if (s == "STS2") {
				m_model.dataset.freeperiod = 120.;
				m_model.setTableFreePeriod("120");
				m_view.setTableFreePeriod("120");
				m_model.dataset.damping = 0.707;
				m_model.setTableDamping("0.707");
				m_view.setTableDamping("0.707");
				System.out.println("Set Free Period to 120 s and damping to 0.707");
			}
			else {
				System.out.println("Not supported ...");
				return;
			}
			
			s = m_view.ShowCalib3();
			if (s == null) {return;}
			m_model.dataset.axis = s;
			if ( (m_model.dataset.axis == "HHZ") || (m_model.dataset.axis == "HHN") || (m_model.dataset.axis == "HHE") ){
				m_model.dataset.samplerate = 200; // temporaire
	    	}else if ( (m_model.dataset.axis == "BHZ") || (m_model.dataset.axis == "BHN") || (m_model.dataset.axis == "BHE") ){
				m_model.dataset.samplerate = 20; // temporaire
	    	}		
		}
	}
	class MenuItemElectricalCalibListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Electrical Calibration");
			

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
