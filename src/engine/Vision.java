package engine;

import java.awt.Color;
import java.util.ArrayList;

class Strip {
	private float height = 0;
	private Color color;
	
	public Strip() { }
	
	public Strip(float height, Color color) {
		this.height = height;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public float getHeight() {
		return height;
	}

}

public class Vision {
	public static final float DRAW_DISTANCE = 500f;
	public static final float MIN_DISTANCE = 10f;
	public static final float MAX_HEIGHT = 500f;
	public static final int STRIP_COUNT = 100;
	public static final float STRIP_WIDTH = Canvas.WIDTH / STRIP_COUNT;
	public static final float FOV = Utils.HALF_PI;
	private static final float STRIP_STEP = FOV / (STRIP_COUNT - 1);
	
	private final Raycast raycast;
	
	public Vision(Raycast raycast) {
		this.raycast = raycast;
	}
	
	private Strip createStrip(HitInfo info) {
		float distance = Math.max(info.getRay().length(), MIN_DISTANCE);
		float height = heightFunc(distance);
		Tile tile = raycast.getContext().getTile(info.getHitTile());
		Color color = colorShade(tile.getColor(), distance);
		return new Strip(height, color);
	}
	
	private Color colorShade(Color color, float distance) {
		float intensity = (float)Math.sqrt(World.TILE_SIDE / distance);
		return Utils.colorShade(color, intensity);
	}
	
	private float heightFunc(float distance) {
		return MAX_HEIGHT * World.TILE_SIDE / distance;
	}
	
	public Strip[] cast(Player player) {
		ArrayList<Strip> strips = new ArrayList<>();
		Vector2f unit = player.relativeForward();
		
		for (int i = 0; i < STRIP_COUNT; i++) {
			float phi = player.rotation - FOV / 2 + i * STRIP_STEP;
			Vector2f dir = Utils.rotateVector(unit, phi);
			HitInfo hit = raycast.raycast(player.position, dir, DRAW_DISTANCE);
			if (hit == null) {
				strips.add(new Strip());
				continue;
			}
			strips.add(createStrip(hit));
		}
		
		return strips.toArray(new Strip[strips.size()]);
	}
	
}
