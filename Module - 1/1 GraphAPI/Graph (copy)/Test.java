import java.util.Scanner;
public class Test {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
		scan.nextLine();
		scan.nextLine();
		String[] lines = scan.nextLine().split(",");
		while (scan.hasNextLine()) {
			String[] values = scan.nextLine().split(" ");
			int v = Integer.parseInt(values[0]);
			int w = Integer.parseInt(values[1]);
			System.out.println(lines[v] + "," + lines[w]);
		}
	}
}