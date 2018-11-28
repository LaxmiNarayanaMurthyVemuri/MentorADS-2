# 4.3.24 Reverse-delete algorithm. Develop an implementation that computes the MST
# as follows: Start with a graph containing all of the edges. Then repeatedly go through
# the edges in decreasing order of weight. For each edge, check if deleting that edge will
# disconnect the graph; if not, delete it. Prove that this algorithm computes the MST.

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

class Edge(object):

    """
      Minimum spanning tree edge representation.
    """

    def __init__(self, vertex_a, vertex_b, weight):
        self._vertex_a = vertex_a
        self._vertex_b = vertex_b
        self._weight = weight

    def either(self):
        return self._vertex_a

    def other(self, vertex):
        if vertex not in (self._vertex_a, self._vertex_b):
            return None
        return self._vertex_a if vertex == self._vertex_b else self._vertex_b

    def __eq__(self, other):
        return self._weight == other._weight

    def __lt__(self, other):
        return self._weight < other._weight

    def __le__(self, other):
        return self._weight <= other._weight

    def __hash__(self):
        return hash('{} {} {}'.format(self._vertex_a, self._vertex_b, self._weight))

    @property
    def weight(self):
        return self._weight

    @weight.setter
    def weight(self, w):
        self._weight = w

    def __repr__(self):
        return "{}-{} {}".format(self._vertex_a, self._vertex_b, self._weight)

