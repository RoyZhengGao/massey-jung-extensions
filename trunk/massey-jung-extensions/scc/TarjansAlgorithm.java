package nz.ac.massey.cs.guery.scc;

/*
 * Copyright 2011 Jens Dietrich Licensed under the GNU AFFERO GENERAL PUBLIC LICENSE, Version 3
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/agpl.html Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */


import java.util.*;

import com.google.common.base.Predicate;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import nz.ac.massey.cs.guery.GraphAdapter;
import nz.ac.massey.cs.guery.impl.ccc.NullFilter;
import static nz.ac.massey.cs.guery.impl.Logging.*;

/**
 * Implementation of Tarjan's algorithm.
 * {@link http://algowiki.net/wiki/index.php?title=Tarjan's_algorithm}
 * @author jens dietrich
 * @param <V>
 * @param <E>
 */
public class TarjansAlgorithm<V, E> {

	private int index = 0;
	private Stack<V> stack = new Stack<V>();
	private Map<V, Integer> indices = new HashMap<V, Integer>();
	private Map<V, Integer> lowlinks = new HashMap<V, Integer>();
	private Map<V,Set<V>> componentMembership = new HashMap<V,Set<V>>();
	private Predicate<E> edgeFilter = NullFilter.DEFAULT;
	
	private DirectedGraph<Set<V>, Integer> componentGraph = null;
	
	
	public void  buildComponentGraph(GraphAdapter<V, E> graph,Predicate<E> edgeFilter) {
		this.componentGraph = new DirectedSparseGraph<Set<V>, Integer>();
		if (edgeFilter!=null) this.edgeFilter = edgeFilter;
		
		for (Iterator<V> iter=graph.getVertices();iter.hasNext();) {
			V v = iter.next();
			if (!indices.containsKey(v)) {
				buildComponent(graph,v);
			}
		}
		
		int id = 0;
		
		// add edges
		for (Iterator<E> iter=graph.getEdges(edgeFilter);iter.hasNext();) {
			E e = iter.next();
			// note that the graph implementation class used will check for and reject parallel edges
			// as a consequence, their may be gaps in the range of assigned ids
			componentGraph.addEdge(id++,componentMembership.get(graph.getStart(e)),componentMembership.get(graph.getEnd(e)));
			
		}
		if (LOG.isDebugEnabled()) {
			System.out.println("Built SCCs using Tarjan's algorithm, graph compression ratio is " + ((double)componentGraph.getVertexCount())/((double)graph.getVertexCount()));
		}
		
	}
	
	public DirectedGraph<Set<V>, Integer> getComponentGraph() {
		return this.componentGraph;
	}
	
	public Map<V,Set<V>> getComponentMembership () {
		return this.componentMembership;
	}
	
	private void buildComponent(GraphAdapter<V, E> graph, V v) {

		indices.put(v, index);
		lowlinks.put(v, index);
		index = index+1;
		stack.push(v);

		for (Iterator<E> iter = graph.getOutEdges(v,edgeFilter);iter.hasNext();) {
			V next = graph.getEnd(iter.next());
			if (!indices.containsKey(next)) {
				buildComponent(graph, next);
				lowlinks.put(v, Math.min(lowlinks.get(v), lowlinks.get(next)));
			} else if (stack.contains(next)) {
				lowlinks.put(v,Math.min(lowlinks.get(v), indices.get(next)));
			}
		}

		// build new component
		if (lowlinks.get(v).equals(indices.get(v))) {
			Set<V> component = new HashSet<V>();
			V v2;
			do {
				v2 = stack.pop();
				component.add(v2);
				componentMembership.put(v2,component); // look up faster later than searching components!
			} while (v2!=v);
			componentGraph.addVertex(component);
		}


	}

}
