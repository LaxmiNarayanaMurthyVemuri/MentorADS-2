import java.util.Arrays;
import java.util.Hashtable;

class PageRank {
	
	private Digraph graph;
	private Digraph reverseGraph;
	private Double[] t0;
	private Double[] t1;

	public PageRank(Digraph graph) {
		this.graph = graph;
		this.reverseGraph = graph.reverse();
		System.out.println(graph);
		danglingPages();
		this.t0 = new Double[graph.V()];
		initPR();
		this.t1 = new Double[graph.V()];
		PR();
	}

	public double getPr(int v) {
		return t1[v];
	}

	private void danglingPages() {
		for(int v = 0; v < graph.V(); v++) {
			if (graph.outdegree(v) == 0) {
				for(int each = 0; each < graph.V(); each++) {
					if (v != each) {
						graph.addEdge(v, each);
						reverseGraph.addEdge(each, v);
					}
				}
			}
		}
	}

	public void initPR() {
		int v = graph.V();
		for (int i = 0; i < v; i++) {
			t0[i] = 1.0d/v;
		}
	}

	public void PR() {
		double d0 = 0, d1 = 0;
		int repeats = 0;
		while (repeats < 1000) {
			for (int w = 0; w < graph.V(); w++) {
				double sum = 0;
				for(int v : reverseGraph.adj(w)) {
					sum += t0[v]/graph.outdegree(v);
				}
				t1[w] = sum;
				d1 += Math.abs(t1[w] - t0[w]);
			}
			repeats++;
			d0 = d1;
			System.arraycopy(t1, 0, t0, 0, t1.length);
		}
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < t1.length; i++) {
			s += i + " - " + t1[i] + "\n";
		}
		return s;
	}
}

class WebSearch {

	private PageRank pr;
	private Hashtable <String, Bag<Integer>> hm;

	public WebSearch(PageRank pr, String fileName) {
		hm = new Hashtable<String, Bag<Integer>>();
		this.pr = pr;
		buildIndex(fileName);
	}

	public Hashtable buildIndex(String fileName) {
 		In in = new In(fileName);
		while (!in.isEmpty()) {
			String[] lines = in.readLine().split(":");
			int id = Integer.parseInt(lines[0]);
			String[] words = lines[1].split(" ");
			for(String word: words) {
				Bag<Integer> bag;
				if (!hm.containsKey(word)) {
					bag = new Bag<Integer>();
					hm.put(word, bag);
				} else {
					bag = hm.get(word);
				}
				bag.add(id);
			}
		}
		return hm;
	}

	//try this feature in google search. :-)
	public int iAmFeelingLucky(String query) {
		Bag<Integer> b = hm.get(query);
		if (b == null) {
			return -1;
		}
		int best = b.iterator().next();
		for(int each : hm.get(query)) {
			if (pr.getPr(best) < pr.getPr(each)){
				best = each;
			}
		}
		return best;
	}
}


public class Solution {
	public static void main(String[] args) {
		int vertices = Integer.parseInt(StdIn.readLine());
		Digraph graph = new Digraph(vertices);
		for (int j = 0; j < vertices; j++) {
			String[] line = StdIn.readLine().split(" ");
			int v = Integer.parseInt(line[0]);
			for(int i = 1; i < line.length; i++) {
				int w = Integer.parseInt(line[i]);
				graph.addEdge(v, w);
			}
		}
		
		PageRank pr = new PageRank(graph);
		System.out.print(pr);
		
		//part - 2
		String file = "WebContent.txt";
		WebSearch webSearch = new WebSearch(pr, file);
		while(!StdIn.isEmpty()) {
			String searchWord = StdIn.readLine().replace("q=","");
			System.out.println(webSearch.iAmFeelingLucky(searchWord));
		}
	}
}
