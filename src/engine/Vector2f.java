package engine;

public class Vector2f{
	public float x = 0;
	public float y = 0;
	
	public Vector2f() {}
	
	public Vector2f(Vector2f v) {
		x = v.x;
		y = v.y;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass())
			return false;
		Vector2f w = (Vector2f)o;
		return w.x == x && w.y == y;
	}
	
	public Vector2f setDim(int dim, float w) {
		if (dim == 0) x = w;
		else if (dim == 1) y = w;
		return this;
	}
	
	public float getDim(int dim) {
		if (dim == 0) return x;
		return y;
	}
	
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2f set(float w) {
		return set(w, w);
	}
	
	public Vector2f set(Vector2f v) {
		return set(v.x, v.y);
	}
	
	public Vector2f add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2f add(Vector2f v) {
		return add(v.x, v.y);
	}
	
	public Vector2f mul(float w) {
		this.x *= w;
		this.y *= w;
		return this;
	}
	
	public Vector2f copy() {
		return new Vector2f(this);
	}
	
	public Vector2f addX(float w) {
		this.x += w;
		return this;
	}
	
	public Vector2f addY(float w) {
		this.y += w;
		return this;
	}
	
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public Vector2i toInt() {
		return new Vector2i((int)x, (int)y);
	}
}
