package calc;

public class CalcDataset {

	/** 
	 * Structure 
	 * */
	
	public int nbsamples = 0;
	public int samplerate = 200;
	public long[] times;
	public double[] samples;
	public double[] samplesAverageRemoved;
	public double[] samplesAfterSez;
	public double[] samplesAfterPol;
	public double[] samplesQuietness;
	public double[] samplesDeconvolved;
	public double[] samplesAfterQuickSort;
	public double[] samplesAfterPol2;
	public double[] samplesDetrended;
	public double[] samplesDetrendedWie;
	public double[] samplesResidual;

	public String axis = "HHZ";
	public double freeperiod = 120;
	public double damping = 0.707;
	public double uVperCount = 0.596045970916748;
	public double bits = 26;
	public double volts = 40;
	public double displacement = 0.182;
	public double safetydistance = 0.5;

	public int nbsamplesaverage = 1200;

	
	/** 
	 * Constructor 
	 * */
	
	public CalcDataset() {
		this.Init(200);
	}
	
	
	/** 
	 * Initialization 
	 * */

	public void Init(int nbs) {

		this.nbsamplesaverage = 6 * this.samplerate; // 6 seconds for average calc
		this.safetydistance = 0.5;
		this.nbsamples = nbs;

		this.times 			       = new long[this.nbsamples];
		this.samples 			   = new double[this.nbsamples];
		this.samplesAverageRemoved = new double[this.nbsamples];
		this.samplesAfterSez       = new double[this.nbsamples];
		this.samplesAfterPol       = new double[this.nbsamples];
		this.samplesQuietness      = new double[this.nbsamples];
		this.samplesDeconvolved    = new double[this.nbsamples];
		this.samplesAfterQuickSort = new double[this.nbsamples];
		this.samplesAfterPol2      = new double[this.nbsamples];
		this.samplesDetrendedWie   = new double[this.nbsamples];
		this.samplesDetrended      = new double[this.nbsamples];
		this.samplesResidual       = new double[this.nbsamples];
	}

}
