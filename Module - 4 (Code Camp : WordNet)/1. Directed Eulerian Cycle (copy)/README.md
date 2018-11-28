Directed Eulerian cycle. An Eulerian cycle is a directed cycle that contains each edge exactly once. Write a graph client Euler that finds an Eulerian cycle or reports that no such tour exists. Hint: Prove that a digraph G has a directed Eulerian cycle if and only if G is connected and each vertex has its indegree equal to its outdegree. 

Input Format:
First line contains integer t number of test cases.
Second line contains integer V number of Edges.
Third line contains integer E number of Edges.
Next E lines contain 2 integers separated by comma.
 
Output Format:
Print true if Eulerian cycle exists otherwise false

Sample Input
3
5
6
0 4
0 0
0 3
3 0
3 0
4 2
5
10
0 3
0 2
0 0
0 0
0 0
1 0
2 4
2 2
3 4
4 1
5
10
0 2
0 2
0 2
0 4
2 1
2 0
2 3
3 0
4 0
4 0

Sample Output:
false
false
false