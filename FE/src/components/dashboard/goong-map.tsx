"use client"

import { useEffect, useRef, useState } from "react"
import { Loader2 } from "lucide-react"

// Extend the Window interface to include goongjs
declare global {
  interface Window {
    goongjs: any
  }
}

interface Location {
  id: string
  name: string
  address: string
  lat: number
  lng: number
}

interface GoongMapProps {
  selectedLocation: Location | null
}

export default function GoongMap({ selectedLocation }: GoongMapProps) {
  const mapRef = useRef<HTMLDivElement>(null)
  const [mapLoaded, setMapLoaded] = useState(false)
  const [map, setMap] = useState<any>(null)
  const [marker, setMarker] = useState<any>(null)
  const [popup, setPopup] = useState<any>(null)

  // Load Goong Maps script
  useEffect(() => {
    if (!mapLoaded) {
      // Load the Goong JS script and CSS
      const script = document.createElement("script")
      script.src = "https://cdn.jsdelivr.net/npm/@goongmaps/goong-js@1.0.9/dist/goong-js.js"
      script.async = true
      script.onload = () => {
        const linkEl = document.createElement("link")
        linkEl.href = "https://cdn.jsdelivr.net/npm/@goongmaps/goong-js@1.0.9/dist/goong-js.css"
        linkEl.rel = "stylesheet"
        document.head.appendChild(linkEl)
        setMapLoaded(true)
      }
      document.body.appendChild(script)
    }
  }, [mapLoaded])

  // Initialize map
  useEffect(() => {
    if (mapLoaded && mapRef.current && !map && typeof window !== "undefined") {
      try {
        // Get the API key from environment variables
        const apiKey = process.env.NEXT_PUBLIC_GOONG_MAPS_KEY

        if (!apiKey) {
          console.error("Goong Maps API key is missing")
          return
        }

        // Set the API access token before initializing the map
        window.goongjs.accessToken = apiKey

        // Default center on Ho Chi Minh City
        const defaultCenter = [106.7, 10.8]

        // Initialize the map with a delay to ensure container is properly sized
        setTimeout(() => {
          if (mapRef.current) {
            const goongMap = new window.goongjs.Map({
              container: mapRef.current,
              style: "https://tiles.goong.io/assets/goong_map_web.json",
              center: defaultCenter,
              zoom: 12,
            })

            // Force resize after map is loaded to fix rendering issues
            goongMap.on("load", () => {
              goongMap.resize()
            })

            setMap(goongMap)
          }
        }, 100)
      } catch (error) {
        console.error("Error initializing Goong Maps:", error)
      }
    }
  }, [mapLoaded, map])

  // Update marker when selected location changes
  useEffect(() => {
    if (map && selectedLocation) {
      // Remove existing marker and popup if any
      if (marker) {
        marker.remove()
      }

      if (popup) {
        popup.remove()
      }

      try {
        // Create a popup with location information
        const newPopup = new window.goongjs.Popup({ offset: 25 }).setHTML(`
            <div>
              <h3 style="font-weight: bold; margin-bottom: 4px;">${selectedLocation.name}</h3>
              <p style="margin: 0;">${selectedLocation.address}</p>
            </div>
          `)

        setPopup(newPopup)

        // Create a new marker at the selected location
        const newMarker = new window.goongjs.Marker()
          .setLngLat([selectedLocation.lng, selectedLocation.lat])
          .setPopup(newPopup)
          .addTo(map)

        setMarker(newMarker)

        // Center the map on the selected location
        map.flyTo({
          center: [selectedLocation.lng, selectedLocation.lat],
          zoom: 15,
          essential: true,
        })

        // Force resize to ensure map renders correctly
        map.resize()
      } catch (error) {
        console.error("Error updating marker:", error)
      }
    }
  }, [map, selectedLocation])

  // Handle resize events to ensure map renders correctly
  useEffect(() => {
    const handleResize = () => {
      if (map) {
        map.resize()
      }
    }

    window.addEventListener("resize", handleResize)

    // Force resize on component mount
    if (map) {
      setTimeout(() => {
        map.resize()
      }, 200)
    }

    return () => {
      window.removeEventListener("resize", handleResize)
    }
  }, [map])

  return (
    <div className="w-full h-full relative">
      {!mapLoaded && (
        <div className="absolute inset-0 flex items-center justify-center bg-gray-100">
          <Loader2 className="h-8 w-8 animate-spin text-blue-600" />
        </div>
      )}
      <div ref={mapRef} className="w-full h-full" style={{ minHeight: "100%" }} />
    </div>
  )
}

