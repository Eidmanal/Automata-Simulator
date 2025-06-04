package ui;

import javax.swing.*;

import handlers.UITheme;

import java.awt.*;

public class ExportDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public static class ExportOptions {
		public String format;
		public Color backgroundColor;
		public boolean transparent;

		public ExportOptions(String format, Color backgroundColor, boolean transparent) {
			this.format = format;
			this.backgroundColor = backgroundColor;
			this.transparent = transparent;
		}
	}

	private JComboBox<String> formatCombo;
	private JCheckBox transparencyBox;
	private JColorChooser colorChooser;
	private boolean confirmed = false;
	private ExportOptions options;

	public ExportDialog(Frame owner) {
		super(owner, "Export Workspace", true);
		UITheme.darkTheme();
		setLayout(new BorderLayout());

		String[] formats = {"png", "jpg", "bmp", "gif"};
		formatCombo = new JComboBox<>(formats);
		transparencyBox = new JCheckBox("Transparent Background");
		colorChooser = new JColorChooser(Color.WHITE);

		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(new JLabel("Format:"));
		topPanel.add(formatCombo);
		topPanel.add(transparencyBox);

		add(topPanel, BorderLayout.NORTH);
		add(colorChooser, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton exportButton = new JButton("Export");
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(exportButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);

		// Enable/disable transparency checkbox based on format
		formatCombo.addActionListener(e -> {
			String format = (String) formatCombo.getSelectedItem();
			transparencyBox.setEnabled("png".equalsIgnoreCase(format));
			if (!"png".equalsIgnoreCase(format)) {
				transparencyBox.setSelected(false);
			}
		});

		exportButton.addActionListener(e -> {
			confirmed = true;
			options = new ExportOptions(
					(String) formatCombo.getSelectedItem(),
					colorChooser.getColor(),
					transparencyBox.isSelected()
			);
			setVisible(false);
		});

		cancelButton.addActionListener(e -> {
			confirmed = false;
			setVisible(false);
		});

		pack();
		setLocationRelativeTo(owner);
	}

	public ExportOptions getOptions() {
		return options;
	}

	public boolean isConfirmed() {
		return confirmed;
	}
}
