Hamiltonian path in DAGs. Given a DAG, design a linear-time algorithm to determine whether there is a directed path that visits each vertex exactly once. Solution: Compute a topological sort and check if there is an edge between each consecutive pair of vertices in the topological order. 


Input Format:
First line contains integer V number of Edges.
Second line contains integer E number of Edges.
From third line onwards, each line contains two vertices separated by space, which are to be connected in Graph.
 
Output Format:
Print True, If Hamiltonian path exists else False.
