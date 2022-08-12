package aaageo;

import math.AMath;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import wblut.geom.*;
import wblut.math.WB_Epsilon;

import java.util.ArrayList;
import java.util.List;

/**
 * geometry tools
 *
 * @author Zhang Baizhou, Li Hongjian
 * @project aaa-jgeo-utils
 * @date 2021/8/17
 * @time 10:45
 */
public final class AGeoOP {
    public static final double epsilon = 0.00000001;

    /*-------- vector & angle --------*/

    /**
     * get the normalized vector
     *
     * @param v original vector
     * @return wblut.geom.WB_Vector
     */
    public static WB_Vector getNormalize(final WB_Vector v) {
        double d = v.getLength();
        if (WB_Epsilon.isZero(d)) {
            return new WB_Vector(0.0D, 0.0D, 0.0D);
        } else {
            return new WB_Vector(v.xd() / d, v.yd() / d, v.zd() / d);
        }
    }

    /**
     * get angle bisector by the order of v0 -> v1 (reflex angle includes)
     *
     * @param v1 first vector
     * @param v2 second vector
     * @return wblut.geom.WB_Vector
     */
    public static WB_Vector getAngleBisectorOrdered(final WB_Vector v1, final WB_Vector v2) {
        if (WB_CoordOp2D.cross2D(v1, v2) > 0) {
            return getNormalize(getNormalize(v1).add(getNormalize(v2))).scale(-1);
        } else if (WB_CoordOp2D.cross2D(v1, v2) < 0) {
            return getNormalize(getNormalize(v1).add(getNormalize(v2)));
        } else {
            if (v1.dot2D(v2) > 0) {
                return getNormalize(v1);
            } else {
                WB_Vector perpendicular = new WB_Vector(v1.yd(), -v1.xd());

                if (!(WB_CoordOp2D.cross2D(v1, perpendicular) > 0)) {
                    perpendicular.scaleSelf(-1);
                }
                return getNormalize(perpendicular);
            }
        }
    }

    /**
     * sort a list of vectors by polar coordinates, return indices in original list
     *
     * @param vectors vector list to be sorted
     * @return int[] - indices of input list
     */
    public static int[] sortPolarAngleIndices(final List<WB_Vector> vectors) {
        assert vectors.size() > 0 : "input list must at least include 1 vector";
        double[] atanValue = new double[vectors.size()];
        for (int i = 0; i < vectors.size(); i++) {
            double curr_value = Math.atan2(vectors.get(i).yd(), vectors.get(i).xd());
            atanValue[i] = curr_value;
        }
        return AMath.getArraySortedIndices(atanValue);
    }

    /*-------- geometry boundary split methods --------*/

    /**
     * split polyline by given step
     *
     * @param pl   input polyline
     * @param step step to split
     * @return java.util.List<wblut.geom.WB_Point>
     */
    public static List<WB_Point> splitPolyLineByStep(final WB_PolyLine pl, final double step) {
        WB_Point start = pl.getPoint(0);
        WB_Point end = pl.getPoint(pl.getNumberOfPoints() - 1);

        WB_Point p1 = start;
        double curr_span = step;
        double curr_dist;

        List<WB_Point> result = new ArrayList<>();
        result.add(p1.copy());
        for (int i = 1; i < pl.getNumberOfPoints(); i++) {
            WB_Point p2 = pl.getPoint(i);
            curr_dist = p1.getDistance2D(p2);
            while (curr_dist >= curr_span) {
                WB_Vector v = p2.sub(p1);
                v.normalizeSelf();
                v.scaleSelf(curr_span);

                WB_Point p = p1.add(v);

                result.add(p);
                p1 = p;
                curr_span = step;
                curr_dist = p1.getDistance2D(p2);
            }
            p1 = p2;
            curr_span = curr_span - curr_dist;
        }

        // close: pt num = seg num
        // open: pt num = seg num + 1
        if (pl instanceof WB_Ring) {
            if (start.getDistance2D(result.get(result.size() - 1)) < epsilon) {
                result.remove(result.size() - 1);
            }
        } else {
            if (end.getDistance2D(result.get(result.size() - 1)) > epsilon) {
                result.add(end.copy());
            }
        }

        return result;
    }

