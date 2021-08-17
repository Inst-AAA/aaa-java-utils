package basicTools;

import org.locationtech.jts.geom.*;
import processing.core.PApplet;
import wblut.processing.WB_Render;

/**
 * HE_Mesh and jts geometry renderer
 *
 * @author Li Biao, Zhang Baizhou
 * @project aaa-jgeo-utils
 * @date 2021/8/14
 * @time 22:47
 */
public final class AAARender extends WB_Render {
    private final PApplet app;

    public AAARender(PApplet app) {
        super(app);
        this.app = app;
    }

    /**
     * draw jts Geometry
     *
     * @param geo input geometry
     */
    public void drawJtsGeometry(Geometry geo) {
        String type = geo.getGeometryType();
        switch (type) {
            case "Point":
                drawPoint(geo);
                break;
            case "LineString":
                drawLineString(geo);
                break;
            case "LinearRing":
                drawLinearRing(geo);
                break;
            case "Polygon":
                drawPolygon(geo);
                break;
            case "MultiPoint":
            case "MultiLineString":
            case "MultiPolygon":
                for (int i = 0; i < geo.getNumGeometries(); i++) {
                    drawJtsGeometry(geo.getGeometryN(i));
                }
                break;
            default:
                PApplet.println("not a basic geo type");
                break;
        }
    }

    /**
     * draw Point as a circle
     *
     * @param geo input geometry
     */
    private void drawPoint(Geometry geo) {
        Point point = (Point) geo;
        app.ellipse((float) point.getX(), (float) point.getY(), 10, 10);
    }

    /**
     * draw LineString as multiple lines
     *
     * @param geo input geometry
     */
    private void drawLineString(Geometry geo) {
        LineString ls = (LineString) geo;
        for (int i = 0; i < ls.getCoordinates().length - 1; i++) {
            app.line((float) ls.getCoordinateN(i).x, (float) ls.getCoordinateN(i).y, (float) ls.getCoordinateN(i + 1).x, (float) ls.getCoordinateN(i + 1).y);
        }
    }

    /**
     * draw LinearRing as a closed shape
     *
     * @param geo input geometry
     */
    private void drawLinearRing(Geometry geo) {
        LinearRing lr = (LinearRing) geo;
        Coordinate[] vs = lr.getCoordinates();
        app.beginShape();
        for (Coordinate v : vs) {
            app.vertex((float) v.x, (float) v.y);
        }
        app.endShape(app.CLOSE);
    }

    /**
     * draw Polygon as a closed shape
     *
     * @param geo input geometry
     */
    private void drawPolygon(Geometry geo) {
        Polygon poly = (Polygon) geo;
        // outer boundary
        app.beginShape();
        LineString shell = poly.getExteriorRing();
        Coordinate[] coord_shell = shell.getCoordinates();
        for (Coordinate c_s : coord_shell) {
            app.vertex((float) c_s.x, (float) c_s.y);
        }
        // inner holes
        if (poly.getNumInteriorRing() > 0) {
            int interNum = poly.getNumInteriorRing();
            for (int i = 0; i < interNum; i++) {
                LineString in_poly = poly.getInteriorRingN(i);
                Coordinate[] in_coord = in_poly.getCoordinates();
                app.beginContour();
                for (int j = 0; j < in_coord.length; j++) {
                    app.vertex((float) in_coord[j].x, (float) in_coord[j].y);
                }
                app.endContour();
            }
        }
        app.endShape();
    }
}
