package grafos;

import interfaces.Graph;
import interfaces.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
/**
 * Clase que representa un grafo simple con nodos V
 * @author jdani
 *
 */
public class SimpleGraph<V extends Comparable> implements Graph<V>{

	/**
	 * Lista de adyacencias del grafo
	 */
	protected ArrayList<ArrayList<V>> gr;

	/**
	 * Lista de nodos
	 */
	protected ArrayList<V> nodes;


	/**
	 * Numero de arcos que hay.
	 */
	private int edges;



	/**
	 * Constructor de la clase. Crea un grafo con N nodos (0,...,N-1) sin
	 * conexiones entre ellos.
	 * @param N el numero de nodos que tiene el grafo.
	 */
	public SimpleGraph(int N){
		nodes = new ArrayList<V>(N);
		gr = new ArrayList<>(N);
		edges = 0;
	}
	public SimpleGraph(){
		nodes = new ArrayList<V>();
		gr = new ArrayList<>();
		edges = 0;
	}

	/**
	 * Metodo para obtener el numero de nodos (que en este caso tambien son los nodos) de G
	 * @return N (el numero de listas de adyacencias que hay, pues hay una por nodo)
	 */
	public int n(){
		return nodes.size();
	}

	/**
	 * Metodo para obtener el numero de ejes simples de G
	 * @return E
	 */
	public int e(){
		return edges;
	}

	/**
	 * Metodo que retorna true si p y q estan conectados
	 * @param p
	 * @param q
	 * @return
	 */
	public boolean conected(V p, V q){
		int indexP = nodes.indexOf(p);
		int indexQ = nodes.indexOf(q);
		if (indexP == -1 || indexQ == -1) return false;
		ArrayList<V> sonsP = gr.get(indexP);
		return sonsP.contains(q);
	}
	
	public boolean conectedBFS(V p, V q){
		LinkedList<V> queue = new LinkedList<>();
		boolean[] visited = new boolean[n()];
		int indexP = nodes.indexOf(p);
		visited[indexP] = true;
		queue.add(p);
		while(!queue.isEmpty()){
			V actual = queue.pop();
			indexP = nodes.indexOf(actual);
			ArrayList<V> sons = gr.get(indexP);
			for (V v : sons) {
				int indexV = nodes.indexOf(v);
				if(!visited[indexV]){
					if(v.equals(q)) return true;
					visited[indexV] = true;
					queue.add(v);
				}
			}
		}
		return false;
	}

	public boolean isEdge(V p, V q){
		int indexP = nodes.indexOf(p);
		int indexQ = nodes.indexOf(q);
		if (indexP == -1 || indexQ == -1) return false;
		ArrayList<V> sonsP = gr.get(indexP);
		return sonsP.contains(q);
	}
	/**
	 * Metodo que conecta p y q con un eje simple
	 * @param p
	 * @param q
	 */
	public void addEdge(V obj1, V obj2){
//		//System.out.println(Arrays.toString(nodes.toArray()));
//		//System.out.println("Va a conectar "+obj1+" con "+obj2);
//		//System.out.println(toString());
		if(conected(obj1, obj2)) {
//			//System.out.println("conected: "+obj1+" - "+obj2);
			return;
		}
		int p = nodes.indexOf(obj1);
		if ( p == -1 ){
			nodes.add(obj1);
			gr.add(new ArrayList<V>());
			p = nodes.indexOf(obj1);
		}
		int q = nodes.indexOf(obj2);
		if ( q == -1 ){
			nodes.add(obj2);
			gr.add(new ArrayList<V>());
			q = nodes.indexOf(obj2);
		}
		gr.get(p).add(obj2);
		gr.get(q).add(obj1);
		edges++;
//		//System.out.println(toString());
	}

	@Override
	public ArrayList<V> getNodeList() {
		return nodes;
	}
	public ArrayList<ArrayList<V>> getEdges(){
		return gr;
	}

	@Override
	public ArrayList<Graph<V>> getConnectedComponents() {
		ArrayList<Graph<V>> ans = new ArrayList<>();
		ArrayList<ArrayList<V>> cnct = getConnectedComponentsBFS();
		for (ArrayList<V> arrayList : cnct) {
			Graph<V> n = new SimpleGraph<>(arrayList.size());
			for (V v : arrayList) {
				int ind = nodes.indexOf(v);
				ArrayList<V> vecinos = gr.get(ind);
				for (V vecino : vecinos) {
					n.addEdge(v, vecino);	
				}
			}
			ans.add(n);
		}

		return ans;
	}

