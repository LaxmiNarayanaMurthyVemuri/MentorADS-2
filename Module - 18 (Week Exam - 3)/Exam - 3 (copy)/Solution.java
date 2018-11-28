import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;
import java.util.Hashtable;

public class Solution {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String fileName = scan.nextLine();
		String[] dictionary = toLoadDictionary(fileName);
		Dictionary t9Dictionary = new Dictionary(dictionary);
		String search = scan.nextLine();

		switch (search) {
		case "dictionary":
			while (scan.hasNextLine()) {
				String keyToSearch = scan.nextLine();
				System.out.println(contains(keyToSearch, dictionary));
			}
			break;

		case "t9search":
			while (scan.hasNextLine()) {
				List<String> wordsSort = new ArrayList<String>();
				String query = scan.nextLine();
				int count = 0;
				Iterable<String> iterable = t9Dictionary.getSuggestions(query);
				for (String word : iterable) {
					count++;
					wordsSort.add(t9Dictionary.getValue(word));
					//System.out.println(t9Dictionary.getValue(word));
				}
				if (count == 0) {
					System.out.println("No match found.");
				} else {
					Collections.sort(wordsSort);
					System.out.println(wordsSort);
				}
			}
			break;

		default:
			break;
		}
	}

	public static boolean contains(String key, String[] words) {
		for (String word : words) {
			if (word.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static String[] toLoadDictionary(String file) {
		In in = new In("/Files/" + file);
		return in.readAllStrings();
	}
}

class Dictionary {

	private String[] words;
	private TrieST<Integer> trieST;
	private Hashtable<String, String> hm;

	public Dictionary(String[] words) {
		this.words = words;
		this.trieST = new TrieST<Integer>();
		this.hm = new Hashtable<String, String>();
		initTrieST();
	}

	private void initTrieST() {
		int i = 0;
		for (String word : words) {
			String numWord = getT9Word(word);
			hm.put(numWord, word);
			//System.out.println(numWord + "---" + word);
			trieST.put(numWord, i++);
		}
	}

	public String getT9Word(String word) {
		String numWord = "";
		for (int i = 0; i < word.length(); i++) {
			word = word.toLowerCase();
			numWord += getValue(word.charAt(i));
		}
		return numWord;
	}

	public int getT9Character(char ch) {
		return ch - '0' - 2;
	}

	public int getValue(char ch) {
		if (ch == 'a' || ch == 'b' || ch == 'c') {
			return 0;
		}
		if (ch == 'd' || ch == 'e' || ch == 'f') {
			return 1;
		}
		if (ch == 'g' || ch == 'h' || ch == 'i') {
			return 2;
		}
		if (ch == 'j' || ch == 'k' || ch == 'l') {
			return 3;
		}
		if (ch == 'm' || ch == 'n' || ch == 'o') {
			return 4;
		}
		if (ch == 'p' || ch == 'q' || ch == 'r' || ch == 's') {
			return 5;
		}
		if (ch == 't' || ch == 'u' || ch == 'v') {
			return 6;
		}
		if (ch == 'w' || ch == 'x' || ch == 'y' || ch == 'z') {
			return 7;
		}
		return -1;
	}

	public Iterable<String> getSuggestions(String query) {
		String numQuery = getRealWordFromT9(query);
		return trieST.keysWithPrefix(numQuery);

	}

	public String getRealWordFromT9(String query) {
		String numWord = "";
		for (int i = 0; i < query.length(); i++) {
			numWord += getT9Character(query.charAt(i));
		}
		return numWord;
	}

	public String getValue(String key) {
		return hm.get(key);
	}
}




class TrieST<Value> {
	private static final int R = 256;


	private Node root;      // root of trie
	private int n;          // number of keys in trie

	// R-way trie node
	private static class Node {
		private Object val;
		private Node[] next = new Node[R];
	}

	/**
	  * Initializes an empty string symbol table.
	  */
	public TrieST() {
	}


	/**
	 * Returns the value associated with the given key.
	 * @param key the key
	 * @return the value associated with the given key if the key is in the symbol table
	 *     and {@code null} if the key is not in the symbol table
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value get(String key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		Node x = get(root, key, 0);
		if (x == null) return null;
		return (Value) x.val;
	}

	/**
	 * Does this symbol table contain the given key?
	 * @param key the key
	 * @return {@code true} if this symbol table contains {@code key} and
	 *     {@code false} otherwise
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public boolean contains(String key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}

	private Node get(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) return x;
		char c = key.charAt(d);
		return get(x.next[c], key, d + 1);
	}

	/**
	 * Inserts the key-value pair into the symbol table, overwriting the old value
	 * with the new value if the key is already in the symbol table.
	 * If the value is {@code null}, this effectively deletes the key from the symbol table.
	 * @param key the key
	 * @param val the value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(String key, Value val) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (val == null) delete(key);
		else root = put(root, key, val, 0);
	}

	private Node put(Node x, String key, Value val, int d) {
		if (x == null) x = new Node();
		if (d == key.length()) {
			if (x.val == null) n++;
			x.val = val;
			return x;
		}
		char c = key.charAt(d);
		x.next[c] = put(x.next[c], key, val, d + 1);
		return x;
	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size() {
		return n;
	}

	/**
	 * Is this symbol table empty?
	 * @return {@code true} if this symbol table is empty and {@code false} otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns all keys in the symbol table as an {@code Iterable}.
	 * To iterate over all of the keys in the symbol table named {@code st},
	 * use the foreach notation: {@code for (Key key : st.keys())}.
	 * @return all keys in the symbol table as an {@code Iterable}
	 */
	public Iterable<String> keys() {
		return keysWithPrefix("");
	}

	/**
	 * Returns all of the keys in the set that start with {@code prefix}.
	 * @param prefix the prefix
	 * @return all of the keys in the set that start with {@code prefix},
	 *     as an iterable
	 */
	public Iterable<String> keysWithPrefix(String prefix) {
		Queue<String> results = new Queue<String>();
		Node x = get(root, prefix, 0);
		collect(x, new StringBuilder(prefix), results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, Queue<String> results) {
		if (x == null) return;
		if (x.val != null) results.enqueue(prefix.toString());
		for (char c = 0; c < R; c++) {
			prefix.append(c);
			collect(x.next[c], prefix, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	/**
	 * Returns all of the keys in the symbol table that match {@code pattern},
	 * where . symbol is treated as a wildcard character.
	 * @param pattern the pattern
	 * @return all of the keys in the symbol table that match {@code pattern},
	 *     as an iterable, where . is treated as a wildcard character.
	 */
	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> results = new Queue<String>();
		collect(root, new StringBuilder(), pattern, results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
		if (x == null) return;
		int d = prefix.length();
		if (d == pattern.length() && x.val != null)
			results.enqueue(prefix.toString());
		if (d == pattern.length())
			return;
		char c = pattern.charAt(d);
		if (c == '.') {
			for (char ch = 0; ch < R; ch++) {
				prefix.append(ch);
				collect(x.next[ch], prefix, pattern, results);
				prefix.deleteCharAt(prefix.length() - 1);
			}
		} else {
			prefix.append(c);
			collect(x.next[c], prefix, pattern, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	/**
	 * Returns the string in the symbol table that is the longest prefix of {@code query},
	 * or {@code null}, if no such string.
	 * @param query the query string
	 * @return the string in the symbol table that is the longest prefix of {@code query},
	 *     or {@code null} if no such string
	 * @throws IllegalArgumentException if {@code query} is {@code null}
	 */
	public String longestPrefixOf(String query) {
		if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
		int length = longestPrefixOf(root, query, 0, -1);
		if (length == -1) return null;
		else return query.substring(0, length);
	}

	// returns the length of the longest string key in the subtrie
	// rooted at x that is a prefix of the query string,
	// assuming the first d character match and we have already
	// found a prefix match of given length (-1 if no such match)
	private int longestPrefixOf(Node x, String query, int d, int length) {
		if (x == null) return length;
		if (x.val != null) length = d;
		if (d == query.length()) return length;
		char c = query.charAt(d);
		return longestPrefixOf(x.next[c], query, d + 1, length);
	}

	/**
	 * Removes the key from the set if the key is present.
	 * @param key the key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void delete(String key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		root = delete(root, key, 0);
	}

	private Node delete(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) {
			if (x.val != null) n--;
			x.val = null;
		} else {
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c], key, d + 1);
		}

		// remove subtrie rooted at x if it is completely empty
		if (x.val != null) return x;
		for (int c = 0; c < R; c++)
			if (x.next[c] != null)
				return x;
		return null;
	}
}
