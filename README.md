# Pokemon game

**Authors: Asif Rot & Maoz Lev**

![](https://stock.wikimini.org/w/images/2/2c/Pok%C3%A9mon.gif)

File list
------------

api.directed_weighted_graph - Interface

api.DWGraph_DS - directed_weighted_graph implementation

api.dw_graph_algorithms - Interface

api.DWGraph_Algo - dw_graph_algorithms implementation

api.edge_data - Interface

api.EdgeData - edge_data implementation

api.edge_location - Interface

api.EdgeLocation - edge_location implementation

api.geo_location - Interface

api.GeoLocation - geo_location implementation

api.node_data - Interface

api.NodeData - node_data implementation

api.game_service - Interface not to be touched

gameClient.CL_Agent - Implements object

gameClient.My_Pokemon - Implements object

gameClient.My_Arena - Function to implement the game

gameClient.Frame - Implements the game frame

gameClient.Panel - Implements the game login panel

gameClient.Ex2 - Implements game_service

Readme.txt - This file

In this file, we will explain:
- Why we chose the data structures we used in this project.
- How we implemented the interfaces.
- Which algorithms we used.


Data structures we used in the project
----------

**HashMap**

we used it in order to search, find, add, connect and remove (Basically we used it almost in every function).
This data structure helped us to do all those functions with time complexity O(1) and it made the work much cleaner and easier.
Also, after a long search this is almost the only (if not the only) data structure that can get time complexity O(1) in these functions.

## Part A

I. Implementations
----------

A. NodeData
	This class defines the node and the values it has.
	Each node will receive a unique key and its values.

B. DWGraph_DS
	This class defines the graph.
	It has hashmaps in order to search, add, remove nodes and connect edges between nodes in the graph. All that in time complexity O(1).
	
C. DWGraph_Algo
	This class defines all the algorithms used in the graph.
	It has hashmaps in order to go through all nodes in the graph.
  One hashmap is for a for loop that checks if we already been in this node, and the other one is to set the weights for every edge.

II. Algorithms
----------

A. DFS - Depth-first search
	We decided implementing DFS algorithm for traversing or searching the graph.
  This algorithm starts by selecting some arbitrary node as the root node in the graph.
  It starts at the source node and explores all the neighbor nodes at the present depth prior to moving on to the nodes at the next depth level.
	This is one of the most efficient ways to go through all the nodes in the graph, and its complexity time is O(|V| + |E|).
  V is the number of nodes and E is the number of edges in the graph.
	
B. Tarjan's strongly connected components algorithm
	The algorithm goes over a directed graph and produces a partition of the graph's vertices into the graph's strongly connected components.
	The main idea is to use DFS algorithm, with DFS, we will visit every node of the graph exactly once, declining to revisit any node that has already been visited.
	Thus, the collection of search trees is a spanning forest of the graph.
	The strongly connected components will be recovered as certain subtrees of this forest.

**What we wanted to achieve:**

If we receive one component - The graph is strongly connected.

C. Dijkstra algorithm is an algorithm for finding the shortest paths between nodes in a graph.
	At first, we initialize all the vertices distance to infinity.
	In the algorithm loop - as long as there are vertices in the queue:
  
- Each neighbor of that node, will be updated to the minimal value of dist.
    
- Adding the node which has the shortest distance to the queue.
		
		
### Functions on DWGraph_Algo:

A. isconnected: This function use the Tarjan Algorithm to check if the graph strongly connected.

B. Shortest_path: This function use Dijkstra Algorithm to find the shortest path on the graph and the weight of it.

## Part B

In this part, we implement gameClient and we will explain more about it in the Wiki:

[Click here](https://github.com/maozlev/OOP_Ex2/wiki/Pokemons-game-%E2%80%90-Explanation-of-the-game)


