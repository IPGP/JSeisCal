package calc;

import java.lang.Math;
import calc.CalcDataset;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.*;


public class Calc {

	/**
	 * Adaptation of Dispcal routine : calculate vertical sensitivity
	 *  
	 * @param  ds			dataset containing samples and recording info
	 * @return sensitivity  in V/m/s
	 */
	public static double VerticalSensitivity(CalcDataset ds) {
		double sensitivity = 0;
		int nbsamples = ds.nbsamples;
		//ds.uVperCount = ds.volts / Math.pow(2,ds.bits) * 1000000;
		ds.uVperCount = 0.596;
	    double fac = (ds.uVperCount/ds.displacement) * 0.001;
		double[] z = new double[nbsamples];
		double[] steps;
		double averageStep = 0;

		System.out.println("Vertical Sensitivity Calculation");
		System.out.println("Sample Rate = " + ds.samplerate + ", Safety Distance = " + ds.safetydistance);
		System.out.println("Bits = " + ds.bits + ", Volts = " + ds.volts + ", uV/count = " + ds.uVperCount+ ", Disp = " + ds.displacement);
		System.out.println("Free Period = " + ds.freeperiod + ", Damping = " + ds.damping);
		
		System.out.println("Nbsamples = " + nbsamples);

		/*
		 * Deconvolution to ground velocity
		 */ 
		RemoveAverage(ds);
		ApplyExactInverseSecondOrderHighPassFilter(ds);
		
		// Init weights
		for (int i = 0; i < nbsamples; i++) { z[i] = 1;	}
		
		ds.samplesAfterPol = RemovePolynomialTrend(ds.samplesAfterSez, z);
		System.arraycopy(ds.samplesAfterPol, 0, ds.samplesDeconvolved, 0, ds.nbsamples);
		// End of deconvolution
		
		/*
		 * Get quiet sections and pulses
		 */
		ds.samplesQuietness = SearchForQuietSections(ds);
		
		/*
		 * Correct signal using weights
		 */
		ds.samplesAfterPol2 = RemovePolynomialTrend(ds.samplesDeconvolved, ds.samplesQuietness);

		/*
		 * Store signal for integration
		 */

		
		System.arraycopy(ds.samplesAfterPol2, 0, ds.samplesDetrendedWie, 0, ds.nbsamples);
		
		// get only pulses
		/*for (int i = 0; i < nbsamples; i++) { 
			if (z[i] > 0.5)
				ds.samplesDetrendedWie[i] = 0;				
		}*/
		
		/* 
		 * Wielandt style
		 * */
		steps = DetrendAndIntegrate(ds.samplesDetrendedWie, ds.samplesQuietness, ds.samplerate, ds.safetydistance);	 // interval size min max
		averageStep = ProcessSteps(steps, fac);
		System.out.println("Wielandt = " + averageStep*fac);
		
		/*
		 * Integrate over all signal
		 */
		
		// get only pulses
		for (int i = 0; i < nbsamples; i++) { 
			if (ds.samplesQuietness[i] > 0.5)
				ds.samplesAfterPol2[i] = 0;				
		}
		
		ds.samplesDetrended = Integrate(ds.samplesAfterPol2, ds.samplerate);
		
		/*
		 * Retrieve each step using z as weight
		 */
		steps = GetSteps(ds.samplesDetrended, ds.samplesQuietness, ds.samplerate, ds.safetydistance);
		
		/*for (int i =0 ; i< steps.length ; i++) {
			System.out.println("Steps = " + steps[i]);
		}*/
		
		/*
		 * Calculate best average step over half the pulses
		 */
		averageStep = ProcessSteps(steps, fac);
		
		/*
		 * Convert step to sensitivity
		 */
		sensitivity = averageStep * fac;
		
		/*
		 * Residual
		 */
		
		ds.samplesResidual = Deriv(ds.samplesDetrended, ds.samplerate);
		for (int i = 0; i < ds.nbsamples; i++) {
			ds.samplesResidual[i] = (double)ds.samplesAverageRemoved[i] * ds.samplesQuietness[i];
		}	
		
			
		return sensitivity;
	}
	
	
	
	
	/**
	 * Adaptation of Tiltal routine : calculate horizontal sensitivity
	 *  
	 * @param  ds			dataset containing samples and recording info
	 * @return sensitivity  in V/m/s
	 */
	public static double HorizontalSensitivity(CalcDataset ds) {
		double sensitivity = 0;
		int nbsamples = ds.nbsamples;
		ds.uVperCount = ds.volts / Math.pow(2,ds.bits) * 1000000;
	    double acc = 9.81 * ds.displacement * 1000/ 396;
		double fac = (ds.uVperCount/acc) * 0.001;
	    
	    
		double[] z = new double[nbsamples];
		double[] steps;
		double averageStep = 0;

		System.out.println("Horizontal Sensitivity Calculation");
		System.out.println("Sample Rate = " + ds.samplerate + ", Safety Distance = " + ds.safetydistance);
		System.out.println("Bits = " + ds.bits + ", Volts = " + ds.volts + ", Disp = " + ds.displacement);
		System.out.println("Free Period = " + ds.freeperiod + ", Damping = " + ds.damping);
		

		System.out.println("Nbsamples = " + nbsamples);

		/*
		 * Deconvolution to ground velocity
		 */ 
		RemoveAverage(ds);
		ApplyExactInverseSecondOrderHighPassFilter(ds);
		
		// Init weights
		for (int i = 0; i < nbsamples; i++) { z[i] = 1;	}
		
		ds.samplesAfterPol = RemovePolynomialTrend(ds.samplesAfterSez, z);
		//System.arraycopy(ds.samplesAfterPol, 0, ds.samplesDeconvolved, 0, ds.nbsamples);
		// End of deconvolution
		
		ds.samplesDeconvolved = Deriv(ds.samplesAfterSez, ds.samplerate);
		
		/*
		 * Get quiet sections and pulses = use sampleDeconvolved
		 */
		z = SearchForQuietSections(ds);
		
		/*
		 * Correct signal using weights
		 */
		//ds.samplesAfterPol2 = RemovePolynomialTrend(ds.samplesDeconvolved, z);
		System.arraycopy(z, 0, ds.samplesAfterPol2, 0, ds.nbsamples);

		for (int i = 0; i < nbsamples; i++) { 
			ds.samplesDetrended[i] = ds.samplesDeconvolved[i] * z[i];
		}
		
		//ds.samplesDetrended = Integrate(ds.samplesDetrended, ds.samplerate);
		
		/*
		 * Retrieve each step using z as weight
		 */
		steps = GetSteps(ds.samplesDeconvolved, z, ds.samplerate, ds.safetydistance);
		
		
		
		
		//averageStep = GetStepsHorizontal(ds.samplesDeconvolved, z, ds.samplerate, ds.safetydistance);
		//System.out.println("averageStep = " + averageStep);
		/*for (int i =0 ; i< steps.length ; i++) {
			System.out.println("Steps = " + steps[i]);
		}*/
		
		/*
		 * Calculate best average step over half the pulses
		 */
		averageStep = ProcessSteps(steps, fac);
		
		/*
		 * Convert step to sensitivity
		 */
		sensitivity = averageStep * fac;
			
		return sensitivity;
	}
	
