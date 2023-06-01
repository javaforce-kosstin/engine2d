package engine;


class HitInfo {
	private final Vector2i hitTile;
	private final Vector2i hitSide;
	private final Vector2f origin;
	private final Vector2f hitPoint;
	private final Vector2f ray;
	
	
	public HitInfo(Vector2i hitTile, Vector2i prev, Vector2f origin, Vector2f hitPoint) {
		this.origin = origin;
		this.hitTile = hitTile;
		this.hitPoint = hitPoint;
		this.ray = new Vector2f(hitPoint.x - origin.x,
				hitPoint.y - origin.y);
		
		int x = Utils.clamp(prev.x - hitTile.x, -1, 1);
		int y = Utils.clamp(prev.y - hitTile.y, -1, 1);
		
		if (x != 0 && y != 0) y = 0;
		
		this.hitSide = new Vector2i(x, y);
	}
	
	public boolean innerHit() {
		return hitSide.x == 0 &&
			   hitSide.y == 0;
	}
	
	public Vector2i getHitTile() {
		return hitTile;
	}
	
	public Vector2i getHitSide() {
		return hitSide;
	}
	
	public Vector2f getRay() {
		return ray;
	}
	
	public Vector2f getHitPoint() {
		return hitPoint;
	}
	
	public Vector2f getOrigin() {
		return origin;
	}
	
	public Vector2i getPrevious() {
		return new Vector2i(hitTile.x + hitSide.x,
				hitTile.y + hitSide.y);
	}
	
	public HitInfo prevent(float delta) {
		if (innerHit()) return this;
		float coef = 1 - delta / ray.length();
		
		Vector2f newHitPoint = new Vector2f(
				origin.x + ray.x * coef,
				origin.y + ray.y * coef);
		return new HitInfo(hitTile, getPrevious(),
				origin, newHitPoint);
	}
}


public class Raycast {
	public static final float THETA = 0.001f;
	
	private final World world;
	
	public Raycast(World world) {
		this.world = world;
	}
	
	public HitInfo raycast(Vector2f pos, Vector2f d, float maxRayLength) {
		float bs = World.TILE_SIDE;
		float l = d.length();
		float w = 0;
		
		Vector2i prevTile = null;
		
		while (w * l < maxRayLength) {
			Vector2f currentPos = new Vector2f(
					pos.x + w * d.x,
					pos.y + w * d.y);
			
			Vector2i currentTile = world.posToId(currentPos);
			
			Tile tile = world.getTile(currentTile);
			
			if (tile != null && prevTile == null)
				return new HitInfo(currentTile, currentTile, pos, pos);
			else if (tile != null && prevTile != null)
				return new HitInfo(currentTile, prevTile, pos, currentPos);
			
			float borderX = currentTile.x * bs + Math.signum(d.x) * bs / 2;
			float borderY = currentTile.y * bs + Math.signum(d.y) * bs / 2;
			
			float alpha = Math.abs((currentPos.x - borderX) / d.x);
			float beta = Math.abs((currentPos.y - borderY) / d.y);
			
			if (Float.isNaN(alpha))
				alpha = Float.POSITIVE_INFINITY;
			if (Float.isNaN(beta))
				beta = Float.POSITIVE_INFINITY;
			
			float coef = Math.min(alpha, beta);
			
			w = w + coef + THETA;
			prevTile = currentTile;
		}
		
		return null;
	}
	
	public HitInfo vectorIntersect(Vector2f origin, Vector2f target) {
		return raycast(origin, target, target.length());
	}
	
	public Vector2f vectorFreeScale(Vector2f origin, Vector2f velocity, float prevent) {
		HitInfo w = vectorIntersect(origin, velocity);
		if (w == null) return velocity;
		return w.prevent(prevent).getRay();
	}
	
	public Vector2f vectorFreeScale(Vector2f origin, Vector2f velocity) {
		return vectorFreeScale(origin, velocity, 0);
	}
	
	public World getContext() {
		return world;
	}
}
