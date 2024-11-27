import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { MapContainer, Polygon, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import proj4 from "proj4";
import * as L from "leaflet";

const Shape = () => {

  const { id } = useParams();

    const navigate = useNavigate();

  if (!id || isNaN(id) || id < 1) {
    return <h1>Id xato!</h1>;
  }

  const purpleOptions = { color: "red" };
  

  const [shape, setShape] = useState({});
  const [geoJSON, setGeoJSON] = useState({});
  const [center, setCenter] = useState([41.311081, 69.240562]);

  const mapRef = useRef();

  const getShapeById = async () => {
    const response = await fetch(`http://localhost:8080/api/shapes/${id}`);
    if (response.ok) {
      const data = await response.json();
      setShape(data);
    } else {
      console.error("Failed to fetch shape:", response.statusText);
    }
  };

  const convertCoordinates = (coordinates, isProjected) => {
    if (isProjected) {
      // Assuming EPSG:3857 (Web Mercator)
      return coordinates.map((polygon) =>
        polygon.map((ring) =>
          ring.map(([x, y]) => {
            const [lon, lat] = proj4("EPSG:3857", "EPSG:4326", [x, y]);
            return [lat, lon];
          })
        )
      );
    } else {
      // Coordinates are already in EPSG:4326
      return coordinates.map((polygon) =>
        polygon.map((ring) =>
          ring.map(([lon, lat]) => [lat, lon])
        )
      );
    }
  };

  useEffect(() => {
    if (shape.geoJSON) {
      // Determine if the coordinates are projected (assuming if the first value is large, it's projected)
      const isProjected = Math.abs(shape.geoJSON.coordinates[0][0][0][0]) > 180;
      const convertedCoordinates = convertCoordinates(shape.geoJSON.coordinates, isProjected);
      setGeoJSON({ ...shape.geoJSON, coordinates: convertedCoordinates });
    }
  }, [shape]);

  useEffect(() => {
    if (geoJSON && geoJSON.coordinates && geoJSON.coordinates.length > 0 && mapRef.current) {
      const bounds = geoJSON.coordinates.flat(2).map(coord => new L.LatLng(coord[0], coord[1]));
      mapRef.current.fitBounds(L.latLngBounds(bounds));
    }
  }, [geoJSON]);

  useEffect(() => {
    getShapeById();
  }, []);

  return (
    <>
      <button style={{ position: "absolute", top: 10, left: 50, zIndex: 8888,}} className="btn btn-primary" onClick={() => navigate(-1)}>
        Orqaga qaytish
        </button>
      <MapContainer
        center={center}
        zoom={13}
        scrollWheelZoom={true}
        style={{ width: "100%", height: "100vh" }}
        attributionControl={true}
        animate={true}
        ref={mapRef}
      >
        <TileLayer
          attribution="Google Maps"
          url="http://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}"
          maxZoom={20}
        />

        {geoJSON && geoJSON.coordinates && (
          <Polygon pathOptions={purpleOptions} positions={geoJSON.coordinates} />
        )}
      </MapContainer>
    </>
  );
};

export default Shape;
