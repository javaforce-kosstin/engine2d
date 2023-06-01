package collision;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import engine.Vector2f;


public class ColliderNode {
	private Map<ColliderNode, Relation> relations;
	private Collider parent;
	private LineSegment segment;
	
	public ColliderNode(LineSegment segment, Collider parent) {
		this.parent = parent;
		this.segment = segment;
		relations = new HashMap<>();
	}
	
	public LineSegment getSegment() {
		return segment;
	}

	public void setParent(Collider parent) {
		this.parent = parent;
	}
	
	public Collider getParent() {
		return parent;
	}
	
	public void removeRelation(ColliderNode node) {
		relations.remove(node);
	}
	
	public void addRelation(ColliderNode node) {
		if (parent != null && node.getParent() == parent) return;
		Relation rel = new NaiveRelation(this.getSegment(), node.getSegment());
		relations.put(node, rel);
	}
	
	public void addRelation(Collider col) {
		for (ColliderNode node : col.getNodes())
			addRelation(node);
	}
	
	public void removeRelation(Collider col) {
		for (ColliderNode node : col.getNodes())
			removeRelation(node);
	}
	
	public void move(Vector2f v) {
		segment.move(v);
	}
	
	
	public ThetaInfo getTheta(Vector2f vel) {
		float theta = 1;
		Collider parent = null;
		for (ColliderNode node : relations.keySet()) {
			float segTheta = relations.get(node).getTheta(vel);
			if (segTheta >= theta) continue;
			theta = segTheta;
			parent = node.getParent();
		}
		return new ThetaInfo(theta, parent);
	}
	
	public void render(Graphics2D g) {
		segment.render(g);
	}
	
}
