package interfaces;

import java.util.ArrayList;

public interface Graph<V> {

	/**
	 * Metodo para obtener el numero de nodos de G
	 * @return N 
	 */
	public int n();
	
	/**
	 * Metodo para obtener el numero de ejes de G
	 * @return E
	 */
	public int e();
	
	/**
	 * Metodo que retorna la lista de nodos
	 * @return
	 */
	public ArrayList<V> getNodeList();
	
	/**
	 * Metodo que retorna true si p y q estan conectados
	 * @param p
	 * @param q
	 * @return
	 */
	public boolean conected(V p, V q);
	
	/**
	 * Metodo que conecta p y q con un eje
	 * @param p
	 * @param q
	 */
	public void addEdge(V p, V q);
	
	/**
	 * Metodo que retorna todos los componentes conexos de un grafo en grafos diferentes
	 * en una lista
	 * @return la lista de componentes conexos
	 * (si solo hay uno entonces retorna una lista consigomismo)
	 */
	public ArrayList<Graph<V>> getConnectedComponents();
	
	/**
	 * Metodo que retorna los subgrafos de G que resultan de remover un cut vertex
	 * @return null si no hay cut vertex
	 */
	public ArrayList<Graph<V>> getCutVertexComponents();
	
	/**
	 * Metodo que contrae todos los vértices de grado 2
	 */
	public void contract();
	
	/**
	 * Metodo que retorna un ciclo de G como un nuevo grafo
	 * @return
	 */
	public Graph getCycle();

	/**
	 * Metodo que retorna los segmentos del ciclo H (param) en G (this)
	 * @param h un ciclo subgrafo de this
	 * @return una lista de segmentos
	 */
	public ArrayList<Graph<V>> getGraphs(Graph<V> h);

	/**
	 * Metodo que retorna las posibles regiones en las que puede estar el segmento s
	 * @param s segmento
	 * @return arreglo de regiones
	 */
	//public ArrayList<Region<V>> getPossibleRegions(Graph<V> s);

	/**
	 * Metodo que retorna un camino en segment entre 2 vértices de this que estén en segment
	 * @param seg
	 * @return
	 */
	//public Graph getPathContactVert(Graph<V> seg);

	/**
	 * Agregarle el camino al grafo
	 * @param p camino a agregar
	 * @param reg region en la cual ponerlo
	 */
	//public void joinPath(Graph<V> p, Region<V> reg);
	
	public boolean isEdge(V p, V q);
	public ArrayList<ArrayList<V>> getEdges();
}
