import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ClosestAntennaPair {

    private double closestDistance = Double.POSITIVE_INFINITY;
    private long counter = 0;

    public ClosestAntennaPair(Point2D[] aPoints, Point2D[] bPoints) {
        int a = aPoints.length;
        int b = bPoints.length;
        if (a <= 1 || b <= 1) return;

        // sort by x-coordinate (breaking ties by y-coordinate via stability)

        Point2D[] aPointsSortedByX = new Point2D[a];
        Point2D[] bPointsSortedByX = new Point2D[b];
        for (int i = 0; i < a; i++) {
            aPointsSortedByX[i] = aPoints[i];
        }
        for (int i = 0; i < b; i++) {
            bPointsSortedByX[i] = bPoints[i];
        }


        //  first sort by Y.   Then sort by X.  This ultimately sorts by x, with ties broken by the y value.
        //  this assumes that Arrays.sort() is a stable sort, namely that it doesn't swap ties, which indeed it is.

        Arrays.sort(aPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(aPointsSortedByX, Point2D.X_ORDER);

        Arrays.sort(bPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(bPointsSortedByX, Point2D.X_ORDER);

        // if there are two co-incident points, then we are done because minimum distance is 0

        for (int i = 0; i < a-1; i++) {
            for (int j = 0; i < b-1; j++) {
                if (aPointsSortedByX[i].equals(bPointsSortedByX[j])) {
                    closestDistance = 0.0;
                    return;
                }
            }
            
        }

        // set up the array that eventually will hold the points sorted by y-coordinate

        Point2D[] aPointsSortedByY = new Point2D[a];
        Point2D[] bPointsSortedByY = new Point2D[b];
        for (int i = 0; i < a; i++) {
            aPointsSortedByY[i] = aPointsSortedByX[i];
        }

        for (int i = 0; i < b; i++) {
            bPointsSortedByY[i] = bPointsSortedByX[i];
        }
            

        // auxiliary arrays
        Point2D[] auxA = new Point2D[a];
        Point2D[] auxB = new Point2D[b];

        closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, 0, 0, a-1, b-1);


    }

    public double closest(Point2D[] aPointsSortedByX, Point2D[] bPointsSortedByX, Point2D[] aPointsSortedByY, Point2D[] bPointsSortedByY, Point2D[] auxA, Point2D[] auxB, int lowA, int lowB, int highA, int highB) {
        // please do not delete/modify the next line!
        counter++;

        //  low and high refer to indices in the list of points.

        // base case is that low==high (one point) or low > high (zero points); in either case, return POSITIVE_INFINITY

        if (highA <= lowA || highB <= lowB) return Double.POSITIVE_INFINITY;

        //  otherwise at least one point is present in the low,high interval

        int midA = lowA + (highA - lowA) / 2;            // if low==high then mid==low
        Point2D medianA = aPointsSortedByX[midA];


        // compute closest pair with such that both points in the pair are either in left subarray or both points are in right subarray
        // the two closest calls below will return with pointsSortedByY[low .. mid] and pointsSortedByY[mid+1 .. high] sorted by Y
        // then the pointsSortedByY will be merged so the range [low,high] is sorted by Y

        closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, 0, 0, midA, midA);

        double delta1 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, 0, 0, midA, midA);    // if low==mid,  then only one point, so it will return POSITIVE_INFINITY
        double delta2 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, midA, midA, highA, highB); // if low==high, then high < mid+1, so it will return POSITIVE_INFINITY
        double delta = Math.min(delta1, delta2);

        // As mentioned above,  now merge back so that pointsSortedByY[low..high] are sorted by y-coordinate.
        // We know the low < high.  Also see preconditions on merge().

        merge(aPointsSortedByY, auxA, lowA, midA, highA);
        merge(bPointsSortedByY, auxB, lowB, midA, highB);

        // The aux array has the same size as point2D[] but we only use the first high-low+1 slots here.

        // aux[0..m-1] = go through the [low,high] range of pointsSortedByY and make a list of those points
        // whose x value is within delta from the median of x;  keep them sorted by y-coordinate
        // These are the points in a 2-delta strip around the x median for [low,high].
        // Note this wipes out any values in aux that were previously there, which is fine since aux is temporary only.

        int m = 0;
        for (int i = low; i <= high; i++) {
            if (Math.abs(pointsSortedByY[i].x() - median.x()) < delta)
                aux[m++] = pointsSortedByY[i];
        }

        // Compare pairs of points within the strip;  we only need to test points with a y separation less than delta
        // Find the closest pair of points and return the distance between them
        // The two points are called best1 and best2

        for (int i = 0; i < m; i++) {

            // a geometric packing argument shows that this loop iterates at most 7 times
            // (we don't need to test explicitly that the max is 7)

            for (int j = i+1; (j < m) && (aux[j].y() - aux[i].y() < delta); j++) {
                double distance = aux[i].distanceTo(aux[j]);
                if (distance < delta) {
                    delta = distance;
                    if (distance < bestDistance)
                        bestDistance = delta;
                }
            }
        }
        return delta;
    

        // Insert your solution here and modify the return statement.
        return -1;
    }

    public double distance() {
        return closestDistance;
    }

    public long getCounter() {
        return counter;
    }

    // stably merge a[low .. mid] with a[mid+1 ..high] using aux[low .. high]
    // precondition: a[low .. mid] and a[mid+1 .. high] are sorted subarrays, namely sorted by y coordinate
    // this is the same as in ClosestPair
    private static void merge(Point2D[] a, Point2D[] aux, int low, int mid, int high) {
        // copy to aux[]
        // note this wipes out any values that were previously in aux in the [low,high] range we're currently using

        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];   // already finished with the low list ?  then dump the rest of high list
            else if (j > high) a[k] = aux[i++];   // already finished with the high list ?  then dump the rest of low list
            else if (aux[i].compareByY(aux[j]) < 0)
                a[k] = aux[i++]; // aux[i] should be in front of aux[j] ? position and increment the pointer
            else a[k] = aux[j++];
        }
    }
}
