Biconnectivity: An articulation vertex (or cut vertex) is a vertex whose removal increases the number of connected components. A graph is biconnected if it has no articulation vertices. Biconnected uses depth-first search to find the articulation vertices. It takes time proportional to V + E in the worst case. 

Input Format :
First line of input contains number of Vertices.
Second line of input contains number of Edges.
From third line onwards, each line contains two vertices separated by space, which are to be connected in Undirected Graph.

Output Format:
Print the vertices in ascending order, if it is a articulation vertex.


Note : Use Bag.java to store adjacent vertices, to print them in output order.