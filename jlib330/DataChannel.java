package jlib330;

import java.util.HashMap;
import java.util.Observable;

public class DataChannel extends Observable {
    
	public long[] times = null;
	public double[] samples = null;
    public int index = 0;
    public boolean newDataFlag = false;
    public String name="";
    public int sampleRate = 1;     
	public HashMap<Integer, Long> stamps;
    public long timeStamp = 0;
    public int lastSegment = 0;
    public int lastNibble = 0;
    
	
	public byte[] 		ringSamples;
	public byte[] 		compressionMap;
	public int          compressionMapLength; 
	public int    		segmentNumber;
	public int    		segmentTotal;
	public int    		segmentIndex;
	public int      	segmentReceived;
	public byte[][]		segmentBlocs;
	public byte[]		reconstructedBloc;
	public int  		reconstructedBlocLength;
	
	public boolean 		channelOn = true;
	
	public int indexStart = 0;
	public int indexStop = 0;
	public int calibLength = 0;
	public double [][] calibSeg;
	
	public int maxTime = 3600; // 1h max
	   
    public DataChannel(String strName, int sr) {
    	name = strName;
    	sampleRate = sr;
    	init();
    }
    
    public void init() {
    	index = 0;
    	timeStamp = 0;
    	stamps = new HashMap<Integer, Long>();
    	samples = new double[sampleRate * maxTime]; 
    	times = new long[sampleRate * maxTime]; 
    	newDataFlag = false;
    	segmentBlocs = new byte[10][]; // max 10 segments    
    	segmentReceived = 0;
    	reconstructedBlocLength = 0;
    }
    
    public void addData(long time, int inSample) {
    	times[index] = time;
    	samples[index] = (double)inSample;
    	setChanged();
    	notifyObservers();
    	index++;
    }
    
    public void addDataBlock(long inTimeStamp, int[] inSamples) {
    	if (channelOn) {
    		if (inTimeStamp != 0) {
    		   	if (sampleRate == 20) {
    				for (int k = 0; k < 20; k++) {
    					times[index] = inTimeStamp + 50 * k - 1700;  // TODO : delay parameter !!
    					samples[index] = (double)inSamples[k+1];
    					index++;
    					}	
    		    	setChanged();
    		    	notifyObservers();
    			}
    			else if (sampleRate == 200) {
    				for (int k = 0; k < 200; k++) {
    					times[index] = inTimeStamp + 5 * k - 90; // TODO : delay as parameter !!
    					samples[index] = (double)inSamples[k+1];
    					index++;
    					}	
    		    	setChanged();
    		    	notifyObservers();
    			}
    		}
    	}
    }
    
    public void clear() {
    	this.channelOn = false;
   		init();
   		this.channelOn = true;
    }
    
    
	public void fillRing(int indexStamp, byte[] blockette) {
    	if (channelOn) {
    		int segmentNumber = (blockette[1] >> 3) & 0x1F;		
    		int size = Util.Bytes2ToInt(blockette,2);
    		boolean isLast = ((size & 0x8000) == 0x8000);
    		size &= 0x3FF;
    		int samplesLength = 0;
    				
    		if (isLast) {
    			segmentTotal = segmentNumber+1;
    		}
    		
    		if (segmentNumber == 0) {
    			// copy full blockette with compression map etc ...
    			segmentBlocs[0] = new byte[blockette.length];
    			System.arraycopy(blockette, 0, segmentBlocs[0], 0, blockette.length);
    		} else {			
    			samplesLength = size - 4;
    			segmentBlocs[segmentNumber] = new byte[samplesLength];
    			System.arraycopy(blockette, 4, segmentBlocs[segmentNumber], 0, samplesLength);
    		}
    		
    		segmentReceived += 1;
    		
    		if ((segmentTotal != 0) && (segmentReceived == segmentTotal)) {			
    			reconstructedBlocLength = 0;
    			for (int i = 0; i < segmentTotal; i++) {
    	    		reconstructedBlocLength += segmentBlocs[i].length;	    		
    	    	}
    			
    			byte[] bloc = new byte[reconstructedBlocLength];
    			int tempIndex = 0;
    			for (int i = 0; i < segmentTotal; i++) {
    				System.arraycopy(segmentBlocs[i], 0, bloc, tempIndex, segmentBlocs[i].length);
    				tempIndex += segmentBlocs[i].length;			
    	    	}
    	    	timeStamp = stamps.get(indexStamp);
    			addDataBlock(timeStamp, extractSamples(bloc, reconstructedBlocLength));
    			
    			segmentTotal = 0;
    			segmentReceived = 0;
    			
    		}
    	}
		

	}
    
