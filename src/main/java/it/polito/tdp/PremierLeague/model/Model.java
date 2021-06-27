package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	SimpleWeightedGraph<Match,DefaultWeightedEdge> grafo;
	Map<Integer,Match> matches;
	List<Adiacenza> adiacenze;
	List<Match> vertici;
	List<Match> soluzione;
	int pesoMAx;
	
	public Model() {
		dao= new PremierLeagueDAO();
		matches= new HashMap<>();
		dao.listAllMatches(matches);
		adiacenze= new LinkedList<>();
		vertici= new LinkedList<>();
		
	}
	public void creaGrafo(int mese, int min) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		vertici=dao.getVertici(matches, mese);
		Graphs.addAllVertices(grafo, vertici);
		adiacenze= dao.getArchi(mese, matches, min);
		for(Adiacenza a: adiacenze) {
			if(grafo.vertexSet().contains(a.m1) && grafo.vertexSet().contains(a.m2)) {
				Graphs.addEdge(grafo, a.m1, a.m2, a.getPeso());
			}
		}
	}
	public int numVertici() {
		return grafo.vertexSet().size();
	}
	public int numArchi() {
		return grafo.edgeSet().size();
	}
	public int pesoMassimo() {
		if(grafo==null) {
			return 0;
		}
		int massimo=0;
		for(DefaultWeightedEdge ede: grafo.edgeSet()) {
			int peso=(int) grafo.getEdgeWeight(ede);
			if(peso>massimo) {
				massimo=peso;
			}
		}
		return massimo;
	}
	public List<Adiacenza> getMax(){
		if(grafo==null) {
			return null;
		}
		List<Adiacenza> result= new LinkedList<>();
		for(Adiacenza a: adiacenze) {
			if(a.getPeso()==pesoMassimo()) {
				result.add(a);
			}
		}
		return result;
	}
	public List<Match> getVertici(){
		if(grafo==null)
			return null;
		return vertici;
	}
	public List<Match> ricorsione(Match partenza, Match arrivo){
		
		this.pesoMAx=0;
		this.soluzione= new LinkedList<>();
		List<Match> parziale= new LinkedList<>();
		parziale.add(partenza);
		cerca(parziale,1, arrivo, 0);
		
		return soluzione;
	}
	private void cerca(List<Match> parziale, int livello, Match arrivo, int peso) {
		
		Match ultimo= parziale.get(parziale.size()-1);
		
		//condizione di terminazione
		if(ultimo.equals(arrivo) && peso>this.pesoMAx) {
			soluzione= new LinkedList<>(parziale);
			this.pesoMAx=peso;
		}
		
		for(Match m: Graphs.neighborListOf(grafo, ultimo)) {
			if(!parziale.contains(m) && !partitaOpposta(m,parziale) ) {
				peso+= grafo.getEdgeWeight(grafo.getEdge(m, ultimo));
				parziale.add(m);
				cerca(parziale, livello+1, arrivo, peso);
				peso-=grafo.getEdgeWeight(grafo.getEdge(m, ultimo));
				parziale.remove(m);
			}
		}
		
	}
	public boolean partitaOpposta(Match m, List<Match> parziale) {
		int casa= m.getTeamHomeID();
		int ospite= m.getTeamAwayID();
		for(Match mm: parziale) {
			if(mm.getTeamAwayID()==casa && mm.getTeamHomeID()==ospite)
				return true;
		}
		return false;
	}
	public int getPesoMAx() {
		return pesoMAx;
	}
}
