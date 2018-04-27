package assignment7;

import java.io.File;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FileShape {
	private File file;
	private Rectangle r;
	private int num;
	public FileShape(File f, int num) {
		file = f;
		this.num = num;
		int length = 20;
		Rectangle r = new Rectangle(length + 2*num,length + 2*num);
		Color c = Color.GREEN;
		for(int i = 0;i<num;i++) {
			r.setFill(c.darker());
		}
		this.r = r;
	}
	public File getFile() {
		return file;
	}
	public Rectangle getRectangle() {
		return r;
	}
	public int getNum() {
		return num;
	}
}
