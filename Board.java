/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {

    private int[][] tiles;
    private int N;
    int manhattanDistance;

    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        N = tiles.length;

        this.tiles = tiles;
        manhattanDistance = manhattan();
    }

    public String toString() {
        String board = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board += String.format("%2d ", tiles[i][j]);
            }
            board += "\n";
        }
        return board;
    }

    public int tileAt(int row, int col) {
        return tiles[row][col];
    }

    public int size() {
        return N;
    }

    public int hamming() {
        int pos = 0;
        int hamming = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pos++;
                if (tiles[i][j] != 0 && tiles[i][j] != pos)
                    hamming++;

            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int val = tiles[i][j];
                if (val == 0) continue;
                // goal position
                int x = (val - 1) / N;
                int y = (val - 1) % N;

                manhattan += Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return manhattan;
    }

    public boolean isSolvable() {
        int inversions = 0;
        int row = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    continue;
                }
                for (int k = i; k < N; k++) {
                    for (int l = (k == i ? j + 1 : 0); l < N; l++) {
                        if (tiles[k][l] != 0 && tiles[i][j] > tiles[k][l])
                            inversions++;
                    }
                }
            }
        }

        if ((N % 2 != 0 && inversions % 2 == 0) || (N % 2 == 0 && (inversions + row) % 2 != 0))
            return true;
        return false;

    }

    // is this board the goal board?
    public boolean isGoal() {

        return hamming() == 0;
    }

    public boolean equals(Object y) {

        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (that.size() != this.size())
            return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                if (tileAt(i, j) != that.tileAt(i, j))
                    return false;

        }
        return true;
    }


    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<Board>();

        int up = 0, down = 0, left = 0, right = 0;
        int zi = 0, zj = 0;
        outer:
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    zi = i;
                    zj = j;
                    up = i - 1;
                    down = i + 1;
                    left = j - 1;
                    right = j + 1;
                    break outer;
                }
            }
        }

        if (inRange(up))
            neighbors.add(neighbor(this, up, zi, zj, "col"));
        if (inRange(down))
            neighbors.add(neighbor(this, down, zi, zj, "col"));
        if (inRange(left))
            neighbors.add(neighbor(this, left, zi, zj, "row"));
        if (inRange(right))
            neighbors.add(neighbor(this, right, zi, zj, "row"));

        return neighbors;
    }


    private boolean inRange(int k) {
        if (k >= 0 && k <= N - 1)
            return true;
        return false;
    }

    private Board neighbor(Board base, int swipe, int zrow, int zcol, String direction) {
        int n = base.N;
        int tiles[][] = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                tiles[i][j] = base.tileAt(i, j);
        }

        if (Objects.equals(direction, "row"))
            swapRow(tiles, swipe, zrow, zcol);
        else if (Objects.equals(direction, "col"))
            swapCol(tiles, swipe, zrow, zcol);
        return new Board(tiles);
    }

    // swaps two neighbor elements in the same row
    private void swapRow(int a[][], int ncol, int zrow, int zcol) {
        int t = a[zrow][zcol];
        a[zrow][zcol] = a[zrow][ncol];
        a[zrow][ncol] = t;


    }

    // swaps two neighbor elements in the same colum
    private void swapCol(int a[][], int nrow, int zrow, int zcol) {
        int t = a[zrow][zcol];
        a[zrow][zcol] = a[nrow][zcol];
        a[nrow][zcol] = t;

    }
}
