package engine;

import java.awt.Color;

public class Utils {
	public static float PI = (float)Math.PI;
	public static float TWO_PI = 2 * Utils.PI;
	public static float HALF_PI = Utils.PI / 2;

	public static float normalizeAngle(float angle) {
		if (angle == 0) return 0;
		int coef = angle > 0? 1 : -1;
		float w = (float)Math.floor(Math.abs(angle) / Utils.TWO_PI);
		angle = angle - coef * w * Utils.TWO_PI;
		if (angle < 0) angle += Utils.TWO_PI;
		return angle;
	}
	
	public static Vector2f rotateVector(float a, float b, float beta) {
		Vector2f aVec = new Vector2f(
				(float)Math.cos(beta) * a,
				(float)Math.sin(beta) * a);
		
		Vector2f bVec = new Vector2f(
				(float)Math.sin(beta) * b,
				(float)Math.cos(beta) * b);
		
		return aVec.add(bVec);
	}
	
	public static Vector2f rotateVector(Vector2f v, float beta) {
		return rotateVector(v.x, v.y, beta);
	}
	
	public static <T extends Number & Comparable<T>> T clamp(T v, T a, T b) {
		if (v.compareTo(a) < 0) v = a;
		if (v.compareTo(b) > 0) v = b;
		return v;
	}
	
	public static Color colorShade(Color c, float v) {
		v = clamp(v, 0f, 1f);
		return new Color((int)(c.getRed()*v),
						 (int)(c.getGreen()*v),
						 (int)(c.getBlue()*v));
	}
	
	public static Color charToColor(char ch) {
		switch (ch) {
		case 'r':
			return Color.red;
		case 'b':
			return Color.blue;
		case 'c':
			return Color.cyan;
		case 'w':
			return Color.white;
		default:
			return Color.white;
		}
	}
	
	public static float distance(float a, float b) {
		return Math.abs(a - b);
	}
	
	public static float round(float w, int k) {
		float d = (float)Math.pow(10, k);
		return Math.round(w * d) / d;
	}
	
	public static float drop(float w, float k) {
		return Math.abs(w) < Math.abs(k)? 0 : w;
	}
	
	
}
