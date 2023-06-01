package engine;


interface UpdateFunc {
	public void action(float dt);
}

class EngineCycle {
	public static final long NANOSECOND = 1000000000L;
	public static final int FRAMERATE = 60;
	private static float frameTime = 1.0f / FRAMERATE;
	
	private final UpdateFunc updateFunc;
	private boolean isRunning = false;
	private int fps = 0;
	
	public EngineCycle(UpdateFunc updateFunc) {
		this.updateFunc = updateFunc;
	}
	
	public void start() {
		long lastTime = System.nanoTime();
		double deltaTime = 0;
		
		isRunning = true;
		
		while (isRunning) {
			long startTime = System.nanoTime();
			long miniDeltaTime = startTime - lastTime;
			double miniDeltaTimeS = (double)miniDeltaTime / NANOSECOND;
			deltaTime += miniDeltaTimeS;
			lastTime = startTime;
			
			if (deltaTime < frameTime)
				continue;
			
			updateFunc.action((float)deltaTime);
			setFps((int)Math.round(1 / deltaTime));
			deltaTime = 0;
		}
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private void setFps(int fps) {
		this.fps = fps;
	}
	
	public int getFps() {
		return fps;
	}

}



public class Engine {
	private final Game game;
	private final EngineCycle cycle;
	private final Window window;
	
	public Engine(Game game) {
		this.game = game;
		cycle = new EngineCycle(dt -> update(dt));
		window = new Window();
	}
	
	public void start() {
		game.start(this);
		cycle.start();
	}
	
	private void update(float dt) {
		game.update(dt);
		window.update();
	}
	
	public void stop() {
		game.terminate();
		cycle.stop();
	}
	
	public Input getInput() {
		return window.getInput();
	}
	
	public Window getWindow() {
		return window;
	}
	
}
