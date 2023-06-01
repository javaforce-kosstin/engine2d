package engine;

import java.awt.Color;

public class Tile {
	private Color color = Color.white;
	
	public Tile(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
}