    /*-------- polygon methods --------*/

    /**
     * find all concave points indices in a polygon (WB)
     *
     * @param polygon input WB_Polygon
     * @return java.util.List<java.lang.Integer> - indices of input polygon
     */
    public static List<Integer> getConcavePoints(final WB_Polygon polygon) {
        List<Integer> ccpID = new ArrayList<>();
        if (polygon.getNormal().zd() > 0) {
            for (int i = 1; i < polygon.getNumberOfPoints(); i++) {
                WB_Vector prev = new WB_Vector(polygon.getPoint(i), polygon.getPoint(i - 1));
                WB_Vector next = new WB_Vector(polygon.getPoint(i), polygon.getPoint((i + 1) % (polygon.getNumberOfPoints() - 1)));
                double crossValue = WB_CoordOp2D.cross2D(next, prev);
                if (crossValue < 0) {
                    if (i == polygon.getNumberOfPoints() - 1) {
                        ccpID.add(0);
                    } else {
                        ccpID.add(i);
                    }
                }
            }
        } else {
            for (int i = 1; i < polygon.getNumberOfPoints(); i++) {
                WB_Vector prev = new WB_Vector(polygon.getPoint(i), polygon.getPoint(i - 1));
                WB_Vector next = new WB_Vector(polygon.getPoint(i), polygon.getPoint((i + 1) % (polygon.getNumberOfPoints() - 1)));
                double crossValue = WB_CoordOp2D.cross2D(prev, next);
                if (crossValue < 0) {
                    if (i == polygon.getNumberOfPoints() - 1) {
                        ccpID.add(0);
                    } else {
                        ccpID.add(i);
                    }
                }
            }
        }
        return ccpID;
    }

    /**
     * find all concave points indices in a polygon(jts)
     *
     * @param polygon input jts Polygon
     * @return java.util.List<java.lang.Integer> - indices of input polygon
     */
    public static List<Integer> getConcavePoints(final Polygon polygon) {
        WB_Polygon wbPolygon = ATransform.PolygonToWB_Polygon(polygon);
        return getConcavePoints(wbPolygon);
    }

    /**
     * get the direction of a OBB
     *
     * @param polygon input polygon
     * @return geometry.ZPoint
     */
    public static WB_Vector miniRectDir(final WB_Polygon polygon) {
        Polygon rect = (Polygon) MinimumDiameter.getMinimumRectangle(ATransform.WB_PolygonToPolygon(polygon));
        return miniRectDir(rect);
    }

    /**
     * get the direction of a OBB
     *
     * @param polygon input polygon
     * @return geometry.ZPoint
     */
    public static WB_Vector miniRectDir(final Polygon polygon) {
        Polygon rect = (Polygon) MinimumDiameter.getMinimumRectangle(polygon);
        WB_Point c0 = ATransform.CoordinateToWB_Point(rect.getCoordinates()[0]);
        WB_Point c1 = ATransform.CoordinateToWB_Point(rect.getCoordinates()[1]);
        WB_Point c2 = ATransform.CoordinateToWB_Point(rect.getCoordinates()[2]);


        WB_Vector dir1 = c0.sub(c1);
        dir1.normalizeSelf();
        WB_Vector dir2 = c2.sub(c1);
        dir2.normalizeSelf();

        return c0.getDistance2D(c1) >= c1.getDistance2D(c2) ? dir2 : dir1;
    }

