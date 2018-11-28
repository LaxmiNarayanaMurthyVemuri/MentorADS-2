import java.util.Scanner;
/**
 * Class for percolation.
 */
class Percolation {
    /**
     * WeightedQuickUnionUF Object.
     */
    private WeightedQuickUnionUF weightedQuickUnionUF;
    /**
     * Integer variable.
     */
    private int n;
    /**
     * Integer variable.
     */
    private int size;
    /**
     * Integer variable.
     */
    private int first;
    /**
     * Integer variable.
     */
    private int last;
    /**
     * Integer variable.
     */
    private int count;
    /**
     * Boolean array variable.
     */
    private boolean[] connected;
    /**
     * Constructs the object.
     *
     * @param      n1    The n 1
     */
    Percolation(final int n1) {
        this.n = n1;
        this.count = 0;
        this.size = n1 * n1;
        this.first = size;
        this.last = size + 1;
        connected = new boolean[size];
        weightedQuickUnionUF =
            new WeightedQuickUnionUF(size + 2);
        for (int i = 0; i < n; i++) {
            weightedQuickUnionUF.union(first, i);
            weightedQuickUnionUF.union(last, size - i - 1);
        }
    }

    /**
     * Searches for the first match.
     *
     * @param      i     { parameter_description }
     * @param      j     { parameter_description }
     *
     * @return     { description_of_the_return_value }
     */
    private int indexOf(final int i, final int j) {
        return n * (i - 1) + j - 1;
    }

    /**
     * Links open sites.
     *
     * @param      row     The row
     * @param      column  The column
     */
    private void linkOpenSites(final int row, final int column) {
        if (connected[column]
            && !weightedQuickUnionUF.connected(row, column)) {
            weightedQuickUnionUF.union(row, column);
        }
    }
    /**
     * open site (row, col) if it is not open already.
     *
     * @param      row   The row
     * @param      col   The col
     */
    public void open(final int row, final int col) {
        int index = indexOf(row, col);
        connected[index] = true;
        count++;
        int bottom = index + n;
        int top = index - n;
        if (n == 1) {
            weightedQuickUnionUF.union(first, index);
            weightedQuickUnionUF.union(last, index);
        }
        if (bottom < size) {
            linkOpenSites(index, bottom);
        }
        if (top >= 0) {
            linkOpenSites(index, top);
        }
        if (col == 1) {
            if (col != n) {
                linkOpenSites(index, index + 1);
            }
            return;
        }

        if (col == n) {
            linkOpenSites(index, index - 1);
            return;
        }

        linkOpenSites(index, index + 1);
        linkOpenSites(index, index - 1);

    }
    /**
     * Determines if open.
     *
     * @param      row   The row
     * @param      col   The col
     *
     * @return     True if open, False otherwise.
     */
    public boolean isOpen(final int row, final int col) {
        return connected[indexOf(row, col)];
    }
/*    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        return false;
    }
    // number of open sites
    */

    /**
     * { function_description }.
     *
     * @return     { description_of_the_return_value }
     */
    public int numberOfOpenSites() {
        return count;
    }
    /**
     * { function_description }.
     *
     * @return     does the system percolate?
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected(first, last);
    }
}


// You can implement the above API to solve the problem
/**
 * Class for solution.
 */
public final class Solution {

    /**
     * Constructs the object.
     */
    private Solution() {
        // We are using it for check style purpose.
    }

    /**
     * client method.
     *
     * @param      args  The arguments
     */
    public static void main(final String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = Integer.parseInt(scan.nextLine());
        Percolation percolation = new Percolation(n);
        while (scan.hasNext()) {
            String[] tokens = scan.nextLine().split(" ");
            percolation.open(Integer.parseInt(tokens[0]),
                             Integer.parseInt(tokens[1]));
        }

        System.out.println(percolation.percolates()
            && percolation.numberOfOpenSites() != 0);
    }
}
