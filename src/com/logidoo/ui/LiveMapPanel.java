package com.logidoo.ui;

import com.logidoo.models.Vehicle;
import com.logidoo.models.Vehicle.VehicleStatus;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Real-time vehicle tracking map panel using OpenStreetMap.
 */
public class LiveMapPanel extends JPanel {
    private JXMapViewer mapViewer;
    private Set<Waypoint> waypoints;
    private List<Vehicle> vehicles;

    public LiveMapPanel() {
        setLayout(new BorderLayout());
        initMap();
        
        com.logidoo.dao.VehicleDAO dao = new com.logidoo.dao.VehicleDAO();
        vehicles = dao.getAllVehicles();
        updateMapWaypoints();
    }

    private void initMap() {
        mapViewer = new JXMapViewer();

        TileFactoryInfo info = new TileFactoryInfo(1, 19, 19,
                256, true, true,
                "https://tile.openstreetmap.org",
                "x", "y", "z") {

            @Override
            public String getTileUrl(int x, int y, int zoom) {
                zoom = getTotalMapZoom() - zoom;
                return this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
            }
        };
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setUserAgent("FleetFlux/1.0");
        mapViewer.setTileFactory(tileFactory);

        GeoPosition dakar = new GeoPosition(14.7167, -17.4677);
        mapViewer.setZoom(10);
        mapViewer.setAddressLocation(dakar);

        org.jxmapviewer.input.PanMouseInputListener mm = new org.jxmapviewer.input.PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mm);
        mapViewer.addMouseMotionListener(mm);
        mapViewer.addMouseWheelListener(new org.jxmapviewer.input.ZoomMouseWheelListenerCenter(mapViewer));

        add(mapViewer, BorderLayout.CENTER);
        
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> updateVehiclePositions());
        timer.start();
    }
    
    public void updateVehicles(List<Vehicle> newVehicles) {
        this.vehicles = newVehicles;
        updateMapWaypoints();
    }

    private void updateVehiclePositions() {
        updateMapWaypoints();
    }

    private void updateMapWaypoints() {
        waypoints = new HashSet<>();
        
        if (vehicles != null && !vehicles.isEmpty()) {
            for (Vehicle v : vehicles) {
                double lat = (v.getLatitude() == 0.0) ? 14.7167 : v.getLatitude();
                double lon = (v.getLongitude() == 0.0) ? -17.4677 : v.getLongitude();
                waypoints.add(new DefaultWaypoint(lat, lon));
            }
        } else {
             waypoints.add(new DefaultWaypoint(14.7167, -17.4677));
        }

        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
        mapViewer.repaint();
    }
}
