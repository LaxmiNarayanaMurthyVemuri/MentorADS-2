import java.util.Hashtable;
import java.util.Collections;
import java.util.ArrayList;


public class WordNet {

	private ArrayList<String> synsets;
	private Hashtable<String, Bag<Integer>> allWords;
	private SAP sap;
	private Digraph digraph;
	// constructor takes the name of the two input files
	public WordNet(String synsetsFileName, String hypernymsFileName) {
		this.synsets = new ArrayList<String>();
		this.allWords = new Hashtable<String, Bag<Integer>>();
		int countOfSynsets = parseSynsets(synsetsFileName);
		digraph = parseHypernyms(hypernymsFileName, countOfSynsets);
		//to be continued...
		//ShortestAncestralPath will take input as digraph.
		sap = new SAP(digraph);
	}

	public Digraph getDigraph() {
		return digraph;
	}

	private int parseSynsets(String synsetsFileName) {
		int count = 0;
		In in = new In(synsetsFileName);
		while (!in.isEmpty()) {
			String[] line = in.readLine().split(",");
			if (line.length < 2) {
				throw new java.lang.IllegalArgumentException("IllegalArgumentException");
			}
			int synsetId = Integer.parseInt(line[0]);
			synsets.add(synsetId, line[1]);
			//The individual nouns that constitute a synset are separated by spaces
			//To be continued...
			String[] words = line[1].split(" ");
			for (String word : words) {
				Bag bag = null;
				if (allWords.containsKey(word)) {
					bag = allWords.get(word);
				} else {
					bag = new Bag<Integer>();
				}
				bag.add(synsetId);
				allWords.put(word, bag);
			}
			count++;
		}
		in.close();
		return count;
	}

	private Digraph parseHypernyms(String hypernymsFileName, int countOfSynsets) {
		Digraph digraph = new Digraph(countOfSynsets);
		In in = new In(hypernymsFileName);
		while (!in.isEmpty()) {
			String superTest = in.readLine();
			if (superTest.equals(null)) {
				throw new java.lang.IllegalArgumentException("IllegalArgumentException");
			}
			String[] line = null;
			int v = 0;
			if (!superTest.contains(","))
				v = Integer.parseInt(superTest);
			line = superTest.split(",");
			v = Integer.parseInt(line[0]);
			for (int i = 1; i < line.length; i++) {
				int w = Integer.parseInt(line[i]);
				digraph.addEdge(v, w);
			}
		}
		in.close();

		DirectedCycle dc = new DirectedCycle(digraph);
		if (dc.hasCycle())
			throw new java.lang.IllegalArgumentException("Cycle detected");
		// check if one root
		int roots = 0;
		for (int v = 0; v < countOfSynsets; v++) {
			if (!digraph.adj(v).iterator().hasNext())
				roots++;
		}
		if (roots > 1)
			throw new java.lang.IllegalArgumentException("Multiple roots");
		return digraph;
	}


	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return Collections.list(allWords.keys());
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null || word.length() == 0) {
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		}
		return allWords.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		Bag<Integer> v = allWords.get(nounA);
		Bag<Integer> w = allWords.get(nounB);
		if (w == null || v == null) {
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		}
		return this.sap.length(v, w);
	}

	// a synset (second field of synsets.txt) that is
	// the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		Bag<Integer> v = allWords.get(nounA);
		Bag<Integer> w = allWords.get(nounB);
		int root = this.sap.ancestor(v, w);
		return this.synsets.get(root);
	}

}
