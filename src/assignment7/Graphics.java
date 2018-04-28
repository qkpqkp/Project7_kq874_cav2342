package assignment7;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Graphics extends Application{
	private Map<Set<File>,Integer> FilePairs;
	private Set<File> files;
	Map<FileShape,List<FileShape>> link;
	public Graphics(Map<Set<File>,Integer> map) {
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
		BorderPane bp = new BorderPane();
		Group g = new Group();
		Group left = new Group();
		bp.setCenter(g);
		bp.setLeft(left);
		HashMap<File, Integer> count=new HashMap<>();

		HashMap<File, List<File>> matches=new HashMap<>();

		HashMap<File, Boolean> countcheck=new HashMap<>();


		//count key:file, value:how many files are matched with it

		for(File f:files){
			if(count.containsKey(f)){
				continue;
			}
			else if(!count.containsKey(f)){
				int c=0;
				for(Set<File> set:FilePairs.keySet()){
					if(set.contains(f)){
						c++;
					}
				}
				if(c==0){
					continue;
				}
				count.put(f,c);
			}
		}
		for(File f:count.keySet()){
			countcheck.put(f, false);
		}
		link = new HashMap<FileShape,List<FileShape>>();
		List<FileShape> shapes = new ArrayList<FileShape>();
		for(File f:files) {
			FileShape s = new FileShape(f,count.get(f));
			shapes.add(s);
		}
		for(int i = 0;i<shapes.size();i++) {
			List<FileShape> s = new ArrayList<FileShape>();
			link.put(shapes.get(i), s);
		}
		for(Set<File> s:FilePairs.keySet()) {
			for(FileShape f:shapes) {
				if(s.contains(f.getFile())) {
					for(File file:s) {
						if(!file.equals(f.getFile())) {
							List<FileShape> tmp = link.get(f);
							for(FileShape t:shapes) {
								if(t.getFile().equals(file)){
									tmp.add(t);
									link.put(f,tmp);
								}
							}
						}
					}
				}
			}
		}
		int leftx = 50, lefty=50;
		Map<FileShape,FileShape> temp = new HashMap<FileShape,FileShape>();
		for(FileShape s:link.keySet()) {
			if(link.get(s).size()==1) {
				if(link.get(link.get(s).get(0)).size()==1) {
					if(!left.getChildren().contains(s)) {
						temp.put(s,link.get(s).get(0));
						temp.put(link.get(s).get(0),s);
						s.setLayoutX(leftx);
						s.setLayoutY(lefty);	
						link.get(s).get(0).setLayoutX(leftx+200);
						link.get(s).get(0).setLayoutY(lefty);
						Text t1 = new Text(s.toString());
						Text t2 = new Text(link.get(s).get(0).toString());
						t1.setLayoutX(leftx);
						t1.setLayoutY(lefty);
						t2.setLayoutX(leftx+200);
						t2.setLayoutY(lefty);
						left.getChildren().addAll(t1,t2);
						left.getChildren().add(s);
						left.getChildren().add(link.get(s).get(0));
						lefty+=100;
					}
				}
			}
		}
		
		for(FileShape s:temp.keySet()) {
			Line line = new Line();
			for(Set<File> set:FilePairs.keySet()) {
				if(set.contains(s.getFile())) {
					Text t = new Text(FilePairs.get(set).toString());
					line.setStartX(s.getLayoutX());
					line.setStartY(s.getLayoutY());
					line.setEndX(temp.get(s).getLayoutX());
					line.setEndY(temp.get(s).getLayoutY());
					t.setLayoutX((line.getStartX()+line.getEndX())/2);
					t.setLayoutY((line.getStartY()+line.getEndY())/2);
					left.getChildren().addAll(t,line);
				}
			}
		}
		for(FileShape s:temp.keySet()) {
			link.remove(s);
		}
		Random rand = new Random();
		int limitx = 150;
		int limity = 150;
		for(FileShape s:link.keySet()) {
			int x = rand.nextInt(150)+limitx;
			int y = rand.nextInt(150)+limity;
			
			s.setLayoutX(x);
			s.setLayoutY(y);
			g.getChildren().add(s);
			Text t = new Text(s.toString());
			t.setLayoutX(x);
			t.setLayoutY(y);
			g.getChildren().add(t);
			if(limitx+150<1.5*size) {
				limitx += 300;
			}
			else {
				limitx=150;
				limity+=300;
			}
		}
		for(Set<File> pair:FilePairs.keySet()) {
			List<File> l = new ArrayList<File>(pair);
			Line line = new Line();
			for(FileShape s:link.keySet()) {
				if(s.getFile().equals(l.get(0))) {
					line.setStartX(s.getLayoutX());
					line.setStartY(s.getLayoutY());
				}
				if(s.getFile().equals(l.get(1))) {
					line.setEndY(s.getLayoutY());
					line.setEndX(s.getLayoutX());
				}
			}
			int num = FilePairs.get(pair);
			Text t = new Text(Integer.toString(num));
			t.setLayoutX((line.getStartX()+line.getEndX())/2);
			t.setLayoutY((line.getStartY()+line.getEndY())/2);
			g.getChildren().add(t);
			g.getChildren().add(line);
		}
		Scene scene = new Scene(bp);
        window.setScene(scene);
        window.show();
	}
}
