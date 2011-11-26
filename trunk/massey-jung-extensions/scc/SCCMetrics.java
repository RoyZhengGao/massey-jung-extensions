/*
 * Copyright 2010 Jens Dietrich Licensed under the GNU AFFERO GENERAL PUBLIC LICENSE, Version 3
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/agpl.html Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package nz.ac.massey.cs.guery.scc;

import java.util.Iterator;
import java.util.Set;
import com.google.common.base.Predicate;
import nz.ac.massey.cs.guery.GraphAdapter;
import nz.ac.massey.cs.guery.Path;
import nz.ac.massey.cs.guery.PathFinder;
import nz.ac.massey.cs.guery.impl.BreadthFirstPathFinder;


/**
 * Utility to compute some SCC related metrics. 
 * @author jens dietrich
 */

public class SCCMetrics {
	
	public static <V,E> double tangledness(final Set<V> scc,final GraphAdapter<V, E> g)  {
        
        if (scc.size()<=2) return 1; // in this case we have min=max
        
        PathFinder<V,E> pf = new BreadthFirstPathFinder<V,E>(false);
        // only traverse edges within the SCC
        Predicate<E> filter = new Predicate<E>() {
                @Override
                public boolean apply(E e) {
                        // we only consider outgoing paths - so we only have to check whether edge links to another vertex within the scc
                        return scc.contains(g.getEnd(e)); 
                }
        };
        int sum = 0;
        int count = 0;
        for (V v:scc) {
                Iterator <E> out = g.getOutEdges(v);
                while (out.hasNext()) {
                        E e = out.next();
                        V target = g.getEnd(e);
                        if (scc.contains(target)) {
                                Iterator<Path<V,E>> paths = pf.findLinks(g,target,1,-1,true,filter,false) ;
                                boolean f = false;
                                while (!f && paths.hasNext()) {
                                        Path<V,E> path = paths.next();
                                        if (path.getEnd().equals(v)) {
                                                sum = sum+path.size();
                                                count = count+1;
                                                f = true;
                                        }
                                }
                                if (!f) {
                                    throw new IllegalStateException ("Cannot find backpath in scc for edge " + e + "- error in algorithm");
                                    //System.out.println("Cannot find backpath in scc for edge " + e + "- error in algorithm");
                                }
                        }
                }
        }
        double avg = ((double)sum)/((double)count);
        double max = scc.size()-1; // max value if scc is circle
        double min = 1;  // min value of scc is clique
        return 1-((avg-min)/(max-min));  // min-max normalisation
}
	
	public static <V,E> double density(final Set<V> scc,final GraphAdapter<V, E> g)  {
		
		if (scc.size()<=2) return 1; // in this case we have min=max
		
		// count edges in SCC
		Predicate<E> filter = new Predicate<E>() {
			@Override
			public boolean apply(E e) {
				return scc.contains(g.getStart(e)) && scc.contains(g.getEnd(e));
			}	
		};
		Iterator<E> edges = g.getEdges(filter);
		int count = 0;
		while (edges.hasNext()) {
			edges.next();
			count = count+1;
		}
		
		double max = (scc.size()-1)*scc.size(); // max value if scc is clique
		double min = scc.size();  // min value of scc is graph
		return (count-min)/(max-min);  // min-max normalisation
	}
	
}
