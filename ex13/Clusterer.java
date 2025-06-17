import java.util.*;

public class Clusterer {
    private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
    private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
    private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
    private double cost; // the distance between the closest pair of clusters

    public Clusterer(double[][] distances, int k) {
        adjList = new ArrayList<>();
        // make adjacency list for each vertex
        for (int i = 0; i < distances.length; i++) {
            adjList.add(new ArrayList<>());
        }
        // make distances into undirected weighted graph
        // only have the positive weights
        for (int i = 0; i < distances.length; i++) {
            for (int j = i + 1; j < distances[0].length; j++) {
                if (distances[i][j] > 0) {
                    adjList.get(i).add(new WeightedEdge<>(i, j, distances[i][j]));
                    adjList.get(j).add(new WeightedEdge<>(j, i, distances[i][j]));
                }
            }
        }
        // build the MST
        prims(0);
        // System.out.print(mstAdjList);
        // form k clusters from the MST
        makeKCluster(k);

    }

    // implement Prim's algorithm to find a MST of the graph.
    // in my implementation I used the mstAdjList field to store this.
    // Implements Prim's algorithm using a PriorityQueue (Min-Heap)
    private void prims(int start) {
        boolean[] visited = new boolean[adjList.size()];

        // make empty MST adjacency list
        mstAdjList = new ArrayList<>();
        for (int i = 0; i < adjList.size(); i++) {
            mstAdjList.add(new ArrayList<>());
        }

        // minheap priority queue to pick edge with the smallest weight
        PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>();

        // start node is visited and add all its edges to the queue
        visited[start] = true;
        for (WeightedEdge<Integer, Double> edge : adjList.get(start)) {
            pq.add(edge);
        }

        // add the smallest edges till all nodes connected
        while (!pq.isEmpty()) {
            // Get the smallest-weight edge
            WeightedEdge<Integer, Double> edge = pq.remove();

            if (!visited[edge.destination]) {
                // new node visited
                visited[edge.destination] = true;

                // Add edge to MST in both directions to avoid missing clusters
                mstAdjList.get(edge.source).add(edge);
                mstAdjList.get(edge.destination).add(new WeightedEdge<>(edge.destination, edge.source, edge.weight));

                // Add all leaving edges from the new node
                for (WeightedEdge<Integer, Double> nextE : adjList.get(edge.destination)) {
                    if (!visited[nextE.destination]) {
                        pq.add(nextE);
                    }
                }
            }
        }
    }

    // After making the minimum spanning tree, use this method to
    // remove its k-1 heaviest edges, then assign integers
    // to clusters based on which nodes are still connected by
    // the remaining MST edges.
    private void makeKCluster(int k) {
        List<WeightedEdge<Integer, Double>> edges = new ArrayList<>();
        int n = mstAdjList.size();
        boolean[][] seen = new boolean[n][n];

        // get unique edges from the MST adjacency list
        for (int i = 0; i < n; i++) {
            for (WeightedEdge<Integer, Double> edge : mstAdjList.get(i)) {
                if (!seen[i][edge.destination]) {
                    edges.add(edge);
                    // do for both directions
                    seen[i][edge.destination] = true;
                    seen[edge.destination][i] = true;
                }
            }
        }

        // Sort edges by descending order by weight with the heaviest weight first
        edges.sort((a, b) -> Double.compare(b.weight, a.weight));

        // Remove the k-1 heaviest edges to form k clusters
        for (int i = 0; i < k - 1 && i < edges.size(); i++) {
            WeightedEdge<Integer, Double> edge = edges.get(i);

            // Remove edge from source to destination
            List<WeightedEdge<Integer, Double>> x = mstAdjList.get(edge.source);
            for (int j = 0; j < x.size(); j++) {
                if (x.get(j).destination == edge.destination) {
                    x.remove(j);
                    break;
                }
            }

            // Remove edge from destination to source
            List<WeightedEdge<Integer, Double>> y = mstAdjList.get(edge.destination);
            for (int j = 0; j < y.size(); j++) {
                if (y.get(j).destination == edge.source) {
                    y.remove(j);
                    break;
                }
            }
        }

        boolean[] visited = new boolean[n];
        clusters = new ArrayList<>();

        // BFS to find connected clusters
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                // store cureent cluster
                List<Integer> cluster = new ArrayList<>();
                // queue for BFS traveersal
                List<Integer> queue = new ArrayList<>();
                queue.add(i);
                visited[i] = true;

                int front = 0;
                // procrss nodes till none left
                while (front < queue.size()) {
                    int node = queue.get(front++);
                    cluster.add(node);
                    // go thru all the connected neightbors for the node in MST
                    for (WeightedEdge<Integer, Double> edge : mstAdjList.get(node)) {
                        // if neightbor wasn't visited mark it
                        if (!visited[edge.destination]) {
                            visited[edge.destination] = true;
                            queue.add(edge.destination);
                        }
                    }
                }
                // add the found cluster
                clusters.add(cluster);
            }
        }

        // Set cost to the weight of the smallest removed edge
        if (k - 2 < edges.size() && k > 1) {
            cost = edges.get(k - 2).weight;
        } else {
            cost = 0;
        }
    }

    public List<List<Integer>> getClusters() {
        return clusters;
    }

    public double getCost() {
        return cost;
    }

}
