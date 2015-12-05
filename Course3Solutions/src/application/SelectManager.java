package application;
import geography.GeographicPoint;

public class SelectManager {
	private GeographicPoint start;
	private GeographicPoint destination;
    private GeographicPoint point;



    public SelectManager() {
        point = null;
    	start = null;
    	destination = null;
    }

    public void give(GeographicPoint point) {

    }
	public GeographicPoint getStart(){return start;}
	public GeographicPoint getDestination(){return destination;}
	public void getStart(GeographicPoint point) {
		start = point;
	}
	public void setDestination(GeographicPoint point) {

		destination = point;
	}

	public void setStart(GeographicPoint point) {
		start = point;
	}

    public void set(GeographicPoint point) {
    	this.point = point;
    }

    public GeographicPoint getPoint() { return point; }




}