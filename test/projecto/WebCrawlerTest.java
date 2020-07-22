/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import projecto.model.Link;
import projecto.model.Page;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import org.junit.*;

/**
 * Ficheiro de testes unitário para a classe WebCrawler
 * 
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawlerTest {
    
    private WebCrawler web;
    private final List<Page> pages;
    private final List<Link> links;
    
    public WebCrawlerTest() {
        pages = new ArrayList();
        links = new ArrayList();
    }

    @Before
    public void setUp(){
        web = new WebCrawler();
        
        Page p1 = new Page("Main", Page.PageRole.MAIN);
        
        Page p2 = new Page("pagina1", Page.PageRole.SUB);
        Page p3 = new Page("pagina2", Page.PageRole.SUB);
        Page p4 = new Page("pagina3", Page.PageRole.SUB);
        
        pages.add(p1);
        pages.add(p2);
        pages.add(p3);
        pages.add(p4);
        
        pages.forEach((p) -> {
            web.addPage(p);
        });
        
        Link l = new Link("main.pt", "Pagina1 para Main");
        Link l1 = new Link("main.pt/pagina1", "Main para Pagina1");
        Link l2 = new Link("main.pt/pagina2", "Main para Pagina2");
        Link l3 = new Link("main.pt/pagina3", "Main para Pagina3");
        Link l4 = new Link("main.pt/pagina3/pagina2", "Pagina3 para Pagina2");
        Link l5 = new Link("main.pt/pagina2/pagina1", "Pagina2 para Pagina1");
        
        links.add(l);
        links.add(l1);
        links.add(l2);
        links.add(l3);
        links.add(l4);
        links.add(l5);
        
        web.addLink(l, p2, p1);
        web.addLink(l1, p1, p2);
        web.addLink(l2, p1, p3);
        web.addLink(l3, p1, p4);
        web.addLink(l4, p4, p3);
        web.addLink(l5, p3, p2);
    }
    
    /***
     * Teste para verificar se a main page existe
     */
    @Test
    public void test_get_main_page(){
        System.out.println("test_get_main_page");
        
        assertEquals("A página Main existe", web.getPagebyTitle("Main"), web.getMainPage().element()); 
    }
    
    /***
     * Teste para verificar se existe uma determinada página
     */
    @Test
    public void test_page_exists_after_insert(){
        System.out.println("test_page_exists_after_insert");
        
        assertEquals("A página Main existe", true, web.havePage(web.getPagebyTitle("Main")));
        assertEquals("A página pagina1 existe", true, web.havePage(web.getPagebyTitle("pagina1")));
        assertEquals("A página pagina3 existe", true, web.havePage(web.getPagebyTitle("pagina3")));
        assertEquals("A página pagina3 existe", false, web.havePage(web.getPagebyTitle("pagina5")));
    }
    
    /***
     * Teste para verificar se existe um determinado link
     */
    @Test
    public void test_link_exists_after_insert(){
        System.out.println("test_page_exists_after_insert");
        
        assertEquals("O link da página pagina1 para a Main existe", true, 
        web.haveLink("main.pt", web.getPagebyTitle("pagina1"), web.getPagebyTitle("Main")));
        
        assertEquals("O link da página Main para a pagina1 existe", true, 
        web.haveLink("main.pt/pagina1", web.getPagebyTitle("Main"), web.getPagebyTitle("pagina1")));
        
        assertEquals("O link da página Main para a pagina6 existe", false, 
        web.haveLink("main.pt/pagina6", web.getPagebyTitle("Main"), web.getPagebyTitle("pagina6")));
    }
    
    /***
     * Teste para verificar se uma página existe
     */
    @Test
    public void test_check_page(){
        System.out.println("test_check_page");
        
        Page pagina = pages.get(0);
        
        Page find = null;
        
        for(Page p : pages){
            if(p.getTitle().equals(pagina.getTitle())){
                find = p;
            }
        }
        
        assertEquals(find, web.checkPage(pagina).element());
    }
    
    /***
     * Teste para verificar se existe um link entre 2 páginas
     */
    @Test
    public void test_links_between(){
        System.out.println("test_links_between");

        assertEquals("main.pt/pagina1", true, web.haveLink(links.get(1).getUrl(),web.getPagebyTitle("Main"), web.getPagebyTitle("pagina1")));
    }
    
    /***
     * Teste para verificar o algoritmo BFS
     */
    @Test
    public void test_BFS(){
        System.out.println("test_BFS");
        
        //Main, pagina1, pagina2, pagina3
        assertTrue("BFS está correto", equals(web.BFS(web.getMainPage()), web.getMainPage().element(),
                web.getPagebyTitle("pagina1"),
                web.getPagebyTitle("pagina2"),
                web.getPagebyTitle("pagina3"))); 
    }
    
    /***
     * Teste para verificar o algoritmo DFS
     */
    @Test
    public void test_DFS(){
        System.out.println("test_DFS");
        
        //Main, pagina3, pagina2, pagina1
        assertTrue("DFS está correto", equals(web.DFS(web.getMainPage()), web.getMainPage().element(),
                web.getPagebyTitle("pagina3"),
                web.getPagebyTitle("pagina2"),
                web.getPagebyTitle("pagina1")));
    }

    /***
     * Método para verificar se listas são iguais
     * @param atual A lista executada pelo método pretendido
     * @param expected A lista esperada
     * @return true se forem iguais, caso contrário, false
     */
    private boolean equals(Iterable<Page> atual, Page... expected){
        int i= 0;
        int count = 0;
        
        for(Page a : atual){
            if(a != expected[i++]) return false;
            count++;
        }
        
        return count == expected.length;
    }

    
}
