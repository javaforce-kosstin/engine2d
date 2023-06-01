package collision;

import java.awt.Graphics2D;
import engine.Utils;
import engine.Vector2f;


public class LineSegment {
	public final float a, b;
	public final Vector2f p1, p2;
	public float c;
	
	public LineSegment(Vector2f p1, Vector2f p2) {
		a = p1.y - p2.y;
		b = p2.x - p1.x;
		this.p1 = p1;
		this.p2 = p2;
		updateConst();
	}
	
	private void updateConst() {
		c = p1.x * p2.y - p2.x * p1.y;
	}
	
	public void render(Graphics2D g) {
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
	}
	
	public void move(Vector2f v) {
		p1.x += v.x;
		p2.x += v.x;
		p1.y += v.y;
		p2.y += v.y;
		updateConst();
	}
	
	public LineSegment rotate(float phi) {
		Vector2f rp1 = Utils.rotateVector(p1, phi);
		Vector2f rp2 = Utils.rotateVector(p2, phi);
		return new LineSegment(rp1, rp2);
	}
	
	public boolean isParallelTo(LineSegment other) {
		return b == 0 && other.b == 0 ||
				a / b == other.a / other.b;
	}
}