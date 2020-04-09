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
	private final int h = 600;

	private final int FRAMES_TOTAL = 100000;
	private final int SKIP_FRAMES = 1;
	private int frame = 0;

	private final Color BG = new Color(200, 200, 200, 255);
	private final Color BLUE = new Color(150, 160, 255, 255);
	private final Color RED = new Color(255, 100, 120, 255);
	private BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	private BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	private BufferedImage sprites[] = new BufferedImage[1];
	private final AffineTransform IDENTITY = new AffineTransform();

	private int NumberCells = 1; // start number of cells
	private final int CELL_RADIUS = 20, ENERGY_RADIUS = 5;

	private ArrayList<Cell> cells = new ArrayList<>();
	private ArrayList<Energy> energies = new ArrayList<>();

	public Window() {

		for (int i = 0; i < sprites.length; i++) {
			try {
				sprites[i] = ImageIO.read(new File("img/m" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.setSize(w, h);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(50, 50);

		for (int i = 0; i < NumberCells; i++) {
			// Cell c = new Cell((float) Math.random() * (w - 100) + 50, (float)
			// Math.random() * (h - 100) + 50, 0);
			Cell c = new Cell(w / 2, h / 2, 0);
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
		///

		for (Energy e : energies) {
			g2.setColor(Energy.COLOR[e.type]);
			g2.fillOval((int) e.w - ENERGY_RADIUS, (int) e.h - ENERGY_RADIUS, ENERGY_RADIUS, ENERGY_RADIUS);
		}

		float cellScale = CELL_RADIUS * 0.01f;

		for (Cell c : cells) {
			float sw = sprites[c.type].getWidth() * cellScale;
			float sh = sprites[c.type].getHeight() * cellScale;
			AffineTransform trans = new AffineTransform();
			trans.setTransform(IDENTITY);
			trans.translate(c.w - sw, c.h - sh);
			trans.rotate(c.rotation + Math.PI / 2, sw, sh);
			trans.scale(cellScale, cellScale);
			g2.drawImage(sprites[c.type], trans, this);
		}

	}

	private void logic() {
		for (Cell c : cells) {
			c.w += c.sw;
			c.h += c.sh;
			c.sw *= c.slip;
			c.sh *= c.slip;
			if (c.w < 0) {
				c.sw += 5;
			} else if (c.w > w) {
				c.sw -= 5;
			}
			if (c.h < 0) {
				c.sh += 5;
			} else if (c.h > h) {
				c.sh -= 5;
			}
		}

		if (frame % 30 == 0) {
			Energy e = new Energy((float) (Math.random() * (w - 100) + 50), (float) (Math.random() * (h - 100) + 50));
			energies.add(e);
		}
		frame++;

	}

	@Override
	public void run() {
		while (frame < FRAMES_TOTAL)
			this.repaint();
	}

}
