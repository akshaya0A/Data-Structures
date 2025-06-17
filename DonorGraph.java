import java.util.*;

public class DonorGraph {
    private List<List<Match>> adjList;
    private int[] donorToBenefit;
    private int[][] matchScores;

    // The donatingTo array indicates which repient each donor is
    // affiliated with. Specifically, the donor at index i has volunteered
    // to donate a kidney on behalf of recipient donatingTo[i].
    // The matchScores 2d array gives the match scores associated with each
    // donor-recipient pair. Specificically, matchScores[x][y] gives the
    // HLA score for donor x and reciplient y.
    public DonorGraph(int[] donorToBenefit, int[][] matchScores) {
        this.donorToBenefit = donorToBenefit;
        this.matchScores = matchScores;
        adjList = new ArrayList<>();
        // first fill in all the empty lists
        for (int i = 0; i < matchScores[0].length; i++) {
            adjList.add(new ArrayList<>());
        }
        // build graph
        for (int donor = 0; donor < donorToBenefit.length; donor++) {
            int beneficiary = donorToBenefit[donor];
            for (int recipient = 0; recipient < matchScores[0].length; recipient++) {
                if (matchScores[donor][recipient] >= 60 && recipient != beneficiary) {
                    adjList.get(beneficiary).add(new Match(donor, beneficiary, recipient));
                }
            }
        }
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public boolean isAdjacent(int start, int end) {
        for (Match m : adjList.get(start)) {
            if (m.recipient == end)
                return true;
        }
        return false;
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public int getDonor(int beneficiary, int recipient) {
        for (Match m : adjList.get(beneficiary)) {
            if (m.recipient == recipient)
                return m.donor;
        }
        return -1;
    }

    // returns a chain of matches to make a donor cycle
    // which includes the given recipient.
    // Returns an empty list if no cycle exists.
    // Returns a chain of matches that forms a cycle including the given recipient.
    // Returns an empty list if no such cycle exists.
    public List<Match> findCycle(int start) {
        List<Match> path = new ArrayList<>();
        boolean[] visited = new boolean[adjList.size()];

        if (findCycle(start, start, visited, path)) {
            return path;
        } else {
            return new ArrayList<>();
        }
    }

    // Helper method to find a cycle in the graph
    private boolean findCycle(int curr, int target, boolean[] visited, List<Match> path) {
        visited[curr] = true;
        for (Match m : adjList.get(curr)) {
            int neighbor = m.recipient;
            // If we find a cycle and the neighbor is the target
            if (neighbor == target && !path.isEmpty()) {
                // Complete the cycle
                path.add(m);
                return true;
            }
            // If the neighbor hasn't been visited, explore it
            if (!visited[neighbor]) {
                path.add(m);
                // System.out.println("Moving to neighbor: " + neighbor);
                // since neighbor is different it goes to the next level of graph basically and
                // checks
                if (findCycle(neighbor, target, visited, path)) {
                    return true;
                }
                // Backtrack if not part of a cycle
                path.remove(path.size() - 1);
            }
        }
        return false;
    }

    // returns true or false to indicate whether there
    // is some cycle which includes the given recipient.
    // returns true or false to indicate whether there
    // is some cycle which includes the given recipient.
    public boolean hasCycle(int recipient) {
        boolean[] visited = new boolean[adjList.size()];
        boolean[] done = new boolean[adjList.size()];
        return hasCycle(recipient, recipient, visited, done);
    }

    // Helper method for the hasCycle
    private boolean hasCycle(int curr, int target, boolean[] visited, boolean[] done) {
        visited[curr] = true;
        done[curr] = true;
        // look at all neighbors
        for (Match m : adjList.get(curr)) {
            int neighbor = m.recipient;
            // if havent visited the node yet use recursion
            if (!visited[neighbor]) {
                if (hasCycle(neighbor, target, visited, done)) {
                    return true;
                }
                // if the neighbor is our recipenet its in cycle
            } else if (neighbor == target) {
                return true;
            }
        }
        done[curr] = false;
        return false;
    }

}
