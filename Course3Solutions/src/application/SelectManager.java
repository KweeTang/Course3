package application;
import geography.GeographicPoint;

public class SelectManager {
	private GeographicPoint start;
	private GeographicPoint destination;
    private GeographicPoint point;
    private CLabel<GeographicPoint> pointLabel;
    private CLabel<GeographicPoint> startLabel;
    private CLabel<GeographicPoint> destinationLabel;



    public SelectManager() {
        point = null;
    	start = null;
    	destination = null;
    }



    public void setPoint(GeographicPoint point) {
        System.out.println("inSetPoint.. passed : " + point);
    	this.point = point;
    	pointLabel.setItem(point);
    }

    public void setPointLabel(CLabel<GeographicPoint> label) { this.pointLabel = label; }
    public void setStartLabel(CLabel<GeographicPoint> label) { this.startLabel = label; }
    public void setDestinationLabel(CLabel<GeographicPoint> label) { this.destinationLabel = label; }

    public GeographicPoint getPoint() { return point; }


	public GeographicPoint getStart(){return start;}
	public GeographicPoint getDestination(){return destination;}
	public void setStart(GeographicPoint point) {
		this.start = point;
		startLabel.setItem(point);
	}
	public void setDestination(GeographicPoint point) {
		destination = point;
		destinationLabel.setItem(point);
	}


}