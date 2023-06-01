package collision;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import engine.Vector2f;

public class World {
	private ColliderSystem colliderSystem;
	private Set<GameObject> objects;
	
	public World() {
		colliderSystem = new ColliderSystem();
		objects = new HashSet<>();
	}
	
	public GameObject identifyObject(Collider col) {
		return colliderSystem.identifyObject(col);
	}
	
	public GameObject[] identifyObjects(Collider[] cols) {
		GameObject[] result = new GameObject[cols.length];
		for (int i = 0; i < cols.length;i++)
			result[i] = identifyObject(cols[i]);
		return result;
	}

	public GameObject addObject(GameObject obj) {
		colliderSystem.addObject(obj);
		obj.setWorld(this);
		objects.add(obj);
		return obj;
	}
	
	public void removeObject(GameObject obj) {
		colliderSystem.removeObject(obj);
		objects.remove(obj);
	}
	
	public void render(Graphics2D g) {
		for(GameObject obj : objects)
			obj.render(g);
	}
	
	public void update(float dt) {
		for (GameObject obj : objects)
			obj.update(dt);
	}
	
	public GameObject catchObject(Vector2f p) {
		for (GameObject obj : objects)
			if(obj.containsPoint(p)) return obj;
		return null;
	}
}
