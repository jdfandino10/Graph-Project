package grafos;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import interfaces.Graph;
import interfaces.Region;

public class RegionGraph<V extends Comparable> extends SimpleGraph<V>{
	
	private Hashtable<Integer,ArrayList<V>> regions;
	private int regionCount;
	public RegionGraph(int N){
		super(N);
		regions = new Hashtable<>();
		regionCount=0;
		
	}
	public RegionGraph(){
		super();
		regions = new Hashtable<>();
		regionCount=0;
	}
	
	public void setInitialRegions(){
		regions.put(regionCount, (ArrayList<V>) nodes.clone());
		regionCount++;
		regions.put(regionCount, (ArrayList<V>) nodes.clone());
		regionCount++;
	}

	public ArrayList<Integer> getPossibleRegions(Graph<V> s) {
		ArrayList<V> nodesInBoth = new ArrayList<>();
		ArrayList<V> listaNodos = s.getNodeList();
		for (V v : listaNodos) {
			if ( nodes.contains(v) )nodesInBoth.add(v);
		}
		Enumeration<Integer> llaves = regions.keys();
		ArrayList<Integer> posibles = new ArrayList<>();
		while ( llaves.hasMoreElements() ){
			int kactual = llaves.nextElement();
			ArrayList<V> faceNodes = regions.get(kactual);
			//System.out.println("cara: "+kactual+" - "+Arrays.toString(faceNodes.toArray()));
			boolean containsAll = true;
			for (V v : nodesInBoth) {
				containsAll = containsAll && faceNodes.contains(v);
				if (!containsAll) break;
			}
			if(containsAll) posibles.add(kactual);
		}
		
		return posibles;
	}
	
	public void joinPath(Graph<V> p, int reg) {
		//System.out.println("P: "+p);
		//System.out.println("this: "+toString());
		ArrayList<V> listaNodos = p.getNodeList();
		ArrayList<ArrayList<V>> ejesP = p.getEdges();
		ArrayList<V> nodosAgregados = new ArrayList<>();
		V inicio = null;
		V fin = null;
		ArrayList<V> nodosOriginales = (ArrayList<V>) nodes.clone();
		for(V nodo : listaNodos){
			if(nodes.contains(nodo)){
				inicio = nodo;
				int index = listaNodos.indexOf(nodo);
				V last = nodo;
				V next = null;
				boolean done = false;
				while(true){
					nodosAgregados.add(last);
					int i=0;
					while (next == null || next.equals(last)){
						next = ejesP.get(index).get(i);
						if(next.equals(inicio)) next=null;
						i++;
					}
					
					boolean yaEstaba = nodosOriginales.contains(next);
					addEdge(last,next);
					last = next;
					index = listaNodos.indexOf(last);
					if(yaEstaba && !last.equals(inicio)) {
						//System.out.println("ultimo que pone: "+last);
						fin = last;
						break;
					}
				}
				break;
			}
		}
		nodosAgregados.add(fin);
		//System.out.println("this + p:" +toString());
		ArrayList<V> nodesR = regions.get(reg);
		ArrayList<V> newFace1 = new ArrayList<>(nodosAgregados);
		ArrayList<V> newFace2 = new ArrayList<>(nodosAgregados);
		boolean done = false;
		//System.out.println("nodos agregados: "+Arrays.toString(nodosAgregados.toArray()));
		//System.out.println("region Original: "+Arrays.toString(nodesR.toArray()));
		//System.out.println("inicio: "+inicio+", fin: "+fin);
		boolean[] visited = new boolean[n()];
		LinkedList<V> stack = new LinkedList<>();
		stack.push(inicio);
		visited[nodes.indexOf(inicio)] = true;
		while(!stack.isEmpty() && !done){
			V actual = stack.pop();
			ArrayList<V> sons = gr.get(nodes.indexOf(actual));
			for (V v : sons) {
				if(!visited[nodes.indexOf(v)] && nodesR.contains(v)){
					visited[nodes.indexOf(v)]=true;
					if (v.equals(fin)){
						done = true;
						break;
					}
					newFace1.add(v);
					//System.out.println("hijos de "+v+": "+Arrays.toString(sons.toArray()));
					break;
				}
			}
		}
		for (V v : nodesR) {
			if ( !newFace1.contains(v) ){
				newFace2.add(v);
			}
		}
		//System.out.println("region dividida1: "+Arrays.toString(newFace1.toArray()));
		//System.out.println("region dividida2: "+Arrays.toString(newFace2.toArray()));
		regions.put(reg, newFace1);
		regions.put(regionCount, newFace2);
		impRegiones();
		regionCount++;
		
	}
	
	public Graph<V> getPathContactVert(Graph<V> seg) {
		ArrayList<V> nodesInBoth = new ArrayList<>();
		ArrayList<V> listaNodos = seg.getNodeList();
		ArrayList<ArrayList<V>> ejes = seg.getEdges();
		for (V v : listaNodos) {
			if ( nodes.contains(v) )nodesInBoth.add(v);
		}
		Graph<V> g = new SimpleGraph<>();
		//System.out.println("nodes in both: "+Arrays.toString(nodesInBoth.toArray()));
		V inicio = nodesInBoth.get(0);
		//System.out.println("inicio: "+inicio);
		LinkedList<V> stack = new LinkedList<>();
		LinkedList<V> path = new LinkedList<>();
		boolean[] visited = new boolean[listaNodos.size()];
		stack.push(inicio);
		visited[listaNodos.indexOf(inicio)]=true;
		V last = null;
		V lastlast = null;
		boolean found=false;
		while(!stack.isEmpty() && !found){
			V actual = stack.pop();
			ArrayList<V> sons = ejes.get(listaNodos.indexOf(actual));
			//System.out.println(Arrays.toString(stack.toArray()));
			for (V v : sons) {
				int ind = listaNodos.indexOf(v);
				if ( !visited[ind]){
					//System.out.println(g.toString());
					//System.out.println("last: "+last+", actual:"+actual+", v:"+v);
					
					visited[ind] = true;
					stack.push(v);
					//System.out.println(Arrays.toString(stack.toArray()));
					g.addEdge(actual, v);
					lastlast = last;
					last = v;
					if (nodesInBoth.contains(v)) found = true;
					break;
					
				}
			}
		}
		return g;
	}
	public void impRegiones(){
		//System.out.println("TODAS LAS REGIONES:");
		Enumeration<Integer> llaves = regions.keys();
		ArrayList<Integer> posibles = new ArrayList<>();
		while ( llaves.hasMoreElements() ){
			int kactual = llaves.nextElement();
			ArrayList<V> faceNodes = regions.get(kactual);
			//System.out.println("cara: "+kactual+" - "+Arrays.toString(faceNodes.toArray()));
			
		}
	}
}
