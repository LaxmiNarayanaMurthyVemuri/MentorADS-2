import java.util.Scanner;
import java.util.Arrays;

public class Solution {
	public static void main(String[] args) {
		String[] words = loadWords();
		Scanner scan = new Scanner(System.in);
		TST<Integer> tst = new TST<Integer>();
		int value = 0;
		for (String word : words) {
			SuffixArray sa = new SuffixArray(word);
			for (int i = 0; i < word.length(); i++) {
				tst.put(sa.select(i), value++);
			}
		}
		for(String each : tst.keysWithPrefix(scan.nextLine())) {
			System.out.println(each);
		}
	}

	public static String[] loadWords() {
		In in = new In("/Files/dictionary-algs4.txt");
		String[] words = in.readAllStrings();
		return words;
	}
}