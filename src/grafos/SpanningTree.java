package grafos;

import java.util.ArrayList;

public class SpanningTree {

	private ArrayList<Integer>[] st;
	private ArrayList<Integer>[] backEdge;
	private int[] num;
	private int[] low;

	public SpanningTree(int N){
		num = new int[N];
		low = new int[N];
		st = new ArrayList[N];
		backEdge = new ArrayList[N];
		for (int i = 0; i < backEdge.length; i++) {
			backEdge[i] = new ArrayList<>();
			st[i] = new ArrayList<>();			
		}
	}

	public void addEdge(int p, int q){
		st[p].add(q);
	}

	public void addBackEdge(int p, int q){
		if (!st[q].contains(p)){
			backEdge[p].add(q);
		}
	}

	public int findArticulationPoint(){
		if ( st[0].size() > 1 ) return 0;
		computeLow(0);
		for (int i = 1; i < st.length; i++) {
			ArrayList<Integer> sons = st[i];
			int numv = num[i];
			for (Integer son : sons) {
				int loww = low[son];
				if ( loww>= numv ) return i;
			}
		}
		return -1;
	}

	public void setNum(int node, int num){
		this.num[node] = num;
	}

	public void computeLow(int n){
		int lowestNum = num[n];
		for (Integer node : backEdge[n]) {
			lowestNum = Math.min(lowestNum, num[n]);
		}
		int lowestLow = Integer.MAX_VALUE;
		for (Integer node : st[n]) {
			if ( low[node] == 0 ) computeLow(node);
			lowestLow = Math.min(lowestLow, low[node]);
		}

		low[n] = Math.min(lowestNum, lowestLow);
	}
	public String toString(){
		String ans = "ST NORMAL: ";
		for (int i = 0; i < st.length; i++) {
			ArrayList<Integer> lista = st[i];
			ans+=(i+" -> [");
			for (int j = 0; j < lista.size(); j++) {
				Integer n = lista.get(j);
				ans+=(n+", ");
			}
			ans+=("]\n");
		}
		ans += "ST BACK EDGE: ";
		for (int i = 0; i < st.length; i++) {
			ArrayList<Integer> lista = backEdge[i];
			ans+=(i+" -> [");
			for (int j = 0; j < lista.size(); j++) {
				Integer n = lista.get(j);
				ans+=(n+", ");
			}
			ans+=("]\n");
		}
		return ans;
	}
}
