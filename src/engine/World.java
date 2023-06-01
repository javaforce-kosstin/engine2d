package engine;

import java.util.Map;
import java.util.Set;
import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;


public class World {
	public static final float TILE_SIDE = 20f;
	
	private Map<Vector2i, Tile> tileGrid;
	
	public World() {
		tileGrid = new HashMap<>();
	}
	
	public Set<Vector2i> getAllIds() {
		return tileGrid.keySet();
	}
	
	public void placeTile(Tile t, Vector2i id) {
		tileGrid.put(id, t);
	}
	
	public Tile getTile(Vector2i id) {
		return tileGrid.get(id);
	}
	
	public Vector2f idToPos(Vector2i id) {
		return new Vector2f(id.x * TILE_SIDE, id.y * TILE_SIDE);
	}
	
	public Vector2i posToId(Vector2f pos) {
		int x = (int)Math.round(pos.x / TILE_SIDE);
		int y = (int)Math.round(pos.y / TILE_SIDE);
		return new Vector2i(x, y);
	}
	
	public void loadFromFile(String filename) {
		tileGrid.clear();
		String path = System.getProperty("user.dir") + "\\" + filename;
		Path filePath = Path.of(path);
		
		try {
			String content = Files.readString(filePath);
			String[] lines = content.split("\r\n");
			
			for (int i = 0; i < lines.length; i++)
				for (int j = 0; j < lines[i].length(); j++) {
					char ch = lines[i].charAt(j);
					if (ch == '.') continue;
					Color color = Utils.charToColor(Character.toLowerCase(ch));
					placeTile(new Tile(color), new Vector2i(i, j));
				}
			
		} catch (Exception ex) {
			return;
		}
	}
	
	
	
	
}
