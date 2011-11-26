/*
 * Copyright © 2011 Jens Dietrich. All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY JENS DIETRICH "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package nz.ac.massey.jung.contrib.algorithms.metrics;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;

public class Modularity {
	
	public static <V,E,M> double computeModularity (Graph<V,E> g,Transformer<V,M> moduleMembership) {
		
		double sum = 0;
		double m2 = (double)(2*g.getEdgeCount());
		
		for (V v1:g.getVertices()) {
			for (V v2:g.getVertices()) {
				M c1 = moduleMembership.transform(v1);
				M c2 = moduleMembership.transform(v2);
				if (c1.equals(c2)) {
					double delta = (g.isNeighbor(v1,v2)?1:0) - (double)g.degree(v1)*(double)g.degree(v2)/m2;
					// System.out.println("delta for vertex pair " + v1 + " , " + v2 + " is " + delta);
					sum = sum+delta;		
				}
			}
		}
		return sum/m2;
	}
	
	// aka assortativity coefficient
	public static <V,E,M> double computeMaxModularity (Graph<V,E> g,Transformer<V,M> moduleMembership) {
		
		double m2 = (double)(2*g.getEdgeCount());
		double sum = m2;
		
		
		for (V v1:g.getVertices()) {
			for (V v2:g.getVertices()) {
				M c1 = moduleMembership.transform(v1);
				M c2 = moduleMembership.transform(v2);
				if (c1.equals(c2)) {
					double delta = (double)g.degree(v1)*(double)g.degree(v2)/m2;
					// System.out.println("delta for vertex pair " + v1 + " , " + v2 + " is " + delta);
					sum = sum-delta;		
				}
			}
		}
		return sum/m2;
	}
	
	public static <V,E,M> double computeScaledModularity(Graph<V,E> g,Transformer<V,M> moduleMembership) {
		double modularity = computeModularity(g,moduleMembership);
		double maxModularity = computeMaxModularity(g,moduleMembership);
		return modularity/maxModularity;
	}

}
