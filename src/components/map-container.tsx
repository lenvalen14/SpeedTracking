"use client";

import React from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css"; // Import Leaflet CSS

const MapComponent = () => {
  const position: [number, number] = [51.505, -0.09]; // Đặt vị trí cho bản đồ

  return (
    <MapContainer center={position} zoom={13} style={{ height: "100%", width: "100%" , borderRadius: "10px"}}>
      {/* Tile Layer: cung cấp các tiles cho bản đồ */}
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      />
      {/* Marker trên bản đồ */}
      <Marker position={position} icon={new L.Icon.Default()}>
        <Popup>Welcome to London!</Popup>
      </Marker>
    </MapContainer>
  );
};

export default MapComponent;
