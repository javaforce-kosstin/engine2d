package engine;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.Set;
import java.awt.Point;

import javax.swing.JFrame;

import java.util.HashMap;



class InputUnit {
	private boolean pressed = false;
	private boolean down = false;
	private boolean up = false;
	
	public InputUnit() { }
	
	public void update(boolean state) {
		down = state && !pressed;
		up = !state && pressed;
		pressed = state;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public boolean isDown() {
		return down;
	}
	
	public boolean isUp() {
		return up;
	}
}


class InputHandler {
	private Map<Integer, InputUnit> units = new HashMap<>();
	private Map<Integer, Boolean> states = new HashMap<>();
	
	public boolean getState(int code) {
		if (!states.containsKey(code))
			states.put(code, false);
		return states.get(code);
	}
	
	protected void setState(int code, boolean state) {
		states.put(code, state);
	}
	
	private InputUnit getUnit(int code) {
		InputUnit unit = units.get(code);
		if (unit == null) {
			unit = new InputUnit();
			units.put(code, unit);
		}
		return unit;
	}
	
	public boolean isUp(int code) {
		return getUnit(code).isUp();
	}
	
	public boolean isDown(int code) {
		return getUnit(code).isDown();
	}
	
	public boolean isPressed(int code) {
		return getUnit(code).isPressed();
	}
	
	public void update() {
		for (int code : states.keySet())
			getUnit(code).update(getState(code));
			
	}
	
}


class MouseHandler extends InputHandler implements MouseListener {
	private Canvas canvas;
	
	public MouseHandler(Window window) {
		canvas = window.getCanvas();
	}
	
	public Vector2i getPosition() {
		Point p = canvas.getMousePosition();
		if (p == null) return null;
		return new Vector2i(p.x, p.y);
	}
	
	public boolean isInside() {
		return canvas.getMousePosition() != null;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		setState(e.getButton(), true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setState(e.getButton(), false);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}



class KeyHandler extends InputHandler implements KeyListener {
	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		setState(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setState(e.getKeyCode(), false);
	}
}


public class Input {
	public static final int MOUSE_LEFT = MouseEvent.BUTTON1;
	public static final int MOUSE_MIDDLE = MouseEvent.BUTTON2;
	public static final int MOUSE_RIGHT = MouseEvent.BUTTON3;
	
	public static final int A = KeyEvent.VK_A;
	public static final int B = KeyEvent.VK_B;
	public static final int C = KeyEvent.VK_C;
	public static final int D = KeyEvent.VK_D;
	public static final int E = KeyEvent.VK_E;
	public static final int F = KeyEvent.VK_F;
	public static final int G = KeyEvent.VK_G;
	public static final int H = KeyEvent.VK_H;
	public static final int I = KeyEvent.VK_I;
	public static final int J = KeyEvent.VK_J;
	public static final int K = KeyEvent.VK_K;
	public static final int L = KeyEvent.VK_L;
	public static final int M = KeyEvent.VK_M;
	public static final int N = KeyEvent.VK_N;
	public static final int O = KeyEvent.VK_O;
	public static final int P = KeyEvent.VK_P;
	public static final int Q = KeyEvent.VK_Q;
	public static final int R = KeyEvent.VK_R;
	public static final int S = KeyEvent.VK_S;
	public static final int T = KeyEvent.VK_T;
	public static final int U = KeyEvent.VK_U;
	public static final int V = KeyEvent.VK_V;
	public static final int W = KeyEvent.VK_W;
	public static final int X = KeyEvent.VK_X;
	public static final int Y = KeyEvent.VK_Y;
	public static final int Z = KeyEvent.VK_Z;
	
	public static final int SHIFT = KeyEvent.VK_SHIFT;
	public static final int SPACE = KeyEvent.VK_SPACE;
	
	private final KeyHandler keyHandler;
	private final MouseHandler mouseHandler;
	
	
	public Input(Window window) {
		keyHandler = new KeyHandler();
		mouseHandler = new MouseHandler(window);
		window.addKeyListener(keyHandler);
		window.addMouseListener(mouseHandler);
	}
	
	public boolean isKeyDown(int code) {
		return keyHandler.isDown(code);
	}
	
	public boolean isKeyUp(int code) {
		return keyHandler.isUp(code);
	}
	
	public boolean isKeyPressed(int code) {
		return keyHandler.isPressed(code);
	}
	
	public boolean isMouseDown(int code) {
		return mouseHandler.isDown(code);
	}
	
	public boolean isMouseUp(int code) {
		return mouseHandler.isUp(code);
	}
	
	public boolean isMousePressed(int code) {
		return mouseHandler.isPressed(code);
	}
	
	public float getCoef(int posCode, int negCode) {
		float coef = 0;
		if (isKeyPressed(posCode))
			coef += 1;
		if (isKeyPressed(negCode))
			coef -= 1;
		return coef;
	}
	
	public void update() {
		keyHandler.update();
		mouseHandler.update();
	}
	
	public Vector2i getMousePosition() {
		return mouseHandler.getPosition();
	}
	
	public boolean isMouseInside() {
		return mouseHandler.isInside();
	}
	
}
