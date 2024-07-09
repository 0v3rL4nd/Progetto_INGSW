
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.*;

import command.HistoryCommandHandler;
import interpreter.CommandInterpreter;
import shapes.controller.GraphicObjectController;
import shapes.model.AbstractGraphicObject;
import shapes.model.CircleObject;
import shapes.model.ImageObject;
import shapes.model.RectangleObject;
import shapes.view.*;

public class TestGraphics2 {

	public static void main(String[] args) {

		JFrame f = new JFrame();

		JToolBar toolbar = new JToolBar();

		JButton undoButt = new JButton("Undo");
		JButton redoButt = new JButton("Redo");

		final HistoryCommandHandler handler = new HistoryCommandHandler();

		undoButt.addActionListener(evt -> handler.undo());

		redoButt.addActionListener(evt -> handler.redo());

		toolbar.add(undoButt);
		toolbar.add(redoButt);

		final GraphicObjectPanel gpanel = new GraphicObjectPanel();

		gpanel.setPreferredSize(new Dimension(400, 400));

		GraphicObjectViewFactory.FACTORY.installView(RectangleObject.class, new RectangleObjectView());
		GraphicObjectViewFactory.FACTORY.installView(CircleObject.class, new CircleObjectView());
		GraphicObjectViewFactory.FACTORY.installView(ImageObject.class, new ImageObjectView());

		AbstractGraphicObject go = new RectangleObject(new Point(180, 80), 20, 50);

		JButton rectButton = new JButton(new CreateObjectAction(go, gpanel, handler));
		rectButton.setText(go.getType());
		toolbar.add(rectButton);

		go = new CircleObject(new Point(200, 100), 10);
		JButton circButton = new JButton(new CreateObjectAction(go, gpanel, handler));
		circButton.setText(go.getType());
		toolbar.add(circButton);

		go = new CircleObject(new Point(200, 100), 100);
		JButton circButton2 = new JButton(new CreateObjectAction(go, gpanel, handler));
		circButton2.setText("big " + go.getType());
		toolbar.add(circButton2);

		go = new ImageObject(new ImageIcon(Objects.requireNonNull(TestGraphics2.class.getResource("shapes/model/NyaNya.gif"))),
				new Point(240, 187));

		JButton imgButton = new JButton(new CreateObjectAction(go, gpanel, handler));
		imgButton.setText(go.getType());
		toolbar.add(imgButton);

		final GraphicObjectController goc = new GraphicObjectController(handler);

		gpanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				goc.setControlledObject(gpanel.getGraphicObjectAt(e.getPoint()));
			}
		});

		f.add(toolbar, BorderLayout.NORTH);
		f.add(new JScrollPane(gpanel), BorderLayout.CENTER);

		JPanel controlPanel = new JPanel(new FlowLayout());

		controlPanel.add(goc);
		f.setTitle("Shapes");
		f.getContentPane().add(controlPanel, BorderLayout.SOUTH);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);

	}


	public static class InterpreterGUI extends JFrame {
		private JTextArea commandInput;
		private JTextArea outputArea;
		private CommandInterpreter interpreter;

		public InterpreterGUI(CommandInterpreter interpreter) {
			this.interpreter = interpreter;
			setupUI();
		}

		private void setupUI() {
			setTitle("MINI-CAD Interpreter");
			setSize(600, 400);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			commandInput = new JTextArea(5, 50);
			outputArea = new JTextArea(15, 50);
			outputArea.setEditable(false);

			JButton executeButton = new JButton("Execute");
			executeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String command = commandInput.getText();
					String result = interpreter.executeCommand(command);
					outputArea.append(command + "\n" + result + "\n\n");
					commandInput.setText("");
				}
			});

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(new JScrollPane(commandInput), BorderLayout.NORTH);
			panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
			panel.add(executeButton, BorderLayout.SOUTH);

			add(panel);

				HistoryCommandHandler handler = new HistoryCommandHandler();
				GraphicObjectController controller = new GraphicObjectController(handler);
				CommandInterpreter interpreter = new CommandInterpreter(handler, controller);
				InterpreterGUI gui = new InterpreterGUI(interpreter);
				gui.setVisible(true);

		}

	}
}