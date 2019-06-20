package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private Team squadraSelezionata;
	private Map<Season, Integer> punteggi;
	
	private Map <Integer, Season> stagioniIdMap;
	
	private Map <String, Team> squadreIdMap;
	
	private List<Team> squadre;
	private List<Season> stagioni;
	private int mass;
	
	


	Graph<Season, DefaultWeightedEdge> grafo;
	
	public Model() {
		SerieADAO dao = new SerieADAO();
		
		this.squadre = dao.listTeams();
		this.stagioni = dao.listAllSeasons();
		this.stagioniIdMap = new HashMap<Integer, Season>();
		this.squadreIdMap = new HashMap<String, Team>();
		for(Season s: this.stagioni) 
			this.stagioniIdMap.put(s.getSeason(), s);
			
		for(Team t : squadre) {
			this.squadreIdMap.put(t.getTeam(), t);
		}
	}
	
	
	//devo popolare la tendina con le squadre
	public List<Team> getSquadre() {
		return this.squadre;
	}
	
	public Map<Season, Integer> calcolaPunteggi(Team squadra) {
		
		this.squadraSelezionata = squadra;
		
		this.punteggi = new HashMap<Season, Integer>();
		SerieADAO dao = new SerieADAO();
		
		List<Match> partite = dao.listMatchesForTeam(squadra, squadreIdMap, stagioniIdMap);
		
		for(Match m : partite) {
			Season stagione = m.getSeason();
			int punti =0;
			
			if(m.getFtr().equals("D")) {
				punti = 1;
			} else {
				if((m.getHomeTeam().equals(squadra) && m.getFtr().equals("H")) ||
					(m.getAwayTeam().equals(squadra) && m.getFtr().equals("A")))
					punti = 3;
			}
			
			Integer attuale = punteggi.get(stagione);
			if (attuale == null) // perchè la prima volta è vuota la mappa
				attuale=0;
			punteggi.put(stagione, attuale+punti);
		}
		
		return punteggi;
	
	}
	
	public Season calcolaAnnataDOro() {
		
		//costruisco il grafo
		
		this.grafo = new SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, punteggi.keySet());
		
		for(Season s1: punteggi.keySet()) {
			for (Season s2 : punteggi.keySet()) {
				if(!s1.equals(s2)) {
					int punti1 = punteggi.get(s1);
					int punti2 = punteggi.get(s2);
					if(punti1>punti2) {
						Graphs.addEdge(this.grafo, s1, s2, punti1-punti2);
					} else {
						Graphs.addEdge(this.grafo, s2, s1, punti2-punti1);
					}
				}
			}
		}
		
		//Trovo l'annata migliore
		Season migliore = null;
		int max =0;
		for(Season s : grafo.vertexSet()) {
			int valore = pesoStagione(s);
			if(valore >max) {
				max= valore;
				migliore = s;
			}
		}
		mass = max;
		return migliore;
	}


	private int pesoStagione(Season s) {
		int somma = 0;
		
		for(DefaultWeightedEdge e : grafo.incomingEdgesOf(s)) {
			somma+= (int)grafo.getEdgeWeight(e);
		}
	
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(s)) {
			somma = somma - (int)grafo.getEdgeWeight(e);
		}
		
		return somma;
	}
	
	public int getMass() {
		return mass;
	}
	
	
}
