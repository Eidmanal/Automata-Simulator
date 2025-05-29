package ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import handlers.UITheme;
import objects.Vertex;

@SuppressWarnings("serial")
public class TransitionWindow extends JDialog {
	boolean closing = false;

	public TransitionWindow() {
		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
		UITheme.windows();

		JPanel[] panels = new JPanel[3];

		for (int i = 0; i < 3; i++) {
			panels[i] = new JPanel();
			panels[i].setLayout(new GridLayout(3, 3, 5, 5));
			panels[i].setBorder(new EmptyBorder(3, 3, 3, 3));
		}

		panels[2].setLayout(new GridBagLayout());

		ArrayList<String> vertices = new ArrayList<>();

		for (Vertex vertex : ObjectPanel.vertices) {
			vertices.add(vertex.getData());
		}

		JComboBox<String> vertexBox = new JComboBox<>(vertices.toArray(new String[0]));
		panels[0].add(new Label("From: "));
		panels[0].add(vertexBox);

		JComboBox<String> endVertexBox = new JComboBox<>(vertices.toArray(new String[0]));
		panels[0].add(new Label("To: "));
		panels[0].add(endVertexBox);

		JTextField input = new JTextField(8);
		JTextField output = new JTextField(8);

		panels[1].add(new Label("Input: "));
		panels[1].add(input);

		panels[1].add(new Label("Output: "));
		panels[1].add(output);

		JButton done = new JButton("Add");
		done.setPreferredSize(new Dimension(200, 25));
		panels[2].add(done);

		for (int i = 0; i < 3; i++)
			add(panels[i]);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				requestFocusInWindow();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				if (!closing)
					Toolkit.getDefaultToolkit().beep();

				SwingUtilities.invokeLater(() -> {
					toFront();
					requestFocus();
				});
			}

			@Override
			public void windowClosing(WindowEvent e) {
				closing = true;
			}

		});

		done.addActionListener(e -> {
			dispose();
		});

		UITheme.crossPlatform();
		setTitle("New Transition");
		setLayout(new GridLayout(3, 1, 5, 5));
		setSize(400, 300);
		setIconImage(icon);
		setAlwaysOnTop(!true);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
