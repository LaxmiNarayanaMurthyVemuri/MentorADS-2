import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class Solution {

	// Don't modify this method.
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String cases = scan.nextLine();

		switch (cases) {
		case "loadDictionary":
			// input000.txt and output000.txt
			BinarySearchST<String, Integer> hash = loadDictionary("/Files/t9.csv");
			while (scan.hasNextLine()) {
				String key = scan.nextLine();
				System.out.println(hash.get(key));
			}
			break;

		case "getAllPrefixes":
			// input001.txt and output001.txt
			T9 t9 = new T9(loadDictionary("/Files/t9.csv"));
			while (scan.hasNextLine()) {
				String prefix = scan.nextLine();
				for (String each : t9.getAllWords(prefix)) {
					System.out.println(each);
				}
			}
			break;

		case "potentialWords":
			// input002.txt and output002.txt
			t9 = new T9(loadDictionary("/Files/t9.csv"));
			int count = 0;
			while (scan.hasNextLine()) {
				String t9Signature = scan.nextLine();
				for (String each : t9.potentialWords(t9Signature)) {
					count++;
					System.out.println(each);
				}
			}
			if (count == 0) {
				System.out.println("No valid words found.");
			}
			break;

		case "topK":
			// input003.txt and output003.txt
			t9 = new T9(loadDictionary("/Files/t9.csv"));
			Bag<String> bag = new Bag<String>();
			int k = Integer.parseInt(scan.nextLine());
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				bag.add(line);
			}
			for (String each : t9.getSuggestions(bag, k)) {
				System.out.println(each);
			}

			break;

		case "t9Signature":
			// input004.txt and output004.txt
			t9 = new T9(loadDictionary("/Files/t9.csv"));
			bag = new Bag<String>();
			k = Integer.parseInt(scan.nextLine());
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				for (String each : t9.t9(line, k)) {
					System.out.println(each);
				}
			}
			break;

		default:
			break;

		}
	}

	// Don't modify this method.
	public static String[] toReadFile(String file) {
		In in = new In(file);
		return in.readAllStrings();
	}

	public static BinarySearchST<String, Integer> loadDictionary(String file) {
		BinarySearchST<String, Integer>  st = new BinarySearchST<String, Integer>();
		String[] words = toReadFile(file);
		for (String word : words) {
			word = word.toLowerCase();
			if (st.contains(word)) {
				st.put(word, st.get(word) + 1);
			} else {
				st.put(word, 1);
			}
		}
		return st;
	}

}

class T9 {

	private TST<Integer> tstDictionary;
	private Character[][] t9Letters = {
		null, //0
		null, //1
		{'a', 'b', 'c'}, //2
		{'d', 'e', 'f'}, //3
		{'g', 'h', 'i'}, //4
		{'j', 'k', 'l'}, //5
		{'m', 'n', 'o'}, //6
		{'p', 'q', 'r', 's'}, //7
		{'t', 'u', 'v'}, //8
		{'w', 'x', 'y', 'z'} //9
	};

	public T9(BinarySearchST<String, Integer> st) {
		tstDictionary = new TST<Integer>();
		createTST(st);
	}

	// load dictionary into TST.
	private void createTST(BinarySearchST<String, Integer> st) {
		// Iterating symbol table and adding keys as words and values as freq(word).
		for (String word : st.keys()) {
			tstDictionary.put(word, st.get(word));
		}
	}

	// get all the prefixes that match with given prefix.
	public Iterable<String> getAllWords(String prefix) {
		return tstDictionary.keysWithPrefix(prefix);
	}

	public Iterable<String> potentialWords(String t9Signature) {
		Set<String> words = new TreeSet<String>();
		dfs(t9Signature, words, 0, "");
		return words;
	}

	private void dfs(String t9Signature, Set<String> words, int index, String prefix) {
		// recursion base break condition.
		if (index == t9Signature.length()) {
			// add prefix if it contains.
			if (tstDictionary.contains(prefix)) {
				words.add(prefix);
			}
			return;
		}

		Character character = t9Signature.charAt(index);
		Integer number = characterToNumber(character);
		Character[] numPadCharacters = t9Letters[number];
		if (!Objects.isNull(numPadCharacters)) {
			for (Character c : numPadCharacters) {
				dfs(t9Signature, words, index + 1, prefix + c);
			}
		}

	}

	private int characterToNumber(Character ch) {
		return Character.getNumericValue(ch) - Character.getNumericValue('0');
	}


	// return all possibilities(words), find top k with highest frequency.
	public Iterable<String> getSuggestions(Iterable<String> words, int k) {
		MaxPQ<Pair> maxPQ = new MaxPQ<Pair>();
		for (String word : words ) {
			for (String each : getAllWords(word)) {
				maxPQ.insert(new Pair(each, tstDictionary.get(each)));
			}
		}
		Set<String> bag = new TreeSet<String>();
		while (k > 0 && !maxPQ.isEmpty()) {
			Pair pair = maxPQ.delMax();
			bag.add(pair.getWord());
			k--;
		}
		return bag;
	}

	// final output
	public Iterable<String> t9(String t9Signature, int k) {
		return getSuggestions(potentialWords(t9Signature), k);
	}
}

class Pair implements Comparable {
	private String word;
	private int frequency;

	public Pair(String word, int frequency) {
		this.word = word;
		this.frequency = frequency;
	}

	public int compareTo(Object object) {
		Pair that = (Pair) object;
		if (this.frequency < that.frequency) {
			return -1;
		} else if (this.frequency > that.frequency) {
			return 1;
		} else if (this.word.compareTo(that.word) < 0) {
			return -1;
		}
		return 1;
	}

	public String getWord() {
		return this.word;
	}

	public String toString() {
		return this.word + " " + this.frequency;
	}

}