	public static double[] GetSteps(double[] in, double[] z, double samplerate, double safetydistance) {
		double[] steps = new double[100];
		int quietMin = (int)(safetydistance * samplerate);
		int quietBefore = 6 * quietMin;
		
		int n=in.length;
		double[] y = new double[n];
		boolean found = false;
		boolean end = false;
		int i=0;
		int n1=0, n2=0, n3=0, n4=0;
		
		int segmentEnd=0;
		int segmentStart=0;
		int relativeN1=0;
		int relativeN2=0;
		int relativeN3=0;
		int relativeN4=0;
		
		int pulseCount=0;
		int pulseLength = 0;
		int segmentLength = 0;
		
		 // Search first quiet interval 
		while ((i<n) && !found) {
			 // first sample of the first quiet interval
			 while ((i<n) && (z[i] < 0.5)) {
				 i++;
			 } 
			 n1=i;
			 
			 // last sample of the first quiet interval = first sample of pulse
			 while ((i<n) && (z[i] > 0.5)) {
				 i++;
			 } 
			 n2=i;
			 
			 // test interval size
			 if (n2 > n1 + quietMin)  {
				 found = true;
			 }
		}

		// Search next interval and loop until the end of the buffer
		while ((i<n) && !end) {
			
			found = false;
			// search next pulse
			while ((i<n) && !found) {
				 // first sample of the next quiet interval = last sample of pulse
				 while ((i<n) && (z[i] < 0.5)) {
					 i++;
				 } 
				 n3=i;
				 
				 // last sample of the first quiet interval = first sample next pulse
				 while ((i<n) && (z[i] > 0.5)) {
					 i++;
				 } 
				 n4=i;
				 
				 // test interval size
				 if (n4 > n3 + quietMin)  {
					 found = true;
				 }
			 }
			
			 if (i<n) {
				 // get segment of interest
				 segmentStart  = Math.max(n1, n2-quietBefore);
				 segmentEnd    = Math.min(n4, n3+quietBefore);
				 pulseLength   = n3-n2;
				 segmentLength = segmentEnd - segmentStart;
				 // store segment
				 System.arraycopy(in, segmentStart, y, 0, segmentLength);
				 // get indexes relative to the segment
				 relativeN1 = 0;
				 relativeN2 = n2-segmentStart;
				 relativeN3 = n3-segmentStart;
				 relativeN4 = segmentEnd-segmentStart;
				 
				 // retrieve and store corresponding average step value
				 steps[pulseCount++] = Math.abs(Steps(y, relativeN1, relativeN2, relativeN3, relativeN4));
				 
				 // prepare for next loop
				 n1 = n3;
				 n2 = n4;
			 }
			 
			 // test end of buffer and max pulse number
			 if ( (i > n-quietBefore) && (pulseCount >= 100) ) {
				 end = true;
			 }
		}		 
		
		// store steps with the good size
		double[] stepsreturn = new double [pulseCount];
		System.arraycopy(steps, 0, stepsreturn, 0, pulseCount);
		
		return stepsreturn;
		
	}
	
