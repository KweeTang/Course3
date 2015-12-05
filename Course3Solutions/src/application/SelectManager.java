package application;
import geography.GeographicPoint;

public class SelectManager {
	private GeographicPoint start;
	private GeographicPoint destination;


    public SelectManager() {
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


}