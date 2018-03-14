package jlib330;

import java.util.HashMap;


public class Data {

	public HashMap<Integer, Long> stamps;
    public long timestamp;
    
    public DataChannel bhz;
    public DataChannel bhn;
    public DataChannel bhe;
    public DataChannel hhz;
    public DataChannel hhn;
    public DataChannel hhe;
    public DataChannel bmz;
    public DataChannel bmn;
    public DataChannel bme;
    
    

    public Data() {
    	Init();
    }
    
    public void Init() {
    	stamps = new HashMap<Integer, Long>();
    	// TODO : make it dynamic !
    	bhz = new DataChannel("BHZ", 20);    	
    	bhn = new DataChannel("BHN", 20);
    	bhe = new DataChannel("BHE", 20);
    	hhz = new DataChannel("HHZ", 200);
    	hhn = new DataChannel("HHN", 200);
    	hhe = new DataChannel("HHE", 200);
    	bmz = new DataChannel("BMZ", 20);
    	bmn = new DataChannel("BMN", 20);
    	bme = new DataChannel("BME", 20);
    	
    }
    
    public void clear() {
    	bhz.clear();
    	bhn.clear();
    	bhe.clear();
    	hhz.clear();
    	hhn.clear();
    	hhe.clear();
    	bmz.clear();
    	bmn.clear();
    	bme.clear();
    }
    
    public void setStart() {
    	bhz.setStart();
    	bhn.setStart();
    	bhe.setStart();
    	hhz.setStart();
    	hhn.setStart();
    	hhe.setStart();
    	bmz.setStart();
    	bmn.setStart();
    	bme.setStart();
    }
    public void setStop() {
    	bhz.setStop();
    	bhn.setStop();
    	bhe.setStop();
    	hhz.setStop();
    	hhn.setStop();
    	hhe.setStop();
    	bmz.setStop();
    	bmn.setStop();
    	bme.setStop();
    }
    
    public void addData(int index, int channel, int freq, int[] samples) {
    	long timer = System.currentTimeMillis();
    	timestamp = stamps.get(index);
    	switch (channel) {
	    	case 0 :
	    		switch (freq) {
		    		case 2 : //BH	
		    			bhz.addDataBlock(timestamp, samples);
		    			break;
					case 6 : // HH
						hhz.addDataBlock(timestamp, samples);
						break;
	    		}
	    		break;
	    	case 1 :
	    		switch (freq) {
		    		case 2 : //BH
		    			bhn.addDataBlock(timestamp, samples);
		    			break;
					case 6 : // HH
						hhn.addDataBlock(timestamp, samples);
						break;
	    		}
	    		break;
	    	case 2 :
	    		switch (freq) {
		    		case 2 : //BH
		    			bhe.addDataBlock(timestamp, samples);
		    			break;
					case 6 : // HH
						hhe.addDataBlock(timestamp, samples);
						break;
	    		}
	    		break;

	    	case 3 :
	    		switch (freq) {
		    		case 2 : //BM	
		    			bmz.addDataBlock(timestamp, samples);
		    			break;					
	    		}
	    		break;
	    	case 4 :
	    		switch (freq) {
		    		case 2 : //BM
		    			bmn.addDataBlock(timestamp, samples);
		    			break;
	    		}
	    		break;
	    	case 5 :
	    		switch (freq) {
		    		case 2 : //BM
		    			bme.addDataBlock(timestamp, samples);
		    			break;
	    		}
	    		break;
	    	default :
	    		break;
    	}
    	timer = (System.currentTimeMillis() - timer);
	} 
    
    
    public void fillRing(int indexStamp, byte[] blockette) {
		int channel = blockette[0] & 0x07;
		int freq = blockette[1];
		freq &= 0x07;
		
    	switch (channel) {
    	case 0 :
    		switch (freq) {
	    		case 2 : //BH	
	    			bhz.fillRing(indexStamp, blockette);	
	    			break;
				case 6 : // HH
					hhz.fillRing(indexStamp, blockette);	
					break;
    		}
    		break;
    	case 1 :
    		switch (freq) {
	    		case 2 : //BH
	    			bhn.fillRing(indexStamp, blockette);	
	    			break;
				case 6 : // HH
					hhn.fillRing(indexStamp, blockette);	
					break;
    		}
    		break;
    	case 2 :
    		switch (freq) {
	    		case 2 : //BH
	    			bhe.fillRing(indexStamp, blockette);	
	    			break;
				case 6 : // HH
					hhe.fillRing(indexStamp, blockette);	
					break;
    		}
    		break;

    	case 3 :
    		switch (freq) {
	    		case 2 : //BM	
	    			bmz.fillRing(indexStamp, blockette);	
	    			break;					
    		}
    		break;
    	case 4 :
    		switch (freq) {
	    		case 2 : //BM
	    			bmn.fillRing(indexStamp, blockette);	
	    			break;
    		}
    		break;
    	case 5 :
    		switch (freq) {
	    		case 2 : //BM
	    			bme.fillRing(indexStamp, blockette);	
	    			break;
    		}
    		break;
    	default :
    		break;
	}
    }
    
    public void setTimeStamp(int indexStamp, long ts) {
    	stamps.put(indexStamp, ts);
    	bhz.stamps.put(indexStamp, ts);
    	bhn.stamps.put(indexStamp, ts);
    	bhe.stamps.put(indexStamp, ts);
    	hhz.stamps.put(indexStamp, ts);
    	hhn.stamps.put(indexStamp, ts);
    	hhe.stamps.put(indexStamp, ts);
    	bmz.stamps.put(indexStamp, ts);
    	bmn.stamps.put(indexStamp, ts);
    	bme.stamps.put(indexStamp, ts);
    	
    }

}
