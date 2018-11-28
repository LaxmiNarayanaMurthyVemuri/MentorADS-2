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
            words.add(prefix);
        }

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (isValidIndex(i, j, board) && !marked[i][j]) {
                    String sequence = addLetter(prefix, board.getLetter(i, j));
                    // update the prefix by special case
                    // prefix = prefix + temp;
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
            throw new java.lang.IllegalArgumentException("You want to score null string!");
        }
        if (word.length() == 0) {
            throw new java.lang.IllegalArgumentException("You want to score empty string!");
        }
        Integer wordScore = dictionaryTST.get(word);
        if (wordScore == null)
            return 0;
        return wordScore;
    }
}

