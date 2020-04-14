
public class Cell {

	public float w, h, speed = 0.05f;
	public boolean toBeDeleted;
	public int age, type;

	public float sightDistance = 100f;

	public Cell(float w, float h, int type) {
		this.w = w;
		this.h = h;
		this.type = type;
		this.age = 0;
		this.toBeDeleted = false;
	}

}
