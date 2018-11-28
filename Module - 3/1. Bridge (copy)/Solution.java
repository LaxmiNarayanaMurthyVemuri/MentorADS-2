import java.util.Scanner;

public class Solution {
    private int bridges;      // number of bridges
    private int cnt;          // counter
    private int[] pre;        // pre[v] = order in which dfs examines v
    private int[] low;        // low[v] = lowest preorder of any vertex connected to v

    public Solution(Graph G) {
        low = new int[G.V()];
        pre = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            low[v] = -1;
        for (int v = 0; v < G.V(); v++)
            pre[v] = -1;
        
        for (int v = 0; v < G.V(); v++)
            if (pre[v] == -1)
                dfs(G, v, v);
    }

    public int components() { return bridges + 1; }

    private void dfs(Graph G, int u, int v) {
        pre[v] = cnt++;
        low[v] = pre[v];
        for (int w : G.adj(v)) {
            if (pre[w] == -1) {
                dfs(G, v, w);
                low[v] = Math.min(low[v], low[w]);
                if (low[w] == pre[w]) {
                    //System.out.println(v + "-" + w + " is a bridge");
                    bridges++;
                }
            }

            // update low number - ignore reverse of edge leading to v
            else if (w != u)
                low[v] = Math.min(low[v], pre[w]);
        }
    }

    // test client
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int V = Integer.parseInt(scan.nextLine()); 
        int e = Integer.parseInt(scan.nextLine()); 
        Graph G = new Graph(V);

        for (int i = 0;i < e ;i++ ) {
            String[] line = scan.nextLine().split(" ");
            int v1 = Integer.parseInt(line[0]);
            int w1 = Integer.parseInt(line[1]);
            G.addEdge(v1,w1);
        }

        Solution bridge = new Solution(G);
        if(bridge.components() == 1) {
            System.out.println("No Bridge");
        } else {
            System.out.println(bridge.components() - 1 + " Bridges");
        }
    }
}
