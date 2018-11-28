import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Iterator;

interface Graph {
    public int V();
    public int E();
    public void addEdge(int v, int w);
    public Iterable<Integer> adj(int v);
    public boolean hasEdge(int v, int w);
}


class AdjacencyList implements Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    
    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     * param V the number of vertices
     *
     * @param  V number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public AdjacencyList(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge v-w to this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (v == w) return;
        if (hasEdge(v, w)) return;
            E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    public boolean hasEdge(int v, int w) {
        for (int each : adj(w)) {
            if (each == v) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the vertices adjacent to vertex {@code v}.
     *
     * @param  v the vertex
     * @return the vertices adjacent to vertex {@code v}, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }


    /**
     * Returns a string representation of this graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists
     */
    public String display(String[] keys) {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges" + NEWLINE);
        if (E == 0) {
            s.append("No edges");
            s.append(NEWLINE);
            return s.toString().trim();
        }
        for (int v = 0; v < V; v++) {
            s.append(keys[v] + ": ");
            for (int w : adj[v]) {
                s.append(keys[w] + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}

class AdjacencyMatrix implements Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private boolean[][] adj;
    
    // empty graph with V vertices
    public AdjacencyMatrix(int V) {
        if (V < 0) throw new IllegalArgumentException("Too few vertices");
        this.V = V;
        this.E = 0;
        this.adj = new boolean[V][V];
    }

    // number of vertices and edges
    public int V() { return V; }
    public int E() { return E; }


    // add undirected edge v-w
    public void addEdge(int v, int w) {
        if (v == w) return;
        if (!hasEdge(v, w)) {
            E++;
        }
        adj[v][w] = true;
        adj[w][v] = true;
    }

    // does the graph contain the edge v-w?
    public boolean hasEdge(int v, int w) {
        return adj[v][w];
    }

    // return list of neighbors of v
    public Iterable<Integer> adj(int v) {
        return new AdjIterator(v);
    }

    // support iteration over graph vertices
    private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
        private int v;
        private int w = 0;

        AdjIterator(int v) {
            this.v = v;
        }

        public Iterator<Integer> iterator() {
            return this;
        }

        public boolean hasNext() {
            while (w < V) {
                if (adj[v][w]) return true;
                w++;
            }
            return false;
        }

        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return w++;
        }

        public void remove()  {
            throw new UnsupportedOperationException();
        }
    }


    // string representation of Graph - takes quadratic time
    public String display(String[] keys) {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges" + NEWLINE);
        if (E == 0) {
            s.append("No edges");
            s.append(NEWLINE);
            return s.toString().trim();
        }
        for (int v = 0; v < V; v++) {
            for (int w = 0; w < V; w++) {
                if (hasEdge(v, w)) {
                    s.append(1 + " ");
                } else {
                    s.append(0 + " ");
                }
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}

public class Solution {
    /**
     * Unit tests the {@code Graph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String representation = scan.nextLine();
        switch(representation) {
            case "List": 
                int v = Integer.parseInt(scan.nextLine());
                int e = Integer.parseInt(scan.nextLine());
                String[] keys = scan.nextLine().split(",");
                AdjacencyList al = new AdjacencyList(v);

                for (int i = 0; i < e; i++) {
                    String[] split = scan.nextLine().split(" ");
                    al.addEdge(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }
                System.out.print(al.display(keys));
                break;
            
            case "Matrix":
                v = Integer.parseInt(scan.nextLine());
                e = Integer.parseInt(scan.nextLine());
                keys = scan.nextLine().split(",");
                AdjacencyMatrix am = new AdjacencyMatrix(v);
                for (int i = 0; i < e; i++) {
                    String[] split = scan.nextLine().split(" ");
                    am.addEdge(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }
                System.out.print(am.display(keys));
                break;
            
            default:
            break;
        }
    }
}
