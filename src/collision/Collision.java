package collision;

public class Collision {
	private final int dim;
	private Collider senderCollider;
	private Collider hitCollider;
	private GameObject senderObject;
	private GameObject hitObject;
	
	public Collision(Collider senderCol, Collider hitCol, int dim) {
		this.dim = dim;
		senderCollider = senderCol;
		hitCollider = hitCol;
		senderObject = null;
		hitObject = null;
	}
	
	public Collision(GameObject senderObj, GameObject hitObj, int dim) {
		this.dim = dim;
		senderObject = senderObj;
		hitObject = hitObj;
		senderCollider = senderObj.getCollider();
		hitCollider = hitObj.getCollider();
	}
	
	public boolean isIdentified() {
		return senderObject != null && hitObject != null;
	}
	
	public boolean isKinematic() {
		if (!isIdentified()) return false;
		return hitObject.isKinematic();
	}
	
	public boolean isStatic() {
		if (!isIdentified()) return false;
		return !hitObject.isKinematic();
	}
	
	public GameObject getKinematic() {
		if (!isIdentified()) return null;
		return senderObject;
	}
	
	public GameObject getStatic() {
		if (!isIdentified() || isKinematic()) return null;
		return hitObject;
	}
	
	public void identify(World world) {
		senderObject = world.identifyObject(senderCollider);
		hitObject = world.identifyObject(hitCollider);
	}
	
	public int getDim() {
		return dim;
	}
	
	public Collider[] getColliders() {
		return new Collider[] {senderCollider, hitCollider};
	}
	
	public GameObject[] getObjects() {
		return new GameObject[] {senderObject, hitObject};
	}
	
	public GameObject getSenderObject() {
		return senderObject;
	}
	
	public GameObject getHitObject() {
		return hitObject;
	}
	
	public Collider getSenderCollider() {
		return senderCollider;
	}
	
	public Collider getHitCollider() {
		return hitCollider;
	}
	
	public GameObject getObject(int i) {
		switch(i) {
		case 0:
			return senderObject;
		case 1:
			return hitObject;
		default:
			return null;
		}
	}
	
	public Collider getCollider(int i) {
		switch(i) {
		case 0:
			return senderCollider;
		case 1:
			return hitCollider;
		default:
			return null;
		}
	}
}
