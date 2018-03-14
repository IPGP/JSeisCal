package Console;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
    
public class Console extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	PipedInputStream piOut;
    PipedInputStream piErr;
    PipedOutputStream poOut;
    PipedOutputStream poErr;
    JTextArea textArea = new JTextArea();

    public Console() throws IOException {
        // Set up System.out
        piOut = new PipedInputStream();
        poOut = new PipedOutputStream(piOut);
        //System.setOut(new PrintStream(poOut, true));

        // Set up System.err
        piErr = new PipedInputStream();
        poErr = new PipedOutputStream(piErr);
        //System.setErr(new PrintStream(poErr, true));

        // Add a scrolling text area
        textArea.setEditable(false);
        textArea.setRows(10);
        textArea.setColumns(20);
        //textArea.setFont(new Font("SansSerif",	Font.PLAIN, 8));
        /*textArea.setRows(5);
        textArea.setColumns(50);*/
        textArea.setLineWrap(true);

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        // Create reader threads
        new ReaderThread(piOut).start();
        //new ReaderThread(piErr).start();
        setBorder(new TitledBorder(LineBorder.createGrayLineBorder(),"Console"));
    }
    
    public void write(String str) {
    	this.textArea.append(str+"\n");
    	this.textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    class ReaderThread extends Thread {
        PipedInputStream pi;

        ReaderThread(PipedInputStream pi) {
            this.pi = pi;
        }

        public synchronized void run() {
            final byte[] buf = new byte[1024];
            try {
                while (true) {
                    final int len = pi.read(buf);
                    if (len == -1) {
                        break;
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            textArea.append(new String(buf, 0, len));

                            // Make sure the last line is always visible
                            textArea.setCaretPosition(textArea.getDocument().getLength());

                            // Keep the text area down to a certain character size
                         /*   int idealSize = 1000;
                            int maxExcess = 500;
                            int excess = textArea.getDocument().getLength() - idealSize;
                            if (excess >= maxExcess) {
                                textArea.replaceRange("", 0, excess);
                            }*/
                        }
                    });
                }
            } catch (IOException e) {
            }
        }
    }
}
