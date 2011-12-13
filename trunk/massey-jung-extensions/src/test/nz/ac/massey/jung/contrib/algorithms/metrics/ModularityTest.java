/*
 * Copyright © 2011 Jens Dietrich. All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY JENS DIETRICH "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package test.nz.ac.massey.jung.contrib.algorithms.metrics;

import static org.junit.Assert.*;
import nz.ac.massey.jung.contrib.algorithms.metrics.Modularity;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
/**
 * Unit tests for modularity metrics.
 * @author jens dietrich
 */
public class ModularityTest {
	
	static double DELTA = 0.01;
	
	Transformer<String,String> componentMembership = new Transformer<String,String>() {
		@Override
		public String transform(String s) {
			return s.substring(0,s.indexOf('.')); // component is first token in name
		}
	};
	
	Predicate<String> isInModule1 = new Predicate<String> () {
		@Override
		public boolean evaluate(String s) {
			return s.substring(0,s.indexOf('.')).equals("c1");
		}
	};
	
	Predicate<String> isInModule2 = new Predicate<String> () {
		@Override
		public boolean evaluate(String s) {
			return s.substring(0,s.indexOf('.')).equals("c2");
		}
	};
	
	/**
	 * Two components connected by one edge.
	 */
	@Test
	public void testUndirected1() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12","c1.v1","c2.v1");
		

		double computed = Modularity.computeModularity(g, componentMembership);
		
		// each component is 3x3 matrix, rows (elements of sum in formula) are:
		// -4/14 8/14  10/14
		// 8/14  -9/14 8/14
		// 10/14 8/14  -4/14
		// this yields (2/14) x (35/14) = 5/14
		assertEquals(5.0/14.0,computed,DELTA);
		
		computed = Modularity.computeMaxModularity(g, componentMembership);
		assertEquals(0.5,computed,DELTA);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(10.0/14.0,computed,DELTA);
		
	}
	
	@Test
	public void testDirected1() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12","c1.v1","c2.v1");
		
		double computed = Modularity.computeModularity(g, componentMembership);
		
		// each component is 3x3 matrix, rows (elements of sum in formula) are:
		// -4/14 8/14  10/14
		// 8/14  -9/14 8/14
		// 10/14 8/14  -4/14
		// this yields (2/14) x (35/14) = 5/14
		assertEquals(5.0/14.0,computed,DELTA);
		
		computed = Modularity.computeMaxModularity(g, componentMembership);
		assertEquals(0.5,computed,DELTA);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(10.0/14.0,computed,DELTA);
		
	}
	
	
	@Test
	public void testUndirectedModular1() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12","c1.v1","c2.v1");
		

		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
		
	}

	@Test
	public void testDirectedModular1() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12","c1.v1","c2.v1");
		

		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
		
	}
	
	@Test
	/**
	 * Two disconnected components. Note that the modularity is not maximal (=1)
	 * This is discussed in Newman: NEtworks on p. 224. Scaling is necessary. 
	 * @throws Exception
	 */
	public void testUndirected2() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");

		Transformer<String,String> componentMembership2 = new Transformer<String,String>() {
			@Override
			public String transform(String s) {
				return "the one big ball on mud"; 
			}
		};
		
		double computed = Modularity.computeModularity(g, componentMembership2);

		// each component is 3x3 matrix, in each row the sum is -4/12 + 8/12 + 8/12 = -1/3 + 2/3 + 2/3 = 1,
		// this yields 1x6 div by 2m=12 is 1/2
//		assertEquals(0.5,computed,DELTA);
//		
//		computed = Modularity.computeMaxModularity(g, componentMembership);
//		assertEquals(0.5,computed,DELTA);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(1.0,computed,DELTA);
		
	}
	
	@Test
	/**
	 * Two disconnected components. Note that the modularity is not maximal (=1)
	 * This is discussed in Newman: NEtworks on p. 224. Scaling is necessary. 
	 * @throws Exception
	 */
	public void testDirected2() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");

		Transformer<String,String> componentMembership2 = new Transformer<String,String>() {
			@Override
			public String transform(String s) {
				return "the one big ball on mud"; 
			}
		};
		
		double computed = Modularity.computeModularity(g, componentMembership2);

		// each component is 3x3 matrix, in each row the sum is -4/12 + 8/12 + 8/12 = -1/3 + 2/3 + 2/3 = 1,
		// this yields 1x6 div by 2m=12 is 1/2
