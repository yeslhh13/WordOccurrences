package occurrence;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

import com.google.common.base.CharMatcher;

/**
 * 2019/05/04
 * Calculates the occurrences of each word in the rtf file
 * @author Yeseul Noh
 * 
 */
public class WordOccurrences {
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Path: ");
		
		String rtfText = extractTextFromRTF(in.nextLine());
		String[] list = wordList(rtfText);
		Map<String, Integer> map = wordMaps(list);
		List<String> sortedKeyList = sortMap(map);
		
		printMap(sortedKeyList, map);
		in.close();
	}
	
	/**
	 * Extracts text from rtf file and return them in String
	 * @param filename
	 * @return String values from the rtf file
	 * @see https://stackoverflow.com/questions/11866935/java-get-plain-text-from-rtf
	 */
	public static String extractTextFromRTF(String filename) {
		//ready to call file
		RTFEditorKit rtfEditor = new RTFEditorKit();
		Document document = rtfEditor.createDefaultDocument();
		String text = null;
		
		try {
			FileInputStream fileinput = new FileInputStream(new File(filename).getAbsolutePath());
			InputStreamReader inputReader = new InputStreamReader(fileinput, "UTF-8");
			
			rtfEditor.read(inputReader, document, 0);
			text = document.getText(0, document.getLength()).toLowerCase();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return text;
	}
	
	/**
	 * Create List with String type from the given String value
	 * and remove non ASCII characters from list values
	 * @param words
	 * @return String[] made by the words
	 * @see https://stackoverflow.com/questions/13960385/split-string-in-java-to-show-only-sequence-of-characters
	 */
	public static String[] wordList(String words) {
		String[] result = words.replaceAll("(^[^a-z]*)|([^a-z]*$)", "").split("[^a-z]+");
		for (int i = 0; i < result.length; i++) {
			result[i] = CharMatcher.inRange('a', 'z').retainFrom(result[i]);
		}
		System.out.println("Words: " + result.length);
		return result;
	}
	
	/**
	 * Make Map with the given word list
	 * @param wordList
	 * @return Map made from the word list
	 * @see
	 */
	public static Map<String, Integer> wordMaps(String[] wordList) {
		Map<String, Integer> map = new HashMap<>();
		
		for (int i = 0; i < wordList.length; i++) {
			if (map.isEmpty()) {
				map.put(wordList[i], 1);
			} else {
				if (map.containsKey(wordList[i])) {
					map.put(wordList[i], map.get(wordList[i]) + 1);
				} else {
					map.put(wordList[i], 1);
				}
			}
		}
		
		return map;
	}
	
	/**
	 * Sort the Map by descending values
	 * @param unsortedMap
	 * @return List that is sorted by the Map value
	 * @see https://ithub.tistory.com/34
	 */
	@SuppressWarnings("unchecked")
	public static List<String> sortMap(Map<String, Integer> unsortedMap) {
		List<String> list = new ArrayList<>();
		list.addAll(unsortedMap.keySet());
		
		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object ob1, Object ob2) {
				Object value1 = unsortedMap.get(ob1);
				Object value2 = unsortedMap.get(ob2);
				
				return ((Comparable<Object>) value2).compareTo(value1);
			}
		});
		
		return list;
	}

	/**
	 * Prints map keys and values
	 * @param list of keys
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public static void printMap(List<?> list, Map<String, Integer> map) {
		Iterator<String> i = (Iterator<String>) list.iterator();
		
		while(i.hasNext()) {
			String str = (String) i.next();
			System.out.println(str + "\t" + map.get(str) + " times");
		}
		
		System.out.println("Map Keys: " + map.size());
	}
}
