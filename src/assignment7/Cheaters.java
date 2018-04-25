package assignment7;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;
import javafx.stage.Stage;

public class Cheaters extends Application {
	//Each n words phrase corresponds to a List of Files
	private static Map<String,List<File>> repeatedPhrases = new HashMap<String,List<File>>();
	//File pairs that contain same phrases.
	private static Map<Set<File>, Integer> FilePairs = new HashMap<Set<File>, Integer>();
	
	private static Stage window;
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the files directory: ");
		String directory = sc.nextLine();
		window = primaryStage;
		File folder=new File(directory);
		File[] listOfFiles = folder.listFiles();
		List<File> files = new ArrayList<File>();
		for(File f:listOfFiles) {
			files.add(f);
		}
		System.out.println("Input how many words in a phrase:");
		int n = sc.nextInt();
		sc.close();
		findCheaters(n,files);
		
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	private static void findCheaters(int n, List<File> listOfFiles) {
		for(File f:listOfFiles) {
			ArrayList<String> list = convertFile(f);
			int p=0;
			for(int i = 0;i < list.size()-n;i++) {
				String s = new String();
				for(int j = i; j < i + n; j++) {
					s += list.get(j);
				}
				if(repeatedPhrases.containsKey(s)) {
					List<File> l1 = repeatedPhrases.get(s);
					if(l1.contains(f)) {
						continue;
					}
					else {
						l1.add(f);
						repeatedPhrases.put(s, l1);
					}
				}
				else {
					List<File> l2 = new ArrayList<File>();
					l2.add(f);
					repeatedPhrases.put(s, l2);
				}
			}
		}
		for(String s:repeatedPhrases.keySet()) {
			addFilePairs(repeatedPhrases.get(s));
		}
		printOutFiles();
		GraphicDemo graphic = new GraphicDemo(FilePairs);
		try {
			graphic.start(window);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Assign each file pair with the number of same phrases they have
	private static void addFilePairs(List<File> files) {
		if(files.size()<=1) {
			return;
		}
		for(int i = 0;i < files.size()-1;i++) {
			for(int j = i+1;j < files.size();j++) {
				Set<File> set = new HashSet<File>();
				set.add(files.get(i));
				set.add(files.get(j));
				Set<File> tmp = checkObject(set);
				if(tmp!=null) {
					FilePairs.put(tmp, FilePairs.get(tmp) + 1);
				}
				else {
					FilePairs.put(set, 1);
				}
			}
		}
	}
	
	//Check if the set already exists in FilePairs
	private static Set<File> checkObject(Set<File> set) {
		for(Set<File> s:FilePairs.keySet()) {
			if(s.equals(set)) {
				return s;
			}
		}
		return null;
	}
	
	//Convert all words in a file to a list of Strings, remove punctuations and spaces
	private static ArrayList<String> convertFile(File f){
		ArrayList<String> list = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(f);
			while(sc.hasNext()) {
				String next = sc.next();
				next = next.replaceAll("\\p{P}","").toUpperCase();
				list.add(next);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private static void printOutFiles() {
		//Print out the outcome by accessing FilePairs
		List<Entry<Set<File>, Integer>> list = new ArrayList<>(FilePairs.entrySet());
        list.sort(Entry.comparingByValue(Collections.reverseOrder()));
        Map<Set<File>, Integer> result = new LinkedHashMap<>();
        for (Entry<Set<File>, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        for(Set<File> set:result.keySet()) {
        	System.out.print(FilePairs.get(set) + ": ");
        	for(File f:set) {
        		System.out.print(f.getName() + " ");
        	}  
        	System.out.println("");
        }
        //Matrix part
        Map<File,Integer> files = new HashMap<File,Integer>();
        int i = 1;
        for(Set<File> set:FilePairs.keySet()) {
        	for(File f: set) {
        		if(!files.containsKey(f)) {
        			files.put(f, i);
        			i++;
        		}
        	}
        }
        if(files.size()>50) {
        	return;
        }
        String[][] Matrix = new String[files.size()+1][files.size()+1];
        for(int j = 0;j<files.size()+1;j++) {
        	for(int k = 0;k<files.size()+1;k++) {
        		Matrix[j][k] = "-";
        		if(j>=2&&k<j) {
        			Matrix[j][k] = Integer.toString(0);
        		}
        	}
        }
        Matrix[0][0] = "                    ";
        for(File f:files.keySet()) {
        	Matrix[0][files.get(f)] = f.getName();
        	Matrix[files.get(f)][0] = f.getName();
        	for(Set<File> set:FilePairs.keySet()) {
        		if(set.contains(f)) {
        			List<File> l = new ArrayList<File>(set);
        			if(l.get(0).equals(f)) {
        				if(files.get(f)>=2&&files.get(l.get(1))<files.get(f)) {
        					Matrix[files.get(f)][files.get(l.get(1))] = FilePairs.get(set).toString();
        				}
        			}
        			else {
        				if(files.get(f)>=2&&files.get(l.get(0))<files.get(f)) {
        					Matrix[files.get(f)][files.get(l.get(0))] = FilePairs.get(set).toString();
        				}
        			}
        		}
        	}
        }
        for(int j = 0;j<files.size()+1;j++) {
        	for(int k = 0;k<files.size()+1;k++) {
        		System.out.print(Matrix[k][j]);
        		for(int m = 0;m<20-Matrix[k][j].length();m++) {
        			System.out.print(" ");
        		}
        	}
        	System.out.println("");
        }
	}

	
}
