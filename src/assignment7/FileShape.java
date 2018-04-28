package assignment7;

import java.io.File;
import java.util.Set;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class FileShape extends Rectangle {
	private File file;
	private int num;
	private Set<FileShape> link;
	public FileShape(File f, int num) {		
		super(25 + 8*num,25+8*num);
		file = f;
		this.num = num;
		double r = 0,g=1,b=0;
		
		for(int i = 0;i<num;i++) {
			if(r<=1) {
				r+=0.2;
			}
			if(g>=0) {
				g-=0.2;
			}
			this.setFill(new Color(r,g,b,1));
		}
	}
	public File getFile() {
		return file;
	}
	public int getNum() {
		return num;
	}
	public String toString() {
		return file.getName();
	}
	
}
