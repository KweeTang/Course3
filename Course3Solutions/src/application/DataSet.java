package application;

public class DataSet {
	String filePath;
	roadgraph.MapGraph graph;
	boolean currentlyDisplayed;

	public DataSet (String path) {
        this.filePath = path;
        graph = null;
        currentlyDisplayed = false;
	}

    public void setGraph(roadgraph.MapGraph graph) {
    	this.graph = graph;
    }

	public String getFileName() {
		return this.filePath;
	}

    public boolean isDisplayed() {
    	return this.currentlyDisplayed;
    }

}