

import java.awt.*;

import javax.swing.*;
import Console.Console;
import calc.*;
import java.awt.event.*;
import java.io.IOException;

public class jSeisCalView extends JFrame {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String version = "0.3";
	
	private static final String SERIAL = "Serial : ";
	//... Components
    private JLabel     m_q330SerialLb   	 = new JLabel(SERIAL);
    public  JTextField m_q330SerialTf   	 = new JTextField(15);
    
    private JLabel     m_q330AddressLb  	 = new JLabel("Address : ");
    public  JTextField m_q330AddressTf  	 = new JTextField(15);
    
    private JLabel     m_q330DataPortLb 	 = new JLabel("Dataport : ");
    String[] dpList = { "1", "2", "3", "4" };
    public  JComboBox  m_q330DataPortLst     = new JComboBox(dpList);
        
    private JLabel     m_q330AuthCodeLb 	 = new JLabel("Code : ");
    public  JTextField m_q330AuthCodeTf 	 = new JTextField(15);
    
    private JTextField m_dialogTf       	 = new JTextField(15);
    private JButton    m_registerBtn    	 = new JButton("Register");
    private JButton    m_deregisterBtn       = new JButton("DeReg");
    
    private JLabel     m_tableAddressLb      = new JLabel("Address : ");
    public  JTextField m_tableAddressTf      = new JTextField(15);
    
    private JLabel     m_tableDisplacementLb = new JLabel("Disp : ");
    public  JTextField m_tableDisplacementTf = new JTextField(15);
    
    private JLabel     m_tableFreePeriodLb   = new JLabel("Free p : ");
    public  JTextField m_tableFreePeriodTf   = new JTextField(15);
    private JLabel     m_tableDampingLb   	 = new JLabel("Damping : ");
    public  JTextField m_tableDampingTf   	 = new JTextField(15);
    
    private JLabel     m_tableBitsLb   		 = new JLabel("Bits : ");
    public  JTextField m_tableBitsTf   	 	 = new JTextField(15);
    
    private JLabel     m_tableVoltsLb   	 = new JLabel("Volts : ");
    public  JTextField m_tableVoltsTf   	 = new JTextField(15);
    
    private JLabel     m_tableChannelLb   	 = new JLabel("Channel : ");
    String[] chList = { "HHZ", "HHN", "HHE", "BHZ", "BHN", "BHE"};
    public  JComboBox  m_tableChannelLst     = new JComboBox(chList);
    
    private JLabel     m_tableTrigHH	   	 = new JLabel("(Trig always on HH)");
    
    private JButton    m_tableConnnectBtn    = new JButton("Connect");
    public JButton     m_tableModeBtn    	 = new JButton("Mode : V");
    private JButton    m_tablePushBtn    	 = new JButton("Push");
    
    private JToggleButton    m_startBtn      = new JToggleButton("Start");
    private JButton    m_stopBtn             = new JButton("Stop");
    
    private JButton    m_metrozetSafeBtn     = new JButton("SAFE");
    private JButton    m_metrozetEnableBtn   = new JButton("ENABLE");
    private JButton    m_metrozetStatusBtn   = new JButton("STATUS");
    private JButton    m_metrozetReturnBtn   = new JButton("RETURN");
    private JButton    m_metrozetSensorBtn   = new JButton("SENSOR");
    private JButton    m_metrozetMotorBtn    = new JButton("MOTOR");
    private JButton    m_metrozetDiagBtn    = new JButton("DIAGNOSTIC");
    
    private JLabel     m_metrozetAddressLb   = new JLabel("Address : ");
    public  JTextField m_metrozetAddressTf   = new JTextField(15);
    
    private JLabel     m_metrozetCommandLb   = new JLabel("Command : ");
    public  JTextField m_metrozetCommandTf   = new JTextField(15);
    
    private MetrozetTabPane	   m_metrozetZ	 = new MetrozetTabPane("Z");
    private MetrozetTabPane	   m_metrozetN	 = new MetrozetTabPane("N");
    private MetrozetTabPane	   m_metrozetE	 = new MetrozetTabPane("E");
    
