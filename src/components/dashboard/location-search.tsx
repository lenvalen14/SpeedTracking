"use client"

import { useState, useEffect } from "react"
import { Search } from "lucide-react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import GoongMap from "@/components/dashboard/goong-map"

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
    if (!searchTerm.trim()) return

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
        setLocations(locationsWithCoords.filter((loc) => loc.lat !== 0 && loc.lng !== 0))
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
      if (searchTerm) {
        searchLocations()
      } else {
        setLocations([])
      }
    }, 500)

    return () => clearTimeout(delayDebounceFn)
  }, [searchTerm])

  // Select a location
  const handleSelectLocation = (location: Location) => {
    setSelectedLocation(location)
  }

  return (
    <div className="h-screen bg-pink-400 p-4"> {/* Thêm padding tổng */}
      <div className="h-full mx-auto max-w-4xl flex flex-col bg-white rounded-lg shadow-md overflow-hidden">
        {/* Phần tìm kiếm */}
        <div className="p-4 border-b">
          <div className="flex items-center gap-2 w-full">
            <div className="relative flex-1 max-w-2xl"> {/* Giới hạn chiều rộng */}
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
  
        {/* Danh sách địa điểm */}
        <div className="w-full border-b overflow-y-auto flex-1 max-h-[40vh]"> {/* Giới hạn chiều cao */}
          <div className="flex justify-between items-center p-4 bg-gray-50 sticky top-0">
            <h2 className="font-semibold text-lg">Street</h2>
            <Button variant="link" className="text-blue-600 p-0">
              View All
            </Button>
          </div>
  
          <div className="divide-y">
            {locations.map((location) => (
              <div
                key={location.id}
                className={`p-4 cursor-pointer hover:bg-gray-50 transition-colors ${
                  selectedLocation?.id === location.id ? "bg-blue-50" : ""
                }`}
                onClick={() => handleSelectLocation(location)}
              >
                <div className="font-semibold text-blue-600 truncate">{location.name}</div>
                <div className="text-gray-500 text-sm truncate">{location.address}</div>
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
  
        {/* Bản đồ */}
        <div className="w-full flex-1 min-h-[300px]">
          <GoongMap selectedLocation={selectedLocation} />
        </div>
      </div>
    </div>
  )
}