	public static double GetStepsHorizontal(double[] in, double[] z, double samplerate, double safetydistance) {
		int quietMin = (int)(safetydistance * samplerate);
		int quietBefore = 6 * quietMin;
		
		int i = quietBefore;
		int length = in.length;
		double avStep = 0;
		for (i = quietBefore; i < length ; i++) {
			avStep += Math.abs(in[i]);
		}
	
		return avStep / length;
	}
	
	
	public static double ProcessSteps(double[] steps, double fac) {
		double averageStep = 0;
		double total = 0;
		double sigma = 0;
		int length = steps.length;
		double[] validstep = new double[length];
		
		// calculate average step over all steps and init validstep for remove process
		averageStep = 0;
		for (int i=0 ; i< length; i++) {
			averageStep += Math.abs(steps[i]) / (double)length;
			validstep[i] = 1.;
		}
		
		// calculate error
		total = 0;
		for (int i=0 ; i < length; i++) {
			total += Math.pow( (Math.abs(steps[i]) - averageStep) , 2);			
		}
		sigma=Math.sqrt(total/((double)length));
		
		System.out.printf("\nRaw AverageStep = %5.2f -- Sensitivity = %5.2f +/- %5.2f V/m/s\n", averageStep, averageStep * fac, sigma*fac);
		
		double steptoremove = 0;
		int indextoremove = 0;
		int iteration = 0;

		// Iterate over half steps
		
		for (iteration=0; iteration < length/2 ; iteration++) {
			// get most excentred step 
			steptoremove = 0;
			for (int i=0 ; i< length; i++) {
				if ( (validstep[i] * (Math.abs(Math.abs(steps[i]) - averageStep ) ) ) > steptoremove) {
					indextoremove = i;
					steptoremove = Math.abs(Math.abs(steps[i])-averageStep);
				}
			}
			// set weight of step to remove to 0
			validstep[indextoremove] = 0.;
			//System.out.println("Removed Step "+indextoremove+" = " + steps[indextoremove]);
		
			// calc average step over remaining steps
			averageStep = 0;
			for (int i=0 ; i< length; i++) {
				averageStep += validstep[i] * Math.abs(steps[i]) / ((double)length - iteration - 1);			
			}
			// calculate error 
			total = 0;
			for (int i=0 ; i< length; i++) {
				total += validstep[i] * Math.pow( (Math.abs(steps[i]) - averageStep) , 2);			
			}
			sigma=Math.sqrt(total/((double)length - iteration - 1));
			
			//System.out.println(iteration + " -- Removed = "+indextoremove+" -- AverageStep = " + (int)averageStep + " -- Sensitivity = " + averageStep * fac + " V/m/s +/- " + sigma*fac + " V/m/s");
			System.out.printf("\n%2d - Removed = %2d -- AverageStep = %5.2f -- Sensitivity = %5.2f +/- %5.2f V/m/s ", iteration, indextoremove, averageStep, averageStep * fac, sigma*fac);
		}
		System.out.println("");
		
		return averageStep;
	}

	public static boolean RemoveAverage(CalcDataset ds) {
		double mean = 0;
		if (ds.nbsamplesaverage < ds.nbsamples) {
			for (int i = 0; i < ds.nbsamplesaverage; i++) {
				mean += (double) ds.samples[i] / (double) ds.nbsamplesaverage;
			}
			
		    System.out.println ("Average on " + ds.nbsamplesaverage + " samples = " + mean);
			
			for (int i = 0; i < ds.nbsamples; i++) {
				ds.samplesAverageRemoved[i] = (double)ds.samples[i] - mean;
			}	
			return true;
		}
		else {
			System.out.println ("Record length too short");
			return false;
		}

	}

