/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Edge;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import com.brunomnsilva.smartgraph.graph.TAD_DiGraph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import projecto.model.Page;
import projecto.model.WebCrawlerException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import projecto.model.Link;
import projecto.model.Memento;
import projecto.model.MementoWebCrawler;
import projecto.model.Observer;
import projecto.model.Originator;
import projecto.model.WebLogger;

/**
 * Classe que, é feita a gestão geral do Digrafo e suas estatística.
 *
 * @author Diogo Venâncio - 160221076
 * @author Miguel Lapão - 170221003
 */
public class WebCrawler implements Originator, Serializable {

    private Digraph<Page, Link> web;
    private HashSet<Observer> observer;
    private List<Page> path;

    /**
     *
     * Construtor da classe
     */
    public WebCrawler() {
        web = new TAD_DiGraph<>();
        observer = new HashSet<>();
        path = new ArrayList<>();
    }

    /**
     * Adiciona um Observer ao HashSet
     *
     * @param obs Observer
     */
    public void add(Observer obs) {
        observer.add(obs);
    }

    /**
     * Remove um Observer do HashSet
     *
     * @param obs Observer
     */
    public void remove(Observer obs) {
        observer.remove(obs);
    }

    /**
     * Notifica todos os observers
     */
    public void notifyObservers() {
        for (Observer obs : observer) {
            obs.update(this);
        }
    }

    /**
     * Retorna o estado atual do Digraph
     *
     * @return Digraph
     */
    public Digraph<Page, Link> getDiGraph() {
        return web;
    }

    /**
     * Alterar o estado do Digraph
     *
     * @param di Digraph
     */
    public void setDiGraph(Digraph<Page, Link> di) {
        this.web = di;
    }

    /**
     * Retorna o estado atual do WebCrawler
     *
     * @return WebCrawler
     */
    public WebCrawler getWebCrawler() {
        return this;
    }

    /**
     * Cria um memento da classe
     *
     * @return Memento
     */
    @Override
    public Memento createMemento() {
        return new MementoWebCrawler(this, path);
    }

    /**
     * Altera o estado do Digraph quando é feito UNDO
     *
     * @param memento Memento
     */
    @Override
    public void setMemento(Memento memento) {

        List<Page> pages = new ArrayList<>();

        for (Vertex<Page> p : memento.getDiGraphMemento().vertices()) {
            pages.add(p.element());
        }

        for (Vertex<Page> p : getDiGraph().vertices()) {
            if (!pages.contains(p.element())) {
                getDiGraph().removeVertex(p);
            }
        }

        this.path = memento.getPathIterativo();
    }

    /**
     *
     * Verifica se uma página existe e retorna-a caso exista
     *
     * @param page Recebe uma página
     * @return Vertex do tipo Page
     * @throws WebCrawlerException Exceção
     */
    public Vertex<Page> checkPage(Page page) throws WebCrawlerException {
        if (page == null) {
            throw new WebCrawlerException("Página não pode estar null");
        }

        Vertex<Page> find = null;
        for (Vertex<Page> v : web.vertices()) {
            if (v.element().equals(page)) {
                find = v;
            }
        }

        if (find == null) {
            throw new WebCrawlerException("A página com o titulo (" + page.getTitle() + ") não existe");
        }

        return find;
    }

    /**
     *
     * Associar uma página a um vértice
     *
     * @param p Recebe uma página
     * @throws WebCrawlerException Exceção
     */
    public void addPage(Page p) throws WebCrawlerException {

        if (p == null) {
            throw new WebCrawlerException("Página não pode estar vazia!");
        }

        web.insertVertex(p);
    }

    /**
     *
     * Relaciona duas páginas com um link
     *
     * @param url Url do link associado ás duas páginas
     * @param idPage1 Página que irá ficar como outbound
     * @param idPage2 Página que irá ficar como inbound
     * @throws WebCrawlerException Exceção
     */
    public void addLink(Link url, Page idPage1, Page idPage2) throws WebCrawlerException {

        Vertex<Page> page1 = checkPage(idPage1);

        if (page1 == null) {
            throw new WebCrawlerException();
        }

        Vertex<Page> page2 = checkPage(idPage2);

        if (page2 == null) {
            throw new WebCrawlerException();
        }

        web.insertEdge(page1, page2, url);
    }

