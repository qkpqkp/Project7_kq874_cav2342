package assignment7;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class GraphicDemo extends Application{
	private Map<Set<File>,Integer> FilePairs;
	private Set<File> files;
	public GraphicDemo(Map<Set<File>,Integer> map) {
		List<Entry<Set<File>, Integer>> list = new ArrayList<>(map.entrySet());
		for(int i = 0;i<list.size();i++) {
			if(list.get(i).getValue()<100) {
				list.remove(i);
				i--;
			}
		}
		FilePairs = new HashMap<Set<File>,Integer>();
		files = new HashSet<File>();
		for (Entry<Set<File>, Integer> entry : list) {
			for(File f:entry.getKey()) {
				files.add(f);
			}
            FilePairs.put(entry.getKey(), entry.getValue());
        }
		
	}
	@Override
	public void start(Stage window) throws Exception {
		final int size = 1000;
		FlowPane fp = new FlowPane();
		Canvas canvas = new Canvas(size,size);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawShapes(gc,size);
		fp.getChildren().add(canvas);
		Scene scene = new Scene(fp);
        window.setScene(scene);
        window.show();

	}
	
	private void drawShapes(GraphicsContext gc,int size) {
		
	}
}