    /**
     * reverse the order of a polygon (holes supported)
     *
     * @param original input polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon reversePolygon(final WB_Polygon original) {
        if (original.getNumberOfHoles() == 0) {
            WB_Point[] newPoints = new WB_Point[original.getNumberOfPoints()];
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = original.getPoint(newPoints.length - 1 - i);
            }
            return new WB_Polygon(newPoints);
        } else {
            WB_Point[] newExteriorPoints = new WB_Point[original.getNumberOfShellPoints()];
            for (int i = 0; i < original.getNumberOfShellPoints(); i++) {
                newExteriorPoints[i] = original.getPoint(original.getNumberOfShellPoints() - 1 - i);
            }

            final int[] npc = original.getNumberOfPointsPerContour();
            int index = npc[0];
            WB_Point[][] newInteriorPoints = new WB_Point[original.getNumberOfHoles()][];

            for (int i = 0; i < original.getNumberOfHoles(); i++) {
                WB_Point[] newHole = new WB_Point[npc[i + 1]];
                for (int j = 0; j < newHole.length; j++) {
                    newHole[j] = new WB_Point(original.getPoint(newHole.length - 1 - j + index));
                }
                newInteriorPoints[i] = newHole;
                index = index + npc[i + 1];
            }

            return new WB_Polygon(newExteriorPoints, newInteriorPoints);
        }
    }

    /**
     * check if two polygon have same direction
     *
     * @param p1 polygon1
     * @param p2 polygon2
     * @return boolean
     */
    public static boolean isNormalEquals(final WB_Polygon p1, final WB_Polygon p2) {
        return p1.getNormal().equals(p2.getNormal());
    }

    /**
     * make a polygon face up (normal vector is in the z direction) (holes supported)
     *
     * @param polygon input polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon polygonFaceUp(final WB_Polygon polygon) {
        if (polygon.getNormal().zd() < 0) {
            return reversePolygon(polygon);
        } else {
            return polygon;
        }
    }

    /**
     * make a polygon face up (normal vector is in the reverse z direction) (holes supported)
     *
     * @param polygon input polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon polygonFaceDown(final WB_Polygon polygon) {
        if (polygon.getNormal().zd() > 0) {
            return reversePolygon(polygon);
        } else {
            return polygon;
        }
    }

    /**
     * find the longest segment and the shortest segment in a polygon
     *
     * @param polygon input polygon
     * @return int[]  0 -> longest 1 -> shortest
     */
    public static int[] getLongestAndShortestSegment(final WB_Polygon polygon) {
        WB_Polygon valid = ATransform.validateWB_Polygon(polygon);
        int maxIndex = 0;
        int minIndex = 0;
        double maxLength = valid.getPoint(0).getSqDistance(valid.getPoint(1));
        double minLength = maxLength;
        for (int i = 1; i < valid.getNumberOfShellPoints() - 1; i++) {
            double currLength = valid.getPoint(i).getSqDistance(valid.getPoint(i + 1));
            if (currLength > maxLength) {
                maxLength = currLength;
                maxIndex = i;
            }
            if (currLength < minLength) {
                minLength = currLength;
                minIndex = i;
            }
        }
        return new int[]{maxIndex, minIndex};
    }

    /**
     * offset one segment of a polygon (input valid, face up polygon)
     *
     * @param poly  input polygon
     * @param index segment index to be offset
     * @param dist  offset distance
     * @return geometry.ZLine
     */
    public static WB_Segment offsetWB_PolygonSegment(final WB_Polygon poly, final int index, final double dist) {
        // make sure polygon's start and end point are coincident
        WB_Polygon polygon = ATransform.validateWB_Polygon(poly);
        assert index <= polygon.getNumberSegments() && index >= 0 : "index out of polygon point number";

        int next = (index + 1) % polygon.getNumberSegments();
        int prev = (index + polygon.getNumberSegments() - 1) % polygon.getNumberSegments();

        WB_Vector v1 = new WB_Vector(polygon.getSegment(prev).getEndpoint(), polygon.getSegment(prev).getOrigin());
        WB_Vector v2 = new WB_Vector(polygon.getSegment(index).getOrigin(), polygon.getSegment(index).getEndpoint());
        WB_Vector bisector1 = getAngleBisectorOrdered(v1, v2);
        WB_Point point1 = new WB_Point(polygon.getSegment(index).getOrigin()).add(bisector1.scale(dist / Math.abs(WB_CoordOp2D.cross2D(getNormalize(v1), bisector1))));

        WB_Vector v3 = new WB_Vector(polygon.getSegment(index).getOrigin(), polygon.getSegment(index).getEndpoint());
        WB_Vector v4 = new WB_Vector(polygon.getSegment(next).getEndpoint(), polygon.getSegment(next).getOrigin());
        WB_Vector bisector2 = getAngleBisectorOrdered(v3, v4);
        WB_Point point2 = new WB_Point(polygon.getSegment(index).getEndpoint()).add(bisector2.scale(dist / Math.abs(WB_CoordOp2D.cross2D(getNormalize(v3), bisector2))));

        return new WB_Segment(point1, point2);
    }

