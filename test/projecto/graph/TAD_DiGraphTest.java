/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto.graph;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.TAD_DiGraph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.junit.*;

/**
 * Ficheiro de testes unitário para a TAD DiGraph
 * 
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class TAD_DiGraphTest {
    
    private TAD_DiGraph graph;
    private Vertex<Integer> p1, p2, p3, p4, p5;
    private Edge<Integer, Vertex<Integer>> l1, l2, l3, l4, l5;
    
    public TAD_DiGraphTest() {
    }
    
    @Before
    public void setUp(){
        graph = new TAD_DiGraph();
        
        p1 = graph.insertVertex(1);
        p2 = graph.insertVertex(2);
        p3 = graph.insertVertex(3);
        p4 = graph.insertVertex(4);
        p5 = graph.insertVertex(5);
        
        l1 = graph.insertEdge(1, 2, 1);
        l2 = graph.insertEdge(2, 3, 2);
        l3 = graph.insertEdge(3, 4, 3);
        l4 = graph.insertEdge(4, 5, 4);
        l5 = graph.insertEdge(5, 1, 5);
    }
    
    /***
     * Teste para verificar o size dos vertices
     */
    @Test
    public void test_vertices(){
        System.out.println("test_vertices");
        
        assertEquals(5, graph.numVertices());
    }
    
    /***
     * Teste para verificar o size dos edges
     */
    @Test
    public void test_edges(){
        System.out.println("test_edges");
        
        assertEquals(5, graph.numEdges());
    }
    
    /***
     * Teste para verificar os edges incidentes
     */
    @Test
    public void test_incident_edges(){
        System.out.println("test_incident_edges");
        
        List<Edge<Integer, Vertex<Integer>>> inc1 = new ArrayList();
        
        inc1.add(l4);
        
        List<Edge<Integer, Vertex<Integer>>> inc2 = new ArrayList();
        
        inc2.add(l2);
        
        
        assertEquals(inc1, graph.incidentEdges(p5));
        assertEquals(inc2, graph.incidentEdges(p3));
    }
    
    /***
     * Teste para verificar os outbound edges
     */
    @Test
    public void test_outbound_edges(){
        System.out.println("test_outbound_edges");
        
        List<Edge<Integer, Vertex<Integer>>> out1 = new ArrayList();
        
        out1.add(l1);
        
        List<Edge<Integer, Vertex<Integer>>> out2 = new ArrayList();
        
        out2.add(l2);
        
        
        assertEquals(out1, graph.outboundEdges(p1));
        assertEquals(out2, graph.outboundEdges(p2));
    }
    
    /***
     * Teste para verificar os vertices adjacentes
     */
    @Test
    public void test_are_adjacents(){
        System.out.println("test_are_adjacents");
        
        assertEquals("São adjacentes", true, graph.areAdjacent(p1,p2));
        assertEquals("São adjacentes", true, graph.areAdjacent(p3,p4));
        assertEquals("Não são adjacentes", false, graph.areAdjacent(p1,p4));
    }
    
    /***
     * Teste remover vertices
     */
    @Test
    public void test_remove_vertice(){
        System.out.println("test_remove_vertice");
        
        graph.removeVertex(p1);
        
        assertEquals("1 removido", true, !graph.vertices().equals(p1));
    }
    
    /***
     * Teste remover edges
     */
    @Test
    public void test_remove_edge(){
        System.out.println("test_remove_edge");
        
        graph.removeEdge(l3);
        graph.removeEdge(l5);
        
        assertEquals("l3 removido", true, !graph.edges().equals(l3));
        assertEquals("l5 removido", true, !graph.edges().equals(l5));
    }
    
}
