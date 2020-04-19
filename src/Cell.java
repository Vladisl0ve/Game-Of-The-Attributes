
public class Cell {

	public float x, y, speed = 0.1f, step = 5f, endPointW, endPointH;
	public boolean toBeDeleted;
	public int age = 0, type, energyTimerDefault = 100, energyTimer = 100;
	// public int strength, agil, intel;

	public float sightDistance = 100f;
	public float catchDistance = 6f;

	public float energy = 10f;
	public float energyCapacity = 25f;

	public double rand = Math.random() * 100 + 1;

	public Cell(float w, float h, int type) {
		this.x = w;
		this.y = h;
		this.type = type;
		this.toBeDeleted = false;
		this.endPointH = h;
		this.endPointW = w;

		// attributes modifiers
		if (type == 0) {
			energy = 20f;
			energyCapacity = 50f;
		} else if (type == 1) {
			speed = 0.2f;
			energyTimerDefault = 200;
			energyTimer = energyTimerDefault;
		} else if (type == 2) { // declared in "void charging"

		}

	}

}
