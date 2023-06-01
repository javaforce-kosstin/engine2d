package engine;

public interface Game {
	public void start(Engine engine);
	public void update(float dt);
	public void terminate();
}
