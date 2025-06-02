package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import objects.MenuButton;

public class SideBar extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	Timer timer;
	ArrayList<MenuButton> buttons = new ArrayList<>();

	int sideBarWidth = 200;

	public SideBar() {
		timer = new Timer(0, this);
		timer.start();

		Dimension size = new Dimension(sideBarWidth, getHeight());
		setPreferredSize(size);

		addMouseListener(this);
		addMouseMotionListener(this);

		initButtons();
	}

	public void initButtons() {

		buttons.add(new MenuButton("DFA"));
		buttons.add(new MenuButton("Mealy"));
		buttons.add(new MenuButton("Moore"));

		for (MenuButton button : buttons) {
			button.setX(sideBarWidth / 2 - button.getWidth() / 2);
			button.setY(buttons.indexOf(button) * button.getHeight());
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(new Color(20, 26, 30));
		g.fillRect(0, 0, getWidth(), getHeight());

		for (MenuButton button : buttons)
			button.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton button : buttons)
			if (button.isIn(e.getX(), e.getY()))
				button.click();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton button : buttons)
			if (button.isIn(e.getX(), e.getY()))
				button.hover();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		for (MenuButton button : buttons)
			if (button.isIn(e.getX(), e.getY()))
				button.hover();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		for (MenuButton button : buttons)
			button.deactivate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();

		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton button : buttons)
			if (button.isIn(e.getX(), e.getY()))
				button.hover();
			else
				button.deactivate();
	}

}
