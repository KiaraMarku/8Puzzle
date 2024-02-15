/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.Arrays;
import java.util.List;

public class Solver {


    public class searchNode implements Comparable {
        Board board;
        searchNode prev;
        int move;
        int hammingPriority;
        int manhattanPriority;


        public searchNode(Board board, searchNode prev, int move) {
            this.board = board;
            this.prev = prev;
            this.move = move;
            this.hammingPriority = board.hamming() + move;
            this.manhattanPriority = board.manhattanDistance + move;
        }

        public int compareTo(Object x) {
            searchNode that = (searchNode) x;
            if (this.hammingPriority != that.hammingPriority)
                return this.hammingPriority - that.hammingPriority;
            return this.manhattanPriority - that.manhattanPriority;
        }
    }

    Board initial;

    public Solver(Board initial) {
        if (!initial.isSolvable())
            throw new IllegalArgumentException("Unsolvable board");
        this.initial = initial;
    }


    public Iterable<Board> solution() {

        MinPQ<searchNode> queue = new MinPQ<searchNode>();

        searchNode state = new searchNode(initial, null, 0);
        queue.insert(state);

        while (true) {
            state = queue.delMin();

            if (state.board.isGoal())
                break;

            for (Board option : state.board.neighbors()) {
                if (state.prev == null || !option.equals(state.prev.board))
                    queue.insert(new searchNode(option, state, state.move + 1));

            }
        }
        int n = state.move;
        Board[] moves = new Board[n + 1];
        while (n >= 0) {
            moves[n--] = state.board;
            state = state.prev;
        }

        return Arrays.asList(moves);
    }

    public String moves() {
        List moves = (List) solution();
        return " " + (moves.size() - 1);
    }
}
