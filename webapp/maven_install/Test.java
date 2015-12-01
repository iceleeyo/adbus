import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import objectexplorer.MemoryMeasurer;
import objectexplorer.ObjectGraphMeasurer;

public class Test {
	int a = 0;
	int b = 0;
	int c = 0;

	public static void main(String[] args) {
		Set<Integer> hashset = new HashSet<Integer>();
		Random rng = new Random();
		int n = 10000;
		for (int i = 1; i <= n; i++) {
			hashset.add(rng.nextInt());
		}
		System.out.println(ObjectGraphMeasurer.measure(hashset));
		long memory = MemoryMeasurer.measureBytes(hashset);
		System.out.println(memory);
		System.out.println(humanReadableByteCount(memory, false));
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