    /**
     * offset several segments of a polygon (input valid, face up polygon)
     * return polyline or polygon
     *
     * @param poly  input polygon
     * @param index segment indices to be offset
     * @param dist  offset distance
     * @return wblut.geom.WB_PolyLine
     */
    public static WB_PolyLine offsetWB_PolygonSegments(final WB_Polygon poly, final int[] index, final double dist) {
        // make sure polygon's start and end point are coincident
        WB_Polygon polygon = ATransform.validateWB_Polygon(poly);

        WB_Point[] linePoints = new WB_Point[index.length + 1];
        for (int i = 0; i < index.length; i++) {
            int prev = (index[i] + polygon.getNumberSegments() - 1) % polygon.getNumberSegments();

            WB_Vector v1 = new WB_Vector(polygon.getSegment(prev).getEndpoint(), polygon.getSegment(prev).getOrigin());
            WB_Vector v2 = new WB_Vector(polygon.getSegment(index[i]).getOrigin(), polygon.getSegment(index[i]).getEndpoint());
            WB_Vector bisector1 = getAngleBisectorOrdered(v1, v2);
            WB_Point point1 = new WB_Point(polygon.getSegment(index[i]).getOrigin()).add(bisector1.scale(dist / Math.abs(WB_CoordOp2D.cross2D(getNormalize(v1), bisector1))));

            linePoints[i] = point1;
        }

        int next = (index[index.length - 1] + 1) % polygon.getNumberSegments();
        WB_Vector v3 = new WB_Vector(polygon.getSegment(index[index.length - 1]).getOrigin(), polygon.getSegment(index[index.length - 1]).getEndpoint());
        WB_Vector v4 = new WB_Vector(polygon.getSegment(next).getEndpoint(), polygon.getSegment(next).getOrigin());
        WB_Vector bisector2 = getAngleBisectorOrdered(v3, v4);
        WB_Point point2 = new WB_Point(polygon.getSegment(index[index.length - 1]).getEndpoint()).add(bisector2.scale(dist / Math.abs(WB_CoordOp2D.cross2D(getNormalize(v3), bisector2))));
        linePoints[linePoints.length - 1] = point2;

        if (linePoints[0].equals(linePoints[linePoints.length - 1])) {
            return new WB_Polygon(linePoints);
        } else {
            return new WB_PolyLine(linePoints);
        }
    }

    /**
     * get shell points of WB_Polygon in CCW
     *
     * @param poly input polygon
     * @return wblut.geom.WB_Coord[]
     */
    public static WB_Point[] getShellPts(WB_Polygon poly) {
        if (poly.getNumberOfContours() == 1)
            return (WB_Point[]) poly.getPoints().toArray();
        int numOut = poly.getNumberOfShellPoints();
        WB_Point[] out = new WB_Point[numOut];
        for (int i = 0; i < numOut; i++) {
            out[i] = poly.getPoint(i);
        }
        return out;
    }