	/**
	 * 
	 */
	public static void ApplyExactInverseSecondOrderHighPassFilter(CalcDataset ds) {
		double f0, f1, f2, g1, g2;
		double zpi, eps, epsq, epss, epssq;
		double a, b, c, as, bs, cs;
		double h = 1;
		double xa, xaa, ya, yaa, xn, yn;

		// rfk
		zpi = 8. * Math.atan(1);		
		eps = zpi / (double)ds.samplerate / 1000000000000. ;		
		f2 = 0;
		g2 = 0;
		epsq = eps * eps;		
		a = 1. - (eps * h) + epsq / 4.;
		b = -2. + epsq / 2.;
		c = 1. + eps * h + epsq / 4.;
		g1 = -b / c;
		g2 = -a / c;
		epss = zpi / ( (double)ds.freeperiod * (double)ds.samplerate);
		epssq = epss * epss;
		as = 1. - epss * ds.damping + epssq / 4.;
		bs = -2. + epssq / 2.;
		cs = 1. + epss * ds.damping + epssq / 4.;
		f0 = cs / c;
		f1 = bs / c;
		f2 = as / c;	
		
		// rekfl
		xa = 0;
		xaa = 0;
		ya = 0;
		yaa = 0;

		for (int j = 0; j < ds.nbsamples; j++) {
			xn = (double) ds.samplesAverageRemoved[j];
			yn = f0 * xn + f1 * xa + f2 * xaa + g1 * ya + g2 * yaa;
			ds.samplesAfterSez[j] = yn;
			xaa = xa;
			xa = xn;
			yaa = ya;
			ya = yn;
		}

	}

/**
 * 
 * @param x Input
 * @param y Output
 * @param z Weights
 */
	public static double[] RemovePolynomialTrend(double[] x, double[] z) {
		double[] y = new double[x.length];
		int m = 3; // default
		double fnh;
		double[][] c = new double[8][8];
		double[] b = new double[8];
		double[] a = new double[8];
		double aikmax;
		double[] h = new double[8];
		int index = 0;
		double q;
		int[] imax = new int[8];
		double xpol;

		// polytrend
		fnh = (double)x.length / 2;
		for (int j = 0; j < m + 1; j++) {
			for (int k = 0; k < m + 1; k++) {
				c[j][k] = 0;
				for (int i = 0; i < x.length; i++) {
					if (z[i] > 0.5) {
						c[j][k] = c[j][k]
								+ Math.pow(((((double)i + 1.) / fnh) - 1.),
										(double) (j + k));
					}
				}
			}
			b[j] = 0;
			for (int i = 0; i < x.length; i++) {
				if (z[i] > 0.5) {
					b[j] = b[j] + Math.pow(((((double)i + 1.) / fnh) - 1.), (double) j) 
							* x[i];
					//System.out.println(b[j]);
				}
			}
		}

		// Gauss
		for (int j = 0; j < 4; j++) {
			aikmax = 0;
			for (int k = 0; k < 4; k++) {
				h[k] = c[j][k];
				if (Math.abs(h[k]) > aikmax) {
					aikmax = Math.abs(h[k]);
					index = k;					
				}
			}
			h[4] = b[j];			
			for (int k = 0; k < 4; k++) {
				q = c[k][index] / h[index];
				for (int l = 0; l < 4; l++) {
					c[k][l] = c[k][l] - q * h[l];
				}
				b[k] = b[k] - q * h[4];
			}
			for (int k = 0; k < 4; k++) {
				c[j][k] = h[k];
			}
			b[j] = h[4];
			imax[j] = index;
		}
		for (int j = 0; j < 4; j++) {
			index = imax[j];
			a[index] = b[j] / c[j][index];
		}

		// polytrend
		for (int i = 0; i < x.length; i++) {
			xpol = a[3];
			for (int j = m - 1; j >= 0; j--) {
				xpol = xpol * (((i + 1) / fnh) - 1) + a[j];
			}
			y[i] = x[i] - xpol;
		}

		return y;
	}
	
