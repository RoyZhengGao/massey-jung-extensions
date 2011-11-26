/*
 * Copyright © 2011 Jens Dietrich. All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY JENS DIETRICH "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package nz.ac.massey.jung.contrib.algorithms.cluster;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.graph.Graph;

/**
 * Implementation of Tarjan's algorithm. This class uses an interface that is compatible with other jung clusterers,
 * for more flexibility (computing components as graph and using edge filters), use the class TarjansAlgorithm directly. 
 * Complexity is O(|V|+|E|).
 * Tarjan, R. E. (1972), "Depth-first search and linear graph algorithms", SIAM Journal on Computing 1 (2): 146Ð160, doi:10.1137/0201010.
 * {@link http://algowiki.net/wiki/index.php?title=Tarjan's_algorithm}
 * @author jens dietrich
 * @param <V>
 * @param <E>
 */

public class TarjanClusterer<V,E> implements Transformer<Graph<V,E>,Set<Set<V>>> {
	@Override
	public Set<Set<V>> transform(Graph<V, E> g) {
		final TarjansAlgorithm<V,E> alg = new TarjansAlgorithm<V,E>();
		alg.buildComponentGraph(g, TarjansAlgorithm.NULL_FILTER);
		final Set<Set<V>> components = new HashSet<Set<V>>();
		components.addAll(alg.getComponentMembership().values());
		return components;
	}

}
