import java.awt.Font;

import javax.swing.UIManager;


public class jSeisCal {

	public static void reset() {
		jSeisCalModel      model      = new jSeisCalModel();
		jSeisCalView       view       = new jSeisCalView(model);
		jSeisCalController controller = new jSeisCalController(model, view);

		view.setVisible(true);
	}

	public static void main(String[] args) {
		reset();

	}
}
