import java.util.*;
/*************************************************************************
 *  Compilation:  javac Solution.java
 *  Execution:    java Solution < input.txt
 *  Dependencies: Digraph.java Stack.java StdOut.java In.java
 *  Data files:   http://algs.cs.princeton.edu/42digraph/tinyDG.txt
 *                http://algs.cs.princeton.edu/42digraph/tinyDAG.txt
 *
 *  Finds a directed cycle in a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java Solution tinyDG.txt 
 *  Cycle: 3 5 4 3 
 *
 *  %  java Solution tinyDAG.txt 
 *  No cycle
 *
 *************************************************************************/

public class Solution {
    private boolean[] marked;        // marked[v] = has vertex v been marked?
    private int[] edgeTo;            // edgeTo[v] = previous vertex on path to v
    private boolean[] onStack;       // onStack[v] = is vertex on the stack?
    private Stack<Integer> cycle;    // directed cycle (or null if no such cycle)

    public Solution(Digraph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);

        // check that digraph has a cycle
        assert check(G);
    }


    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {

            // short circuit if directed cycle found
            if (cycle != null) return;

            //found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle()        { return cycle != null;   }
    public Iterable<Integer> cycle() { return cycle;           }


    // certify that digraph is either acyclic or has a directed cycle
    private boolean check(Digraph G) {

        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) {
                    first = v;
                }
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int V = Integer.parseInt(scan.nextLine());
        int E = Integer.parseInt(scan.nextLine());
        Digraph G = new Digraph(V);

        for (int i = 0;i < E ;i++ ) {
            String line[] = scan.nextLine().split(" ");
            int v = Integer.parseInt(line[0]);
            int w = Integer.parseInt(line[1]);
            G.addEdge(v,w);
        }

        System.out.print(G);
        Solution finder = new Solution(G);
        if (finder.hasCycle()) {
            System.out.println("Cycle exists.");
        }
        else {
            System.out.println("Cycle doesn't exists.");
        }
    }

}
