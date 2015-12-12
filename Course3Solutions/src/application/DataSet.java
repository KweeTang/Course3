package application;

import java.util.HashMap;
import java.util.HashSet;
import util.GraphLoader;

public class DataSet {
	String filePath;
	roadgraph.MapGraph graph;
    private HashMap<geography.GeographicPoint,HashSet<geography.RoadSegment>>  roads;
	boolean currentlyDisplayed;

	public DataSet (String path) {
		this.filePath = path;
        graph = null;
        roads = null;
        currentlyDisplayed = false;
	}

    public void setGraph(roadgraph.MapGraph graph) {
    	this.graph = graph;
    }

    public void setRoads(HashMap<geography.GeographicPoint,HashSet<geography.RoadSegment>>  roads) { this.roads = roads; }
    public roadgraph.MapGraph getGraph(){ return graph; }
    public HashMap<geography.GeographicPoint,HashSet<geography.RoadSegment>>  getRoads() { return this.roads; }

    public void initializeGraph() {
        graph = new roadgraph.MapGraph();
        roads = new HashMap<geography.GeographicPoint, HashSet<geography.RoadSegment>>();
    	GraphLoader.loadRoadMap(filePath, graph, roads);
    }

	public String getFilePath() {
		return this.filePath;
	}

    public boolean isDisplayed() {
    	return this.currentlyDisplayed;
    }

}