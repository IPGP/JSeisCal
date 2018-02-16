import java.util.Timer;
import java.util.TimerTask;


public class TableBangs {

	  Timer t;
	  int nbrBangs;
	  ModBus tableMB;

	  public TableBangs(ModBus table, int period, int counts) {
		  tableMB = table;
		  nbrBangs = counts;
		  t = new Timer();
		  t.schedule(new Bang(), period*1000, period*1000);
	  }
	  
	  public void stop() {
		  t.cancel();
	  }

	  class Bang extends TimerTask {

	    public void run() {
	      if (nbrBangs > 0) {
	        tableMB.push();
	      } else {
	        System.out.println("End !");
	        t.cancel();
	        }
	    }
	    
	  }
	  
	  
	  public static void main(String[] args) {
		  ModBus table = new ModBus();
		  TableBangs tb = new TableBangs(table, 1, 30);
		  
		  
	  }

}

