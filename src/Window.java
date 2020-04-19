import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Window extends JFrame implements Runnable {

	private final int w = 800;
	private final int h = 800;

	private final int FRAMES_TOTAL = 100000;
	private final int SKIP_FRAMES = 1;
	private int frame = 0;

	private final Color BG = new Color(200, 200, 200, 255);
	private final Color BLUE = new Color(150, 160, 255, 255);
	private final Color RED = new Color(255, 100, 120, 255);
	private BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	private BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	private BufferedImage sprites[] = new BufferedImage[3];
	private final AffineTransform IDENTITY = new AffineTransform();

	private int NumberCells = 10; // start number of cells
	private final int CELL_RADIUS = 20, ENERGY_RADIUS = 5;

	private ArrayList<Cell> cells = new ArrayList<>();
	private ArrayList<Energy> energies = new ArrayList<>();

	public Window() {

		for (int i = 0; i < sprites.length; i++) {
			try {
				sprites[i] = ImageIO.read(new File("img/cell" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.setSize(w, h);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(50, 50);

		for (int i = 0; i < NumberCells; i++) {
			Cell c = new Cell((float) Math.random() * (w - 100) + 50, (float) Math.random() * (h - 100) + 50, (int) Math.random() * 4);
			// Cell c = new Cell(w / 2, h / 2, 0);
			cells.add(c);
		}
	}

	@Override
	public void paint(Graphics g) {
		try {
			drawScene(img);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < SKIP_FRAMES; i++)
			logic();
		Graphics2D g2 = buf.createGraphics();
		g2.drawImage(img, null, 0, 0);
		((Graphics2D) g).drawImage(buf, null, 8, 30);

	}

	private void drawScene(BufferedImage image) throws IOException {
		Graphics2D g2 = image.createGraphics();
		g2.setColor(BG);
		g2.fillRect(0, 0, w, h);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (Energy e : energies) {
			g2.setColor(Energy.COLOR[e.type]);
			g2.fillOval((int) e.x - ENERGY_RADIUS, (int) e.y - ENERGY_RADIUS, ENERGY_RADIUS, ENERGY_RADIUS);
		}

		float cellScale = CELL_RADIUS * 0.01f;

		for (Cell c : cells) {
			float sw = sprites[c.type].getWidth() * cellScale;
			float sh = sprites[c.type].getHeight() * cellScale;
			AffineTransform trans = new AffineTransform();
			// trans.setTransform(IDENTITY);
			trans.translate(c.x - sw, c.y - sh);
			// trans.rotate(c.rotation + Math.PI / 2, sw, sh);
			trans.scale(cellScale, cellScale);
			g2.drawImage(sprites[c.type], trans, this);
		}

	}

	private void moveToDest(Cell c) {
		if (c.endPointW > c.x)
			c.x += c.step * c.speed;
		else if (c.endPointW < c.x)
			c.x -= c.step * c.speed;

		if (c.endPointH > c.y)
			c.y += c.step * c.speed;
		else if (c.endPointH < c.y)
			c.y -= c.step * c.speed;
	}

	private void charging(Cell c, Energy e) {
		if (c.energy >= c.energyCapacity)
			return;
		/*
		 * prowerka na bug System.out.println("C.x: " + c.x);
		 * System.out.println("E.x: "+ e.x); System.out.println("C.y: " + c.y);
		 * System.out.println("E.y: " + e.y); System.out.println("");
		 */
		if (Math.abs(c.x - e.x) <= c.catchDistance)
			if (Math.abs(c.y - e.y) <= c.catchDistance) {
				e.toBeDeleted = true;

				if (c.type == 2)
					c.energy += 3;
				else
					c.energy++;
			}

	}

	private void starvation(Cell c) {

		c.energyTimer--;
		if (c.energyTimer <= 0) {
			c.energy--;
			c.energyTimer = c.energyTimerDefault;
		}

		if (c.energy < 0)
			c.toBeDeleted = true;
	}

	private void breeding(Cell c) {
		double rand = Math.random();

		if (c.energy > 10 && rand < 0.0001) {
			c.energy -= 5;
			Cell newc = new Cell((float) Math.random() * (w - 100) + 50, (float) Math.random() * (h - 100) + 50, 0);
			cells.add(newc);
		}
	}

	private void death(Cell c) {

		if (c.rand < 0.000001)
			c.toBeDeleted = true;
		if (c.age > 10000) {
			if (c.rand < 0.001)
				c.toBeDeleted = true;
		}
		if (c.age > 12000) {
			if (c.rand < 0.1)
				c.toBeDeleted = true;
		}
		if (c.age > 15000) {
			if (c.rand < 10)
				c.toBeDeleted = true;
		}
	}

	private void logic() {
		for (Cell c : cells) {
			c.rand = Math.random() * 100;

			moveToDest(c);
			starvation(c);
			death(c);

			// Checking on being in the visible area
			if (c.x > w)
				c.x = 5;
			else if (c.x < 0)
				c.x = w - 5;

			if (c.y > h)
				c.y = 5;
			else if (c.y < 0)
				c.y = h - 5;
			// Stop checking on area

			float distClosestEnergy = w * w + h * h; // 1 280 000
			Energy closestEnergy = null;
			for (Energy e : energies) {
				float distApplicant = (c.x - e.x) * (c.x - e.x) + (c.y - e.y) * (c.y - e.y); // 80000
				if (distClosestEnergy > distApplicant) {
					distClosestEnergy = distApplicant;
					closestEnergy = e;
				}
			}

			if (closestEnergy != null) {
				c.endPointH = closestEnergy.y;
				c.endPointW = closestEnergy.x;
				charging(c, closestEnergy);
			}

			c.age++;
		}

		for (int i = 0; i < energies.size(); i++) {
			if (energies.get(i).toBeDeleted) {
				energies.remove(i);
				i--;
			}
		}

		for (int i = 0; i < cells.size(); i++) {
			breeding(cells.get(i));
			if (cells.get(i).toBeDeleted) {
				cells.remove(i);
				i--;
				System.out.println("F");
			}
		}

		if (frame % 30 == 0) {
			Energy e = new Energy((float) (Math.random() * (w - 100) + 50), (float) (Math.random() * (h - 100) + 50));
			energies.add(e);
		}
		// System.out.println(cells.size());
		frame++;

	}

	@Override
	public void run() {
		while (frame < FRAMES_TOTAL)
			this.repaint();
	}

}
