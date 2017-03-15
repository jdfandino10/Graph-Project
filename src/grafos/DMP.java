package grafos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import interfaces.Graph;
import interfaces.Region;
import interfaces.Graph;

/**
 * Clase que realiza el algoritmo de DMP para saber
 * si un grafo es planar o no
 * Basado en http://www2.fiu.edu/~ramsamuj/graphtheory/chap6.pdf
 * @author jdani
 *
 */
public class DMP<V> {
	/**
	 * Metodo que dice si un grafo es planar o no
	 * @param g el grafo del cual se quiere saber si es planar
	 * @return true si es planar, false de lo contrario
	 */
	public boolean isPlanar(Graph<V> g){
		//PRE-PROCESAMIENTO
//		0. (Propuesto por mi) si V<=4
		if (g.n() <= 4) return true;
//		1. If num(E) > num(V) - 6, then the graph must be nonplanar.
		//System.out.println("3*n-6: "+(3*g.n() - 6)+", e: "+g.e() );
		if (g.e() > 3*g.n() - 6) return false;
		//System.out.println("ni con 0 ni con 1");
//		2. If the graph is disconnected, consider each component separately.
		ArrayList<Graph<V>>cc = g.getConnectedComponents();
		if (cc.size()>1){
			boolean isPlanar = true;
			for (int i = 0; i < cc.size() && isPlanar; i++) {
				isPlanar = isPlanar && isPlanar(cc.get(i));
			}
			return isPlanar;
		}
		//System.out.println("ni con comp");
//		3. If the graph contains a cut vertex, then it is clearly planar if,
//		and only if, each of its blocks is planar. Thus, we can limit our attention
//		to 2-connected graphs.
		ArrayList<Graph<V>>cutVertComp = g.getCutVertexComponents();
		if (cutVertComp.size() > 0){
			boolean isPlanar = true;
			for (int i = 0; i < cutVertComp.size() && isPlanar; i++) {
				//System.out.println(cutVertComp.get(i));
				isPlanar = isPlanar && isPlanar(cutVertComp.get(i));
			}
			return isPlanar;
		}
		//System.out.println("ni cut vert");

//		4. Loops and multiple edges change nothing; hence, we need only consider graphs.
		/*El grafo ya se supone simple*/
//		5. A vertex of degree 2 can certainly be replaced by an edge joining its neighbors.
//		This contraction of all vertices of degree 2 constructs a homeomorphic graph
//		with the smallest number of vertices. Certainly, a graph is planar if, and only
//		if, the contraction is planar.
		g.contract();
//		Post: (Propuesto por mi) repetir pasos 0 y 1 pues contraer modifica el numero de ejes y nodos
		if (g.n() <= 4) return true;
		if (g.e() > 3*g.n() - 6) return false;
		//System.out.println("ni 0 ni 1 otra vez");
		//fin preprocesamiento, retornar dmp
		return dmp(g);		
	}
	
	private boolean dmp(Graph<V> g){
		int i=1;
		int r=2;
		Graph<V> h = g.getCycle();
		//System.out.println(h);
		if (h == null) return true; 	
		while (r != g.e() - g.n() + 2){ //caracteristica de euler
			ArrayList<Graph<V>>segments = g.getGraphs(h);
			//System.out.println(h);
			Graph<V> seg = segments.get(0);
			ArrayList<Integer>picked = ((RegionGraph)h).getPossibleRegions(seg);
			//System.out.println("segment 0: "+seg);
			//System.out.println("picked: "+Arrays.toString(picked.toArray()));
			if (picked.size() == 0) return false;
			for (Graph<V> s : segments) {
				ArrayList<Integer>actual = ((RegionGraph)h).getPossibleRegions(s);
				if (actual.size() == 0) return false;
				if (actual.size() < picked.size()){
					seg = s;
					picked = actual;
				}
			}
			Integer reg = picked.get(0);
			//System.out.println("segmento escojido: \n"+seg);
			//System.out.println("cara escojida: "+reg);
			Graph<V> p = ((RegionGraph)h).getPathContactVert(seg);
			//System.out.println(p);
			((RegionGraph)h).joinPath(p, reg);
			i++;
			r++;
		}
		return g.e()==h.e() && g.n()==h.n(); 
	}
	
}
