package collision;

import engine.Utils;
import engine.Vector2f;

public class RotateRelation implements Relation {
	private final float zeta;
	private final LineSegment s1, s2;
	private final boolean areParallel;
	
	public RotateRelation(LineSegment s1, LineSegment s2) {
		zeta = zetaAngle(s1, s2);
		this.s1 = s1;
		this.s2 = s2;
		areParallel = s1.isParallelTo(s2);
	}
	
	public float getZeta() {
		return zeta;
	}
	
	private static float zetaAngle(float p, float q) {
		if (p == 0 || q == 0) return 0;
		float f = (float)Math.atan(-q / p);
		if (f < 0) f = f + Utils.HALF_PI;
		return f;
	}
	
	private static float zetaAngle(LineSegment s) {
		return zetaAngle(s.p1.x - s.p2.x, s.p1.y - s.p2.y);
	}
	
	private static float zetaAngle(LineSegment s1, LineSegment s2) {
		float z1 = zetaAngle(s1);
		float z2 = zetaAngle(s2);
		
		if (Utils.distance(z1, z2) < 0.01f) {
			float f = z1 - 0.5f;
			return f >= 0? f : f + Utils.HALF_PI;
		}
		
		return (z1 + z2) / 2;
	}
	
	private static float getMovementCoef(LineSegment s, Vector2f v) {
		return v.x * (s.p2.y - s.p1.y) + v.y * (s.p1.x - s.p2.x);
	}
	
	private static boolean checkProjectionIntersect(float a1, float b1, float a2, float b2) {
		Interval i1 = new Interval(a1, b1, true);
		Interval i2 = new Interval(a2, b2, true);
		return i1.union(i2).hasSolutions();
	}
	
	private static float valTheta(float theta) {
		if(!Float.isFinite(theta)) return 1;
		return theta < 0? 1 : Math.min(theta, 1f);
	}
	
	private static float valTheta(Interval i) {
		Interval w = i.union(new Interval(0, 1));
		return w.hasSolutions()? w.getA() : 1;
	}
	
	private static float getThetaDefault(LineSegment s1, LineSegment s2, Vector2f v) {
		IntervalUnion union = new IntervalUnion();
		union.append(new Interval(s1.p1.x, s1.p2.x, true));
		union.append(new Interval(s2.p1.x, s2.p2.x, true));
		
		union.mul(s2.a * s1.b - s1.a * s2.b);
		union.add(-s2.b * s1.c + s1.b * s2.c);
		
		float f = getMovementCoef(s1, v);
		
		union.divide(new float[] {
			s2.b * f - v.x * s2.a * s1.b + v.x * s1.a * s2.b,
			s2.b * f
		});
		
		return valTheta(union.solve());
	}
	
	private static float getThetaParallel(LineSegment s1, LineSegment s2, Vector2f v) {
		if (!checkProjectionIntersect(s1.p1.x + v.x, s1.p2.x + v.x, s2.p1.x, s2.p2.x)) return 1;
		float p = (s1.b * s2.c - s2.b * s1.c) / (s2.b * getMovementCoef(s1, v));
		return valTheta(p);
	}
	
	public float getTheta(Vector2f vel) {
		vel = Utils.rotateVector(vel, zeta);
		LineSegment sr1 = s1.rotate(zeta);
		LineSegment sr2 = s2.rotate(zeta);
		if (areParallel)
			return getThetaParallel(sr1, sr2, vel);
		return getThetaDefault(sr1, sr2, vel);
	}
}