	public int[] extractSamples(byte[] bloc, int size) {
		/* get nibbles */
		int offset = Util.Bytes2ToInt(bloc, 8);
		int nibblesoffset = 10;
		int nibbles = (size - offset) / 4;
		int n = 0;
		int currNibble = 0;
		int dnib = 0;
		int[] values = new int[1024];
		int valuesoffset = offset;
		int tempInt;
		int tempval = 0;
		int currNum = 0;
		int i = 0;
		int j = 0;
		
		int previous = Util.DWordToInt(bloc, 4);
		if (previous >  536870911)
			previous -= 1073741824;
		
		values[currNum++] = previous;

		for (i = nibblesoffset; (i < offset)&& (n < nibbles); i++) {
			for (j = 0; (j < 4) && (n < nibbles); j++) {
				currNibble = (bloc[i] >> (6 - j * 2)) & 0x03;
				switch (currNibble) {
				case 0:
					n++;
					//end = true;
					break;
				case 1:
					n++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					break;
				case 2:
					n++;
					tempInt = Util.DWordToInt(bloc, valuesoffset);
					dnib = (tempInt >> 30) & 0x03;
					switch (dnib) {
					case 1:
						tempval = tempInt & 0x3FFFFFFF;
						//tempval = ((tempInt << 2) >>> 2);
						if (tempval >  536870911)
							tempval -= 1073741824;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						//System.out.println(currNibble + " " + dnib + " " + currNum + " "+ valuesoffset);
						break;
					case 2:
						tempval = (tempInt >> 15) & 0x00007fff;
						if (tempval > 16383)
							tempval -= 32768;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = tempInt & 0x00007fff;;
						if (tempval > 16383)
							tempval -= 32768;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						//System.out.println(currNibble + " " + dnib + " " + currNum + " "+ valuesoffset);
						break;
					case 3:
						tempval = (tempInt >> 20) & 0x000003ff;
						if (tempval > 511)
							tempval -= 1024;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 10) & 0x000003ff;
						if (tempval > 511)
							tempval -= 1024;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = tempInt & 0x000003ff;
						if (tempval > 511)
							tempval -= 1024;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						//System.out.println(currNibble + " " + dnib + " " + currNum + " "+ valuesoffset);
						break;
					default:
						System.out.println("error 2");
						break;
					}
					break;
				case 3:
					n++;
					tempInt = Util.DWordToInt(bloc, valuesoffset);
					dnib = (tempInt >> 30) & 0x03;
					switch (dnib) {
					case 0:
						tempval = (tempInt >> 24) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 18) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 12) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 6) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = tempInt & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						//System.out.println(currNibble + " " + dnib + " " + currNum + " "+ valuesoffset);
						break;
					case 1:
						tempval = (tempInt >> 25) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 20) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 15) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 10) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 5) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = tempInt & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						//System.out.println(currNibble + " " + dnib + " " + currNum + " "+ valuesoffset);
						break;
					case 2:
						tempval = (tempInt >> 24) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 20) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 16) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 12) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 8) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = (tempInt >> 4) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = tempInt & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						//System.out.println(currNibble + " " + dnib + " " + currNum + " "+ valuesoffset);
						break;
					default:
						System.out.println("error 3");
						break;
					}
				}
			}
		}
		return values;
	}
	
	public void setStart() {
		this.indexStart = this.index;
	}
	
	public void setStop() {
		this.indexStop = this.index;
		this.calibLength = this.indexStop - this.indexStart;
	}
	
	public double[][] getSegment() {
		double[][] seg = new double[2][this.calibLength];
		
		return seg;
	}
    
   
}
