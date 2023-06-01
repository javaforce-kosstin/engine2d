package collision;

import engine.Vector2f;

public class Physics {
	public static final float g = 0f;
	public static final float MIN_R_VEL = 10f;
	public static final float C_ENERGY_LOSS = 300f;
	public static final float AIR_RESISTANCE = 20f;
	public static final float MAX_VELOCITY = 60f;
	
	
	private static float getEnergyLoss(float vel) {
		return Math.abs(vel) * C_ENERGY_LOSS;
	}
	
	public static float[] kinematicCollision(float v1, float v2, float m1, float m2) {
		float loss = getEnergyLoss(v1 - v2);
		float phi = m1 * v1 * v1 + m2 * v2 * v2 - 2 * loss;
		float mu = (m1 * v1 + m2 * v2) / m1;
		
		float a = m2 + m2 * m2 / m1;
		float b = -2 * mu * m2;
		float c = m1 * mu * mu - phi;
		
		float d = b * b - 4 * a * c;
		
		if (d < 0) d = 0;
		
		float v2new = (-b + Math.signum(v1) * (float)Math.sqrt(d)) / (2 * a);
		
		float v1new = mu - m2 / m1 * v2new;
		
		return new float[] {v1new, v2new};
	}
	
	public static void kinematicCollision(Collision col) {
		GameObject o1 = col.getObject(0);
		GameObject o2 = col.getObject(1);
		
		Vector2f v1 = o1.getVelocity();
		Vector2f v2 = o2.getVelocity();
		
		float[] xVels = kinematicCollision(v1.x, v2.x, o1.getMass(), o2.getMass());
		float[] yVels = kinematicCollision(v1.y, v2.y, o1.getMass(), o2.getMass());
		
		Vector2f v1new = new Vector2f(xVels[0], yVels[0]);
		Vector2f v2new = new Vector2f(xVels[1], yVels[1]);
		
		o1.setVelocity(v1new);
		o2.setVelocity(v2new);
	}
	
	public static float staticCollision(float v, float mass, int currentDim, int hitDim) {
		boolean r = hitDim == currentDim;
		if (r && Math.abs(v) < MIN_R_VEL) return 0;
		float loss = getEnergyLoss(v);
 		float w = v * v - loss / mass;
		if (w < 0) return 0;
		float coef = r? -1 : 1;
		return coef * Math.signum(v) * (float)Math.sqrt(w);
	}
	
;	public static void staticCollision(Collision col) { 
		GameObject o = col.getSenderObject();
		Vector2f v = o.getVelocity();
		
		float newXV = staticCollision(v.x, o.getMass(), 0, col.getDim());
		float newYV = staticCollision(v.y, o.getMass(), 1, col.getDim());
		
		o.setVelocity(new Vector2f(newXV, newYV));
	}
}