	public static double[] SearchForQuietSections(CalcDataset ds) {
		double gap = ds.safetydistance;					// safety distance = 1.0 dans l'exemple
		int iab = (int)(gap * ds.samplerate);			//iab=nint(gap/dt)
			
		double[] p = new double[ds.nbsamples];
		double[] y = new double[ds.nbsamples];
		double[] x = new double[ds.nbsamples];
		double[] z = new double[ds.nbsamples];
		double[] xk = new double[ds.nbsamples];
		
		for (int i=0; i<ds.nbsamples;i++) {
			x[i] = ds.samplesDeconvolved[i];
			z[i] = 1.;
			p[i] = 0;
			y[i] = 0;
			xk[i] = 0.;
		}
		
		//call krum(x,n,2*iab+1,p)
		
		//subroutine krum(x,n,ng,xk)
		int ng = 2*iab + 1;

		int n = ds.nbsamples;
	    												//  dimension x(n), xk(n)
		int nh = (ng - 1) / 2; 						    //nh = (ng - 1) / 2	    
											    		//  If (ng .lt. 3 .Or. ng .ne. 2 * nh + 1) Then
											  			//   Stop "ng ist keine ungerade Zahl >=3"
											    		//  EndIf
													//c Konstanten
	    double sj = ng * (ng + 1) / 2.; 				// sj = ng * (ng + 1) / 2
	    double sjq = ng * (ng + 1) * (2 * ng + 1) / 6.; //sjq = ng * (ng + 1) * (2 * ng + 1) / 6.
	    											// c Erster Punkt des Krummheitsmasses
	    double sx = 0.;									// sx = 0.
	    double sjx = 0.;								// sjx = 0.
	    for (int j=0;j<ng;j++) {						// do j = 1, ng
	    	sx = sx + x[j];								// sx = sx + x(j)
	    	sjx = sjx + (j-1) * x[j];					// sjx = sjx + j * x(j)
	    }												// enddo
	    double det = ng * sjq - sj*sj;  				// det = ng * sjq - sj ** 2
	    double a = (sx * sjq - sjx * sj) / det;			// a = (sx * sjq - sjx * sj) / det
	    double b = (ng * sjx - sj * sx) / det;			// b = (ng * sjx - sj * sx) / det
	    double var = 0.;								// var = 0.
	    for (int j=0;j<ng;j++) {						// Do j = 1, ng
	    	var = var + Math.pow((x[j] - a - (j+1) * b), 2); // var = var + (x(j) - a - j * b) ** 2
	    }												// enddo
	      
	    xk[0] = Math.sqrt(var / ng); 					// xk(1) = Sqrt(var / ng)
	    for (int j=1; j<nh+1;j++) {					// Do j = 2, nh + 1
	    	xk[j] = xk[0];								// xk(j) = xk(1)
	    }												// enddo
	    												// c Weitere Punkte rekursiv
	    for (int k = nh+1; k<(n-nh);k++) {	    		// Do k = nh + 2, n - nh
	        sjx = sjx - sx + ng * x[nh + k];			//   sjx = sjx - sx + ng * x(nh + k)
	        sx = sx - x[k - nh - 1] + x[k + nh];		//   sx = sx - x(k - nh - 1) + x(k + nh)
	        a = (sx * sjq - sjx * sj) / det; 			//   a = (sx * sjq - sjx * sj) / det
	        b = (ng * sjx - sj * sx) / det;				//   b = (ng * sjx - sj * sx) / det				
	        var = 0.;									//   var = 0.;
	        for (int j = k-nh-1 ; j < k+nh ; j++) { 	//   Do j = k - nh, k + nh
	        	var = var + Math.pow(x[j] - a - (j - k + nh + 1) * b , 2); // var = var + (x(j) - a - (j - k + nh + 1) * b) ** 2
	        }											//   enddo
	        xk[k] = Math.sqrt(var / ng);	 			//   xk(k) = Sqrt(var / ng)	  
	    }												// enddo
	    for (int k = n-nh; k < n ; k++) {				// Do k = n - nh + 1, n
	    	xk[k] = xk[n-nh-1];	  						//  xk(k) = xk(n - nh)
	    }												// enddo
													    // return
													    // end
	    // end krum -> xk = p
	    
	    
	    for (int i = 0; i<n;i++) {						// do i=1,n
	    	p[i]=Math.log10( Math.max(xk[i], 1.e-6) );	//   p(i)=alog10(max(p(i),1.e-6))
	        z[i]=p[i]; 									//   z(i)=p(i)	
	    }												// enddo
	    
	    //  call heapsort(n,z)	replaced by quicksort
	    
	    quicksort(z, 0, n-1);

	    //  end call heapsort(n,z)
	    
	    int n50=(int)(50*n/100.);
	    int n95=(int)(95*n/100.);
	    double zlim = 0.5*(z[n50]+z[n95]); 				// zlim = 0.5*(z(n50)+z(n95))
		for (int i = 0; i<n;i++) {						// do i=1,n
			p[i]=p[i]-zlim;								//   p(i)=p(i)-zlim
			//z[i]=z[i]-zlim;								//   z(i)=z(i)-zlim
			ds.samplesAfterQuickSort[i] = p[i];
		}												// enddo
		
		System.out.println("Threshold = " + Math.pow(10, zlim));
		
	    for (int i=0;i<n;i++) { 						// do 1005 i=1,n
	    	if (p[i] < 0.)	    						// if(p(i).lt.0.) then
	    		z[i] = 1.;								//   z(i)=1.    
	        else										// else
	        	z[i] = 0.;				          		// z(i)=0.
	        											// endif
	    }												// 1005 continue
	    
	    /*double zn1 = z[n-2];							// zn1=z(n-1)
	    double zn  = z[n-1];							// zn=z(n)
	    z[n-2] = -0.4;									// z(n-1)=-0.4
	    z[n-1] = -1.4;									// z(n)=1.4
	    
	    z[n-2] = zn1;									// z(n-1)=zn1
	    z[n-1] = zn;									// z(n)=zn
	    */
		/*for (int i = 0; i<n;i++) {						// do i=1,n
			ds.samplesAfterQuickSort[i] = z[i];
		}*/	

	    return z;
	}
	
