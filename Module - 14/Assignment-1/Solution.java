import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		String[] words = loadWords();
		//Your code goes here...
	}

	public static String[] loadWords() {
		In in = new In("/Files/dictionary-algs4.txt");
		String[] words = in.readAllStrings();
		return words;
	}
}