    private JPanel	   m_metrozetDiag	 = new JPanel();
    private JButton    m_metrozetDiagAnalPwrPlusAdc    = new JButton("ANALOGPWR+ADC");
    private JButton    m_metrozetDiagAnalPwrMinusAdc    = new JButton("ANALOGPWR-ADC");
    private JButton    m_metrozetDiagInputPwrPlusAdc    = new JButton("INPUTPWR+ADC");
    private JButton    m_metrozetDiagInputPwrMinusAdc    = new JButton("INPUTPWR-ADC");
    
    
    String[] eCalibSourceList = { "Metrozet Internal", "Q330" };
    public  JComboBox  m_eCalibSourceLst         = new JComboBox(eCalibSourceList);
    private JButton    m_metrozetReturnCalibBtn  = new JButton("RETURN");
    private JButton    m_metrozetCalibrate       = new JButton("CALIBRATE");
    private JButton    m_metrozetIntCalVelocity  = new JButton("INTCALVELOCITY");
    private JButton    m_metrozetIntCalDisconnect= new JButton("INTCALDISCONNECT");
    private JButton    m_metrozetNZSig           = new JButton("NZSIGNALOUT");
    private JButton    m_metrozetEZSig           = new JButton("EZSIGNALOUT");
    String[] sigList = { "SWEEP", "STEP", "0.01HZSINE", "0.10HZSINE", "1.00HZSINE" };
    public  JComboBox  m_metrozetSigLst          = new JComboBox(sigList);
    String[] sisType = { "STS1", "STS2"};
    public  JComboBox  m_sisLst                  = new JComboBox(sisType);
    private JLabel     m_sisTypeLb  	 		 = new JLabel("Seismometer : ");
    String[] chType = { "HH", "BH"};
    public  JComboBox  m_chLst                   = new JComboBox(chType);
    private JLabel     m_chTypeLb  	 			 = new JLabel("Channel Type : ");
    private JToggleButton    m_calibStartBtn     = new JToggleButton("Start");
    
    
    private JPanel     leftPanel        	 = new JPanel();
    private JPanel     q330Panel        	 = new JPanel();
    private JPanel     tablePanel       	 = new JPanel();
    private JPanel     metrozetPanel    	 = new JPanel();
    private JPanel     eCalibPanel       	 = new JPanel();
    private JTabbedPane metrozetTabbedPane 	 = new JTabbedPane();
    
    public  JTextField metrozetCommandTf  	 = new JTextField(15);
    public  Console console  				 = null;
    
    public JTabbedPane tabbedPane 			 = new JTabbedPane();
    private JTabbedPane tabbedPaneConfig	 = new JTabbedPane();

    private JMenuBar bar 					 = new JMenuBar();
    private JMenu menuViews 				 = new JMenu("Views");
    private JMenuItem menuItemSTS1 			 = new JMenuItem("STS1");
    private JMenuItem menuItemSTS2 			 = new JMenuItem("STS2");
    private JMenu menuWizards 			 	 = new JMenu("Wizards");
    private JMenuItem menuItemSetup 		 = new JMenuItem("Setup Q330");
    private JMenuItem menuItemAbsoluteCalib  = new JMenuItem("Absolute Calibration");
    private JMenuItem menuItemElectricalCalib = new JMenuItem("Electrical Calibration");
    private JMenu menuOptions 				 = new JMenu("Options");
	private JMenuItem menuItemAbout			 = new JMenuItem("About");
    private JMenuItem menuItemSave 			 = new JMenuItem("Save");
    private JMenuItem menuItemOpen 			 = new JMenuItem("Reset");
    private JMenuItem menuCalib 			 = new JMenuItem("Calibration View");
    private JMenuItem menuQuit 				 = new JMenuItem("Quit");
    
    private GridBagConstraints gbcLeft 		 = new GridBagConstraints();

    
    public DisplayChannel bhz 		= null;
    public DisplayChannel bhn 		= null;
    public DisplayChannel bhe 		= null;
    public DisplayChannel hhz 		= null;
    public DisplayChannel hhn 		= null;
    public DisplayChannel hhe 		= null;
    public DisplayChannelPOS bmz 	= null;
    public DisplayChannelPOS bmn 	= null;
    public DisplayChannelPOS bme 	= null;
    
    public ChannelsOverview over 	= null;
    
    private jSeisCalModel  m_model;
    
