package grafos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import interfaces.Graph;

public class Main {
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while((line=br.readLine())!=null){
			int N = Integer.parseInt(line);
			if (N<=2) {
				System.out.println('Y');
				continue;
			}
			Graph<Integer> g = new SimpleGraph<Integer>(N);
			for (int i = 0; i < N; i++) {
				String[] vals = br.readLine().split(" ");
				int p = i;
				for (int j = 0; j < vals.length; j++) {
					int q = Integer.parseInt(vals[j])-1;
					g.addEdge(p, q);
				}
			}
			DMP<Integer> p = new DMP<Integer>();
			System.out.println(p.isPlanar(g)?'Y':'N');
		}
	}
}
/*
5
2 3 4 5
1 3 4 5
1 2 4 5
1 2 3
1 2 3
5
2 3 4 5
1 3 4 5
1 2 4 5
1 2 3 5
1 2 3 4
6
2 3 4 5
1 3 4 5
1 2 5 6
1 2 5 6
1 2 3 4
3 4
6
4 5 6
4 5 6
4 5 6
1 2 3
1 2 3
1 2 3
5
2 3 4 5
1 3 4 5
1 2 5
1 2 5
1 2 3 4
 */