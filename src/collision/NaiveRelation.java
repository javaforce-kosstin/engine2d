package collision;

import engine.Vector2f;

interface Relation {
	public float getTheta(Vector2f v);
}

interface RelationFunc {
	public float getTheta(LineSegment s1, LineSegment s2, Vector2f v);
}

public class NaiveRelation implements Relation {
	private final LineSegment s1, s2;
	private final RelationFunc relFunc;
	
	public NaiveRelation(LineSegment s1, LineSegment s2) {
		this.s1 = s1;
		this.s2 = s2;
		relFunc = getRelFunc(s1, s2);
	}
	
	public float getTheta(Vector2f vel) {
		return relFunc.getTheta(s1, s2, vel);
	}
	
	private static float valTheta(float theta) {
		if(!Float.isFinite(theta)) return 1;
		return theta < 0? 1 : Math.min(theta, 1f);
	}
	
	private static float valTheta(Interval i) {
		Interval w = i.union(new Interval(0, 1));
		return w.hasSolutions()? w.getA() : 1;
	}
	
	private static float getMovementCoef(LineSegment s, Vector2f v) {
		return v.x * (s.p2.y - s.p1.y) + v.y * (s.p1.x - s.p2.x);
	}
	
	private static boolean checkProjectionIntersect(float a1, float b1, float a2, float b2) {
		Interval i1 = new Interval(a1, b1, true);
		Interval i2 = new Interval(a2, b2, true);
		return i1.union(i2).hasSolutions();
	}
	
	private static float verVerRel(LineSegment s1, LineSegment s2, Vector2f v) {
		float f = getMovementCoef(s1, v);
		if (!checkProjectionIntersect(s1.p1.y + v.y, s1.p2.y + v.y, s2.p1.y, s2.p2.y) || f == 0) return 1;
		float p = (s1.a * s2.c - s2.a * s1.c) / (s2.a * f);
		return valTheta(p);
	}
	
	private static float bCoefParallelRel(LineSegment s1, LineSegment s2, Vector2f v) {
		if (!checkProjectionIntersect(s1.p1.x + v.x, s1.p2.x + v.x, s2.p1.x, s2.p2.x)) return 1;
		float p = (s1.b * s2.c - s2.b * s1.c) / (s2.b * getMovementCoef(s1, v));
		return valTheta(p);
	}
	
	private static float regularRel(LineSegment s1, LineSegment s2, Vector2f v) {
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
	
	private static float verHorRel(LineSegment s1, LineSegment s2, Vector2f v) {
		IntervalUnion u = new IntervalUnion();
		Interval i1 = new Interval(s1.p1.y, s1.p2.y, true);
		Interval i2 = new Interval(s2.p1.x, s2.p2.x, true);
		
		i1.add(s2.c / s2.b);
		i1.divide(-v.y);
		u.append(i1);
		
		i2.mul(-s1.a);
		i2.add(-s1.c);
		i2.divide(getMovementCoef(s1, v));
		u.append(i2);
		
		return valTheta(u.solve());
	}
	
	private static float horVerRel(LineSegment s1, LineSegment s2, Vector2f v) {
		IntervalUnion u = new IntervalUnion();
		Interval i1 = new Interval(s1.p1.x, s1.p2.x, true);
		Interval i2 = new Interval(s2.p1.y, s2.p2.y, true);
		
		i1.add(s2.c / s2.a);
		i1.divide(-v.x);
		u.append(i1);
		
		i2.mul(-s1.b);
		i2.add(-s1.c);
		i2.divide(getMovementCoef(s1, v));
		u.append(i2);
		
		return valTheta(u.solve());
	}
	
	private static float verRegularRel(LineSegment s1, LineSegment s2, Vector2f v) {
		IntervalUnion union = new IntervalUnion();
		union.append(new Interval(s1.p1.y, s1.p2.y, true));
		union.append(new Interval(s2.p1.y, s2.p2.y, true));
		
		float f = getMovementCoef(s1, v);
		
		union.mul(s1.a * s2.b);
		union.add(-s2.a * s1.c + s2.c * s1.a);
		union.divide(new float[] {	
			s2.a * f - v.y * s1.a * s2.b,
			s2.a * f
		});
		
		return valTheta(union.solve());
	}
	
	private static float regularVerRel(LineSegment s1, LineSegment s2, Vector2f v) {
		IntervalUnion union = new IntervalUnion();
		union.append(new Interval(s1.p1.y, s1.p2.y, true));
		union.append(new Interval(s2.p1.y, s2.p2.y, true));
		
		float f = getMovementCoef(s1, v);
		
		union.mul(s1.b * s2.a);
		union.add(-s1.a * s2.c + s2.a * s1.c);
		union.divide(new float[] {
			-(s2.a * f + v.y  * s2.a * s1.b),
			-s2.a * f
		});
		
		return valTheta(union.solve());
	}

	public static RelationFunc getRelFunc(LineSegment s1, LineSegment s2) {
		if (s1.b == 0 && s2.b == 0)
			return (a1, a2, a3) -> verVerRel(a1, a2, a3);
		
		if (s1.b != 0 && s2.b != 0) {
			if (s2.a / s2.b == s1.a / s1.b)
				return (a1, a2, a3) -> bCoefParallelRel(a1, a2, a3);
			return (a1, a2, a3) -> regularRel(a1, a2, a3);
		}
		
		if (s1.b == 0 && s2.a == 0)
			return (a1, a2, a3) -> verHorRel(a1, a2, a3);
		
		if (s1.a == 0 && s2.b == 0)
			return (a1, a2, a3) -> horVerRel(a1, a2, a3);
	
		if (s1.b == 0)
			return (a1, a2, a3) -> verRegularRel(a1, a2, a3);
		
		return (a1, a2, a3) -> regularVerRel(a1, a2, a3);
	}
	
}