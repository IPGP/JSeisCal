package calc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Locale;

import javax.swing.JFileChooser;

import jlib330.Data;

public class ECalibDataset {

	int sr = 200;
	int nbsamples = 0;
	double[][] samples;
	long[] times;
	String filename;
	int samplerate = 200;

	public ECalibDataset(int ns) {
		Init(ns);
	}
	
	public ECalibDataset(Data buffers, boolean hData) {
		int lenghtMin = Integer.MAX_VALUE;
		
		if (hData) {
			this.samplerate = 200;
			lenghtMin = Math.min(lenghtMin, buffers.hhz.calibLength);
			lenghtMin = Math.min(lenghtMin, buffers.hhn.calibLength);
			lenghtMin = Math.min(lenghtMin, buffers.hhe.calibLength);
			nbsamples = lenghtMin;		
			samples = new double[3][nbsamples];
			System.arraycopy(buffers.hhz.samples, buffers.hhz.indexStart, samples[0], 0, nbsamples);
			System.arraycopy(buffers.hhn.samples, buffers.hhz.indexStart, samples[1], 0, nbsamples);
			System.arraycopy(buffers.hhe.samples, buffers.hhz.indexStart, samples[2], 0, nbsamples);
		} else {
			this.samplerate = 20;
			lenghtMin = Math.min(lenghtMin, buffers.bhz.calibLength);
			lenghtMin = Math.min(lenghtMin, buffers.bhn.calibLength);
			lenghtMin = Math.min(lenghtMin, buffers.bhe.calibLength);	
			nbsamples = lenghtMin;		
			samples = new double[3][nbsamples];
			System.arraycopy(buffers.bhz.samples, buffers.bhz.indexStart, samples[0], 0, nbsamples);
			System.arraycopy(buffers.bhn.samples, buffers.bhz.indexStart, samples[1], 0, nbsamples);
			System.arraycopy(buffers.bhe.samples, buffers.bhz.indexStart, samples[2], 0, nbsamples);
		}
		
		times = new long[nbsamples];
		for (int i=0; i<nbsamples;i++) {
			times[i] = (long) (1000*i/this.samplerate);
		}
		

		

	}

	public void Init(int ns) {
		nbsamples = ns;
		samplerate = 200; //temporaire
		samples = new double[3][nbsamples];
		times = new long[nbsamples];
	}

	public void setData(int start, int stop, double[] sig1, double[] sig2, double[] sig3) {
		nbsamples = stop - start;

		samples = new double[3][nbsamples];

		System.arraycopy(sig1, start, samples[0], 0, nbsamples);
		System.arraycopy(sig2, start, samples[1], 0, nbsamples);
		System.arraycopy(sig3, start, samples[2], 0, nbsamples);
	}

	public void saveDatasetToFile() {
		try{
			//Create a file chooser
			JFileChooser fc = new JFileChooser();

			//In response to a button click:
			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				FileWriter fstream = new FileWriter(fc.getSelectedFile().getAbsolutePath());
				BufferedWriter out = new BufferedWriter(fstream);
				String strLine="";
				System.out.println(nbsamples);
				for (int i=0;i<nbsamples;i++) {
					strLine = String.format(Locale.US, "%13.3f\t%13.3f\t%13.3f",
							samples[0][i],
							samples[1][i],
							samples[2][i]);
					out.write(strLine);
					out.newLine();

				}

				//Close the output stream
				out.close();
			} else {
				System.out.println(returnVal);
			}
			
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}



	}
}
