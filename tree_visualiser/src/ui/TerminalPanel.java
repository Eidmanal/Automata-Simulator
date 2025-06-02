package ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import handlers.UITheme;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class TerminalPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextPane outputArea;
    private JTextField inputField;

    public TerminalPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 120));
        setBorder(BorderFactory.createEmptyBorder());
        
        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(10, 20, 27));
        outputArea.setForeground(Color.white);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        UITheme.darkTheme();
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        inputField = new JTextField();
        inputField.addActionListener(e -> runCommand(inputField.getText()));
        inputField.setBorder(BorderFactory.createEmptyBorder());
        inputField.setPreferredSize(new Dimension(1000, 20));
        
        UITheme.crossPlatform();
        
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        appendOutput("<Command Terminal>\n", Color.WHITE);
    }

    private void runCommand(String input) {
        inputField.setText("");
        appendOutput("\n> ", new Color(180, 85, 10));
        appendOutput(input + "\n", Color.WHITE);

        if (input.equals("exit")) {
            appendOutput("Exiting...\n", new Color(20, 85, 130));
            System.exit(0);
            return;
        }

        // Use ProcessBuilder to run system commands
        try {
        	List<String> command = new ArrayList<>();
        	if (System.getProperty("os.name").toLowerCase().contains("win")) {
        	    command.add("cmd.exe");
        	    command.add("/c");
        	} else {
        	    command.add("bash");
        	    command.add("-c");
        	}
        	command.add(input);
        	ProcessBuilder builder = new ProcessBuilder(command);

            builder.directory(new File(System.getProperty("user.dir")));

            Process process = builder.start();

            // Read output
            readStream(process.getInputStream(), false);
            readStream(process.getErrorStream(), true);

            process.waitFor();
        } catch (Exception ex) {
            appendOutput("Error: " + ex.getMessage() + "\n", Color.RED);
        }
    }

    private void readStream(InputStream stream, boolean isError) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    appendOutput(line + "\n", isError ? Color.RED : Color.WHITE);
                }
            } catch (IOException ex) {
                appendOutput("Stream error: " + ex.getMessage() + "\n", Color.RED);
            }
        }).start();
    }


    private void appendOutput(String text, Color color) {
        SwingUtilities.invokeLater(() -> {
            StyledDocument doc = outputArea.getStyledDocument();
            Style style = outputArea.addStyle("style", null);
            StyleConstants.setForeground(style, color);
            try {
                doc.insertString(doc.getLength(), text, style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }




}
