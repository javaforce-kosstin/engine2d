package collision;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class ColliderSystem {
	private ArrayList<Collider> colliders;
	private Map<Collider, GameObject> identifyTable;
	
	public ColliderSystem() {
		colliders = new ArrayList<>();
		identifyTable = new HashMap<>();
	}
	
	public GameObject identifyObject(Collider col) {
		return identifyTable.get(col);
	}
	
	public void addObject(GameObject obj) {
		addCollider(obj.getCollider());
		identifyTable.put(obj.getCollider(), obj);
	}
	
	public void removeObject(GameObject obj) {
		removeCollider(obj.getCollider());
		identifyTable.remove(obj.getCollider());
	}
	
	private void addCollider(Collider col) {
		for (Collider c : colliders) {
			c.addRelation(col);
			col.addRelation(c);
		}
		colliders.add(col);
	}
	
	private void removeCollider(Collider col) {
		for (Collider c : colliders) {
			c.removeRelation(col);
			col.removeRelation(c);
		}
		colliders.remove(col);
	}
}
