

#!/usr/bin/env python
# -*- encoding:UTF-8 -*-
import copy
import doctest
from collections import defaultdict
 
class Node(object):
 
    def __init__(self, val):
        self._val = val
        self.next_node = None
 
    @property
    def val(self):
        return self._val
 
    @val.setter
    def val(self, value):
        self._val = value
 
    @property
    def next_node(self):
        return self._next_node
 
    @next_node.setter
    def next_node(self, node):
        self._next_node = node
 
#Stack
class Stack(object):
 
    def __init__(self):
        self._first = None
        self._size = 0
 
    def __iter__(self):
        node = self._first
        while node:
            yield node.val
            node = node.next_node
 
    def is_empty(self):
        return self._first is None
 
    def size(self):
        return self._size
 
    def push(self, val):
        node = Node(val)
        old = self._first
        self._first = node
        self._first.next_node = old
        self._size += 1
 
    def pop(self):
        if self._first:
            old = self._first
            self._first = self._first.next_node
            self._size -= 1
            return old.val
        return None
 
    # 1.3.7 practice
    def peek(self):
        if self._first:
            return self._first.val
        return None
 
#Queue
class Queue(object):
 
    def __init__(self, q=None):
        self._first = None
        self._last = None
        self._size = 0
        if q:
            for item in q:
                self.enqueue(item)
 
    def __iter__(self):
        node = self._first
        while node:
            yield node.val
            node = node.next_node
 
    def is_empty(self):
        return self._first is None
 
    def size(self):
        return self._size
 
    def enqueue(self, val):
        old_last = self._last
        self._last = Node(val)
        self._last.next_node = None
        if self.is_empty():
            self._first = self._last
        else:
            old_last.next_node = self._last
        self._size += 1
 
    def dequeue(self):
        if not self.is_empty():
            val = self._first.val
            self._first = self._first.next_node
            if self.is_empty():
                self._last = None
            self._size -= 1
            return val
        return None
 
#Bag
class Bag(object):
 
    def __init__(self):
        self._first = None
        self._size = 0
 
    def __iter__(self):
        node = self._first
        while node is not None:
            yield node.val
            node = node.next_node
 
    def __contains__(self, item):
        tmp = self._first
        while tmp:
            if tmp == item:
                return True
        return False
 
    def add(self, val):
        node = Node(val)
        old = self._first
        self._first = node
        self._first.next_node = old
        self._size += 1
 
    def is_empty(self):
        return self._first is None
 
    def size(self):
        return self._size
 
#Digraph
class Digragh(object):
 
    """
      Directed graph implementation. Every edges is directed, so if v is
    reachable from w, w might not be reachable from v.There would ba an
    assist data structure to mark all available vertices, because
    self._adj.keys() is only for the vertices which outdegree is not 0.
    Directed graph is almost the same with Undirected graph,many codes
    from Gragh can be reusable.
    >>> # 4.2.6 practice
    >>> graph = Digragh()
    >>> test_data = [(4, 2), (2, 3), (3, 2), (6, 0), (0, 1), (2, 0),
    ...              (11, 12), (12, 9), (9, 10), (9, 11), (8, 9), (10, 12),
    ...              (11, 4), (4, 3), (3, 5), (7, 8), (8, 7), (5, 4), (0, 5),
    ...              (6, 4), (6, 9), (7, 6)]
    >>> for a, b in test_data:
    ...     graph.add_edge(a, b)
    ...
    >>> graph.vertices_size()
    13
    >>> graph.edges_size()
    22
    >>> [i for i in graph.get_adjacent_vertices(2)]
    [0, 3]
    >>> [j for j in graph.get_adjacent_vertices(6)]
    [9, 4, 0]
    >>> [v for v in graph.vertices()]
    [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    >>> graph
    13 vertices, 22 edges
    0: 5 1
    2: 0 3
    3: 5 2
    4: 3 2
    5: 4
    6: 9 4 0
    7: 6 8
    8: 7 9
    9: 11 10
    10: 12
    11: 4 12
    12: 9
    <BLANKLINE>
    >>>
    """
 
    def __init__(self, graph=None):
        self._edges_size = 0
        self._adj = defaultdict(Bag)
        self._vertices = set()
 
        # 4.2.3 practice, generate graph from another graph.
        if graph:
            self._adj = copy.deepcopy(graph._adj)
            self._vertices_size = graph.vertices_size()
            self._edges_size = graph.edges_size()
            self._vertices = copy.copy(graph.vertices())
 
    def vertices_size(self):
        return len(self._vertices)
 
    def edges_size(self):
        return self._edges_size
 
    def add_edge(self, start, end):
        # 4.2.5 practice, parallel edge and self cycle are not allowed
        if self.has_edge(start, end) or start == end:
            return
        self._vertices.add(start)
        self._vertices.add(end)
        self._adj[start].add(end)
        self._edges_size += 1
 
    def get_adjacent_vertices(self, vertex):
        return self._adj[vertex]
 
    def vertices(self):
        return self._vertices
 
    def reverse(self):
        reverse_graph = Digragh()
        for vertex in self.vertices():
            for adjacent_vertex in self.get_adjacent_vertices(vertex):
                reverse_graph.add_edge(adjacent_vertex, vertex)
        return reverse_graph
 
    # 4.2.4 practice, add has_edge method for Digraph
    def has_edge(self, start, end):
        edge = next((i for i in self._adj[start] if i == end), None)
        return edge is not None
 
    def __repr__(self):
        s = str(len(self._vertices)) + ' vertices, ' + str(self._edges_size) + ' edges\n'
        for k in self._adj:
            try:
                lst = ' '.join([vertex for vertex in self._adj[k]])
            except TypeError:
                lst = ' '.join([str(vertex) for vertex in self._adj[k]])
            s += '{}: {}\n'.format(k, lst)
        return s
 
