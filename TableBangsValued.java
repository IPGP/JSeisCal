import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import jlib330.DataChannel;


public class TableBangsValued  extends TimerTask implements Observer{

	  Timer t;
	  int nbrBangs = 0;
	  ModBus tableMB;
	  DataChannel dc;
	  boolean flagIncrease = false;
	  boolean flagFirstPass = true;
	  double threshold = 16777216;

	  public TableBangsValued(ModBus table, int period, int counts, DataChannel dataChannel, double thresh) {
		  dc = dataChannel;
		  dc.addObserver(this);
		  tableMB = table;
		  nbrBangs = counts;
		  threshold = thresh;
		  
		  Timer timer = new Timer();
		  
		  timer.schedule(this, 10000);
		
	  }
	  
	  public void stop() {
		  tableMB.ForceCoil(1, false);
		  dc.deleteObserver(this);
	  }
	 

		public void update(Observable arg0, Object arg1) {
			if (nbrBangs > 0) {
				if (flagFirstPass) {
					if ( (dc.samples[dc.index-1] >= threshold) ) {
						tableMB.push();
						flagIncrease = false;
						flagFirstPass = false;
						//nbrBangs--;
					} else if ( (dc.samples[dc.index-1] <= -threshold) ) {
						tableMB.push();
						flagIncrease = true;
						flagFirstPass = false;
						//nbrBangs--;
					}
				}else {
					if ( (dc.samples[dc.index-1] >= threshold) && flagIncrease ) {
						flagIncrease = false;
						tableMB.push();
						//nbrBangs--;
					} else if ( (dc.samples[dc.index-1] <= -threshold) && !flagIncrease ) {
						flagIncrease = true;
						tableMB.push();
						//nbrBangs--;
					}
				}
			}
			
			
		}
		
		
	  public static void main(String[] args) {
		  ModBus table = new ModBus();
		  TableBangs tb = new TableBangs(table, 1, 30);
	  }

	@Override
	public void run() {
		tableMB.ForceCoil(1, true);
	}



}

