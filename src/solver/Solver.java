package solver;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

public class Solver {

    private SearchNode solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        MinPQ<SearchNode> minPQTwin = new MinPQ<>();
        SearchNode sn = new SearchNode(initial, 0, null);
        boolean done = false;
        SearchNode snTwin = new SearchNode(initial.twin(), 0, null);
        minPQ.insert(sn);
        minPQTwin.insert(snTwin);
        while (!done) {
            SearchNode current = minPQ.min();
            SearchNode currentTwin = minPQTwin.min();
            if (current.isGoal()) {
                solution = current;
                break;
            }
            if (currentTwin.isGoal()) {
                break;
            }
            Iterable<Board> listNeighbors = current.board.neighbors();
            Iterable<Board> listNeighborsTwin = currentTwin.board.neighbors();
            for (Board b : listNeighbors) {
                if (current.previous != null) {
                    if (b.equals(current.previous.board)) {
                        continue;
                    }
                }
                SearchNode node = new SearchNode(b, current.moves + 1, current);
                minPQ.insert(node);
            }
            for (Board b : listNeighborsTwin) {
                if (currentTwin.previous != null) {
                    if (b.equals(currentTwin.previous.board)) {
                        continue;
                    }
                }
                SearchNode node = new SearchNode(b, currentTwin.moves + 1, current);
                minPQTwin.insert(node);
            }
            minPQ.delMin();
            minPQTwin.delMin();
        }
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return solution.moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        int moves = moves();
        ArrayList<Board> listSolution = new ArrayList<>(moves);
        for (int i = 0; i < moves + 1; i++) {
            listSolution.add(null);
        }
        int i = 0;
        SearchNode node = solution;
        while (node != null) {
            listSolution.add(moves - i, node.board);
            listSolution.remove(moves - i + 1);
            node = node.previous;
            i++;
        }
        return listSolution;
    }

    private class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final int moves;
        private final int manhattan;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattan = board.manhattan();
            this.priority = moves + manhattan;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        @Override
        public String toString() {
            return board.toString();
        }

        @Override
        public int compareTo(SearchNode t) {
            return this.priority - t.priority;
        }

    }

    public static void main(String[] args) {

        In in = new In("_data/puzzle3x3-05.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

}
