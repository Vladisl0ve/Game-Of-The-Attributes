import java.awt.Color;

public class Energy {

	public int type;
	public float h, w;
	public boolean toBeDeleted;

	public static Color[] COLOR = { new Color(0, 255, 51), new Color(0, 102, 0), new Color(255, 204, 0),
			new Color(255, 102, 0), new Color(255, 204, 51), new Color(150, 50, 150) };

	public Energy(float x, float y) {
		this.w = x;
		this.h = y;
		this.type = (int) (Math.random() * 6);
		this.toBeDeleted = false;
	}

}
