package advancedTools;

import basicTools.AAAGeoFactory;
import basicTools.AAATransform;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * simple catmull-rom curve algorithms
 * inspired by https://github.com/jurajstrecha/CatmullRomSpline
 *
 * @author zhangbz ZHANG Baizhou
 * @project aaa-jgeo-utils
 * @date 2021/8/17
 * @time 16:56
 */
public class AAACatmullRom {
    private int dividePerSpan = 10;
    private boolean closed = false;

    private final List<WB_Point> curveControlPts;
    private final List<WB_Point> curveDividePts;

    /* ------------- constructor ------------- */

    public AAACatmullRom(List<WB_Point> controlPoints, int samplesPerSpan, boolean closed) {
        setDividePerSpan(samplesPerSpan);
        setClosed(closed);

        this.curveControlPts = controlPoints;
        this.curveDividePts = calculateCatmull(curveControlPts);
    }

    public AAACatmullRom(WB_Coord[] controlPoints, int samplesPerSpan, boolean closed) {
        setDividePerSpan(samplesPerSpan);
        setClosed(closed);

        this.curveControlPts = new ArrayList<>();
        for (WB_Coord c : controlPoints) {
            curveControlPts.add(new WB_Point(c));
        }
        this.curveDividePts = calculateCatmull(curveControlPts);
    }

    /* ------------- member function ------------- */

    /**
     * Enumerates interpolation spline points using a set of control points,
     * samples per span count and Catmull-Rom polynom equations.
     *
     * @param controlPoints a set of control points that will be interpolated by the curve
     * @return Resulting set of curve points
     */
    public List<WB_Point> calculateCatmull(List<WB_Point> controlPoints) {
        if (controlPoints != null && controlPoints.size() > 1) {
            if (!closed) {
                List<WB_Point> dividePoints = new ArrayList<>();

                double deltaT = 1.0 / dividePerSpan;
                double t;

                // copy start and end point at the beginning and the end of the set
                // to include the first and last control point in spline
                List<WB_Point> extended = extendCtrlPointsSet(controlPoints);

                for (int i = 1; i < extended.size() - 2; i++) {
                    for (int j = 0; j < dividePerSpan; j++) {
                        t = deltaT * j;
                        dividePoints.add(
                                CREquation(
                                        extended.get(i - 1),
                                        extended.get(i),
                                        extended.get(i + 1),
                                        extended.get(i + 2),
                                        t
                                )
                        );
                    }
                }
                dividePoints.add(extended.get(extended.size() - 1));

                return dividePoints;
            } else {
                if (controlPoints.size() > 2) {
                    List<WB_Point> dividePoints = new ArrayList<>();

                    double deltaT = 1.0 / dividePerSpan;
                    double t;

                    for (int i = 0; i < controlPoints.size(); i++) {
                        for (int j = 0; j < dividePerSpan; j++) {
                            t = deltaT * j;
                            dividePoints.add(
                                    CREquation(
                                            controlPoints.get((i + controlPoints.size() - 1) % controlPoints.size()),
                                            controlPoints.get(i),
                                            controlPoints.get((i + 1) % controlPoints.size()),
                                            controlPoints.get((i + 2) % controlPoints.size()),
                                            t
                                    )
                            );
                        }
                    }
                    dividePoints.add(controlPoints.get(0));

                    return dividePoints;
                }
            }
        }
        return null;
    }

    /**
     * Enumerates a new point for the interpolation spline according to the
     * t-parameter using polynomial formulas.
     *
     * @param p1 First control point - affects a shape of the spline
     * @param p2 Second control point - start of the interval
     * @param p3 Third control point - end of the interval
     * @param p4 Fourth control point - affects a shape of the spline
     * @param t  Relative t-parameter inside the interval
     * @return Point from the resulting spline
     */
    private WB_Point CREquation(WB_Point p1, WB_Point p2, WB_Point p3, WB_Point p4, double t) {
        double t2 = t * t;
        double t3 = t2 * t;

        double b1 = 0.5f * (-t3 + 2 * t2 - t);
        double b2 = 0.5f * (3 * t3 - 5 * t2 + 2);
        double b3 = 0.5f * (-3 * t3 + 4 * t2 + t);
        double b4 = 0.5f * (t3 - t2);

        double x = b1 * p1.xd() + b2 * p2.xd() + b3 * p3.xd() + b4 * p4.xd();
        double y = b1 * p1.yd() + b2 * p2.yd() + b3 * p3.yd() + b4 * p4.yd();

        WB_Point result = new WB_Point();
        result.set(x, y);

        return result;
    }

    /**
     * Copies the first/last point at the start/end of the set.
     *
     * @param points Set of points to be extended by the first and last item
     */
    private List<WB_Point> extendCtrlPointsSet(List<WB_Point> points) {
        List<WB_Point> result = new ArrayList<>();
        if (points != null && !points.isEmpty() && points.size() > 1) {
            result.addAll(points);
            result.add(points.get(points.size() - 1));
            result.add(0, points.get(0));
        }
        return result;
    }

    /* ------------- setter & getter ------------- */

    public void setDividePerSpan(int dividePerSpan) {
        this.dividePerSpan = dividePerSpan;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public List<WB_Point> getCurveControlPts() {
        return curveControlPts;
    }

    public List<WB_Point> getCurveDividePts() {
        return curveDividePts;
    }

    public double getCurveLength(List<WB_Point> spline) {
        double length = 0.0;
        WB_Point recentPoint = spline.get(0);
        WB_Point currentPoint;
        double xLen, xLen2, yLen, yLen2;
        for (int i = 1; i < spline.size(); i++) {
            currentPoint = spline.get(i);
            xLen = recentPoint.xd() - currentPoint.xd();
            xLen2 = xLen * xLen;
            yLen = recentPoint.yd() - currentPoint.yd();
            yLen2 = yLen * yLen;
            length += Math.sqrt(xLen2 + yLen2);
            recentPoint = currentPoint;
        }
        return length;
    }

    public WB_PolyLine getAsWB_PolyLine() {
        return new WB_PolyLine(curveDividePts);
    }

    public LineString getAsLineString() {
        Coordinate[] coordinates = new Coordinate[curveDividePts.size()];
        int length = coordinates.length;
        for (int i = 0; i < length; i++) {
            coordinates[i] = AAATransform.WB_CoordToCoordinate(curveDividePts.get(i));
        }
        return AAAGeoFactory.jtsgf.createLineString(coordinates);
    }

    public WB_Polygon getAsWB_Polygon() {
        return new WB_Polygon(curveDividePts);
    }

    public Polygon getAsPolygon() {
        Coordinate[] coordinates = new Coordinate[curveDividePts.size()];
        int length = coordinates.length;
        for (int i = 0; i < length; i++) {
            coordinates[i] = AAATransform.WB_CoordToCoordinate(curveDividePts.get(i));
        }
        return AAAGeoFactory.jtsgf.createPolygon(coordinates);
    }
}
