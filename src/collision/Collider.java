package collision;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import engine.Vector2f;


public class Collider {
	public static final float PREVENT = 3f;
	public static final float DROP = 0.05f;
	
	private Vector2f pos = new Vector2f();
	private ColliderNode[] nodes;
	
	public Collider(float[] coords) {
		ArrayList<ColliderNode> nodes = new ArrayList<>();
		for (int i = 0; i < coords.length / 4; i++) {
			int j = i * 4;
			
			Vector2f p1 = new Vector2f(coords[j], coords[j + 1]);
			Vector2f p2 = new Vector2f(coords[j + 2], coords[j + 3]);
			LineSegment seg = new LineSegment(p1, p2);
			
			nodes.add(new ColliderNode(seg, this));
		}
		this.nodes = nodes.toArray(new ColliderNode[nodes.size()]);
	}
	
	public void move(Vector2f vel) {
		for (ColliderNode node : nodes)
			node.move(vel);
		pos.add(vel);
	}
	
	public void setPosition(Vector2f newPos) {
		Vector2f vel = newPos.add(pos.copy().mul(-1));
		move(vel);
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public ColliderNode[] getNodes() {
		return nodes;
	}
	
	public ThetaInfo getTheta(Vector2f vel) {
		ThetaInfo current = null;
		
		for (ColliderNode n : nodes) {
			ThetaInfo segTheta = n.getTheta(vel);
			if (current == null || segTheta.theta < current.theta)
				current = segTheta;
		}
		
		return current;
	}
	
	public void addRelation(Collider collider) {
		for (ColliderNode n : nodes)
			n.addRelation(collider);
	}
	
	public void removeRelation(Collider collider) {
		for (ColliderNode n : nodes)
			n.removeRelation(collider);
	}
	
	public void render(Graphics2D g) {
		for (ColliderNode n : nodes)
			n.render(g);
	}
	
	public void render(Graphics2D g, Color c, int thickness) {	
		g.setStroke(new BasicStroke(thickness));
		g.setColor(c);
		render(g);
	}
}