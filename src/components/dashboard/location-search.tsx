"use client"

import { useState, useEffect } from "react"
import { Search } from "lucide-react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import GoongMap from "./goong-map"

// Define types for location data
interface Location {
  id: string
  name: string
  address: string
  lat: number
  lng: number
}

interface GoongPrediction {
  description: string
  place_id: string
  structured_formatting: {
    main_text: string
    secondary_text: string
  }
}

interface GoongPlaceDetail {
  geometry: {
    location: {
      lat: number
      lng: number
    }
  }
}

// Mock data for initial display
const mockLocationsData: Location[] = [
  {
    id: "1",
    name: "Phạm Văn Đồng",
    address: "Hồ Chí Minh",
    lat: 10.8231,
    lng: 106.7559,
  },
  {
    id: "2",
    name: "Phạm Văn Đồng",
    address: "Thủ Đức, Hồ Chí Minh",
    lat: 10.824,
    lng: 106.757,
  },
  {
    id: "3",
    name: "Phạm Văn Đồng",
    address: "Gò Vấp, Hồ Chí Minh",
    lat: 10.825,
    lng: 106.758,
  },
  {
    id: "4",
    name: "Phạm Văn Đồng",
    address: "Bình Thạnh, Hồ Chí Minh",
    lat: 10.826,
    lng: 106.759,
  },
  {
    id: "5",
    name: "Phạm Văn Đồng",
    address: "Quận 9, Hồ Chí Minh",
    lat: 10.827,
    lng: 106.76,
  },
  {
    id: "6",
    name: "Phạm Văn Đồng",
    address: "Tân Bình, Hồ Chí Minh",
    lat: 10.828,
    lng: 106.761,
  },
]

export default function LocationSearch() {
  const [searchTerm, setSearchTerm] = useState<string>("")
  const [locations, setLocations] = useState<Location[]>([])
  const [selectedLocation, setSelectedLocation] = useState<Location | null>(null)
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  // Function to get place details (coordinates) from Goong API
  const getPlaceDetails = async (placeId: string): Promise<GoongPlaceDetail | null> => {
    try {
      const apiKey = process.env.NEXT_PUBLIC_GOONG_API_KEY
      if (!apiKey) {
        throw new Error("Goong API key is missing")
      }

      const response = await fetch(`https://rsapi.goong.io/Place/Detail?place_id=${placeId}&api_key=${apiKey}`)

      if (!response.ok) {
        throw new Error(`API error: ${response.status}`)
      }

      const data = await response.json()
      return data.result
    } catch (error) {
      console.error("Error fetching place details:", error)
      return null
    }
  }

  // Function to search for locations using Goong API
  const searchLocations = async () => {
    if (!searchTerm.trim()) {
      // If search is cleared, show initial mock data
      setLocations(mockLocationsData)
      if (mockLocationsData.length > 0) {
        setSelectedLocation(mockLocationsData[0])
      }
      return
    }

    setLoading(true)
    setError(null)

    try {
      const apiKey = process.env.NEXT_PUBLIC_GOONG_API_KEY
      if (!apiKey) {
        throw new Error("Goong API key is missing")
      }

      // Call the Goong API for autocomplete
      const response = await fetch(
        `https://rsapi.goong.io/Place/AutoComplete?api_key=${apiKey}&input=${encodeURIComponent(searchTerm)}`,
      )

      if (!response.ok) {
        throw new Error(`API error: ${response.status}`)
      }

      const data = await response.json()

      if (data.predictions && Array.isArray(data.predictions)) {
        // Process each prediction to get full location details
        const locationPromises = data.predictions.map(async (prediction: GoongPrediction) => {
          const details = await getPlaceDetails(prediction.place_id)

          return {
            id: prediction.place_id,
            name: prediction.structured_formatting.main_text,
            address: prediction.structured_formatting.secondary_text,
            lat: details?.geometry.location.lat || 0,
            lng: details?.geometry.location.lng || 0,
          }
        })

        const locationsWithCoords = await Promise.all(locationPromises)
        const validLocations = locationsWithCoords.filter((loc) => loc.lat !== 0 && loc.lng !== 0)
        setLocations(validLocations)

        // Auto-select first location
        if (validLocations.length > 0) {
          setSelectedLocation(validLocations[0])
        }
      } else {
        setLocations([])
      }
    } catch (error) {
      console.error("Error searching locations:", error)
      setError(error instanceof Error ? error.message : "Failed to search locations")
      setLocations([])
    } finally {
      setLoading(false)
    }
  }

  // Handle search when user types
  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      searchLocations()
    }, 500)

    return () => clearTimeout(delayDebounceFn)
  }, [searchTerm])

  // Select a location
  const handleSelectLocation = (location: Location) => {
    setSelectedLocation(location)
  }

  // Load initial mock data
  useEffect(() => {
    setLocations(mockLocationsData)
    setSelectedLocation(mockLocationsData[0])
  }, [])

  return (
    <div className="h-full flex flex-col bg-white rounded-lg shadow-md overflow-hidden">
      {/* Search input */}
      <div className="p-4 border-b">
        <div className="flex items-center gap-2 w-full">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
            <Input
              type="text"
              placeholder="Search for a location..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 w-full"
            />
          </div>
          <Button onClick={searchLocations} disabled={loading} className="shrink-0">
            {loading ? "Searching..." : "Search"}
          </Button>
        </div>
        {error && <div className="mt-2 text-sm text-red-500">Error: {error}</div>}
      </div>

      {/* Header with Street and View All */}
      <div className="flex justify-between items-center p-4 bg-gray-50 sticky top-0 z-10">
        <h2 className="font-semibold text-lg">Street</h2>
        <Button variant="link" className="text-blue-600 p-0">
          View All
        </Button>
      </div>

      {/* Location list */}
      <div className="overflow-y-auto flex-shrink-0" style={{ maxHeight: "30%" }}>
        <div className="divide-y">
          {locations.map((location, index) => (
            <div
              key={location.id}
              className={`p-4 cursor-pointer transition-colors ${
                selectedLocation?.id === location.id ? "bg-blue-600 text-white" : "hover:bg-gray-50"
              }`}
              onClick={() => handleSelectLocation(location)}
            >
              <div className={`font-semibold ${selectedLocation?.id === location.id ? "text-white" : "text-blue-600"}`}>
                {location.name}
              </div>
              <div className={`text-sm ${selectedLocation?.id === location.id ? "text-blue-100" : "text-gray-500"}`}>
                {location.address}
              </div>
            </div>
          ))}

          {locations.length === 0 && searchTerm && !loading && !error && (
            <div className="p-4 text-center text-gray-500">No locations found</div>
          )}

          {locations.length === 0 && !searchTerm && !loading && !error && (
            <div className="p-4 text-center text-gray-500">Please enter a location to search</div>
          )}

          {loading && <div className="p-4 text-center text-gray-500">Loading...</div>}
        </div>
      </div>

      {/* Map */}
      <div className="flex-grow relative min-h-[300px] m-2">
        <GoongMap selectedLocation={selectedLocation} />
      </div>
    </div>
  )
}