class MaxPQ(object):

    def __init__(self, data=None):
        self._pq = []
        if data:
            for item in data:
                self.insert(item)

    def is_empty(self):
        return len(self._pq) == 0

    def size(self):
        return len(self._pq)

    def swim(self, pos):
        while pos > 0 and self._pq[(pos - 1) // 2] < self._pq[pos]:
            self._pq[(pos - 1) // 2], self._pq[pos] = self._pq[pos], self._pq[(pos - 1) // 2]
            pos = (pos - 1) // 2

    def sink(self, pos):
        length = len(self._pq) - 1
        while 2 * pos + 1 <= length:
            index = 2 * pos + 1
            if index < length and self._pq[index] < self._pq[index + 1]:
                index += 1
            if self._pq[pos] >= self._pq[index]:
                break
            self._pq[index], self._pq[pos] = self._pq[pos], self._pq[index]
            pos = index

    def insert(self, val):
        self._pq.append(val)
        self.swim(len(self._pq) - 1)

    def del_max(self):
        max_val = self._pq[0]
        last_index = len(self._pq) - 1
        self._pq[0], self._pq[last_index] = self._pq[last_index], self._pq[0]
        self._pq.pop(last_index)
        self.sink(0)
        return max_val

    def max_val(self):
        return self._pq[0]

class Bag(object):

    '''
      Bag data structure linked-list implementation.
    >>> bag = Bag()
    >>> bag.size()
    0
    >>> bag.is_empty()
    True
    >>> for i in range(1, 6):
    ...     bag.add(i)
    ...
    >>> bag.size()
    5
    >>> [i for i in bag]
    [5, 4, 3, 2, 1]
    >>> bag.is_empty()
    False
    '''

    def __init__(self):
        self._first = None
        self._size = 0

    def __iter__(self):
        node = self._first
        while node is not None:
            yield node.val
            node = node.next_node

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

class Queue(object):

    """
      Queue FIFO data structure linked-list implementation.
    >>> q = Queue()
    >>> q.is_empty()
    True
    >>> q.size()
    0
    >>> q.enqueue(1)
    >>> q.enqueue(2)
    >>> q.enqueue(3)
    >>> q.enqueue(4)
    >>> q.size()
    4
    >>> q.is_empty()
    False
    >>> [item for item in q]
    [1, 2, 3, 4]
    >>> q.dequeue()
    1
    >>> q.dequeue()
    2
    >>> q.dequeue()
    3
    >>> q.dequeue()
    4
    >>> q.dequeue()
    >>> q.dequeue()
    >>> q.size()
    0
    >>> old = Queue()
    >>> for i in range(5):
    ...     old.enqueue(i)
    ...
    >>> new_queue = Queue(old)
    >>> [i for i in new_queue]
    [0, 1, 2, 3, 4]
    >>> new_queue.enqueue(6)
    >>> [i for i in old]
    [0, 1, 2, 3, 4]
    >>> [i for i in new_queue]
    [0, 1, 2, 3, 4, 6]
    """

    # 1.3.41 practice

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

class EdgeWeightedGraph(object):

    """
      Undirected graph with weighted edge implementation, a new class named Edge will
    represent a weighted edge.
    >>> test_data = ((4, 5, 0.35), (4, 7, 0.37), (5, 7, 0.28), (0, 7, 0.16), (1, 5, 0.32),
    ...              (0, 4, 0.38), (2, 3, 0.17), (1, 7, 0.19), (0, 2, 0.26), (1, 2, 0.36),
    ...              (1, 3, 0.29), (2, 7, 0.34), (6, 2, 0.4), (3, 6, 0.52), (6, 0, 0.58),
    ...              (6, 4, 0.93))
    >>> ewg = EdgeWeightedGraph()
    >>> for a, b, weight in test_data:
    ...    edge = Edge(a, b, weight)
    ...    ewg.add_edge(edge)
    ...
    >>> ewg.edges_size()
    16
    >>> ewg.vertices_size()
    8
    >>> [e for e in ewg.adjacent_edges(5)]
    [1-5 0.32, 5-7 0.28, 4-5 0.35]
    >>> ewg
    8 vertices, 16 edges.
    0: 6-0 0.58, 0-2 0.26, 0-4 0.38, 0-7 0.16
    1: 1-3 0.29, 1-2 0.36, 1-7 0.19, 1-5 0.32
    2: 6-2 0.4, 2-7 0.34, 1-2 0.36, 0-2 0.26, 2-3 0.17
    3: 3-6 0.52, 1-3 0.29, 2-3 0.17
    4: 6-4 0.93, 0-4 0.38, 4-7 0.37, 4-5 0.35
    5: 1-5 0.32, 5-7 0.28, 4-5 0.35
    6: 6-4 0.93, 6-0 0.58, 3-6 0.52, 6-2 0.4
    7: 2-7 0.34, 1-7 0.19, 0-7 0.16, 5-7 0.28, 4-7 0.37
    <BLANKLINE>
    """

    def __init__(self, graph=None):
        self._adj = defaultdict(Bag)
        self._edges_size = 0

        if graph:
            self._adj = copy.deepcopy(graph._adj)
            self._edges_size = graph.edges_size()

    def edges_size(self):
        return self._edges_size

    def vertices_size(self):
        return len(self._adj.keys())

    def add_edge(self, edge):
        a = edge.either()
        b = edge.other(a)
        self._adj[a].add(edge)
        self._adj[b].add(edge)
        self._edges_size += 1

    def adjacent_edges(self, vertex):
        return self._adj[vertex]

    def vertices(self):
        return self._adj.keys()

    def edges(self):
        result = set()
        for v in self.vertices():
            for edge in self.adjacent_edges(v):
                if edge.other(v) != v:
                    result.add(edge)
        return result

    # 4.3.17 practice, implement a method printing out the whole graph.
    def __repr__(self):
        print_string = '{} vertices, {} edges.\n'.format(
            self.vertices_size(), self.edges_size())
        for v in self.vertices():
            try:
                lst = ', '.join([vertex for vertex in self._adj[v]])
            except TypeError:
                lst = ', '.join([str(vertex) for vertex in self._adj[v]])
            print_string += '{}: {}\n'.format(v, lst)
        return print_string

class ReverseDeleteMST(object):
    def __init__(self, graph):
        deleted_edges = set()
        max_pq = MaxPQ(graph.edges())
        self._mst = Queue()
        while not max_pq.is_empty():
            edge = max_pq.del_max()
            if self._graph_connected(graph, edge, deleted_edges):
                deleted_edges.add(edge)
            else:
                self._mst.enqueue(edge)

    def _graph_connected(self, graph, candidate_edge, deleted_edges):
        self._marked = defaultdict(bool)
        start_vertex = candidate_edge.either()
        self._marked[start_vertex] = True
        for edge in graph.adjacent_edges(start_vertex):
            a = edge.other(start_vertex)
            if edge is not candidate_edge and edge not in deleted_edges and not self._marked[a]:
                self._dfs(graph, a, candidate_edge, deleted_edges)
        connected_vertices = len([v for v in self._marked if self._marked[v]])
        return graph.vertices_size() == connected_vertices

    def _dfs(self, graph, vertex, candidate_edge, deleted_edges):
        self._marked[vertex] = True
        for edge in graph.adjacent_edges(vertex):
            v = edge.other(vertex)
            if edge is not candidate_edge and edge not in deleted_edges and not self._marked[v]:
                self._dfs(graph, v, candidate_edge, deleted_edges)

    def edges(self):
        return self._mst



# test_data = ((0,1,10),(0,2,6),(0,3,5),(1,3,15),(2,3,4))

# test_data = ((0, 1, 4),(0, 7, 8),(1, 2, 8),(1, 7, 11),(2, 3, 7),(2, 8, 2),(2, 5, 4),(3, 4, 9),(3, 5, 14),(4, 5, 10),(5, 6, 2),(6, 7, 1),(6, 8, 6),(7, 8, 7))

# test_data = (
# (4, 5, 0.35), (4, 7, 0.37), (5, 7, 0.28), (0, 7, 0.16), (1, 5, 0.32),
#               (0, 4, 0.38), (2, 3, 0.17), (1, 7, 0.19), (0, 2, 0.26), (1, 2, 0.36),
#               (1, 3, 0.29), (2, 7, 0.34), (6, 2, 0.4), (3, 6, 0.52), (6, 0, 0.58),
#               (6, 4, 0.93))

ewg = EdgeWeightedGraph()
V = int(input())
E = int(input())
s = []
for i in range(0,E):
	s = input().split(" ")
	edge = Edge(int(s[0]),int(s[1]),int(s[2]))
	ewg.add_edge(edge)

rd_mst = ReverseDeleteMST(ewg)
lst = sorted([edge for edge in rd_mst.edges()])
for i in lst:
	print(i)