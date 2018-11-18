// public class Percolation {
//    public Percolation(int n)                // create n-by-n grid, with all sites blocked
//    public    void open(int row, int col)    // open site (row, col) if it is not open already
//    public boolean isOpen(int row, int col)  // is site (row, col) open?
//    public boolean isFull(int row, int col)  // is site (row, col) full?
//    public     int numberOfOpenSites()       // number of open sites
//    public boolean percolates()              // does the system percolate?
// }


// You can implement the above API to solve the problem




import java.util.*;
class percolate {
    int opensitescount;
    boolean[][] arr;
    Graph obj;
    int n;
   public percolate(int n) { // create n-by-n grid, with all sites blocked
        this.n = n;
        arr = new boolean[n][n];
        obj = new Graph((n)*(n) + 2);
   }                
   public void open(int i, int j) { // open site (row, col) if it is not open already
        
        arr[i][j] = true;
        if(i == 0){
            obj.addEdge(n*n, cal(i,j));
        }
        if(i == n-1){
            obj.addEdge(n*n+1, cal(i,j));
        }
        if (i < n-1 && arr[i+1][j]) obj.addEdge(cal(i, j), cal(i+1, j));
        if (i > 0   && arr[i-1][j]) obj.addEdge(cal(i, j), cal(i-1, j));
        if (j < n-1 && arr[i][j+1]) obj.addEdge(cal(i, j), cal(i, j+1));
        if (j > 0   && arr[i][j-1]) obj.addEdge(cal(i, j), cal(i, j-1));

   } 

   public int cal(int i, int j){
        return (n*i)+j;
   }   

   public boolean percolates() { // does the system percolate?
   	CC obj1 = new CC(obj);
        if (obj1.connected(n*n, n*n+1)){
            return true;
        }return false; 
   }       
}

public class Solution{
    public static void main(String[] args){// test client (optional)
        Scanner sc = new Scanner(System.in);
        int noofinputs = Integer.parseInt(sc.nextLine());
        percolate obj = new percolate(noofinputs);
        // System.out.println(noofinputs);
        while(sc.hasNextLine()){
          String s = sc.nextLine();
          if(s.length() > 0){
            String[] input = s.trim().split(" ");
          // System.out.println(input[0] +" "+input[input.length-1] );
          obj.open(Integer.parseInt(input[0])-1, Integer.parseInt(input[input.length-1])-1);
          
          }
          
        }
        System.out.println(obj.percolates());
   }   
}