package collision;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import engine.Utils;
import engine.Vector2f;


public class GameObject {
	private final float width, height, mass;
	private Vector2f velocity = new Vector2f();
	private boolean kinematic = true;
	private Collider collider;
	private World world;
	
	public GameObject(float width, float height, float mass) {
		this.width = width;
		this.height = height;
		this.mass = mass;
		
		float hW = width / 2;
		float hH = height / 2;
		
		collider = new Collider(new float[] {
			-hW, -hH, hW, -hH, // top
			-hW, hH, hW, hH, // bottom
			-hW, -hH, -hW, hH, // left
			hW, -hH, hW, hH // right
		});
	}
	
	public GameObject(Collider collider, Vector2f hitBox, float mass) {
		this.width = hitBox.x;
		this.height = hitBox.y;
		this.mass = mass;
		this.collider = collider;
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public GameObject freeMove(Vector2f v) {
		collider.move(v);
		return this;
	}
	
	public GameObject setPosition(Vector2f p) {
		collider.setPosition(p);
		return this;
	}
	
	public Vector2f getPosition() {
		return collider.getPosition();
	}
	
	public GameObject togglePhysics(boolean state) {
		kinematic = state;
		return this;
	}
	
	public boolean containsPoint(Vector2f point) {
		Vector2f pos = getPosition();
		return point.x <= pos.x + width / 2 && point.x >= pos.x - width / 2 &&
				point.y <= pos.y + height / 2 && point.y >= pos.y - height / 2;
	}
	
	private void moveAlpha(float vel, int dim, Set<Collision> col) {
		Vector2f t = (new Vector2f()).setDim(dim, 1);
		ThetaInfo thetaA, thetaB;
		
		float a = -Collider.PREVENT;
		float b = Collider.PREVENT;
		
		if (vel < 0) a += vel;
		else b += vel;
		
		thetaA = collider.getTheta(t.copy().mul(a));
		thetaB = collider.getTheta(t.copy().mul(b));
		
		float total = Utils.drop(thetaA.mul(a) + thetaB.mul(b), Collider.DROP * vel);
		
		freeMove(t.copy().mul(total));
		
		if (thetaA.theta < 1)
			col.add(new Collision(this.collider, thetaA.collider, dim));
		if (thetaB.theta < 1)
			col.add(new Collision(this.collider, thetaB.collider, dim));
	}
	
	public Collision[] move(Vector2f v) {
		if (v.x == 0 && v.y == 0) return new Collision[0];
		
		Set<Collision> col = new HashSet<>();
		
		moveAlpha(v.x, 0, col);
		moveAlpha(v.y, 1, col);
		
		return col.stream().toArray(Collision[]::new);
	}
	
	public void addVelocity(Vector2f v) {
		velocity.add(v);
		velocity.x = Utils.clamp(velocity.x,
				-Physics.MAX_VELOCITY, Physics.MAX_VELOCITY);
		velocity.y = Utils.clamp(velocity.y,
				-Physics.MAX_VELOCITY, Physics.MAX_VELOCITY);
	}
	
	public float getMass() {
		return mass;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2f v) {
		velocity.set(v);
	}
	
	public Collider getCollider() {
		return collider;
	}
	
	public void render(Graphics2D g) {
		collider.render(g);
	}
	
	private void updateGravity(float dt) {
		float gravity = dt * mass * Physics.g;
		addVelocity(new Vector2f(0, gravity));
	}
	
	public float getEnergy() {
		return mass * (float)Math.pow(velocity.length(), 2) / 2;
	}
	
	private void updateAirResistance(float dt) {
		if (velocity.x == 0 && velocity.y == 0)
			return;
		float resistance = dt * Physics.AIR_RESISTANCE;
		float k = Utils.clamp(1 - resistance / velocity.length(), 0f, 1f);
		velocity.mul(k);
	}
	
	public void update(float dt) {
		if (kinematic)
			updateGravity(dt);
		updateAirResistance(dt);

		Collision[] cols = move(velocity);
		
		for (Collision col : cols) {
			col.identify(world);
			if (!col.isKinematic())
				Physics.staticCollision(col);
		}
		
		for (Collision col : cols)
			if (col.isKinematic())
				Physics.kinematicCollision(col);
	}
	
	public boolean isKinematic() {
		return kinematic;
	}
}
