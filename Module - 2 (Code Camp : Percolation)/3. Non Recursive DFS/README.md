Write a program Non recursive DFS that implements depth-first search with an explicit stack instead of recursion.

Here is an alternate implementation suggested by Bin Jiang in the early 1990s. The only extra memory is for a stack of vertices but that stack must support arbitrary deletion (or at least the movement of an arbitrary item to the top of the stack). 

Input Format :
First line of input contains number of Vertices.
Second line of input contains number of Edges.
Third line of input contains source vertex where the DFS to be started.
From fourth line onwards, each line contains two vertices separated by space, which are to be connected in Undirected Graph.

Output Format:
Print the stack object (toString()) before popping the element from stack.


Note : Use Bag.java to store adjacent vertices, to print them in output order.