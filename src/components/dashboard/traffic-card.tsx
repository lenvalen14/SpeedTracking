"use client"

import { Camera, ChevronRight, Car, AlertTriangle, TrendingUp, Clock ,LucideCctv} from "lucide-react"
import Link from "next/link"

type CameraCardProps = {
  count: number
  showDetails?: boolean
  metricLabel?: string
  metricValue?: string
}

export default function TrafficCards() {
  return (
    <div className="p-4 grid grid-cols-1 gap-1 h-full overflow-y-auto">
      {/* Camera Cards */}
      <div className="grid grid-cols-1 gap-1">
        <CameraCard count={10} />
        <CameraCard count={10} showDetails={true} metricLabel="Speed Average" metricValue="47,43" />
      </div>

      {/* Violation and Traffic Flow Cards */}
      <ViolationCard />
      <TrafficFlowCard />
    </div>
  )
}

function CameraCard({ count, showDetails = false, metricLabel, metricValue }: CameraCardProps) {
  return (
    <div className="bg-(--its-blue) rounded-lg overflow-hidden shadow-sm">
      <div className="p-4 flex items-center justify-between">
        <div className="flex items-center">
          <div className="text-white mr-3">
            <LucideCctv size={20} />
          </div>
          <div>
            <span className="text-white font-medium">{count} cameras</span>
            <div className="text-blue-200 text-xs">
              <Link href="/cameras">View Detail</Link>
            </div>
          </div>
        </div>
      </div>

      {showDetails && metricLabel && metricValue && (
        <div className="bg-[#4b7bec] p-4 flex items-center justify-between">
          <div>
            <div className="text-blue-200 text-xs">{metricLabel}</div>
            <div className="text-white text-2xl font-bold">{metricValue}</div>
          </div>
          <ChevronRight className="text-white" />
        </div>
      )}
    </div>
  )
}

function ViolationCard() {
  return (
    <div className="bg-(--its-blue) rounded-lg p-4 flex flex-col border border-blue-100 shadow-sm">
      <div className="flex justify-between items-start mb-2">
        <h3 className="text-lg font-semibold text-white">Violations</h3>
        <div className="bg-blue-500 p-1.5 rounded-full text-white">
          <AlertTriangle size={16} />
        </div>
      </div>

      <div className="flex items-baseline mb-1">
        <span className="text-3xl font-bold text-white">247</span>
        <span className="ml-2 text-sm text-white">today</span>
      </div>

      <div className="flex items-center text-sm text-white">
        <TrendingUp size={14} className="mr-1" />
        <span>+12% from yesterday</span>
      </div>

      <div className="mt-auto pt-3 border-t border-blue-100 flex justify-between text-xs text-white">
        <div className="flex flex-col">
          <span className="font-medium">Speed</span>
          <span className="font-bold">156</span>
        </div>
        <div className="flex flex-col">
          <span className="font-medium">Red Light</span>
          <span className="font-bold">91</span>
        </div>
      </div>
    </div>
  )
}

function TrafficFlowCard() {
  return (
    <div className="bg-(--its-blue) rounded-lg p-4 flex flex-col border border-blue-100 shadow-sm">
      <div className="flex justify-between items-start mb-2">
        <h3 className="text-lg font-semibold text-white">Traffic Flow</h3>
        <div className="bg-blue-500 p-1.5 rounded-full text-white">
          <Car size={16} />
        </div>
      </div>

      <div className="flex items-baseline mb-1">
        <span className="text-3xl font-bold text-white">1,842</span>
        <span className="ml-2 text-sm text-white">vehicles/h</span>
      </div>

      <div className="flex items-center text-sm text-white">
        <Clock size={14} className="mr-1" />
        <span>Peak hours: 7-9 AM, 5-7 PM</span>
      </div>

      <div className="mt-auto pt-3 border-t border-blue-100 flex justify-between text-xs text-white">
        <div className="flex flex-col">
          <span className="font-medium">Cars</span>
          <span className="font-bold">78%</span>
        </div>
        <div className="flex flex-col">
          <span className="font-medium">Trucks</span>
          <span className="font-bold">14%</span>
        </div>
        <div className="flex flex-col">
          <span className="font-medium">Bikes</span>
          <span className="font-bold">8%</span>
        </div>
      </div>
    </div>
  )
}

