
public class Cell {

	public float x, y, speed = 0.1f, step = 5f, endPointW, endPointH;
	public boolean toBeDeleted;
	public int age, type, energyTimer;

	public float sightDistance = 100f;
	public float catchDistance = 6f;
	public float energy;

	public Cell(float w, float h, int type) {
		this.x = w;
		this.y = h;
		this.type = type;
		this.age = 0;
		this.toBeDeleted = false;
		this.endPointH = h;
		this.endPointW = w;
		this.energy = 10f;
		this.energyTimer = 0;
	}

}
