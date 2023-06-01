package collision;

public class Interval {
	private float a;
	private float b;
	
	public Interval() {
		a = Float.NEGATIVE_INFINITY;
		b = Float.POSITIVE_INFINITY;
	}
	
	public Interval(float a, float b) {
		this.a = a;
		this.b = b;
	}
	
	public Interval(float a, float b, boolean norm) {
		this.a = a;
		this.b = b;
		if (norm) normalize();
	}
	
	public Interval normalize() {
		if (a > b) swap();
		return this;
	}
	
	public float getA() {
		return a;
	}
	
	public float getB() {
		return b;
	}
	
	public boolean includes(float w) {
		return a <= w && w <= b;
	}
	
	public boolean hasSolutions() {
		return a <= b;
	}
	
	private Interval swap() {
		float f = a;
		a = b;
		b = f;
		return this;
	}
	
	public Interval mul(float k) {
		if (k < 0) swap();
		a *= k;
		b *= k;
		return this;
	}
	
	public Interval add(float k) {
		a += k;
		b += k;
		return this;
	}
	
	public Interval divide(float k) {
		return mul(1 / k);
	}
	
	public Interval union(Interval other) {
		float aNew = Math.max(a, other.getA());
		float bNew = Math.min(b, other.getB());
		return new Interval(aNew, bNew, false);
	}
}
