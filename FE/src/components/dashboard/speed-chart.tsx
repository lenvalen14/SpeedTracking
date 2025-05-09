"use client"

import { useState, useMemo } from "react"
import ChartToggle from "@/components/dashboard/chart-toggle"
import LineChart from "@/components/dashboard/line-chart"

export default function SpeedChart() {
  const [selectedType, setSelectedType] = useState("Speed Average")
  const [timeRange, setTimeRange] = useState<"Monthly" | "Weekly">("Monthly")

  // Calculate average value based on selected type
  const averageValue = useMemo(() => {
    const getAverageData = () => {
      if (timeRange === "Weekly") {
        switch (selectedType) {
          case "Speed Average":
            return 48.75 // Average of [45, 50, 47, 53]
          case "Density Average":
            return 25.25 // Average of [22, 26, 28, 25]
          case "Traffic Average":
            return 123.75 // Average of [110, 125, 140, 120]
          default:
            return 0
        }
      }

      switch (selectedType) {
        case "Speed Average":
          return 47.33 // Average of speed data
        case "Density Average":
          return 23.75 // Average of density data
        case "Traffic Average":
          return 120.08 // Average of traffic data
        default:
          return 0
      }
    }

    return getAverageData()
  }, [selectedType, timeRange])

  // Get unit based on selected type
  const getUnit = () => {
    switch (selectedType) {
      case "Speed Average":
        return "km/h"
      case "Density Average":
        return "veh/km"
      case "Traffic Average":
        return "veh/h"
      default:
        return ""
    }
  }

  return (
    <div className="h-full p-4 bg-white rounded-lg flex flex-col">
      {/* Header with title and time range selector */}
      <div className="flex justify-between items-center mb-2">
        <h2 className="text-lg font-semibold">Violations</h2>
        <select
          value={timeRange}
          onChange={(e) => setTimeRange(e.target.value as "Monthly" | "Weekly")}
          className="border border-gray-300 rounded px-2 py-1 text-sm"
        >
          <option value="Monthly">Monthly</option>
          <option value="Weekly">Weekly</option>
        </select>
      </div>

      {/* Average value display */}
      <div className="mb-2 flex items-center">
        <div className="text-3xl font-bold text-green-500">{averageValue.toFixed(1)}</div>
        <div className="ml-2 text-gray-500">{getUnit()}</div>
      </div>



      {/* Chart container */}
      <div className="flex-1 min-h-0">
        <LineChart dataType={selectedType} timeRange={timeRange} />
      {/* Chart toggle buttons */}
      <ChartToggle selectedType={selectedType} onChangeType={setSelectedType} />
      </div>

    </div>
  )
}

