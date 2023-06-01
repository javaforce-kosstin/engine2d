package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;


class Canvas extends JPanel {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private RenderFunc renderFunc;
	
	Canvas() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void setBackground(int r, int g, int b) {
		this.setBackground(new Color(r, g, b));
	}
	
	public void setRenderFunc(RenderFunc paintFunc) {
		this.renderFunc = paintFunc;
	}
	
	public void update() {
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if (renderFunc == null) return;
		renderFunc.action((Graphics2D)g);
		g.dispose();
	}
	
}



public class Window extends JFrame {
	private final Canvas canvas;
	private final Input input;
	
	Window() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		
		canvas = new Canvas();
		this.add(canvas);
		
		this.pack();
		
		input = new Input(this);
	}
	
	public Input getInput() {
		return input;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void setRenderFunc(RenderFunc r) {
		canvas.setRenderFunc(r);
	}
	
	public void update() {
		input.update();
		canvas.update();
	}
}