    /**
     *
     * Retorna a lista de links entre 2 páginas
     *
     * @param page1 Recebe uma página
     * @param page2 Recebe uma página
     * @return Uma lista de links que estão associados a essas páginas
     * @throws WebCrawlerException Exceção
     */
    public List<Link> getLinksBetween(Page page1, Page page2) {

        List<Link> links = new ArrayList<>();

        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.vertices()[0].element() == page1 && edge.vertices()[1].element() == page2
                    || edge.vertices()[0].element() == page2 && edge.vertices()[1].element() == page1) {
                links.add(edge.element());
            }
        }
        return links;
    }

    /**
     *
     * Retorna o vertice correspondente á página passada como parametro
     *
     * @param page Recebe uma página
     * @return Vertex do tipo Page
     */
    public Vertex<Page> getPage(Page page) {
        for (Vertex<Page> v : web.vertices()) {
            if (v.element().equals(page)) {
                return v;
            }
        }
        return null;
    }

    /**
     *
     * Retorna true se a página existir
     *
     * @param page Recebe uma página
     * @return Vertex do tipo Page
     */
    public boolean havePage(Page page) {
        for (Vertex<Page> v : web.vertices()) {
            if (v.element().equals(page)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Retorna true se existe um link entre as páginas
     *
     * @param url Url de um link
     * @param page Recebe uma página
     * @param page1 Recebe uma página
     * @return true se existe esse link entre as 2 páginas
     */
    public boolean haveLink(String url, Page page, Page page1) {
        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.vertices()[0].element().equals(page) && edge.vertices()[1].element().equals(page1) && edge.element().getUrl().equals(url)
                    || edge.vertices()[0].element().equals(page1) && edge.vertices()[1].element().equals(page) && edge.element().getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Retorna o edge correspondente ao link passado como parametro
     *
     * @param link Recebe um link
     * @return Elemento ao qual o link está associdado
     */
    public Edge<Link, Page> getLinkEdge(Link link) {
        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.element().equals(link)) {
                return edge;
            }
        }
        return null;
    }

    /**
     *
     * Retorna true se corresponder ao url passado como parametro
     *
     * @param page Page
     * @param url Url a verificar
     * @return true se existir
     */
    public boolean LinkExist(Page page, String url) {
        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.vertices()[0].element().equals(page) && edge.element().getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Retorna true se corresponder ao url passado como parametro
     *
     * @param page Outbound page
     * @param page1 Inbound page
     * @param url Url entre as pages
     * @return true se existir
     */
    public boolean LinkExistBetweenPages(Page page, Page page1, String url) {
        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.vertices()[0].element().equals(page) && edge.vertices()[1].element().equals(page1)
                    && edge.element().getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Retorna uma lista de links incidentes a uma determinada página
     *
     * @param page Recebe um vertice de uma página
     * @return Lista de links que são incidentes a uma página
     */
    public List<Link> getIncidentLinks(Vertex<Page> page) {

        List<Link> inboundEdges = new ArrayList<>();

        for (Edge<Link, Page> edges : web.incidentEdges(page)) {
            inboundEdges.add(edges.element());
        }

        return inboundEdges;
    }

    /**
     *
     * Retorna uma lista de links que saiem de uma determinada página
     *
     * @param page Recebe um vertice de uma página
     * @return Lista de links que estão outbound a uma página
     */
    public List<Link> getOutboundLinks(Vertex<Page> page) {

        List<Link> outboundEdges = new ArrayList<>();

        for (Edge<Link, Page> edges : web.outboundEdges(page)) {
            outboundEdges.add(edges.element());
        }

        return outboundEdges;
    }

    /**
     *
     * Retorna a página consoante o seu título
     *
     * @param title Titulo de uma página
     * @return Retorna uma página consoante o seu titulo
     */
    public Page getPagebyTitle(String title) {

        for (Vertex<Page> v : web.vertices()) {
            if (v.element().getTitle().equals(title)) {
                return v.element();
            }
        }
        return null;
    }

    /**
     *
     * Retorna a página inicial
     *
     * @return Retorna um vertice com a página inicial
     */
    public Vertex<Page> getMainPage() {
        ArrayList<Vertex<Page>> main = new ArrayList<>();

        for (Vertex<Page> v : web.vertices()) {
            if (v.element().isRole(Page.PageRole.MAIN)) {
                main.add(v);
            }
        }
        return main.get(0);
    }

    /**
     *
     * Retorna o número total de sub páginas associadas à página inicial
     *
     * @param p Recebe uma página
     * @return count Retorna o número de página ao qual esta está associdada
     * @throws WebCrawlerException Exceção
     */
    public int getNumberofSubPages(Page p) throws WebCrawlerException {

        if (p == null) {
            throw new WebCrawlerException("Página não pode estar vazia!");
        }

        Vertex<Page> page = checkPage(p);

        int count = 0;

        for (Edge<Link, Page> edge : web.incidentEdges(page)) {
            Page subpage = web.opposite(page, edge).element();
            if (subpage.isRole(Page.PageRole.SUB)) {
                count++;
            }
        }

        return count;
    }

    /**
     *
     * Retorna a página com mais ligações
     *
     * @return Pagina que tem o maior número de relações
     */
    public Page getMostLinkedPage() {

        Page popular = null;
        int maxRelationships = 0;

        for (Vertex<Page> v : web.vertices()) {
            int nRela = getAdjacent(v).size();
            if (nRela > maxRelationships) {
                maxRelationships = nRela;
                popular = v.element();
            }
        }
        return popular;
    }

    /**
     *
     * Retorna a lista de páginas adjacentes a uma determinada página
     *
     * @param page Recebe um vertice de uma página
     * @return Lista de vertices
     */
    public List<Vertex<Page>> getAdjacent(Vertex<Page> page) {

        List<Vertex<Page>> list = new ArrayList();

        for (Vertex<Page> p : web.vertices()) {
            if (web.areAdjacent(page, p)) {
                list.add(p);
            }
        }

        return list;
    }

    /**
     * Lista de links associados a um conjunto de links
     *
     * @param elements Recebe um conjunto de links
     * @return Lista de links
     */
    private List<Element> getLinks(Elements elements) {
        List<Element> elem = new ArrayList<>();
        elements.forEach((link) -> {
            elem.add(link);
        });
        return elem;
    }

    /**
     * Retorna a lista de todos os links associados a um link "principal"
     *
     * @param url Recebe um link
     * @return Lista de links
     */
    private List<Element> getLinksByUrl(String url) throws IOException {
        List<Element> elem = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        elem.addAll(getLinks(links));
        return elem;
    }

    /**
     * Retorna o url associado a uma página
     *
     * @param p Recebe uma página
     * @return Url
     */
    public String getUrlPage(Page p) {
        String url = null;

        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.vertices()[1].element().equals(p)) {
                url = edge.element().getUrl();
            }
        }
        return url;
    }

    /**
     * Retorna uma pagina relacionada a um url
     *
     * @param url Url pretendido
     * @return Page
     */
    public Page getPageByUrl(String url) {
        Page p = null;

        for (Edge<Link, Page> edge : web.edges()) {
            if (edge.element().getUrl().equals(url)) {
                p = edge.vertices()[1].element();
            }
        }
        return p;
    }

    /**
     * Retorna o caminho atual feito pelo Digraph
     *
     * @return Lista de pages
     */
    public List<Page> getPath() {
        return path;
    }

    /**
     * Verifica se o url está vazio
     *
     * @param string Url
     * @return true se não tiver
     */
    public static boolean urlNotEmpty(String string) {
        return !(string == null || string.length() == 0);
    }

    /**
     * Imprime os stats para a consola do Digraph atual
     *
     * @return String
     */
    public String printStats() {
        String str = "";

        str += "Páginas existentes: " + getNumPages() + "\n";
        str += "Links existentes: " + getNumLinks() + "\n";

        str += "Número de páginas não encontradas: " + getRedPages() + "\n";

        String titulo = getMostLinkedPage().getTitle();

        if (titulo.equals("")) {
            str += "Página com mais relações: NULL\n";
        } else {
            str += "Página com mais relações: " + titulo + "\n";
        }

        str += "\nCaminho percorrido: ";
        for (Page pagina : path) {
            if (pagina.getTitle().equals(path.get(path.size() - 1).getTitle())) {
                str += pagina.getTitle();
            } else {
                str += pagina.getTitle() + " -> ";
            }

        }

        return str;
    }

    /**
     * Retorna o número de Pages contidas no Digraph
     *
     * @return Total pages
     */
    public int getNumPages() {
        return web.vertices().size();
    }

    /**
     * Retorna o número de Links contidas no Digraph
     *
     * @return Total links
     */
    public int getNumLinks() {
        return web.edges().size();
    }

    /**
     * Retorna o número de páginas que não foram encontradas
     *
     * @return Total not found
     */
    public int getRedPages() {
        int count = 0;
        for (Vertex<Page> p : web.vertices()) {
            if (p.element().getColor().equals("red") || p.element().getTitle().contains("404")) {
                count++;
            }
        }
        return count;
    }

    /**
     * *
     * Gera o graph consoante a página que o utilizador deseja seguir
     *
     * @param urlAddress Url da página inicial
     * @throws java.io.IOException Exceção
     */
    public void grafoGenerator(String urlAddress) throws IOException {

        Queue<Page> queue = new LinkedList<>();

        List<Element> ele = getLinksByUrl(urlAddress);

        Document doc = Jsoup.connect(urlAddress).get();
        String title = doc.title();

        Page p = getPageByUrl(urlAddress);

        if (p == null) {
            Page p1 = new Page(title, Page.PageRole.MAIN);
            addPage(p1);
            queue.offer(p1);
        } else {
            queue.offer(p);
        }

        while (!queue.isEmpty()) {
            if (queue.size() == 1) {
                Page pagina = queue.poll();

                if (!path.contains(pagina)) {
                    path.add(pagina);

                    String linkP = getUrlPage(pagina);
                    if (linkP != null) {
                        ele = getLinksByUrl(linkP);
                    }

                    for (Element elem : ele) {

                        String url = elem.attr("abs:href");
                        String label = elem.text();

                        if (!label.contains("404")) {
                            if (urlNotEmpty(url)) {
                                Document titlePage = Jsoup.connect(url).get();
                                if (titlePage != null) {
                                    String titulo = titlePage.title();
                                    if (!label.isEmpty()) {
                                        Page pp = getPagebyTitle(titulo);
                                        if (pp == null || !havePage(pp)) {
                                            Page newPage = new Page(titulo, Page.PageRole.SUB);
                                            addPage(newPage);
                                            Vertex<Page> VPage = getPage(newPage);

                                            Link link1 = new Link(url, label);
                                            addLink(link1, pagina, VPage.element());

                                            WebLogger.getInstance().saveToLog(pagina, newPage, link1, this);
                                        } else if (!pagina.getTitle().equals(pp.getTitle()) && !LinkExistBetweenPages(pagina, pp, url)) {
                                            Link link1 = new Link(url, label);
                                            addLink(link1, pagina, getPagebyTitle(titulo));

                                            WebLogger.getInstance().saveToLog(pagina, pp, link1, this);
                                        }
                                    } else {
                                        createPageNotFound(pagina, url);
                                    }
                                }
                            }
                        } else {
                            createPageNotFound(pagina, url);
                        }
                    }
                }
            }
        }
    }

    /**
     * Método responsável pelo BFS
     *
     * @param visited Lista de visitados
     * @param pages Lista de todas as paginas
     * @param queue Queue
     * @param pagina Pagina a ser usada
     * @param titulo Titulo da nova pagina
     * @param url Url para o novo link
     * @param label Descrição do novo link
     */
    private void generateBFS(Set<Page> visited, List<Vertex<Page>> pages,
            Queue<Page> queue, Page pagina, String titulo, String url, String label) {
        Page newPage = new Page(titulo, Page.PageRole.SUB);
        addPage(newPage);
        Vertex<Page> VPage = getPage(newPage);

        Link link1 = new Link(url, label);
        addLink(link1, pagina, VPage.element());

        WebLogger.getInstance().saveToLog(pagina, newPage, link1, this);

        queue.offer(newPage);
        pages.add(getPage(newPage));
        visited.add(newPage);
    }

    /**
     * Método responsável pelo DFS
     *
     * @param visited Lista de visitados
     * @param pages Lista de todas as paginas
     * @param stack Stack
     * @param pagina Pagina a ser usada
     * @param titulo Titulo da nova pagina
     * @param url Url para o novo link
     * @param label Descrição do novo link
     */
    private void generateDFS(Set<Page> visited, List<Vertex<Page>> pages,
            Stack<Page> stack, Page pagina, String titulo, String url, String label) {
        Page newPage = new Page(titulo, Page.PageRole.SUB);
        addPage(newPage);
        Vertex<Page> VPage = getPage(newPage);

        Link link1 = new Link(url, label);
        addLink(link1, pagina, VPage.element());

        WebLogger.getInstance().saveToLog(pagina, newPage, link1, this);

        stack.push(newPage);
        pages.add(getPage(newPage));
        visited.add(newPage);
    }

    /**
     * Método para criar páginas 404
     *
     * @param pagina Pagina atual
     * @param url Url para o novo link
     */
    private void createPageNotFound(Page pagina, String url) {
        Page p1 = new Page("404 – Not Found", Page.PageRole.SUB);
        addPage(p1);

        Link link1 = new Link(url, "Not found");
        addLink(link1, pagina, p1);

        WebLogger.getInstance().saveToLog(pagina, p1, link1, this);
    }

    /**
     * Método que, ao receber um url, irá criar uma pagina inicial
     *
     * @param url Url
     * @return Page
     * @throws IOException Caso o link seja inválido
     */
    private Page createMainPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();

        Page p = new Page(title, Page.PageRole.MAIN);
        return p;
    }

    /**
     * *
     * Gera o graph com bfs automáticamente
     *
     * @param urlAddress Url da página inicial
     * @throws java.io.IOException Exceção
     */
    public void grafoGenerator_BFS(String urlAddress) throws IOException {

        WebLogger.getInstance().clearLog();

        path.clear();
        List<Vertex<Page>> pages = new ArrayList();
        Set<Page> visited = new HashSet<>();

        Queue<Page> queue = new LinkedList<>();

        List<Element> ele = getLinksByUrl(urlAddress);

        Page p = createMainPage(urlAddress);

        addPage(p);
        queue.offer(p);
        visited.add(p);
        pages.add(getPage(p));

        while (!queue.isEmpty()) {
            Page pagina = queue.poll();

            path.add(pagina);

            String linkP = getUrlPage(pagina);
            if (linkP != null) {
                ele = getLinksByUrl(linkP);
            }

            for (Element elem : ele) {

                String url = elem.attr("abs:href");
                String label = elem.text();

                if (!label.contains("404")) {
                    if (urlNotEmpty(url)) {
                        Document titlePage = Jsoup.connect(url).get();
                        if (titlePage != null) {
                            String titulo = titlePage.title();

                            if (!label.isEmpty()) {
                                Page pp = getPagebyTitle(titulo);
                                if (!visited.contains(pp)) {
                                    if (pp == null || !havePage(pp)) {
                                        generateBFS(visited, pages, queue, pagina, titulo, url, label);
                                    }
                                } else if (!pagina.getTitle().equals(pp.getTitle()) && !LinkExistBetweenPages(pagina, pp, url)) {
                                    Link link1 = new Link(url, label);
                                    addLink(link1, pagina, getPagebyTitle(titulo));

                                    WebLogger.getInstance().saveToLog(pagina, pp, link1, this);
                                }
                            } else {
                                createPageNotFound(pagina, url);
                            }
                        }
                    }
                } else {
                    createPageNotFound(pagina, url);
                }
            }
        }
    }

    /**
     * *
     * Gera o graph com bfs até x páginas
     *
     * @param urlAddress Url da página inicial
     * @param maxPages O número maximo de páginas que o bfs vai percorrer
     * @throws java.io.IOException Exceção
     */
    public void grafoGenerator_MaxPages_BFS(String urlAddress, int maxPages) throws IOException {

        WebLogger.getInstance().clearLog();

        path.clear();
        List<Vertex<Page>> pages = new ArrayList();
        Set<Page> visited = new HashSet<>();

        Queue<Page> queue = new LinkedList<>();

        List<Element> ele = getLinksByUrl(urlAddress);

        Page p = createMainPage(urlAddress);

        addPage(p);
        queue.offer(p);
        visited.add(p);
        pages.add(getPage(p));

        int count = 1;

        while (!queue.isEmpty()) {
            Page pagina = queue.poll();

            path.add(pagina);

            String linkP = getUrlPage(pagina);
            if (linkP != null) {
                ele = getLinksByUrl(linkP);
            }

            for (Element elem : ele) {

                String url = elem.attr("abs:href");
                String label = elem.text();

                if (!label.contains("404")) {
                    if (urlNotEmpty(url)) {
                        Document titlePage = Jsoup.connect(url).get();
                        String titulo = titlePage.title();

                        if (count < maxPages) {
                            if (!label.isEmpty()) {
                                Page pp = getPagebyTitle(titulo);
                                if (!visited.contains(pp)) {
                                    if (pp == null || !havePage(pp)) {
                                        generateBFS(visited, pages, queue, pagina, titulo, url, label);
                                        count++;
                                    }
                                } else if (!pagina.getTitle().equals(pp.getTitle()) && !LinkExistBetweenPages(pagina, pp, url)) {
                                    Link link1 = new Link(url, label);
                                    addLink(link1, pagina, getPagebyTitle(titulo));

                                    WebLogger.getInstance().saveToLog(pagina, pp, link1, this);
                                }
                            } else {
                                createPageNotFound(pagina, url);
                            }
                        } else {
                            queue.clear();
                            break;
                        }
                    }
                } else {
                    createPageNotFound(pagina, url);
                }

            }
        }
    }

    /**
     * *
     * Gera o graph com dfs até x links
     *
     * @param urlAddress Url da página inicial
     * @param maxLinks O número maximo de links que o dfs vai percorrer
     * @throws java.io.IOException Exceção
     */
    public void grafoGenerator_MaxLink_DFS(String urlAddress, int maxLinks) throws IOException {

        WebLogger.getInstance().clearLog();

        path.clear();
        List<Vertex<Page>> pages = new ArrayList();
        Set<Page> visited = new HashSet<>();

        Stack<Page> stack = new Stack<>();

        List<Element> ele = getLinksByUrl(urlAddress);

        Page p = createMainPage(urlAddress);

        addPage(p);
        stack.push(p);
        visited.add(p);
        pages.add(getPage(p));

        int count = 0;

        while (!stack.isEmpty()) {
            Page pagina = stack.pop();

            path.add(pagina);

            String linkP = getUrlPage(pagina);
            if (linkP != null) {
                ele = getLinksByUrl(linkP);
            }

            for (Element elem : ele) {

                String url = elem.attr("abs:href");
                String label = elem.text();

                if (!label.contains("404")) {
                    if (urlNotEmpty(url)) {
                        Document titlePage = Jsoup.connect(url).get();
                        String titulo = titlePage.title();

                        if (count < maxLinks) {
                            if (!label.isEmpty()) {
                                Page pp = getPagebyTitle(titulo);
                                if (!visited.contains(pp)) {
                                    if (pp == null || !havePage(pp)) {
                                        generateDFS(visited, pages, stack, pagina, titulo, url, label);
                                        count++;
                                    }
                                } else if (!pagina.getTitle().equals(pp.getTitle()) && !LinkExistBetweenPages(pagina, pp, url)) {
                                    Link link1 = new Link(url, label);
                                    addLink(link1, pagina, getPagebyTitle(titulo));
                                    count++;

                                    WebLogger.getInstance().saveToLog(pagina, pp, link1, this);
                                }
                            } else {
                                createPageNotFound(pagina, url);
                                count++;
                            }
                        } else {
                            stack.clear();
                            break;
                        }
                    }
                } else {
                    createPageNotFound(pagina, url);
                    count++;
                }

            }
        }
    }

    /**
     * *
     * Gera o graph com dfs até uma pagina atingir x links, não pode expandir
     * mais
     *
     * @param urlAddress Url da página inicial
     * @param maxLinks O número maximo de links que as paginas podem expandir
     * @throws java.io.IOException Exceção
     */
    public void grafoGenerator_MaxRelan_DFS(String urlAddress, int maxLinks) throws IOException {

        WebLogger.getInstance().clearLog();

        path.clear();
        List<Vertex<Page>> pages = new ArrayList();
        Set<Page> visited = new HashSet<>();

        Stack<Page> stack = new Stack<>();

        List<Element> ele = getLinksByUrl(urlAddress);

        Page p = createMainPage(urlAddress);

        addPage(p);
        stack.push(p);
        visited.add(p);
        pages.add(getPage(p));

        while (!stack.isEmpty()) {
            Page pagina = stack.pop();

            path.add(pagina);

            String linkP = getUrlPage(pagina);
            if (linkP != null) {
                ele = getLinksByUrl(linkP);
            }

            for (Element elem : ele) {

                String url = elem.attr("abs:href");
                String label = elem.text();
                if (getOutboundLinks(getPage(pagina)).size() < maxLinks) {
                    if (!label.contains("404")) {
                        if (urlNotEmpty(url)) {
                            Document titlePage = Jsoup.connect(url).get();
                            String titulo = titlePage.title();
                            if (!label.isEmpty()) {
                                Page pp = getPagebyTitle(titulo);
                                if (!visited.contains(pp)) {
                                    if (pp == null || !havePage(pp)) {
                                        generateDFS(visited, pages, stack, pagina, titulo, url, label);
                                    }
                                } else if (!pagina.getTitle().equals(pp.getTitle()) && !LinkExistBetweenPages(pagina, pp, url)) {
                                    //if (getNumberofRelationships(getPage(getPagebyTitle(titulo))) < maxLinks) {
                                    Link link1 = new Link(url, label);
                                    addLink(link1, pagina, getPagebyTitle(titulo));

                                    WebLogger.getInstance().saveToLog(pagina, pp, link1, this);
                                    //}
                                }
                            } else {
                                createPageNotFound(pagina, url);
                            }
                        }
                    } else {
                        createPageNotFound(pagina, url);
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * *
     * Método BFS para uso dos testes
     *
     * @param page Vertice da Main page
     * @return Lista de pages
     */
    public List<Page> BFS(Vertex<Page> page) {

        List<Page> path = new ArrayList();
        List<Vertex<Page>> pages = new ArrayList();
        Set<Vertex<Page>> visited = new HashSet<>();

        Queue<Vertex<Page>> queue = new LinkedList<>();

        queue.offer(page);
        visited.add(page);

        while (!queue.isEmpty()) {
            Vertex<Page> look = queue.poll();
            path.add(look.element());

            for (Vertex<Page> adj : getAdjacent(look)) {
                if (!visited.contains(adj)) {
                    visited.add(adj);
                    pages.add(adj);
                    queue.offer(adj);
                }
            }
        }
        return path;
    }

    /**
     * *
     * Método DFS para uso dos testes
     *
     * @param page Vertice da Main page
     * @return Lista de pages
     */
    public List<Page> DFS(Vertex<Page> page) {

        List<Page> path = new ArrayList();
        List<Vertex<Page>> pages = new ArrayList();
        Set<Vertex<Page>> visited = new HashSet<>();

        Stack<Vertex<Page>> stack = new Stack<>();

        stack.push(page);
        visited.add(page);

        while (!stack.isEmpty()) {
            Vertex<Page> look = stack.pop();
            path.add(look.element());

            for (Vertex<Page> adj : getAdjacent(look)) {
                if (!visited.contains(adj)) {
                    visited.add(adj);
                    pages.add(adj);
                    stack.push(adj);
                }
            }
        }
        return path;
    }

    /**
     * *
     * Método para visualizar a página web através do seu link
     *
     * @param url Url a visitar no browser
     * @throws IOException Exceção
     */
    public void seeLink(String url) throws IOException {
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI(url));

        } catch (URISyntaxException ex) {
            Logger.getLogger(WebCrawler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *
     * Determinar qual o caminho mais curto
     *
     * @param orig Vertice page
     * @param costs custos
     * @param predecessors Predecessors
     */
    private void dijkstra(Vertex<Page> orig,
            Map<Vertex<Page>, Double> costs,
            Map<Vertex<Page>, Vertex<Page>> predecessors) {

        List<Vertex<Page>> unvisited = new ArrayList<>();

        /*
        * Percorre todas as pages do grafo
        * Adiciona as pages aos não visitados
        * Coloca o o mesmo dentro dos custos
        * Insere o mesmo nos predecessors
         */
        for (Vertex<Page> v : web.vertices()) {
            unvisited.add(v);
            costs.put(v, Double.MAX_VALUE);
            predecessors.put(v, null);
        }

        costs.put(orig, 0.0);

        /*
        * Enquanto os não visitados forem diferentes de vazio
         */
        while (!unvisited.isEmpty()) {

            /*
            * Vertice não visitado correspondente á page mais low em cost
             */
            Vertex<Page> lowCostVertex = findLowerCostVertex(unvisited, costs);
            unvisited.remove(lowCostVertex);

            /*
            * Percorre o grafo consoante os edges incidentes do vertex mais low
             */
            for (Edge<Link, Page> edge : web.incidentEdges(lowCostVertex)) {

                /*
                * Vertex que corresponde ao opposite consoante o vertex mais low em cost
                * e um edge que tenha o mesmo
                 */
                Vertex<Page> opposite = web.opposite(lowCostVertex, edge);

                /*
                * Caso os não visitados contenham o opposite
                 */
                if (unvisited.contains(opposite)) {
                    double cost = 1;

                    /*
                    * Soma depois o valor corresponde ao vertix de low cost
                     */
                    cost += costs.get(lowCostVertex);

                    /*
                    * Caso o cost do opposite seja maior que o cost
                     */
                    if (costs.get(opposite) > cost) {
                        costs.put(opposite, cost);
                        predecessors.put(opposite, lowCostVertex);
                    }
                }
            }
        }
    }

    /**
     * *
     * Retorna o vertice ao qual vai corresponder á page que tem menor custo
     *
     * @param unvisited Lista de vertices não visitados type page
     * @param costs custo de cada page
     * @return Vertex<Page>
     */
    private Vertex<Page> findLowerCostVertex(List<Vertex<Page>> unvisited,
            Map<Vertex<Page>, Double> costs) {

        double min = Double.MAX_VALUE;

        Vertex<Page> minAir = null;

        for (Vertex<Page> v : unvisited) {
            if (costs.get(v) <= min) {
                minAir = v;
                min = costs.get(v);
            }
        }
        return minAir;
    }

    /**
     * Retorna o custo do menor caminho
     *
     * @param orig Page
     * @param dst Page
     * @param pages Lista de pages
     * @param links Lista de links
     * @return Total custo
     * @throws WebCrawlerException Exceção
     */
    public int minimumCostPath(Page orig, Page dst,
            List<Page> pages, List<Link> links)
            throws WebCrawlerException {

        if (orig == null || dst == null) {
            throw new WebCrawlerException("Page inválida");
        }

        Map<Vertex<Page>, Vertex<Page>> predecessors = new HashMap<>();
        Map<Vertex<Page>, Double> costs = new HashMap<>();

        pages.clear();

        Vertex<Page> origVertex = getPage(orig);
        Vertex<Page> dstVertex = getPage(dst);

        dijkstra(origVertex, costs, predecessors);

        double cost = costs.get(dstVertex);
        Vertex<Page> v = dstVertex;

        do {
            pages.add(0, v.element());

            /*
            * Antes de colocar o dstVertex como predecessor
             */
            Vertex<Page> before = v;
            v = predecessors.get(v);

            /*
            * Depois de colocar o dstVertex como predecssor
             */
            Vertex<Page> after = v;

            /*
            * Encontrar o link entre os 2
             */
            for (Link l : getLinksBetween(before.element(), after.element())) {

                /*
                * Criar um edge que corresponda ao link encontrado
                 */
                Edge<Link, Page> edge = getLinkEdge(l);

                /*
                * Se 2 pages corresponderem ao link, adiciona
                 */
                if (edge.vertices()[0] == before.element() || edge.vertices()[1].element() == after.element()) {
                    links.add(l);
                }
            }

        } while (v != origVertex);

        pages.add(0, orig);
        Collections.reverse(pages);
        return (int) cost;
    }
}