//		assertEquals(0.5,computed,DELTA);
//		
//		computed = Modularity.computeMaxModularity(g, componentMembership);
//		assertEquals(0.5,computed,DELTA);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(1.0,computed,DELTA);
		
	}
	
	@Test
	/**
	 * Two disconnected components. Note that the modularity is not maximal (=1)
	 * This is discussed in Newman: NEtworks on p. 224. Scaling is necessary. 
	 * @throws Exception
	 */
	public void testUndirectedModular2() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");

		
		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
		
	}
	
	@Test
	/**
	 * Two disconnected components. Note that the modularity is not maximal (=1)
	 * This is discussed in Newman: NEtworks on p. 224. Scaling is necessary. 
	 * @throws Exception
	 */
	public void testDirectedModular2() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");

		
		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
		
	}
	
	/**
	 * Scenario with stronger (3) inter-component links.
	 * @throws Exception
	 */
	@Test
	public void testUndirected3() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computed = Modularity.computeModularity(g, componentMembership);
		
		// each component is 3x3 matrix, in each row the sum is -9/18 + 9/18 + 9/18 = -1/2 + 1/2 + 1/2 = 1/2,
		// this yields 6x1/2 div by 2m=18 is 1/6
		assertEquals(1.0/6.0,computed,DELTA);
		
		computed = Modularity.computeMaxModularity(g, componentMembership);
		assertEquals(0.5,computed,0.1);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(1.0/3.0,computed,DELTA);

		
	}
	
	/**
	 * Scenario with stronger (3) inter-component links.
	 * @throws Exception
	 */
	@Test
	public void testDirected3() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computed = Modularity.computeModularity(g, componentMembership);
		
		// each component is 3x3 matrix, in each row the sum is -9/18 + 9/18 + 9/18 = -1/2 + 1/2 + 1/2 = 1/2,
		// this yields 6x1/2 div by 2m=18 is 1/6
		assertEquals(1.0/6.0,computed,DELTA);
		
		computed = Modularity.computeMaxModularity(g, componentMembership);
		assertEquals(0.5,computed,0.1);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(1.0/3.0,computed,DELTA);

		
	}
	
	/**
	 * Scenario with stronger (3) inter-component links.
	 * @throws Exception
	 */
	@Test
	public void testUndirectedModular3() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
	
	}
	
	/**
	 * Scenario with stronger (3) inter-component links.
	 * @throws Exception
	 */
	@Test
	public void testDirectedModular3() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
	
	}
	
	/**
	 * Scenario with no intra-component links.
	 * @throws Exception
	 */
	@Test
	public void testUndirected4() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");

		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computed = Modularity.computeModularity(g, componentMembership);
		
		// each component is 3x3 matrix, in each row the sum is -1/6 + -1/6 + -1/6 = -1/2,
		// this yields 6x-1/2 div by 2m=6 is -1/2
		assertEquals(-0.5,computed,DELTA);
		
		computed = Modularity.computeMaxModularity(g, componentMembership);
		assertEquals(0.5,computed,DELTA);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(-1.0,computed,DELTA);
	}
	
	/**
	 * Scenario with no intra-component links.
	 * @throws Exception
	 */
	@Test
	public void testDirected4() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");

		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computed = Modularity.computeModularity(g, componentMembership);
		
		// each component is 3x3 matrix, in each row the sum is -1/6 + -1/6 + -1/6 = -1/2,
		// this yields 6x-1/2 div by 2m=6 is -1/2
		assertEquals(-0.5,computed,DELTA);
		
		computed = Modularity.computeMaxModularity(g, componentMembership);
		assertEquals(0.5,computed,DELTA);
		
		computed = Modularity.computeScaledModularity(g, componentMembership);
		assertEquals(-1.0,computed,DELTA);
	}
	
	/**
	 * Scenario with no intra-component links.
	 * @throws Exception
	 */
	@Test
	public void testUndirectedModular4() throws Exception {
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");

		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
	}
	
	/**
	 * Scenario with no intra-component links.
	 * @throws Exception
	 */
	@Test
	public void testDirectedModular4() throws Exception {
		
		DirectedGraph<String,String> g = new DirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");

		// inter-component edges
		g.addEdge("e12-1","c1.v1","c2.v1");
		g.addEdge("e12-2","c1.v2","c2.v2");
		g.addEdge("e12-3","c1.v3","c2.v3");
		
		double computedMonolithic = Modularity.computeModularity(g, componentMembership);
		double computedModule1 = Modularity.computeModuleModularity(g, isInModule1);
		double computedModule2 = Modularity.computeModuleModularity(g, isInModule2);
		double combinedModularity = computedModule1 + computedModule2;
		assertEquals(computedMonolithic,combinedModularity,DELTA);
	}
	
	/**
	 * Scenario with only one component.
	 * @throws Exception
	 */
	@Test
	public void testMonolithic() throws Exception {
		
		Transformer<String,String> componentMembership2 = new Transformer<String,String>() {
			@Override
			public String transform(String s) {
				return "42"; // only one component
			}
		};
		
		Graph<String,String> g = new UndirectedSparseGraph<String,String>();
		// component 1
		g.addVertex("c1.v1");
		g.addVertex("c1.v2");
		g.addVertex("c1.v3");
		// component 2
		g.addVertex("c2.v1");
		g.addVertex("c2.v2");
		g.addVertex("c2.v3");
		// edges within component 1
		g.addEdge("c1.e12","c1.v1","c1.v2");
		g.addEdge("c1.e23","c1.v2","c1.v3");
		g.addEdge("c1.e31","c1.v3","c1.v1");
		// edges within component 2
		g.addEdge("c2.e12","c2.v1","c2.v2");
		g.addEdge("c2.e23","c2.v2","c2.v3");
		g.addEdge("c2.e31","c2.v3","c2.v1");
		// inter-component edges
		g.addEdge("e12","c1.v1","c2.v1");
		
		double computed = Modularity.computeModularity(g, componentMembership2);
		
		// each component is 3x3 matrix, in each row the sum is -1/6 + -1/6 + -1/6 = -1/2,
		// this yields 3x-1/2 div by 2m=6 is -1/2
		// assertEquals(-0.5,computed,DELTA);
		
		
		computed = Modularity.computeScaledModularity(g, componentMembership2);
		assertEquals(0.0,computed,DELTA);
	}
	
}
