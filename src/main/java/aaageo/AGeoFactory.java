package aaageo;

import org.locationtech.jts.geom.*;
import wblut.geom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * geometry creator
 * including GeometryFactory in jts and WB_GeometryFactory in HE_Mesh
 *
 * @author zhangbz ZHANG Baizhou
 * @project aaa-jgeo-utils
 * @date 2021/8/14
 * @time 22:54
 */
public final class AGeoFactory {
    public static final WB_GeometryFactory wbgf = new WB_GeometryFactory();
    public static final GeometryFactory jtsgf = new GeometryFactory();

    /*-------- geometry factory --------*/

    /**
     * create a LineString from a Coordinate list
     *
     * @param list Coordinate list
     * @return org.locationtech.jts.geom.LineString
     */
    public static LineString createLineStringFromList(final List<Coordinate> list) {
        Coordinate[] array = new Coordinate[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return jtsgf.createLineString(array);
    }

    /**
     * create a LinearRing from a Coordinate list    *
     *
     * @param list Coordinate list
     * @return org.locationtech.jts.geom.LinearRing
     */
    public static LinearRing createLinearRingFromList(final List<Coordinate> list) {
        Coordinate[] array = new Coordinate[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return jtsgf.createLinearRing(array);
    }

    /**
     * create a Polygon from a Coordinate list
     *
     * @param list Coordinate list
     * @return org.locationtech.jts.geom.Polygon
     */
    public static Polygon createPolygonFromList(final List<Coordinate> list) {
        Coordinate[] array = new Coordinate[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return jtsgf.createPolygon(array);
    }

    /**
     * out points in Counter-ClockWise, inner points in ClockWise
     *
     * @param out exterior polygon
     * @param in  interior polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon createWB_PolyWithHole(WB_Polygon out, WB_Polygon... in) {
        WB_Coord[] outPts = AGeoOP.getShellPts(out);
        WB_Coord[][] ptsIn = new WB_Point[in.length][];

        for (int i = 0; i < in.length; i++) {
            List<WB_Coord> pts = in[i].getPoints().toList();
            ptsIn[i] = new WB_Point[pts.size()];
            for (int j = 0; j < pts.size(); j++) {
                ptsIn[i][j] = new WB_Point(pts.get(pts.size() - 1 - j));
            }
        }
        return new WB_Polygon(outPts, ptsIn);
    }

    /**
     * break WB_PolyLine by giving point indices to break
     *
     * @param polyLine   polyLine to be break
     * @param breakPoint indices of break point
     * @return java.util.List<wblut.geom.WB_PolyLine>
     */
    public static List<WB_PolyLine> breakWB_PolyLine(final WB_PolyLine polyLine, final int[] breakPoint) {
        List<WB_PolyLine> result = new ArrayList<>();
        if (polyLine instanceof WB_Ring) {
            for (int i = 0; i < breakPoint.length; i++) {
                assert breakPoint[i] > 0 && breakPoint[i] < polyLine.getNumberOfPoints() - 1 : "index must among the middle points";
                WB_Point[] polyPoints = new WB_Point[
                        (breakPoint[(i + 1) % breakPoint.length] + polyLine.getNumberOfPoints() - 1 - breakPoint[i])
                                % (polyLine.getNumberOfPoints() - 1)
                                + 1];
                for (int j = 0; j < polyPoints.length; j++) {
                    polyPoints[j] = polyLine.getPoint((j + breakPoint[i]) % (polyLine.getNumberOfPoints() - 1));
                }
                result.add(wbgf.createPolyLine(polyPoints));
            }
        } else {
            int count = 0;
            for (int index : breakPoint) {
                assert index > 0 && index < polyLine.getNumberOfPoints() - 1 : "index must among the middle points";
                List<WB_Point> polyPoints = new ArrayList<>();
                for (int i = count; i < index + 1; i++) {
                    polyPoints.add(polyLine.getPoint(i));
                }
                result.add(wbgf.createPolyLine(polyPoints));
                count = index;
            }
            // add last one
            List<WB_Point> polyPoints = new ArrayList<>();
            for (int i = count; i < polyLine.getNumberOfPoints(); i++) {
                polyPoints.add(polyLine.getPoint(i));
            }
            result.add(wbgf.createPolyLine(polyPoints));
        }
        return result;
    }

    /**
     * break LineString by giving point indices to break
     *
     * @param lineString lineString to break
     * @param breakPoint indices of break point
     * @return java.util.List<org.locationtech.jts.geom.LineString>
     */
    public static List<LineString> breakLineString(final LineString lineString, final int[] breakPoint) {
        List<LineString> result = new ArrayList<>();
        int count = 0;
        for (int index : breakPoint) {
            assert index > 0 && index < lineString.getNumPoints() - 1 : "index must among the middle points";
            Coordinate[] coords = new Coordinate[index + 1 - count];
            for (int i = 0; i < coords.length; i++) {
                coords[i] = lineString.getCoordinateN(i + count);
            }
            result.add(jtsgf.createLineString(coords));
            count = index;
        }
        // add last one
        Coordinate[] coords = new Coordinate[lineString.getNumPoints() - count];
        for (int i = 0; i < coords.length; i++) {
            coords[i] = lineString.getCoordinateN(i + count);
        }
        result.add(jtsgf.createLineString(coords));
        return result;
    }

    /**
     * cut out a WB_PolyLine from a WB_Polygon by giving indices
     *
     * @param polygon polygon to be extracted
     * @param index   segment indices to extract
     * @return wblut.geom.WB_PolyLine
     */
    public static WB_PolyLine createPolylineFromPolygon(final WB_Polygon polygon, final int[] index) {
        WB_Point[] points = new WB_Point[index.length + 1];
        for (int i = 0; i < index.length; i++) {
            points[i] = polygon.getPoint(index[i]);
        }
        points[index.length] = polygon.getPoint(
                (index[index.length - 1] + 1) % polygon.getNumberOfShellPoints()
        );
        return new WB_PolyLine(points);
    }

}
