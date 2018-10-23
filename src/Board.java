
import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;
// import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;

public class Board {

    private final int[][] blocks;
    private final int dimension;

    public Board(int[][] blocks) {
        this.blocks = arrayCopy(blocks);
        this.dimension = blocks.length;
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        int h = 0;
        int initValue = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    initValue++;
                    continue;
                }
                if (blocks[i][j] != initValue) {
                    h++;
                }
                initValue++;
            }
        }
        return h;
    }

    public int manhattan() {
        int m = 0;
        int initValue = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                initValue++;
                if (blocks[i][j] == initValue || blocks[i][j] == 0) {
                    continue;
                }
                int hor = (blocks[i][j] - 1) / dimension;
                int vert = (blocks[i][j] - 1) % dimension;
                m += Math.abs(j - vert) + Math.abs(i - hor);
            }
        }
        return m;
    }

    public boolean isGoal() {
        int initValues = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != initValues) {
                    return i == dimension - 1 && j == dimension - 1 && blocks[i][j] == 0;
                }
                initValues++;
            }
        }
        return true;
    }

    /* public Board twin() {
        int[] values = new int[4];
        int counter = 0;
        int[] blankSquare = findBlankSquare();
        int blankSquare1D = blankSquare[0] * dimension + blankSquare[1];
        int random1 = StdRandom.uniform(dimension * dimension - 1);
        if (blankSquare1D <= random1) {
            random1++;
        }
        int random2 = StdRandom.uniform(dimension * dimension - 2);
        if (random1 <= random2) {
            random2++;
        }
        if (blankSquare1D <= random2) {
            random2++;
            if (random1 == random2) {
                random2++;
            }
        }
        // StdOut.printf("random1 ---> %d, random2 ---> %d, blankSquare1D ---> %d\n", random1, random2, blankSquare1D);
        int[] randoms = new int[]{random1, random2};
        for (int i = 0; i < 2; i++) {
            int row = randoms[i] / dimension;
            int column = randoms[i] % dimension;
            values[counter++] = row;
            values[counter++] = column;
        }
        int[][] twinBlocks = arrayCopy(blocks);
        int temp = twinBlocks[values[0]][values[1]];
        twinBlocks[values[0]][values[1]] = twinBlocks[values[2]][values[3]];
        twinBlocks[values[2]][values[3]] = temp;
        return new Board(twinBlocks);
    } */
    public Board twin() {
        int[][] twinBlocks = arrayCopy(blocks);
        for (int i = 0; i < dimension * dimension; i++) {
            int row1 = i / dimension;
            int column1 = i % dimension;
            int row2 = (i + 1) / dimension;
            int column2 = (i + 1) % dimension;
            // StdOut.printf("(%d, %d), (%d, %d)\n", row1, column1, row2, column2);
            if (twinBlocks[row1][column1] != 0 && twinBlocks[row2][column2] != 0) {
                int temp = twinBlocks[row1][column1];
                twinBlocks[row1][column1] = twinBlocks[row2][column2];
                twinBlocks[row2][column2] = temp;
                break;
            }
        }
        return new Board(twinBlocks);
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> listNeighbors = new ArrayList<>();
        int[] blankSquare = findBlankSquare();
        if (blankSquare[1] + 1 <= dimension - 1) {
            Board b = moveSquare(blankSquare, 0, 1, 0, 0);
            listNeighbors.add(b);
        }
        if (blankSquare[1] - 1 >= 0) {
            Board b = moveSquare(blankSquare, 0, 0, 0, 1);
            listNeighbors.add(b);
        }
        if (blankSquare[0] + 1 <= dimension - 1) {
            Board b = moveSquare(blankSquare, 0, 0, 1, 0);
            listNeighbors.add(b);
        }
        if (blankSquare[0] - 1 >= 0) {
            Board b = moveSquare(blankSquare, 1, 0, 0, 0);
            listNeighbors.add(b);
        }
        /* for (Board b : listNeighbors) {
            StdOut.printf("%s\n", b);
        } */
        return listNeighbors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int length = blocks.length;
        sb.append(length + "\n");
        int maxNumberOfDigits = numberOfDigits(length * length - 1);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int value = blocks[i][j];
                int lengthDifference = maxNumberOfDigits - numberOfDigits(value);
                sb.append(" ");
                for (int k = 0; k < lengthDifference; k++) {
                    sb.append(" ");
                }
                sb.append(blocks[i][j]);
                sb.append(" ");
                if (j == length - 1) {
                    sb.append("\n");
                }
            }
        }
        // sb.append("\nmanhattan ---> " + this.manhattan() + "\nhamming ---> " + this.hamming() + "\n");
        return sb.toString();
    }

    private static int numberOfDigits(int number) {
        /* int counter = 1;
        int denominator = 10;
        while (true) {
            int temp = number / denominator;
            if (temp == 0) {
                break;
            }
            counter++;
            denominator = denominator * 10;
        }
        return counter; */
        return (String.valueOf(number)).length();
    }

    private static int[][] arrayCopy(int[][] array) {
        int dimension = array.length;
        int[][] newArray = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    private int[] findBlankSquare() {
        int[] blankSquare = new int[2];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    blankSquare[0] = i;
                    blankSquare[1] = j;
                }
            }
        }
        return blankSquare;
    }

    private Board moveSquare(int[] blankSquare, int up, int right, int down, int left) {
        int[][] arrayCopy = arrayCopy(blocks);
        int temp = arrayCopy[blankSquare[0] - up + down][blankSquare[1] - left + right];
        arrayCopy[blankSquare[0] - up + down][blankSquare[1] - left + right] = 0;
        arrayCopy[blankSquare[0]][blankSquare[1]] = temp;
        Board b = new Board(arrayCopy);
        return b;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !object.getClass().equals(this.getClass())) {
            return false;
        }
        Board b = (Board) object;
        if (this.dimension != b.dimension) {
            return false;
        }
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.blocks[i][j] != b.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        In in = new In("8puzzle/puzzle3x3-01.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        /* Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.printf("isGoal() ---> %s\n", initial.isGoal());
        StdOut.printf("%s\n", initial.toString());
        StdOut.printf("manhattan() ---> %d\n", initial.manhattan());
        Board twin1 = initial.twin();
        StdOut.printf("TWIN 1\n%s\n", twin1);
        StdOut.printf("hamming() ---> %d\n", initial.hamming());
        StdOut.printf("manhattan() ---> %d\n", initial.manhattan());
        Board twin2 = initial.twin();
        StdOut.printf("TWIN 2\n%s\n", twin2);
        StdOut.printf("twin1 == twin2 ---> %s\n", twin1.equals(twin2)); */
        /* int counter = 0;
        for (int i = 0; i < 100000; i++) {
            int j = i + 1;
            Board bTwin = b.twin();
            StdOut.printf("%d. %s\n", j, b.equals(bTwin));
            if (b.equals(bTwin)) {
                counter++;
            }
        }
        StdOut.printf("\n\nCOUNTER ---> %d\nrandom1 == random2 ---> %s\nrandom1 == blankSquare ---> %s\nrandom2 == blankSquare ---> %s\n", counter); */
    }
}
