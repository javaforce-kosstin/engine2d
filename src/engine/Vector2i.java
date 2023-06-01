package engine;

public class Vector2i {
	public int x = 0;
	public int y = 0;
	
	public Vector2i() {}
	
	public Vector2i(Vector2i v) {
		x = v.x;
		y = v.y;
	}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return (x + " " + y).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass())
			return false;
		Vector2i w = (Vector2i)o;
		return w.x == x && w.y == y;
	}
	
	public Vector2i setDim(int dim, int w) {
		if (dim == 0) x = w;
		else if (dim == 1) y = w;
		return this;
	}
	
	public int getDim(int dim) {
		if (dim == 0) return x;
		return y;
	}
	
	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2i set(int w) {
		return set(w, w);
	}
	
	public Vector2i set(Vector2i v) {
		return set(v.x, v.y);
	}
	
	public Vector2i add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2i add(Vector2i v) {
		return add(v.x, v.y);
	}
	
	public Vector2i mul(int w) {
		this.x *= w;
		this.y *= w;
		return this;
	}
	
	public Vector2i copy() {
		return new Vector2i(this);
	}
	
	public Vector2i addX(int w) {
		this.x += w;
		return this;
	}
	
	public Vector2i addY(int w) {
		this.y += w;
		return this;
	}
	
	public Vector2f toFloat() {
		return new Vector2f(x, y);
	}
}
