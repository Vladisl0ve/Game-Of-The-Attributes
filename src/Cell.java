
public class Cell {

	public float w, h, speed = 0.5f;
	public boolean toBeDeleted;
	public int age, type;

	public Cell(float w, float h, int type) {
		this.w = w;
		this.h = h;
		this.type = type;
		this.age = 0;
		this.toBeDeleted = false;
	}

}
