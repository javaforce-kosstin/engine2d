package collision;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import engine.Engine;
import engine.Game;
import engine.Input;
import engine.Vector2f;
import engine.Vector2i;
import engine.Window;


class Hook {
	private GameObject object;
	private Vector2f offset;
	
	public Hook(GameObject object, Vector2f mousePos) { 
		this.object = object;
		this.offset = new Vector2f(mousePos.x - object.getPosition().x,
								   mousePos.y - object.getPosition().y);
	}
	
	public GameObject getObject() {
		return object;
	}
	
	public Vector2f getOffset() {
		return offset;
	}
	
	public Vector2f getHookedPoint() {
		return object.getPosition().copy().add(offset);
	}
}


public class TestGame implements Game {
	
	private Hook hook = null;
	private Input input;
	private Window window;
	private World world;
	
	@Override
	public void start(Engine engine) {
		input = engine.getInput();
		window = engine.getWindow();
		window.setRenderFunc(g -> render(g));
		
		world = new World();
		world.addObject(new GameObject(200, 200, 30)).setPosition(new Vector2f(150, 150));
		world.addObject(new GameObject(100, 100, 80)).setPosition(new Vector2f(350, 150));
		world.addObject(new GameObject(100, 100, 80)).setPosition(new Vector2f(500, 150));
		
		world.addObject(new GameObject(800, 30, 100)).setPosition(new Vector2f(400, 0)).togglePhysics(false);
		world.addObject(new GameObject(800, 30, 100)).setPosition(new Vector2f(400, 600)).togglePhysics(false);
		world.addObject(new GameObject(30, 600, 100)).setPosition(new Vector2f(0, 300)).togglePhysics(false);
		world.addObject(new GameObject(30, 600, 100)).setPosition(new Vector2f(800, 300)).togglePhysics(false);

		/*
		
		world.addObject(new GameObject(new Collider(new float[] {
				-50, -50, 50, -75,
				-50, -50, -150, 50,
				70, 80, -150, 50,
				70, 80, 50, -75
			}), new Vector2f(), 50)).setPosition(new Vector2f(280, 400)).togglePhysics(false);
		
		world.addObject(new GameObject(new Collider(new float[] {
				-50, -50, 0, -80,
				0, -80, 60, 0,
				60, 0, 30, 40,
				30, 40, -50, 20,
				-50, 20, -50, -50
			}), new Vector2f(), 50)).setPosition(new Vector2f(600, 400)).togglePhysics(false);
		
		world.addObject(new GameObject(new Collider(new float[] {
				0, -50, -20, -10,
				-20, -10, 20, -10,
				20, -10, 0, -50
			}), new Vector2f(100, 100), 10)).setPosition(new Vector2f(750, 400));*/
		
		world.addObject(new GameObject(new Collider(new float[] {
				0, 200, -200, 100
			}), new Vector2f(100, 100), 20)).setPosition(new Vector2f(400, 250)).togglePhysics(false);
		
	}
	
	private void drawHook(Graphics2D g) {
		if (hook == null) return;
		
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2));
		
		Vector2i p1 = hook.getHookedPoint().toInt();
		Vector2i p2 = input.getMousePosition();
		
		if (p2 == null) return;
		
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
	
	private void drawWorld(Graphics2D g) {
		if (world == null) return;
		world.render(g);
	}

	private void render(Graphics2D g) {
		drawWorld(g);
		drawHook(g);
	}
	
	private void updateHook(float dt) {
		Vector2i mousePos = input.getMousePosition();
		
		if (input.isMouseUp(Input.MOUSE_LEFT) || mousePos == null) {
			hook = null;
			return;
		}
		else if (input.isMouseDown(Input.MOUSE_LEFT) && mousePos != null) {
			GameObject c = world.catchObject(mousePos.toFloat());
			if (c == null || !c.isKinematic()) return;
			hook = new Hook(c, mousePos.toFloat());
		}
		
		if (hook == null) return;
		
		Vector2f hp = hook.getHookedPoint();
		Vector2f deltaVel = new Vector2f(mousePos.x - hp.x, mousePos.y - hp.y);
		deltaVel.mul(dt);
		
		hook.getObject().addVelocity(deltaVel);
	}
	
	@Override
	public void update(float dt) {
		updateHook(dt);
		world.update(dt);
	}
	

	@Override
	public void terminate() {
		
	}

}
