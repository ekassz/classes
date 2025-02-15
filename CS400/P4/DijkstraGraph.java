// --== CS400 File Header Information ==--
// Name: Emili Robles
// Email: ejrobles@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: <name of your lecturer>
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes.  This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
    extends BaseGraph<NodeType,EdgeType>
    implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph.  The final node in this path is stored in it's node
     * field.  The total cost of this path is stored in its cost field.  And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in it's node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;
        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }
        public int compareTo(SearchNode other) {
            if( cost > other.cost ) return +1;
            if( cost < other.cost ) return -1;
            return 0;
        }
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations.  The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *         or when either start or end data do not correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // TODO: implement in step 6
        //nodes is the graph
        if(!nodes.containsKey(start) || !nodes.containsKey(end)){
            throw new NoSuchElementException("Couldn't find the start or end node in the graph");
        }

        //Priority Queue (distance ascending order)
        PriorityQueue<SearchNode> pq = new PriorityQueue<>(Comparator.comparingDouble(node1 -> node1.cost));
        Map<NodeType, SearchNode> searchNodes = new HashMap<>();

        SearchNode startSearchNode = new SearchNode(nodes.get(start), 0, null);
        pq.add(startSearchNode);
        searchNodes.put(start, startSearchNode);

        while(!pq.isEmpty()){
            SearchNode currSearchNode = pq.poll();
            if(currSearchNode.node.data.equals(end)){
                return currSearchNode;
            }
            for(Edge edge : currSearchNode.node.edgesLeaving){
                NodeType nexNodeType = edge.successor.data;
                double newCost = currSearchNode.cost + edge.data.doubleValue();

                if(!searchNodes.containsKey(nexNodeType) || searchNodes.get(nexNodeType).cost > newCost){
                    SearchNode nextSearchNode = new SearchNode(edge.successor, newCost, currSearchNode);

                    searchNodes.put(nexNodeType, nextSearchNode);
                    pq.add(nextSearchNode);
                }
            }
        }
        throw new NoSuchElementException("No path from start to end was found :(");
        
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value.  This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path.  This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // TODO: implement in step 7
        //shortest path
        SearchNode endNode = computeShortestPath(start, end);

        //new list for this path 
        List<NodeType> path = new LinkedList<>();

        //follow predecessor links from end node to start node
        //we add each nodes' data to start
        for(SearchNode node = endNode; node!= null; node = node.predecessor){
            path.add(0, node.node.data);
        }
        //new path for shortest path
        return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data.  This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // TODO: implement in step 7
        //call the computeShortestPath method

        SearchNode endNode = computeShortestPath(start, end);

        //cost of path is stored in cost field of the end node!
        return endNode.cost;
    }

    // TODO: implement 3+ tests in step 8.
    /**
     * First test will be to check if the path is correct , as well as the
     * start and end nodes when a graph is constructed.
     * 
     * 1.Create a test that makes use of an example graph you previously 
     * traced by hand, and confirm that the results of your implementation match what you previously computed by hand.
     */
    @Test
    public void testShortestPath(){
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        //add nodes
        // Nodes = "A" "B" "C" "D"
        // Edges = "A"-"B":3.0, "B"-"C":2.0, "C"-"D":4.0, "A"-"D":10.0

        //add nodes
       graph.insertNode("A");
       graph.insertNode("B");
       graph.insertNode("C");
       graph.insertNode("D");
       

        // //make the edges
        graph.insertEdge("A", "B", 3.0);
        graph.insertEdge("B", "C", 2.0);
        graph.insertEdge("C", "D", 4.0);
        graph.insertEdge("A", "D", 10.0);

        //test to see if the path is right and start and end nodes
        List<String> expectedPath = Arrays.asList("A", "B", "C", "D");
        assertEquals(expectedPath, graph.shortestPathData("A", "D"));


    }

    /**
     * Testing another path with it's cost
     * Using the same graph, now I'm just testing on Nodes B - C - D 
     * with a cost of 6
     * 
     * 2. Create another test using the same graph as you did for the 
     * test above, but check the cost and sequence of data along the shortest path between a different start and end node.
     */
    @Test
    public void testShortestPathCost(){
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        //add nodes
        // Nodes = "A" "B" "C" "D"
        // Edges = "A"-"B":3.0, "B"-"C":2.0, "C"-"D":4.0, "A"-"D":10.0

        //add nodes
       graph.insertNode("A");
       graph.insertNode("B");
       graph.insertNode("C");
       graph.insertNode("D");
       

        // //make the edges
        graph.insertEdge("A", "B", 3.0);
        graph.insertEdge("B", "C", 2.0);
        graph.insertEdge("C", "D", 4.0);
        graph.insertEdge("A", "D", 10.0);

        // Expected path from B to D is B -> C -> D and cost is 6.0
        List<String> expectedPath = Arrays.asList("B", "C", "D");
        assertEquals(expectedPath, graph.shortestPathData("B", "D"));
        assertEquals(6.0, graph.shortestPathCost("B", "D"), 0.0001);


    }

    /**
     * Testing when there isn't a short path, using the same graph and modifying it
     * to see if it catches that there's no path when trying to go from C - D
     * 
     * Create a test that checks the behavior of your implementation when the node 
     * that you are searching for a path between exist in the graph, but there is 
     * no sequence of directed edges that connects them from the start to the end.
     * @throws NoSuchElementException
     */
    @Test(expected = NoSuchElementException.class)
    public void testNoShortPath() throws NoSuchElementException{
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        //add nodes
        // Nodes = "A" "B" "C" "D"
        // Edges = "A"-"B":3.0, "B"-"C":2.0, "C"-"D":4.0, "A"-"D":10.0

        //add nodes
       graph.insertNode("A");
       graph.insertNode("B");
       graph.insertNode("C");
       graph.insertNode("D");
       

        // //make the edges
        graph.insertEdge("A", "B", 3.0);
        graph.insertEdge("B", "C", 2.0);
        graph.insertEdge("A", "D", 10.0);

        //assertThrows(NoSuchElementException.class, () -> { //can only use this in junit5
            graph.shortestPathData("C", "D");
        //});


    }
}
