package ui;

import javax.swing.*;

import handlers.UITheme;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class TerminalPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextArea outputArea;
    private JTextField inputField;

    public TerminalPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 120));
        setBorder(BorderFactory.createEmptyBorder());
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(0, 12, 27));
        outputArea.setForeground(Color.white);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        UITheme.windows();
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        inputField = new JTextField();
        inputField.addActionListener(e -> runCommand(inputField.getText()));
        inputField.setBorder(BorderFactory.createEmptyBorder());
        inputField.setPreferredSize(new Dimension(1000, 20));
        
        UITheme.crossPlatform();
        
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        appendOutput("<Command Terminal>\n");
    }

    private void runCommand(String input) {
        inputField.setText("");
        appendOutput("\n> " + input + "\n");

        if (input.equals("exit")) {
            appendOutput("Exiting...\n");
            System.exit(0);
            return;
        }

        // Use ProcessBuilder to run system commands
        try {
            List<String> command = splitCommand(input);
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File(System.getProperty("user.dir")));

            Process process = builder.start();

            // Read output
            readStream(process.getInputStream(), false);
            readStream(process.getErrorStream(), true);

            process.waitFor();
        } catch (Exception ex) {
            appendOutput("Error: " + ex.getMessage() + "\n");
        }
    }

    private void readStream(InputStream stream, boolean isError) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    appendOutput((isError ? "[error] " : "") + line + "\n");
                }
            } catch (IOException ex) {
                appendOutput("Stream error: " + ex.getMessage() + "\n");
            }
        }).start();
    }

    private void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text));
    }

    private List<String> splitCommand(String input) {
        // Basic splitting by space (does not handle quotes)
        List<String> result = new ArrayList<>();
        for (String part : input.trim().split("\\s+")) {
            if (!part.isEmpty()) result.add(part);
        }
        return result;
    }


}