	private ArrayList<ArrayList<V>> getConnectedComponentsBFS() {
		int[] componente = new int[n()];
		int totComp = 0;
		for (int i = 1; i <= componente.length; i++) {
			int j = componente[i-1];
			if(j==0){
				totComp++;
				componente[i-1]=i;
				LinkedList<V> queue = new LinkedList<>();
				queue.add(nodes.get(i-1));
				while(!queue.isEmpty()){
					V elem = queue.pop();
					ArrayList<V> sons = gr.get(nodes.indexOf(elem));
					for (V son : sons) {
						int ind = nodes.indexOf(son);
						if ( componente[ind] == 0){
							queue.add(son);
							componente[ind] = i;
						}
					}
				}
			}
		}
		ArrayList<ArrayList<V>> ans = new ArrayList<ArrayList<V>>(totComp);
		boolean[] done = new boolean[n()];
		for (int i = 0; i < totComp; i++) {
			int compBuscado = -1;
			ArrayList<V> c = new ArrayList<>();
			for (int j = 0; j < componente.length; j++) {
				int b = componente[j];
				if (!done[j] && (compBuscado == -1 || compBuscado == b)){
					compBuscado = b;
					c.add(nodes.get(j));
					done[j] = true;
				}

			}
			ans.add(c);
		}
		return ans;
	}
	@Override
	public ArrayList<Graph<V>> getCutVertexComponents() {
		V cutVertex = findArticulationPoint();
		//System.out.println("Cut vertex"+cutVertex);
		if ( cutVertex==null ) return new ArrayList<>();
		int index = nodes.indexOf(cutVertex);
		ArrayList<V> sons = gr.get(index);
		boolean [] ya = new boolean[n()];
		int i = 1;
		ArrayList<Graph<V>> ans = new ArrayList<>();
		int cutVertexInd = nodes.indexOf(cutVertex);
		for (V son : sons) {
			int ind = nodes.indexOf(son);
			if ( !ya[ind] ){
				ans.add(generateGraph(son,ya,cutVertexInd));
			}
		}
		return ans;
	}

	private Graph<V> generateGraph(V son, boolean[] ya, int cutVertInd) {
		Graph<V> g = new SimpleGraph<V>();
		LinkedList<V> queue = new LinkedList<>();
		queue.add(son);
		int ind = nodes.indexOf(son);
		ya[ind] = true;
		while (!queue.isEmpty()){
			V actual = queue.pop();
			ind = nodes.indexOf(son);
			ArrayList<V> sons = gr.get(ind);
			for (V s : sons) {
				int sInd = nodes.indexOf(s);
				if(sInd!=cutVertInd && !ya[sInd]) {
					g.addEdge(actual, s);
					ya[sInd] = true;
					queue.add(s);
				}
			}
		}
		return g;
	}

	/**
	 * http://www.eecs.wsu.edu/~holder/courses/CptS223/spr08/slides/graphapps.pdf
	 * @return
	 */
	private V findArticulationPoint(){
		LinkedList<V> stack = new LinkedList<>();
		SpanningTree st = new SpanningTree(n());
		boolean[] visited = new boolean[n()];
		stack.push(nodes.get(0));
		visited[0] = true;
		int i=1;
		while(!stack.isEmpty()){
			V node = stack.pop();
			int nodeIndex = nodes.indexOf(node);
			st.setNum(nodeIndex, i);
			ArrayList<V> sons = gr.get(nodeIndex);
			boolean added = false;
			for (V actual : sons) {
				int actualIndex = nodes.indexOf(actual);
				if ( !visited[actualIndex]){
//					//System.out.println(st.toString());
					visited[actualIndex] = true;
					if(!added){
						added = true;
						st.addEdge(nodeIndex, actualIndex);
					}
					stack.push(actual);
				}else{
					st.addBackEdge(nodeIndex, actualIndex);
				}
			}
			i++;
		}
//		//System.out.println(st.toString());
		int artV = st.findArticulationPoint();
		return artV!=-1?nodes.get(artV):null;
	}
	@Override
	public void contract() {
		for (int i = 0; i < gr.size(); i++) {
			ArrayList<V> sons = gr.get(i);
			if(sons.size()==2){
				V s1 = sons.get(0);
				V s2 = sons.get(1);
				int s1ind = nodes.indexOf(s1);
				int s2ind = nodes.indexOf(s2);
				gr.get(s1ind).remove(nodes.get(i));
				gr.get(s2ind).remove(nodes.get(i));
				edges--;
				addEdge(s1, s2);
				nodes.remove(i);
				gr.remove(i);
				i--;
			}else if(sons.size()==1){
				V s1 = sons.get(0);
				int s1ind = nodes.indexOf(s1);
				gr.get(s1ind).remove(nodes.get(i));
				edges--;
				nodes.remove(i);
				gr.remove(i);
				i--;
			}
		}
	}

