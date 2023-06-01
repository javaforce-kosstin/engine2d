package engine;

public class Player {
	public static final float SPEED = 70f;
	public static final float ROTATION_SPEED = 5f;
	
	public Vector2f position = new Vector2f();
	public float rotation = 0;
	
	public Player() {
		
	}
	
	public Vector2f relativeForward() {
		return new Vector2f(0, -1);
	}
	
	public Vector2f getLookVector() {
		return Utils.rotateVector(relativeForward(), rotation);
	}
	
	public void translate(Vector2f v) {
		translate(v.x, v.y);
	}
	
	public void translate(float x, float y) {
		Vector2f vel = Utils.rotateVector(x, y, rotation);
		move(vel);
	}
	
	public void move(Vector2f v) {
		position.add(v);
	}
	
	public void move(float x, float y) {
		position.add(x, y);
	}
	
	public void rotate(float phi) {
		rotation = Utils.normalizeAngle(rotation + phi);
	}
}
