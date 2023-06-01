package engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class MyGame implements Game {
	public static final float PLAYER_RADIUS = 15f;
	public static final float RAY_LENGTH = 25f;
	
	private Window window;
	private Input input;
	private Player player;
	private World world;
	private Raycast raycast;
	private Vision vision;
	
	public void start(Engine engine) {
		input = engine.getInput();
		window = engine.getWindow();
		player = new Player();
		window.setRenderFunc(g -> render(g));
		window.getCanvas().setBackground(Color.black);
		
		world = new World();
		world.loadFromFile("world.txt");
		
		raycast = new Raycast(world);
		vision = new Vision(raycast);
		
		player.position = world.idToPos(new Vector2i(-2, -2));

	}
	
	public void renderTest(Graphics2D g) {
		g.setColor(Color.black);
		g.fillOval((int)(player.position.x - PLAYER_RADIUS),
				(int)(player.position.y - PLAYER_RADIUS),
				2 * (int)PLAYER_RADIUS,
				2 * (int)PLAYER_RADIUS);
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(3));
		
		Vector2f phi = player.getLookVector().mul(RAY_LENGTH).add(player.position);
		
		g.drawLine((int)player.position.x, (int)player.position.y, (int)phi.x, (int)phi.y);
	}
	
	public void render(Graphics2D g) {
		if (vision == null) return;
		Strip[] strips = vision.cast(player);
		
		for(int i = 0; i < Vision.STRIP_COUNT; i++) {
			Strip strip = strips[i];
			int height = (int)strip.getHeight();
			if (height == 0)
				continue;
			g.setColor(strip.getColor());
			int y0 = (int)(Canvas.HEIGHT / 2 - height / 2);
			g.fillRect((int)(i * Vision.STRIP_WIDTH), y0,
					(int)Vision.STRIP_WIDTH, height);
		}
	}
	
	public void update(float dt) {
		float dx = input.getCoef(Input.D, Input.A) * dt * Player.SPEED;
		float dy = input.getCoef(Input.S, Input.W) * dt * Player.SPEED;
		
		Vector2f vel = Utils.rotateVector(new Vector2f(dx, dy), player.rotation);
		Vector2f xVel = new Vector2f(vel.x, 0);
		Vector2f yVel = new Vector2f(0, vel.y);
		
		xVel = raycast.vectorFreeScale(player.position, xVel, 0.1f);
		yVel = raycast.vectorFreeScale(player.position, yVel, 0.1f);
		
		player.move(xVel.add(yVel));
		
		float rVel = input.getCoef(Input.E, Input.Q) * dt * Player.ROTATION_SPEED;
		player.rotate(rVel);
	}
	
	public void terminate() {
		
	}
}