    //======================================================= constructor
    /** Constructor */
    jSeisCalView(jSeisCalModel model) {
        //... Set up the logic
        m_model = model;
        
        // dummy
        //m_dialogTf.setEditable(false);
        

        /*
         * Build control panels
         */
        
        BuildQ330Panel();
        BuildTablePanel();
        BuildMetrozetPanel();
        BuildeCalibPanel();

        try {
			console = new Console();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		/*
		 * Assemble tabbed control panels
		 */
		
		tabbedPaneConfig.add("Q330", q330Panel);
		tabbedPaneConfig.add("Table", tablePanel);
		tabbedPaneConfig.add("Metr.", metrozetPanel );
		tabbedPaneConfig.add("Elec.", eCalibPanel);
		
		/*
		 * Set Left Panel
		 */
		
		leftPanel.setLayout(new GridBagLayout());
        gbcLeft.gridx = gbcLeft.gridy = 0;
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
		leftPanel.add(tabbedPaneConfig, gbcLeft);
        gbcLeft.gridy = 1;
        leftPanel.add(console, gbcLeft);

        
        /*
         * Set data displays => tabbed
         */
        
        bhz = new DisplayChannel(m_model.buffers.bhz,21600000); // 4h
        m_model.buffers.bhz.addObserver(bhz);
        bhn = new DisplayChannel(m_model.buffers.bhn,21600000); // 4h
        m_model.buffers.bhn.addObserver(bhn);
        bhe = new DisplayChannel(m_model.buffers.bhe,21600000); // 4h
        m_model.buffers.bhe.addObserver(bhe);
        bhz.setOpaque(true);
        bhn.setOpaque(true);
        bhe.setOpaque(true);
        tabbedPane.add("BHZ",bhz);
        tabbedPane.add("BHN",bhn);
        tabbedPane.add("BHE",bhe);
        
        /*hhz = new DisplayChannel(m_model.buffers.hhz,1200000);
        m_model.buffers.hhz.addObserver(hhz);
        hhn = new DisplayChannel(m_model.buffers.hhn,1200000);
        m_model.buffers.hhn.addObserver(hhn);
        hhe = new DisplayChannel(m_model.buffers.hhe,1200000);
        m_model.buffers.hhe.addObserver(hhe);
        hhz.setOpaque(true);
        hhn.setOpaque(true);
        hhe.setOpaque(true);
        tabbedPane.add("HHZ",hhz);
        tabbedPane.add("HHN",hhn);
        tabbedPane.add("HHE",hhe);*/
        
        bmz = new DisplayChannelPOS(m_model.buffers.bmz,200000);
        m_model.buffers.bmz.addObserver(bmz);
        bmn = new DisplayChannelPOS(m_model.buffers.bmn,200000);
        m_model.buffers.bmn.addObserver(bmn);
        bme = new DisplayChannelPOS(m_model.buffers.bme,200000);
        m_model.buffers.bme.addObserver(bme);
        bmz.setOpaque(true);
        bmn.setOpaque(true);
        bme.setOpaque(true);
        tabbedPane.add("BMZ",bmz);
        tabbedPane.add("BMN",bmn);
        tabbedPane.add("BME",bme);
        
        over = new ChannelsOverview(model.buffers);
        m_model.buffers.bhz.addObserver(over);
        tabbedPane.add("Overview",over);        
        tabbedPane.setOpaque(true);
        
        
        /*
         * Arrange main panel
         */
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(leftPanel, gbc);        

        gbc.fill = GridBagConstraints.BOTH;        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.;
        gbc.weighty = 1.;
        add(tabbedPane, gbc);
        
          
        /*
         * Add Menus
         */
        
        menuViews.add(menuItemSTS1);
        menuViews.add(menuItemSTS2);
        
        
        menuWizards.add(menuItemSetup);
        menuWizards.add(menuItemAbsoluteCalib);
        menuWizards.add(menuItemElectricalCalib);
        
       	menuOptions.add(menuItemSave);
		menuOptions.add(menuItemOpen);
		menuOptions.add(menuItemAbout);
		menuOptions.add(menuCalib);

		//bar.add(menuViews);
		//bar.add(menuWizards);
		bar.add(menuOptions);
		//bar.add(menuQuit);
		setJMenuBar(bar);

		/*
		 * Finalize
		 */
        pack();
        setTitle("jSeisCal - v" + version);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

    }
    

    
    void clearChannels() {
    	bhz.clear();
    	bhn.clear();
    	bhe.clear();
    	//hhz.clear();
    	//hhn.clear();
    	//hhe.clear();
    	bmz.clear();
    	bmn.clear();
    	bme.clear();
    }
    
    
    private void BuildTablePanel() {
    	
        tablePanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcTable = new GridBagConstraints();
        gbcTable.gridwidth = 1;
        gbcTable.gridheight = 1;
        gbcTable.weightx = 1.;
        gbcTable.fill = GridBagConstraints.HORIZONTAL;
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 0;
        gbcTable.gridwidth = 1;
        tablePanel.add(m_tableAddressLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 0;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableAddressTf, gbcTable);
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 1;        
        gbcTable.gridwidth = GridBagConstraints.REMAINDER;
        
        JPanel tableCommands = new JPanel(new GridLayout(2,2));
        tableCommands.add(m_tableConnnectBtn);
        tableCommands.add(m_tableModeBtn);
        tableCommands.add(m_tablePushBtn);
        tablePanel.add(tableCommands, gbcTable);
        
        gbcTable.gridwidth = 1;
        gbcTable.gridx = 0;
        gbcTable.gridy = 3;
        tablePanel.add(m_tableDisplacementLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 3;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableDisplacementTf, gbcTable);

        gbcTable.gridx = 0;
        gbcTable.gridy = 4;
        gbcTable.gridwidth = 1;
        tablePanel.add(m_tableFreePeriodLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 4;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableFreePeriodTf, gbcTable);   
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 5;
        gbcTable.gridwidth = 1;
        tablePanel.add(m_tableDampingLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 5;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableDampingTf, gbcTable);
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 6;
        gbcTable.gridwidth = 1;
        tablePanel.add(m_tableBitsLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 6;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableBitsTf, gbcTable);
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 7;
        gbcTable.gridwidth = 1;
        tablePanel.add(m_tableVoltsLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 7;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableVoltsTf, gbcTable);
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 8;
        gbcTable.gridwidth = 1;
        tablePanel.add(m_tableChannelLb, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 8;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableChannelLst, gbcTable);
        gbcTable.gridx = 1;
        gbcTable.gridy = 9;
        gbcTable.gridwidth = 2;
        tablePanel.add(m_tableTrigHH, gbcTable);
        
        gbcTable.gridx = 0;
        gbcTable.gridy = 10;
        gbcTable.gridwidth = 3;
        gbcTable.fill = GridBagConstraints.HORIZONTAL;
        tablePanel.add(m_startBtn, gbcTable);

        //... Initialize components
        m_tableAddressTf.setText(m_model.getTableAddress());
        m_tableDisplacementTf.setText(m_model.getTableDisplacement());
        m_tableFreePeriodTf.setText(m_model.getTableFreePeriod());
        m_tableDampingTf.setText(m_model.getTableDamping());
        m_tableBitsTf.setText(m_model.getTableBits());
        m_tableVoltsTf.setText(m_model.getTableVolts());
        m_tableChannelLst.setSelectedItem(m_model.getTableChannel());
        
        //tablePanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Calibration Table"));
        
        
    }
    
private void BuildMetrozetPanel() {
    	
        metrozetPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcMetrozet = new GridBagConstraints();
        gbcMetrozet.weightx = 1.;
        gbcMetrozet.gridwidth = 1;
        gbcMetrozet.gridheight = 1;
        gbcMetrozet.fill = GridBagConstraints.HORIZONTAL;
        
        gbcMetrozet.gridx = 0;
        gbcMetrozet.gridy = 0;
        metrozetPanel.add(m_metrozetAddressLb, gbcMetrozet);
        
        gbcMetrozet.gridx = 1;
        gbcMetrozet.gridy = 0;
        gbcMetrozet.gridwidth = 2;
        metrozetPanel.add(m_metrozetAddressTf, gbcMetrozet);
        
        gbcMetrozet.gridx = 0;
        gbcMetrozet.gridy = 1;
        gbcMetrozet.gridwidth = 1;
        metrozetPanel.add(m_metrozetCommandLb, gbcMetrozet);
        
        gbcMetrozet.gridx = 1;
        gbcMetrozet.gridy = 1;
        gbcMetrozet.gridwidth = 2;
        metrozetPanel.add(m_metrozetCommandTf, gbcMetrozet);
        
        gbcMetrozet.gridx = 0;
        gbcMetrozet.gridy = 2;
        gbcMetrozet.gridwidth = GridBagConstraints.REMAINDER;
        
        JPanel metrozetCommands = new JPanel(new GridLayout(4,0));

        metrozetCommands.add(m_metrozetEnableBtn);
        metrozetCommands.add(m_metrozetSafeBtn);
        metrozetCommands.add(m_metrozetStatusBtn);
        metrozetCommands.add(m_metrozetReturnBtn);
        metrozetCommands.add(m_metrozetSensorBtn);
        metrozetCommands.add(m_metrozetMotorBtn);
        metrozetCommands.add(m_metrozetDiagBtn);
        
        metrozetPanel.add(metrozetCommands, gbcMetrozet);
        
        // Build Diag panel
        m_metrozetDiag.setLayout(new GridLayout(4,2));
        m_metrozetDiag.add(m_metrozetDiagAnalPwrPlusAdc);
        m_metrozetDiag.add(m_metrozetDiagAnalPwrMinusAdc);        
        m_metrozetDiag.add(m_metrozetDiagInputPwrPlusAdc);
        m_metrozetDiag.add(m_metrozetDiagInputPwrMinusAdc);
        
        //... Initialize components
        m_metrozetAddressTf.setText(m_model.getMetrozetAddress());        
        metrozetTabbedPane.add("Z", m_metrozetZ);
        metrozetTabbedPane.add("N", m_metrozetN);
        metrozetTabbedPane.add("E", m_metrozetE);
        metrozetTabbedPane.add("Diag", m_metrozetDiag);
        
        
        gbcMetrozet.gridx = 0;
        gbcMetrozet.gridy = 4;
        gbcMetrozet.gridwidth = 3;
        metrozetPanel.add(metrozetTabbedPane, gbcMetrozet);

        //metrozetPanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Metrozet"));
        
        
    }



private void BuildeCalibPanel() {
	int x=0;
	int y=0;
	
	eCalibPanel.setLayout(new GridBagLayout());
    
    GridBagConstraints gbceCalib = new GridBagConstraints();
    gbceCalib.weightx = 1.;
    gbceCalib.gridwidth = 2;
    gbceCalib.gridheight = 1;
    gbceCalib.fill = GridBagConstraints.HORIZONTAL;
    
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    eCalibPanel.add(m_metrozetCalibrate,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    eCalibPanel.add(m_metrozetReturnCalibBtn,gbceCalib ); 
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    eCalibPanel.add(m_metrozetIntCalVelocity,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    eCalibPanel.add(m_metrozetIntCalDisconnect,gbceCalib ); 
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    eCalibPanel.add(m_metrozetEZSig,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    eCalibPanel.add(m_metrozetNZSig,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    gbceCalib.gridwidth = 2;
    eCalibPanel.add(m_metrozetSigLst,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    gbceCalib.gridwidth = 1;    
    eCalibPanel.add(m_sisTypeLb,gbceCalib);    
    gbceCalib.gridx = 1;
    gbceCalib.gridy = y;
    gbceCalib.gridwidth = 1;
    eCalibPanel.add(m_sisLst,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    gbceCalib.gridwidth = 1;    
    eCalibPanel.add(m_chTypeLb,gbceCalib);    
    gbceCalib.gridx = 1;
    gbceCalib.gridy = y;
    gbceCalib.gridwidth = 1;
    eCalibPanel.add(m_chLst,gbceCalib );
    y++;
    gbceCalib.gridx = 0;
    gbceCalib.gridy = y;
    gbceCalib.gridwidth = 2;
    eCalibPanel.add(m_calibStartBtn,gbceCalib );
    y++;
    

    
    
}
   
    private void BuildQ330Panel() {
    	
        q330Panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcQ330 = new GridBagConstraints();
        gbcQ330.gridwidth = 1;
        gbcQ330.gridheight = 1;
        gbcQ330.weightx = 1.;
        gbcQ330.fill = GridBagConstraints.HORIZONTAL;
        
        q330Panel.add(m_q330SerialLb, gbcQ330);
        gbcQ330.gridx = 1;
        gbcQ330.gridwidth = 2;
        q330Panel.add(m_q330SerialTf, gbcQ330);
        
        gbcQ330.gridx = 0;
        gbcQ330.gridy = 1;
        gbcQ330.gridwidth = 1;
        q330Panel.add(m_q330AddressLb, gbcQ330);
        
        gbcQ330.gridx = 1;
        gbcQ330.gridy = 1;
        gbcQ330.gridwidth = 2;
        q330Panel.add(m_q330AddressTf, gbcQ330);
        
        gbcQ330.gridx = 0;
        gbcQ330.gridy = 2;
        gbcQ330.gridwidth = 1;
        q330Panel.add(m_q330DataPortLb, gbcQ330);
        
        m_q330DataPortLst.setSelectedIndex(2);
        gbcQ330.gridx = 1;
        gbcQ330.gridy = 2;
        gbcQ330.gridwidth = 2;
        q330Panel.add(m_q330DataPortLst, gbcQ330);



        gbcQ330.gridx = 0;
        gbcQ330.gridy = 3;
        gbcQ330.gridwidth = 1;
        q330Panel.add(m_q330AuthCodeLb, gbcQ330);
        
        gbcQ330.gridx = 1;
        gbcQ330.gridy = 3;
        gbcQ330.gridwidth = 2;
        q330Panel.add(m_q330AuthCodeTf, gbcQ330);
        
        gbcQ330.gridx = 0;
        gbcQ330.gridy = 4;
        gbcQ330.gridwidth = GridBagConstraints.REMAINDER;
        
        JPanel q330Commands = new JPanel(new GridLayout(1,4));
        
        q330Commands.add(m_registerBtn);
        q330Commands.add(m_deregisterBtn);
        q330Panel.add(q330Commands, gbcQ330);
        
        
        //... Initialize components
        m_q330SerialTf.setText(m_model.getQ330Serial());
        m_q330AddressTf.setText(m_model.getQ330Address());
        m_q330DataPortLst.setSelectedItem(m_model.getQ330DataPort());
        m_q330AuthCodeTf.setText(m_model.getQ330AuthCode());

    }
 
/**
 * Get methods
 * @return
 */
    String getQ330Serial() {
        return m_q330SerialTf.getText();
    }
    
    String getQ330Address() {
        return m_q330AddressTf.getText();
    }
    
    String getQ330DataPort() {
        return m_q330DataPortLst.getSelectedItem().toString();
    }
    
    String getQ330AuthCode() {
        return m_q330AuthCodeTf.getText();
    }
    
    String getTableAddress() {
        return m_tableAddressTf.getText();
    }
    
    String getTableDisplacement() {
        return m_tableDisplacementTf.getText();
    }
    
    String getTableFreePeriod() {
        return m_tableFreePeriodTf.getText();
    }
    
    String getTableDamping() {
        return m_tableDampingTf.getText();
    }
    String getTableBits() {
        return m_tableBitsTf.getText();
    }
    String getTableVolts() {
        return m_tableVoltsTf.getText();
    }
    String getTableChannel() {
        return m_tableChannelLst.getSelectedItem().toString();
    }
    
    String getMetrozetAddress() {
        return m_metrozetAddressTf.getText();
    }
    
    String getMetrozetCommand() {
        return m_metrozetCommandTf.getText();
    }
    
    boolean getChType() {
    	return (this.m_chLst.getSelectedItem() == "HH");
    }
    
/**
 * Set methods
 * @param s
 */    
    void setQ330Address(String s) {
    	this.m_q330AddressTf.setText(s);
    }
    void setQ330Serial(String s) {
    	this.m_q330SerialTf.setText(s);
    }
    void setQ330AuthCode(String s) {
    	this.m_q330AuthCodeTf.setText(s);
    }
    void setQ330DataPort(String s) {
    	this.m_q330DataPortLst.setSelectedItem(s);;
    }
    void setTableAddress(String s) {
    	this.m_tableAddressTf.setText(s);
    }
    void setTableDisplacement(String s) {
    	this.m_tableDisplacementTf.setText(s);
    }
    void setTableFreePeriod(String s) {
    	this.m_tableFreePeriodTf.setText(s);
    }
    void setTableDamping(String s) {
    	this.m_tableDampingTf.setText(s);
    }
    void setTableBits(String s) {
    	this.m_tableBitsTf.setText(s);
    }
    void setTableVolts(String s) {
    	this.m_tableVoltsTf.setText(s);
    }
    void setTableChannel(String s) {
    	this.m_tableChannelLst.setSelectedItem(s);
    }
    void setMetrozetAddress(String s) {
    	this.m_metrozetAddressTf.setText(s);
    }
    
    void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
    
/**
 * add Listeners methods
 * @param mal
 */
    void addRegisterListener(ActionListener mal) {
    	m_registerBtn.addActionListener(mal);
    }
    void addDeRegisterListener(ActionListener mal) {
    	m_deregisterBtn.addActionListener(mal);
    }
    void addStartListener(ActionListener mal) {
    	m_startBtn.addActionListener(mal);
    }
    void addStopListener(ActionListener mal) {
    	m_stopBtn.addActionListener(mal);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////    
    
    void addTableConnectListener(ActionListener mal) {
    	m_tableConnnectBtn.addActionListener(mal);
    }
    void addTableModeListener(ActionListener mal) {
    	m_tableModeBtn.addActionListener(mal);
    }
    void addTablePushListener(ActionListener mal) {
    	m_tablePushBtn.addActionListener(mal);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////    
    
    void addQ330AddressTfListener(ActionListener mal) {
    	m_q330AddressTf.addActionListener(mal);
    	m_q330AddressTf.addFocusListener((FocusListener) mal);
    }
    void addQ330SerialTfListener(ActionListener mal) {
    	m_q330SerialTf.addActionListener(mal);
    	m_q330SerialTf.addFocusListener((FocusListener) mal);
    }
    void addQ330DataPortTfListener(ActionListener mal) {
    	m_q330DataPortLst.addActionListener(mal);
    	m_q330DataPortLst.addFocusListener((FocusListener) mal);
    }
    void addQ330DataPortLstListener(ActionListener mal) {
    	m_q330DataPortLst.addActionListener(mal);
    	m_q330DataPortLst.addFocusListener((FocusListener) mal);
    }
    void addQ330AuthCodeTfListener(ActionListener mal) {
    	m_q330AuthCodeTf.addActionListener(mal);
    	m_q330AuthCodeTf.addFocusListener((FocusListener) mal);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////    
    
    void addTableAddressTfListener(ActionListener mal) {
    	m_tableAddressTf.addActionListener(mal);
    	m_tableAddressTf.addFocusListener((FocusListener) mal);
    }
    void addTableDisplacementTfListener(ActionListener mal) {
    	m_tableDisplacementTf.addActionListener(mal);
    	m_tableDisplacementTf.addFocusListener((FocusListener) mal);
    }
    void addTableFreePeriodTfListener(ActionListener mal) {
    	m_tableFreePeriodTf.addActionListener(mal);
    	m_tableFreePeriodTf.addFocusListener((FocusListener) mal);
    }    
    void addTableDampingTfListener(ActionListener mal) {
    	m_tableDampingTf.addActionListener(mal);
    	m_tableDampingTf.addFocusListener((FocusListener) mal);
    }
    void addTableBitsTfListener(ActionListener mal) {
    	m_tableBitsTf.addActionListener(mal);
    	m_tableBitsTf.addFocusListener((FocusListener) mal);
    }
    void addTableVoltsTfListener(ActionListener mal) {
    	m_tableVoltsTf.addActionListener(mal);
    	m_tableVoltsTf.addFocusListener((FocusListener) mal);
    }
    void addTableChannelLstListener(ActionListener mal) {
    	m_tableChannelLst.addActionListener(mal);
    	m_tableChannelLst.addFocusListener((FocusListener) mal);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
    
    void addMetrozetAddressTfListener(ActionListener mal) {
    	m_metrozetAddressTf.addActionListener(mal);
    	m_metrozetAddressTf.addFocusListener((FocusListener) mal);
    }
    void addMetrozetCommandTfListener(ActionListener mal) {
    	m_metrozetCommandTf.addActionListener(mal);
    }  
    
    void addMetrozetListener(ActionListener mal) {
    	
    	m_metrozetEnableBtn.addActionListener(mal);
    	m_metrozetSafeBtn.addActionListener(mal);
    	m_metrozetStatusBtn.addActionListener(mal);
    	m_metrozetReturnBtn.addActionListener(mal);
    	m_metrozetMotorBtn.addActionListener(mal);
    	m_metrozetSensorBtn.addActionListener(mal);
    	m_metrozetDiagBtn.addActionListener(mal);
    	
    	m_metrozetDiagAnalPwrPlusAdc.addActionListener(mal);
    	m_metrozetDiagAnalPwrMinusAdc.addActionListener(mal);
    	m_metrozetDiagInputPwrPlusAdc.addActionListener(mal);
    	m_metrozetDiagInputPwrMinusAdc.addActionListener(mal);
    	
    	m_metrozetZ.dampBtn.addActionListener(mal);
    	m_metrozetZ.undampBtn.addActionListener(mal);
    	m_metrozetZ.moveMinusBtn.addActionListener(mal);
    	m_metrozetZ.movePlusBtn.addActionListener(mal);
    	m_metrozetZ.lpBtn.addActionListener(mal);
    	m_metrozetZ.spBtn.addActionListener(mal);
    	m_metrozetZ.stopBtn.addActionListener(mal);
    	m_metrozetZ.boomAdcBtn.addActionListener(mal);
    	
    	m_metrozetN.dampBtn.addActionListener(mal);
    	m_metrozetN.undampBtn.addActionListener(mal);
    	m_metrozetN.moveMinusBtn.addActionListener(mal);
    	m_metrozetN.movePlusBtn.addActionListener(mal);
    	m_metrozetN.lpBtn.addActionListener(mal);
    	m_metrozetN.spBtn.addActionListener(mal);
    	m_metrozetN.stopBtn.addActionListener(mal);
    	m_metrozetN.boomAdcBtn.addActionListener(mal);
    	
    	m_metrozetE.dampBtn.addActionListener(mal);
    	m_metrozetE.undampBtn.addActionListener(mal);
    	m_metrozetE.moveMinusBtn.addActionListener(mal);
    	m_metrozetE.movePlusBtn.addActionListener(mal);
    	m_metrozetE.lpBtn.addActionListener(mal);
    	m_metrozetE.spBtn.addActionListener(mal);
    	m_metrozetE.stopBtn.addActionListener(mal);
    	m_metrozetE.boomAdcBtn.addActionListener(mal);
    	
    	/*m_metrozetDiagAnalPwrMinusAdc.addActionListener(mal);
    	m_metrozetDiagAnalPwrPlusAdc.addActionListener(mal);
    	m_metrozetDiagInputPwrMinusAdc.addActionListener(mal);
    	m_metrozetDiagInputPwrPlusAdc.addActionListener(mal);*/
    	
    	
    }
    
    
    void addElectricalCalibListener(ActionListener mal) {	
    	m_metrozetEZSig.addActionListener(mal);
    	m_metrozetNZSig.addActionListener(mal);
    	m_calibStartBtn.addActionListener(mal);  
    	m_metrozetCalibrate.addActionListener(mal);  
    	m_metrozetIntCalDisconnect.addActionListener(mal);  
    	m_metrozetIntCalVelocity.addActionListener(mal);  
    	m_metrozetReturnCalibBtn.addActionListener(mal);  
    	
    }

    
    ////////////////////////////////////////////////////////////////////////////////////////
    
    void addMenuItemSTS1Listener(ActionListener mal) {
    	menuItemSTS1.addActionListener(mal);
    }
    void addMenuItemSTS2Listener(ActionListener mal) {
    	menuItemSTS2.addActionListener(mal);
    }    
    void addMenuItemSaveListener(ActionListener mal) {
    	menuItemSave.addActionListener(mal);
    }
    void addMenuItemOpenListener(ActionListener mal) {
    	menuItemOpen.addActionListener(mal);
    }
    void addMenuItemAboutListener(ActionListener mal) {
    	menuItemAbout.addActionListener(mal);
    }
    void addMenuQuitListener(ActionListener mal) {
    	menuQuit.addActionListener(mal);
    }
    void addMenuCalibListener(ActionListener mal) {
    	menuCalib.addActionListener(mal);
    }
    void addMenuItemSetupListener(ActionListener mal) {
    	menuItemSetup.addActionListener(mal);
    }
    void addMenuItemAbsoluteCalibListener(ActionListener mal) {
    	menuItemAbsoluteCalib.addActionListener(mal);
    }
    void addMenuItemElectricalCalibListener(ActionListener mal) {
    	menuItemElectricalCalib.addActionListener(mal);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
    
    void ShowAbout() {
    	JOptionPane.showMessageDialog(null, "Java Seismometer Calibration Tool - v"+version+" \nN. LEROY / GEOSCOPE - 2010\n\nDisplay Based On JFreeChart");
    }
    void ShowResult(String value) {
    	CalcResult result = new CalcResult(m_model.dataset, true);
    	result.console.write(value);
    	result.setVisible(true);
    }
    public DisplayChannel getChannel () { 	
    	return (DisplayChannel) tabbedPane.getSelectedComponent();
    }
    
/**
 * Wizards methods
 */
    public String ShowCalib1() {
    	Object[] possibilities = {"Q330HR", "Q330SR", "Other"};
    	String s = (String)JOptionPane.showInputDialog(
    	                    null,
    	                    "Digitizer",
    	                    "Customized Dialog",
    	                    JOptionPane.PLAIN_MESSAGE,
    	                    null,
    	                    possibilities,
    	                    "Q330HR");


		System.out.println(s);
		return s;
    }
    
    
    public String ShowCalib2() {
    	Object[] possibilities = {"STS1", "STS2", "Other"};
    	String s = (String)JOptionPane.showInputDialog(
    	                    null,
    	                    "Seismometer",
    	                    "Customized Dialog",
    	                    JOptionPane.PLAIN_MESSAGE,
    	                    null,
    	                    possibilities,
    	                    "STS1");


		System.out.println(s);
		return s;
    }
    public String ShowCalib3() {
    	Object[] possibilities = {"HHZ", "HHN", "HHE", "BHZ", "BHN", "BHE"};
    	String s = (String)JOptionPane.showInputDialog(
    	                    null,
    	                    "Axis",
    	                    "Customized Dialog",
    	                    JOptionPane.PLAIN_MESSAGE,
    	                    null,
    	                    possibilities,
    	                    "HHZ");
		System.out.println(s);
		return s;
    }
    
    
    public String ShowSetup1() {
    	String s = (String)JOptionPane.showInputDialog(null, "Q330 IP Address : ");
		System.out.println(s);
		return s;
    }
    public String ShowSetup2() {
    	String s = (String)JOptionPane.showInputDialog(null, "Q330 Serial Number : ");
		System.out.println(s);
		return s;
    }
    public String ShowSetup3() {
    	String s = (String)JOptionPane.showInputDialog(null, "Q330 Authorization code : ");
		System.out.println(s);
		return s;
    }
    public String ShowSetup4() {
    	String s = (String)JOptionPane.showInputDialog(null, "Q330 Data Port : ");
		System.out.println(s);
		return s;
    }
    
    
    public void STS1View() {
    	tabbedPane.add("BMZ", bmz);
    	tabbedPane.add("BMN", bmn);
    	tabbedPane.add("BME", bme);
		tabbedPaneConfig.add("Metr.", metrozetPanel );
		tabbedPaneConfig.add("Elec.", eCalibPanel);
    	this.pack();
    }
    public void STS2View() {
    	tabbedPane.remove(bmz);
    	tabbedPane.remove(bmn);
    	tabbedPane.remove(bme);
    	tabbedPaneConfig.remove(this.metrozetPanel);
    	tabbedPaneConfig.remove(this.eCalibPanel);
    	this.pack();
    }
    
    public void ToggleStartStop() {
    	if (m_startBtn.getText() == "Start") {
    		m_startBtn.setText("Stop");	
    	}
    	else {
    		m_startBtn.setText("Start");
    	}	
    }
    
    public void ToggleECalibStartStop() {
    	if (m_calibStartBtn.getText() == "Start") {
    		m_calibStartBtn.setText("Stop");	
    	}
    	else {
    		m_calibStartBtn.setText("Start");
    	}	
    }
    
    public void ToggleCalibStartStop() {
    	if (m_calibStartBtn.getText() == "Start") {
    		m_calibStartBtn.setText("Stop");	
    	}
    	else {
    		m_calibStartBtn.setText("Start");
    	}	
    }
    
    public void refresh() {
    	
    }
    
	public static void main(String[] args) {
        jSeisCalModel      model      = new jSeisCalModel();
        jSeisCalView       view       = new jSeisCalView(model);
        view.setVisible(true);

	}
}



