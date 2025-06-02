package main;

import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.Frame;

public class LoadScreen extends Frame {
	private static final long serialVersionUID = 1L;

	public LoadScreen() {
		
		setSize(666, 388);
		add(new BackgroundPanel("/loading_screen.png"));
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		openWindow();
	}

	private void openWindow() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dispose();
		new Window();
	}
	
}

@SuppressWarnings("serial")
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/loading_screen.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOpaque(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(this), backgroundImage.getHeight(this), this);
        }
    }
}