	public static double[] DetrendAndIntegrate(double[] in, double[] z, int samplerate, double safetydistance) {
		int n = in.length;
		int pulseCount = 0;
		int quietMin = (int)(safetydistance * samplerate);
		int quietBefore = 6 * quietMin;
		
		double[] steps = new double [100]; 
		double[] y = new double[n];
		double[] in2 = new double[n];
		
		int segmentEnd=0;
		int segmentStart=0;
		int relativeN1=0;
		int relativeN2=0;
		int relativeN3=0;
		int relativeN4=0;
		
		/*for (int i = 0; i<n ; i++) {
			in[i] = in[i] * z[i];
		}*/
			
		int i=1;
		int n1=0, n1a=0;
		int n2=0, n2a=0;
		int n3=0, n3a=0;
		int n4=0, n4a=0;
		
		int pulseLength = 0;
		int segmentLength = 0;
		
		boolean found = false;
		boolean end = false;
		 
		 // Search first quiet interval 
		while ((i<n) && !found) {
			 // first sample of the first quiet interval
			 while ((i<n) && (z[i] < 0.5)) {
				 i++;
			 } 
			 n1=i;
			 
			 // last sample of the first quiet interval = first sample of pulse
			 while ((i<n) && (z[i] > 0.5)) {
				 i++;
			 } 
			 n2=i;
			 
			 // test interval size
			 if (n2 > n1 + quietMin)  {
				 found = true;
			 }
		}
		
		
		 
		while ((i<n) && !end) {
			
			found = false;
			// search next pulse
			while ((i<n) && !found) {
				 // first sample of the next quiet interval = last sample of pulse
				 while ((i<n) && (z[i] < 0.5)) {
					 i++;
				 } 
				 n3=i;
				 
				 // last sample of the first quiet interval = first sample next pulse
				 while ((i<n) && (z[i] > 0.5)) {
					 i++;
				 } 
				 n4=i;
				 
				 // test interval size
				 if (n4 > n3 + quietMin)  {
					 found = true;
				 }
			 }
			 if (i<n) {
				 // get segment of interest
				 segmentStart = Math.max(n1, n2-quietBefore);
				 segmentEnd = Math.min(n4, n3+quietBefore);
				 pulseLength = n3-n2;
				 segmentLength = segmentEnd - segmentStart;
				 System.arraycopy(in, segmentStart, y, 0, segmentLength);
				 relativeN1 = 0;
				 relativeN2 = n2-segmentStart;
				 relativeN3 = n3-segmentStart;
				 relativeN4 = segmentEnd-segmentStart;
				 // Apply correction 
				 ZSpline(y, relativeN1, relativeN2, relativeN3, relativeN4);
				 // Integrate over segment

				 double[] y2 = new double[segmentLength];
				 y2=Integrate(y, samplerate); // sample rate to pass
				 
				 // retrieve and store corresponding average step value
				 steps[pulseCount++] = Steps(y2, relativeN1, relativeN2, relativeN3, relativeN4);
				 
				 /*for (int j = n1; j<segmentStart ; j++) {					 
					 in[j] = in[n1-1];
				 }*/
				 /*for (int j = segmentStart; j<segmentEnd ; j++) {					 
					 in[j] = y2[j-segmentStart];
				 }*/
				 /*for (int j = segmentEnd; j<n4 ; j++) {					 
					 in[j] = in[n1-1] + y2[segmentLength-1];
				 }*/
				
				 
				 n1 = n3;
				 n2 = n4;
			 }
			 
			 if ( (i > n-quietBefore) && (pulseCount >= 100) ) {
				 end = true;
			 }
		}		 
		
		// correct end of buffer
		System.arraycopy(in, n1, y, 0, n-n1);
		// Integrate over segment
		double[] y2 = new double[n-n1];
		y2=Integrate(y, samplerate); // sample rate to pass
		
		/*for (int j = 0; j<n-n1 ; j++) {
			//in[n1 + j] = in[n1-1] + y2[j];
			in[n1 + j] = 0;
		}*/
		
		double[] stepsreturn = new double [pulseCount];
		System.arraycopy(steps, 0, stepsreturn, 0, pulseCount);
		
		return stepsreturn;
		
	}
	
	private static double Steps(double[] x, int n1, int n2, int n3, int n4) {
		double average1 = 0;
		double average2 = 0;
		
		for (int i=n1;i<n2;i++) {
			average1 += x[i] / (n2 - n1);
		}
		
		for (int i=n3;i<n4;i++) {
			average2 += x[i] / (n4 - n3);
		}
			
		return (average2 - average1);

	}
	
