
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int t = Integer.parseInt(scan.nextLine());
        for(int j = 0;j < t;j++) {
            String[] l = scan.nextLine().split(",");
            String text = l[0];
            if (text.length() == 0){
            	System.out.println("-1");
            } else {
	            int n = Integer.parseInt(l[1]);
	            SuffixArray sa = new SuffixArray(text);
	            int N = sa.length();
	            boolean check = false;
	            String lrs = "";
	            for (int i = 1; i < N; i++) {
	                int length = sa.lcp(i);
	                if (length >= n){
	                    lrs = sa.select(i).substring(0, length);
	                    check = true;
	                    System.out.println(lrs);
	                }
	            }
	            if(!check) {
	                System.out.println("-1");
	            }
        	}
        }
    }
}