    /**
     * get inner points of WB_Polygon in CW
     *
     * @param poly input polygon
     * @return wblut.geom.WB_Point[][]
     */
    public static WB_Point[][] getInnerPts(WB_Polygon poly) {
        if (poly.getNumberOfContours() == 1)
            return null;
        WB_Point[][] in = new WB_Point[poly.getNumberOfHoles()][];
        int[] num = poly.getNumberOfPointsPerContour();
        int count = num[0];
        for (int i = 0; i < in.length; i++) {
            WB_Point[] pts = new WB_Point[num[i + 1]];
            for (int j = 0; j < pts.length; j++) {
                pts[j] = poly.getPoint(count + j);
            }
            in[i] = pts;
            count += pts.length;
        }
        return in;
    }

    /**
     * get a copy of a polygon, also can be used with polygon with hole
     *
     * @param poly original polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon duplicatePolygon(WB_Polygon poly) {
        WB_Coord[] out = getShellPts(poly);
        WB_Coord[][] in = getInnerPts(poly);
        return new WB_Polygon(out, in);
    }

    /*-------- other methods --------*/

    /**
     * calculate area from a series of points, no need to construct a polygon
     *
     * @param pts a series of points
     * @return double
     */
    public static double areaFromPoints(final WB_Point[] pts) {
        double area = 0;
        for (int i = 0; i < pts.length; i++) {
            WB_Point p = pts[i];
            WB_Point q = pts[(i + 1) % pts.length];
            area += (q.xd() * p.yd() - p.xd() * q.yd());
        }
        return 0.5 * Math.abs(area);
    }

    /**
     * get the center of a series of points
     *
     * @param pts points
     * @return wblut.geom.WB_Point
     */
    public static WB_Point centerFromPoints(WB_Point[] pts) {
        int length = pts.length;
        double x = 0, y = 0, z = 0;
        for (WB_Point pt : pts) {
            x += pt.xd();
            y += pt.yd();
            z += pt.zd();
        }
        return new WB_Point(x / length, y / length, z / length);
    }

    /**
     * set jts precision model (FLOAT, FLOAT_SINGLE, FIXED)
     *
     * @param geometry geometry to be applied
     * @param pm       precision model
     */
    public static void applyJtsPrecisionModel(final Geometry geometry, final PrecisionModel pm) {
        Coordinate[] coordinates = geometry.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            pm.makePrecise(coordinate);
        }
    }

    /**
     * add the first Coordinate to the last position of array (close)
     *
     * @param coords original Coordinates
     * @return org.locationtech.jts.geom.Coordinate[]
     */
    public static Coordinate[] addFirst2Last(Coordinate... coords) {
        Coordinate[] cs = new Coordinate[coords.length + 1];
        int i = 0;
        for (; i < coords.length; i++) {
            cs[i] = coords[i];
        }
        cs[i] = coords[0];
        return cs;
    }

    /**
     * when use Polygon.getCoordinates, use this method to remove the overlap Coordinate of start and end point
     *
     * @param coords original Coordinates
     * @return org.locationtech.jts.geom.Coordinate[]
     */
    public static Coordinate[] subLast(Coordinate... coords) {
        Coordinate[] cs = new Coordinate[coords.length - 1];
        int i = 0;
        for (; i < coords.length - 1; i++) {
            cs[i] = coords[i];
            cs[i].z = 0;
        }
        return cs;
    }

    /**
     * make z ordinate to 0 if NaN
     *
     * @param coords original Coordinates
     * @return org.locationtech.jts.geom.Coordinate[]
     */
    public static Coordinate[] filterNaN(Coordinate... coords) {
        Coordinate[] result = new Coordinate[coords.length];
        for (int i = 0; i < coords.length; i++) {
            double z = coords[i].getZ();
            result[i] = new Coordinate(coords[i].getX(), coords[i].getY(), Double.isNaN(z) ? 0 : z);
        }
        return result;
    }

    /**
     * transform from polygon plane to XY plane with origin point at (0,0,0)
     *
     * @param poly input polygon
     * @return wblut.geom.WB_Transform3D
     */
    public static WB_Transform3D toWCSOri(WB_Polygon poly) {
        return new WB_Transform3D(poly.getCenter(), poly.getNormal(), WB_Vector.ORIGIN(), WB_Vector.Z());
    }
}
