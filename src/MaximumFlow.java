/**
 * Student ID - 2019710
 * Name - Vishani Raveendran
 */

import java.lang.*;
import java.util.*;
import java.util.LinkedList;

public class MaximumFlow {
    //----------------Number of vertices in graph-----------------//
    static int Vertices;

    //------Returns true if there is a path from source to sink in residual graph. Also fills parent[] to store the path-----//
    static boolean bfs(int rGraph[][], int source, int sink, int parent[]) {
        //------Create a visited array and mark all vertices not visited------//
        boolean visited[] = new boolean[Vertices];
        for (int i = 0; i < Vertices; ++i) {
            visited[i] = false;
        }

        //-----queue of nodes to explore (BFS to FIFO queue)-------//
        LinkedList<Integer> vertexQueue = new LinkedList<Integer>();
        //-----add source node-----//
        vertexQueue.add(source);
        visited[source] = true;
        parent[source] = -1;

        //-----------Standard BFS Loop-----------//
        while (vertexQueue.size() != 0) {

            //--------get a node from the queue-------//
            int u = vertexQueue.remove();

            //---------Check all edges to v by checking all values in the row of the matrix--------//
            for (int v = 0; v < Vertices; v++) {
                //--------residualGraph[u][v] > 0 means there actually is an edge-------//
                if (visited[v] == false && rGraph[u][v] > 0) {
                    vertexQueue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        //------------Returns true or false if path is found from source to vertex-----------//
        return visited[sink];
    }

    //--------Returns max flow from Source to Sink in a graph----------//
    static int fordFulkerson(int graph[][], int source, int sink) {
        //--------------There is no flow initially-----------//
        int max_flow = 0;
        //--------Iterate nodes to loop over the matrix------//
        int u = 0;
        int v = 0;
        //---------Holds parent of a node when a path is found (By BFS method)-------//
        int parent[] = new int[Vertices];

        //--------if there's an edge between nodes i and j. 0 =  no edge, positive value = capacity of the edge--------------//
        int residualGraph[][] = new int[Vertices][Vertices];

        //------copy over every edge from the original graph into residual graph----------//
        for (u = 0; u < Vertices; u++) {
            for (v = 0; v < Vertices; v++) {
                residualGraph[u][v] = graph[u][v];
            }
        }


        //----------if a path exists from source to sink-----------//
        while (bfs(residualGraph, source, sink, parent)) {

            //------------------Finds bottleneck value by looping over path from BFS using parent[] array-----------//
            int bottleNeckFlow = Integer.MAX_VALUE; //First it will be set to maximum value possible and updated if its smaller

            //------To loop backward through the path using parent[] array-----//
            for (v = sink; v != source; v = parent[v]) {
                //--------gets the previous node in the path-------//
                u = parent[v];
                //----------------Minimum value of the previous bottleneck and the capacity of the new edge-----------//
                bottleNeckFlow = Math.min(bottleNeckFlow, residualGraph[u][v]);

            }

            //-----------Updates residual graph capacities & reverse edges along the path------------//
            for (v = sink; v != source; v = parent[v]) {
                //--------loop backwards over the path--------//
                u = parent[v];
                residualGraph[u][v] -= bottleNeckFlow;//back edge
                residualGraph[v][u] += bottleNeckFlow;//forward edge
            }

            //-------------smallest flow found in the augmentation path to the overall flow will be added-------------//
            max_flow += bottleNeckFlow;
        }

        //---------Return the overall flow-----------//
        return max_flow;
    }

    int[][] deleteNode(int n, int graph[][]) {
        int i, j;
        for (i = 0; i < graph.length; i++) {
            if (i == n) {
                for (j = 0; j < graph.length; j++) {
                    graph[n][j] = 0;
                    graph[j][n] = 0;
                }
            }
        }
        return graph;
    }

    public static void main(String[] args) throws java.lang.Exception {
        long start = System.currentTimeMillis();
        int numberOfNodes = 0;


        //--------Reading file data---------//
        In in = new In("ladder_3.txt");
        int[] a = in.readAllInts();

        //-------Declaring first line of the file as a node------//
        numberOfNodes = a[0];
        Vertices = a[0];

        int size = a.length;

        //------------Assigning the source,sink,capacity as 0 initially-----------//
        int source = 0, sink = 0, capacity = 0;

        //---------------Printing the Number of nodes---------------//
        System.out.println("Number of nodes --> " + numberOfNodes);

        int[][] graph = new int[numberOfNodes][numberOfNodes];

        //-------------Printing statement for each edge--------//
        for (int i = 1; i < size; i = i + 3) {
            //--------------Assigning values from file--------------//
            source = a[i];
            sink = a[i + 1];
            capacity = a[i + 2];
            System.out.println("Edge from node " + source + " to node " + sink + " with capacity " + capacity);
            graph[source][sink] = capacity;
        }

        for (int[] innerArray : graph) {
            System.out.println(Arrays.toString(innerArray));

        }

        MaximumFlow maximumFlow = new MaximumFlow();

        System.out.println("The maximum possible flow is " + maximumFlow.fordFulkerson(graph, 0, numberOfNodes - 1));
        long now = System.currentTimeMillis();
        double elapsed = (now - start) / 1000.0;

        System.out.println("Elapsed time = " + elapsed + " seconds");

        Scanner userInput = new Scanner(System.in);
        System.out.println("1. Delete node \n2. Exit");
        System.out.println("Select option : ");
        int option = userInput.nextInt();
        while (option != 2) {
            if (option == 1) {
                System.out.println("Which node do u want to delete : ");
                int deleteNode = userInput.nextInt();
                int[][] newGraph = maximumFlow.deleteNode(deleteNode, graph);
                System.out.println("The maximum possible flow : " + maximumFlow.fordFulkerson(newGraph, source, sink));
                System.out.println("Elapsed time = " + elapsed + " seconds");

            } else {
                System.out.println("Invalid input");
            }
            System.out.println("1. Delete node \n2. Exit");
            System.out.println("Select option : ");
            option = userInput.nextInt();

        }

    }

}
