
import java.util.Comparator;

class Point2D {

	public static final Comparator<Point2D> X_ORDER = new XOrder();
	public static final Comparator<Point2D> Y_ORDER = new YOrder();

	private final double x;    // x coordinate
	private final double y;    // y coordinate
	static int numberOfPoint2D;

	public Point2D(double x, double y){
		this.x = x;
		this.y = y;
		numberOfPoint2D += 1 ;  // increment
	}

	public Point2D(int x, int y){
		this.x = x;
		this.y = y;
		numberOfPoint2D += 1 ;  // increment
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}
	
	public double distanceTo(Point2D p){
		return( Math.sqrt( (this.x - p.x) * (this.x - p.x)
				+ (this.y - p.y) * (this.y - p.y) ));
	}

	//  @Team A:   I discussed the following static method in the lectures, but 
	//             note used in this assignment.

	public static double distanceBetween(Point2D p1, Point2D p2){
		return( Math.sqrt( (p1.x - p2.x) * (p1.x - p2.x)
				+ (p1.y - p2.y) * (p1.y - p2.y) ));
	}


	public boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		if (other.getClass() != this.getClass()) return false;
		Point2D that = (Point2D) other;
		return this.x == that.x && this.y == that.y;
	}

	// compare points according to their x-coordinate
	private static class XOrder implements Comparator<Point2D> {
		public int compare(Point2D p, Point2D q) {
			if (p.x < q.x) return -1;
			if (p.x > q.x) return +1;
			return 0;
		}
	}

	// compare points according to their y-coordinate
	private static class YOrder implements Comparator<Point2D> {
		public int compare(Point2D p, Point2D q) {
			if (p.y < q.y) return -1;
			if (p.y > q.y) return +1;
			return 0;
		}
	}

	
	public int compareByY(Point2D other) {
		if (this.y() < other.y()) return -1;
        if (this.y() > other.y()) return 1;
		if (this.x() < other.x()) return -1;
		if (this.x() > other.x()) return 1;
		return 0;
	}
	
	public int compareTo(Object o) {
		try {
			if (o instanceof Point2D) {
				if (this.x < ((Point2D) o).x)
					return -1;
				else if (this.x > ((Point2D) o).x)
					return 1;
				else //  this.x == ((Point 2D) o).x
					if (this.y < ((Point2D) o).y)
						return -1;
					else if (this.y > ((Point2D) o).y)
						return 1;
					else return 0;
			}
		} catch (ClassCastException e) {
			System.err.println("ClassCastException:  compareTo argument cannot be cast to Point2D");
			e.printStackTrace();
		}
		return 0;  // never reaches this statement, but it has to be here
	}

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
	public static void main(String[] args) {
		Point2D p1 = new Point2D( 23, 85 );
		Point2D p2 = new Point2D( 5,  6 ) ;

		System.out.println( distanceBetween(p1,p2));
		System.out.println( p1.distanceTo(p2));
	}


}