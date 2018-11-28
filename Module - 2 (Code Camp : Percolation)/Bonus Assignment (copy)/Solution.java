/*Lazy implementation of Dijkstra's algorithm. Develop an implementation of the lazy version of 
Dijkstra's algorithm that is described in the text. 
*/
/******************************************************************************
 *  Compilation:  javac Solution.java
 *  Execution:    java Solution V E
 *  Dependencies: EdgeWeightedDigraph.java MinPQ.java Stack.java DirectedEdge.java
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *  % java Solution 10 30
 *
 *  % java Solution < digraph6.txt
 *
 *  The disadvantage of this approach is that the number of items on the 
 *  priority queue can grow to be proportional to E instead of V.
 *
 *  The advantage is that it uses a MinPQ instead of an IndexMinPQ.
 *
 ******************************************************************************/

import java.util.Scanner;
import java.util.Comparator;

public class Solution {
    private boolean[] marked;        // has vertex v been relaxed?
    private double[] distTo;         // distTo[v] = length of shortest s->v path
    private DirectedEdge[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path
    private MinPQ<DirectedEdge> pq;  // PQ of fringe edges

    private class ByDistanceFromSource implements Comparator<DirectedEdge> {
        public int compare(DirectedEdge e, DirectedEdge f) {
            double dist1 = distTo[e.from()] + e.weight();
            double dist2 = distTo[f.from()] + f.weight();
            return Double.compare(dist1, dist2);
        }
    }

    // single-source shortest path problem from s
    public Solution(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) { 
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        pq = new MinPQ<DirectedEdge>(new ByDistanceFromSource());
        marked = new boolean[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];

        // initialize
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
        relax(G, s);

        // run Dijkstra's algorithm
        while (!pq.isEmpty()) {
            DirectedEdge e = pq.delMin();
            int v = e.from(), w = e.to();
            if (!marked[w]) relax(G, w);   // lazy, so w might already have been relaxed
        }
    }

    // relax vertex v
    private void relax(EdgeWeightedDigraph G, int v) {
        marked[v] = true;
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                pq.insert(e);
            }
        }
    }
        

    // length of shortest path from s to v, infinity if unreachable
    public double distTo(int v) {
        return distTo[v];
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {  
        return marked[v];
    }

    // return view of shortest path from s to v, null if no such path
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    public static void main(String[] args) {
    	Scanner scan = new Scanner(System.in);
    	int V = Integer.parseInt(scan.nextLine());
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);

        for (int i = 0; i < V; i++) {
        	String line[] = scan.nextLine().split(" ");
        	for (int j = 0; j < V; j++) {
	        	float weight = Float.parseFloat(line[j]);
	        	G.addEdge(new DirectedEdge(i, j, weight));
        	}
        }
        System.out.println(G.E());

        // run Dijksra's algorithm from vertex 0
        int s = 0;
        Solution spt = new Solution(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (spt.hasPathTo(v)) {
            	double roundOff = Math.round(spt.distTo(v) * 100.0) / 100.0;
                System.out.println(s +" to "+ v +" :"+ roundOff);
            }
        }
    }

}