	@Override
	public Graph<V> getCycle() {
		LinkedList<V> stack = new LinkedList<>();
		boolean[] visited = new boolean[n()];
		stack.push(nodes.get(0));
		visited[0] = true;
		boolean found = false;
		V first = null;
		Graph<V> g = new RegionGraph<V>();
		V last = null;
		V lastlast = null;
		while(!stack.isEmpty() && !found){
			V actual = stack.pop();
			ArrayList<V> sons = gr.get(nodes.indexOf(actual));
			//System.out.println(Arrays.toString(stack.toArray()));
			for (V v : sons) {
				int ind = nodes.indexOf(v);
				if ( !visited[ind]){
					//System.out.println(g.toString());
					//System.out.println("last: "+last+", actual:"+actual+", v:"+v);
					
					visited[ind] = true;
					stack.push(v);
					//System.out.println(Arrays.toString(stack.toArray()));
					g.addEdge(actual, v);
					lastlast = last;
					last = v;
					break;
					
				}else if(lastlast!=null && !lastlast.equals(v)) {
					//System.out.println("V: "+v+", last:"+last);
					found = true;
					g.addEdge(actual, v);
					break;
				}
			}
		}
		((RegionGraph)g).setInitialRegions();
		return g;
	}
	

	private Graph<V> generateGraph(LinkedList<V> stack) {
		if(stack.size()<=1) return null;
		Graph<V> g = new RegionGraph<V>();
		V first = stack.pop();
		V last = first;
		while(!stack.isEmpty()){
			V next = stack.pop();
			g.addEdge(last, next);
			last = next;
		}
		g.addEdge(last, first);
		((RegionGraph)g).setInitialRegions();
		return g;
	}
	@Override
	public ArrayList<Graph<V>> getGraphs(Graph<V> h) {
		ArrayList<V> nodosH = h.getNodeList();
		boolean[] inH = new boolean[n()];
		for (V b : nodosH) {
			inH[nodes.indexOf(b)] = true;
		}
		boolean[] visited = new boolean[n()];
		ArrayList<Graph<V>> ans = new ArrayList<>();
		for (V b : nodosH) {
			int root = nodes.indexOf(b);
			ArrayList<V> rootSons = gr.get(root);
			for (V v : rootSons) {
				if(inH[nodes.indexOf(v)] && h.isEdge(b, v)) continue;
				Graph<V> nuevo = new SimpleGraph<V>();
				nuevo.addEdge(b, v);
				//System.out.println("n:\n"+nuevo);
				if(inH[nodes.indexOf(v)]) {
					ans.add(nuevo);
					continue;
				}
				//BFS
				LinkedList<V> queue = new LinkedList<>();
				int indexP = nodes.indexOf(v);
				visited[indexP] = true;
				queue.add(v);
				while(!queue.isEmpty()){
					V actual = queue.pop();
					indexP = nodes.indexOf(actual);
					ArrayList<V> sons = gr.get(indexP);
					for (V x : sons) {
						int indexV = nodes.indexOf(x);
						if(!visited[indexV]){
							nuevo.addEdge(actual, x);
							int indX = nodes.indexOf(x);
							if(!inH[indX]) {
								queue.add(x);
								visited[indexV] = true;
							}
						}
					}
				}
				ans.add(nuevo);
			}
		}
		//System.out.println("ESTE"+Arrays.toString(ans.toArray()));
		return ans;
	}
	
	public String toString(){
		String ans = "";
		for (int i = 0; i < gr.size(); i++) {
			ArrayList<V> lista = gr.get(i);
			ans+=(nodes.get(i)+" -> [");
			for (int j = 0; j < lista.size(); j++) {
				V n = lista.get(j);
				ans+=(n+", ");
			}
			ans+=("]\n");
		}
		return ans;
	}
	
}
