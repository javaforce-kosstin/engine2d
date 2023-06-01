package engine;

import collision.Physics;
import collision.TestGame;

public class Launcher {

	public static void main(String[] args) {
		Engine engine = new Engine(new TestGame());
		engine.start();
	}

}
