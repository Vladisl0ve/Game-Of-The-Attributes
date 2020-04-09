
public class Cell {

	public float w, h, speed = 0.5f, rotation, rotationSpeed = 0.2f, slip = 0.8f, sw, sh, tx, ty;
	public boolean toBeDeleted;
	public int age, type;
	
	public float sightDistance = 100f;

	public Cell(float w, float h, int type) {
		this.w = w;
		this.h = h;
		this.sh = 0f;
		this.sw = 0f;
		this.type = type;
		this.age = 0;
		this.toBeDeleted = false;
		this.rotation = 0f;
	}

}
