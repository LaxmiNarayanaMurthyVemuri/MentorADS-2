
public class Solution {
	public static void main(String[] args) {
		String synsets = StdIn.readString();
		String hypernyms = StdIn.readString();
		String queries = StdIn.readString();
		try {
		WordNet d = new WordNet("Files/" + synsets, "Files/" + hypernyms);
			if (queries.equals("Graph") ) {
				System.out.println(d.getDigraph());
			} else {
				while (!StdIn.isEmpty()) {
					String v = StdIn.readString();
					String w = StdIn.readString();
					if (v.equals(null) || w.equals(null)) {
						throw new java.lang.IllegalArgumentException();
					}
					String ancestor = d.sap(v, w);
					int distance = d.distance(v, w);
					System.out.printf("distance = %d, ancestor = %s\n", distance, ancestor);
				}
			}
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}