package collision;

public class ThetaInfo {
	public final float theta;
	public final Collider collider;
	
	public ThetaInfo(float theta, Collider collider) {
		this.theta = theta;
		this.collider = collider;
	}
	
	public float mul(float a) {
		return a * theta;
	}
}
