Depth-first orders: Implement Depth-first search search that visits each vertex exactly once. Three vertex orderings are of interest in typical applications:
Preorder: Put the vertex on a queue before the recursive calls.
Postorder: Put the vertex on a queue after the recursive calls.
Reverse postorder: Put the vertex on a stack after the recursive calls.


Input Format:
First line contains integer V number of Edges.
Second line contains integer E number of Edges.
From third line onwards, each line contains two vertices separated by space, which are to be connected in Graph.
 
Output Format:
Print Preorder, postorder and Reverse postorder
Preorder:  
0 2 3 1 
Postorder: 
3 2 1 0 
Reverse postorder: 
0 1 2 3