	/**
	 * Integrate over buffer
	 * @param x
	 * @param n
	 * @param samplerate
	 */
	private static double[] Integrate(double[] x, double samplerate) {
		double [] y = new double[x.length];
		int n=x.length;
		// rekfl
		double xa  = 0;
		double xaa = 0;
		double ya  = 0;
		double yaa = 0;
		double xn  = 0;
		double yn  = 0;
		
		double f0 = 1. / 2. / samplerate ;
		double f1 = f0;
		double f2 = 0;
		double g1 = 1;
		double g2 = 0;

		for (int j = 0; j < n; j++) {
			xn = x[j];
			yn = f0 * xn + f1 * xa + f2 * xaa + g1 * ya + g2 * yaa;
			y[j] = yn;
			xaa = xa;
			xa = xn;
			yaa = ya;
			ya = yn;
		}
		
		return y;
	}
	
	
	private static double[] Deriv(double[] x, double samplerate) {
		double[] y = new double[x.length];
		int n = x.length;
		double dt = 2. * 1./samplerate; 
				
		for (int i=0;i<n-2;i++) {
			y[i] = (x[i+2] - x[i]) / dt; 
		}
		
		y[n-1] = y[n-2];
		
		return y;
		
	}
	
	private static void ZSpline(double[] in, int n1, int n2, int n3, int n4) {
		double x2 = in[n2];
		double x3 = in[n3];
		double[] segment1, segment3;
		double[] line1 = new double[2];
		double[] line3 = new double[2];
		
		if ( (n1 < n2) && (n2 < n3) && (n3 < n4) ) {
			// Calculate trend line
			segment1 = new double[n2-n1+1];			
			segment3 = new double[n4-n3+1];
			System.arraycopy(in, n1, segment1, 0, n2-n1+1);
			System.arraycopy(in, n3, segment3, 0, n4-n3+1);
			line1 = LeastSquareLine(segment1);
			line3 = LeastSquareLine(segment3);
			
			// Remove trend line for segment 1 and 3 and adjust for segment 2
			for (int i=n1;i<n2;i++) {
				in[i] -= line1[0] + line1[1] * (i-n1);
			}
			for (int i=n2;i<n3;i++) {
				in[i] -= (x2 +(x3-x2)*(i-n2) / (n3-n2)); 
			}
			for (int i=n3;i<n4;i++) {
				in[i] -= line3[0] + line3[1] * (i-n3);
			}
			
			System.arraycopy(in, n1, segment1, 0, n2-n1+1);
			System.arraycopy(in, n3, segment3, 0, n4-n3+1);
			line1 = LeastSquareLine(segment1);
			line3 = LeastSquareLine(segment3);
		}
	}
	
	private static void Trend(double[] in, int n, int n1) {
		double[] segment;
		double[] line = new double[2];
		
		if (n1 < n)  {
			// Calculate trend line
			segment = new double[n-n1];			
			
			System.arraycopy(in, n1, segment, 0, n-n1);			
			line = LeastSquareLine(segment);
			
			// Remove trend line for segment
			for (int i=n1;i<n;i++) {
				in[i] -= line[0] + line[1] * (i-n1);
			}
		}
	}
	
	/**
	 * Get approximation line using least square method
	 * 
	 * @param x		Input buffer
	 * @return		Line coefficients : y = a + b * x => [0] = a, [1] = b 
	 */
	public static double[] LeastSquareLine(double[] y) {
		double[] coeffs = new double[2];
		int n = y.length;
		/*double alpha = 0.5 * n * (n+1);
		double beta = (2*n+1) * (n+1) * n / 6.;
		double det = n * beta - alpha*alpha;
		double sx = 0;
		double sjx = 0;

		
		for (int j = 0 ; j<n ; j++) {
			sx  += x[j];
			sjx += (j+1) * x[j];  
		}
		
		// y = a + b * x
		
		coeffs[0] = (sx * beta - sjx * alpha) / det;	// a
		coeffs[1] = (sjx * n - sx * alpha) / det;		// b
		*/
		
		double[] x = new double[n];
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int j = 0 ; j<n ; j++) {
            x[j] = j;
            sumx  += x[j];
            sumx2 += x[j] * x[j];
            sumy  += y[j];
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        double beta1 = xybar / xxbar;
        double beta0 = ybar - beta1 * xbar;

        // print results
        System.out.println("y   = " + beta1 + " * x + " + beta0);
        
        coeffs[0] = beta0;
        coeffs[1] = beta1;
		
		return coeffs;
				
	}
	
	/**
	 * Quicksort algorithm 
	 * @param a		Input buffer
	 * @param lo	Lower index for segment to sort
	 * @param hi	Higher index for segment to sort
	 */
    private static void quicksort (double[] a, int lo, int hi)
    {
    //  lo is the lower index, hi is the upper index
    //  of the region of array a that is to be sorted
        int i=lo, j=hi;
        double h;
        double x = a[(lo+hi)/2];

        
        //  partition
        do
        {    
            while (a[i]<x) i++; 
            while (a[j]>x) j--;
            if (i<=j)
            {
                h=a[i]; a[i]=a[j]; a[j]=h;
                i++; j--;
            }
        } while (i<=j);

        //  recursion
        if (lo<j) quicksort(a, lo, j);
        if (i<hi) quicksort(a, i, hi);
        
    }