class DepthFirstOrder(object):
 
    def __init__(self, graph):
        self._pre = Queue()
        self._post = Queue()
        self._reverse_post = Stack()
        self._marked = defaultdict(bool)
 
        for v in graph.vertices():
            if not self._marked[v]:
                self.dfs(graph, v)
 
    def dfs(self, graph, vertex):
        self._pre.enqueue(vertex)
        self._marked[vertex] = True
        for v in graph.get_adjacent_vertices(vertex):
            if not self._marked[v]:
                self.dfs(graph, v)
 
        self._post.enqueue(vertex)
        self._reverse_post.push(vertex)
 
    def prefix(self):
        return self._pre
 
    def postfix(self):
        return self._post
 
    def reverse_postfix(self):
        return self._reverse_post
 
class DirectedCycle(object):
 
    def __init__(self, graph):
        self._marked = defaultdict(bool)
        self._edge_to = {}
        self._on_stack = defaultdict(bool)
        self._cycle = Stack()
        for v in graph.vertices():
            if not self._marked[v]:
                self.dfs(graph, v)
 
    def dfs(self, graph, vertex):
        self._on_stack[vertex] = True
        self._marked[vertex] = True
 
        for v in graph.get_adjacent_vertices(vertex):
            if self.has_cycle():
                return
            elif not self._marked[v]:
                self._edge_to[v] = vertex
                self.dfs(graph, v)
            elif self._on_stack[v]:
                tmp = vertex
                while tmp != v:
                    self._cycle.push(tmp)
                    tmp = self._edge_to[tmp]
                self._cycle.push(v)
                self._cycle.push(vertex)
        self._on_stack[vertex] = False
 
    def has_cycle(self):
        return not self._cycle.is_empty()
 
    def cycle(self):
        return self._cycle
 
class Topological(object):
 
    def __init__(self, graph):
        cycle_finder = DirectedCycle(graph)
        self._order = None
        if not cycle_finder.has_cycle():
            df_order = DepthFirstOrder(graph)
            self._order = df_order.reverse_postfix()
 
    def order(self):
        return self._order
 
    def is_DAG(self):
        return self._order is not None
 
 
 
 
def is_hamiltonian_path(graph):
    ts = Topological(graph)
    if ts.order() == None:
        return False
    vertices = [v for v in ts.order()]
    has_path = True
    for i in range(len(vertices) - 1):
        if not graph.has_edge(vertices[i], vertices[i+1]):
            has_path = False
    return has_path
 

v = int(input())
e = int(input())
digraph = Digragh()
for i in range(0, e):
    s = input().split(" ")
    a = int(s[0])
    b = int(s[1])
    digraph.add_edge(a, b)
print(is_hamiltonian_path(digraph))
