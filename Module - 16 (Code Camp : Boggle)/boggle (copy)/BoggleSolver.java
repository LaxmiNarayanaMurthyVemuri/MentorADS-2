import java.util.Set;
import java.util.TreeSet;

import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {

    private final TST<Integer> dictionaryTST;
    private boolean[][] marked;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictionaryTST = new TST<Integer>();
        // points for word are updated in this array.
        int[] points = {0, 0, 0, 1, 1, 2, 3, 5, 11};
        // Iterating each word and updating in TST, with points.
        for (String word : dictionary) {
            // If word is greater than or equal to 8, update 8 points.
            if (word.length() >= points.length - 1) {
                dictionaryTST.put(word, points[points.length - 1]);
            } else {
                dictionaryTST.put(word, points[word.length()]);
            }
        }
        //System.out.println(dictionaryTST.keysWithPrefix("QU"));

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new java.lang.IllegalArgumentException("board is null");
        }

        int rows = board.rows();
        int columns = board.cols();
        marked = new boolean[rows][columns];
        Set<String> words = new TreeSet<String>();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                String sequence = addLetter("", board.getLetter(row, column));
                // DFS starts here....
                dfs(board, row, column, sequence, words);
            }
        }
        return words;
    }

    private String addLetter(String to, char letter) {
        if (letter == 'Q') return to + "QU";
        else return to + letter;
    }

    private void dfs(BoggleBoard board, int row, int column, String prefix, Set<String> words) {
        if (!dictionaryTST.hasPrefix(prefix)) return;

        marked[row][column] = true;
        if (isValidWord(prefix)) {
            System.out.println(prefix);
            words.add(prefix);
        }

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (isValidIndex(i, j, board) && !marked[i][j]) {
                    String sequence = addLetter(prefix, board.getLetter(i, j));
                    // update the prefix by special case
                    // prefix = prefix + temp;
                    //  System.out.println(prefix);
                    dfs(board, i, j, sequence, words);
                }
            }
        }
        marked[row][column] = false;
    }


    private boolean isValidIndex(int row, int column, BoggleBoard board) {
        return (row >= 0 && row < board.rows() && column >= 0 && column < board.cols());
    }


    private boolean isValidWord(String word) {
        // word is null, false
        if (word == null) return false;
        // word length is less than or equal to 2, false
        if (word.length() <= 2) return false;
        // word is in in dictionary, return true
        return dictionaryTST.contains(word);
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException("IllegalArgumentException");
        }
        if (word.length() == 0) {
            throw new java.lang.IllegalArgumentException("IllegalArgumentException");
        }
        Integer wordScore = dictionaryTST.get(word);
        if (wordScore == null)
            return 0;
        return wordScore;
    }
}



class TST<Value> {
    private int n;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

        // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d + 1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) n++;
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d + 1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    public boolean hasPrefix(String prefix) {
        Node prefixNode = get(root, prefix, 0);
        if (prefixNode == null) return false;
        if (prefixNode.val != null) return true;
        if (prefixNode.left == null && prefixNode.mid == null &&
                prefixNode.right == null) return false;
        return true;
    }
}