    /**
     * Write 'seife' format file for winplot5
     * 
     * @param buffer
     * @param filename
     * @param format
     * @param dataformat
     */
    public static void WriteFile(double[] buffer, String filename, String format, String dataformat) {
    	try{
    		String strLine="";
            // Create file 
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Toto");
            out.newLine();
            out.write(dataformat);
            out.newLine();
            
            for (int i=0;i<buffer.length;i=i+5) {            	
            	strLine = String.format(format,
            			buffer[i],
            			buffer[i+1],
            			buffer[i+2],
            			buffer[i+3],
            			buffer[i+4] );
                out.write(strLine);
                out.newLine();
            }
            
            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
          System.err.println("Error: " + e.getMessage());
        }
        
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String header="";
		String dataformat="";
	    String strLine="";
	    
		System.out.println("Test Calc class");

		CalcDataset test = new CalcDataset();
		/*
		// Read "seife" data file and fill calcdataset
	      try{
	    	    //FileInputStream fstream = new FileInputStream("dispcal/cal7.dat");
	    	    FileInputStream fstream = new FileInputStream("tiltcal/process.txt");
	    	    DataInputStream in = new DataInputStream(fstream);
	    	    BufferedReader  br = new BufferedReader(new InputStreamReader(in));
	    	    
	    	    strLine = br.readLine();
	    	    header = strLine;
	    	    System.out.println ("Header = " + header);
	    	    
	    	    strLine = br.readLine();	    	    
	    	    dataformat = strLine;
	    	    System.out.println ("Data Format = " + dataformat);
	    	    
        	    StringTokenizer st = new StringTokenizer(strLine);
        	    int nbsamples = Integer.valueOf(st.nextToken());
        	    System.out.println ("nbsamples = " + nbsamples);
        	    String format = st.nextToken();
        	    System.out.println ("format = " + format);
        	    double sampleperiod = Double.valueOf(st.nextToken());
        	    System.out.println ("sampleperiod = " + sampleperiod);
	    	    
        	    // Init dataset with data size
        	    //int nbsamples = 5580;
        	    test.Init(nbsamples);
        	    //test.Init(5580);
        	    //test.Init(34000);
        	    // Fill dataset with samples
        	    int i = 0;
	    	    while (((strLine = br.readLine()) != null) && (i<nbsamples) )  {
	    	    	StringTokenizer tok = new StringTokenizer(strLine);
	    	    	for (int j=0;j<1;j++) {
	    	    	//for (int j=0;j<6;j++) {
	    	    		test.samples[i++] = Integer.valueOf(tok.nextToken());
	    	    	}
	    	    	
	    	    }
	    	    //Close the input stream
	    	    in.close();
    	    }catch (Exception e){//Catch exception if any
    	      System.err.println("Error read: " + e.getMessage());
    	    }
    	    
    	//System.out.printf("\nSENSITIVITY = %d",Math.round(VerticalSensitivity(test)));
    	System.out.printf("\nSENSITIVITY = %d",Math.round(HorizontalSensitivity(test)));
 
    	dataformat = "     12980(5f13.9)                 0.005     0.000     0.000";
    	
    	WriteFile(test.samplesAverageRemoved,"/home/leroy/.wine/drive_c/testcalib/_1_dat.txt","%13.3f%13.3f%13.3f%13.3f%13.3f", dataformat  );
    	WriteFile(test.samplesAfterSez,"/home/leroy/.wine/drive_c/testcalib/_2_sez.txt","%13.0f%13.0f%13.0f%13.0f%13.0f", dataformat  );
    	WriteFile(test.samplesAfterPol,"/home/leroy/.wine/drive_c/testcalib/_3_pol.txt","%13.3f%13.3f%13.3f%13.3f%13.3f", dataformat  );
    	WriteFile(test.samplesAfterQuickSort,"/home/leroy/.wine/drive_c/testcalib/_4_str.txt","%13.3f%13.3f%13.3f%13.3f%13.3f", dataformat  );
    	WriteFile(test.samplesAfterPol2,"/home/leroy/.wine/drive_c/testcalib/_5_pol2.txt","%13.3f%13.3f%13.3f%13.3f%13.3f", dataformat  );
    	WriteFile(test.samplesDetrended,"/home/leroy/.wine/drive_c/testcalib/_6_det.txt","%13.0f%13.0f%13.0f%13.0f%13.0f", dataformat  ); 	
    	WriteFile(test.samplesDetrendedWie,"/home/leroy/.wine/drive_c/testcalib/_6_wie.txt","%13.3f%13.3f%13.3f%13.3f%13.3f", dataformat  );
*/
	}

}

