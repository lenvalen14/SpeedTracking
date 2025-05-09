"use client"

import { LucideCctv } from "lucide-react"

type MetricCardProps = {
  title: string
  value: string
  color: "green" | "blue"
}

const MetricCard = ({ title, value, color }: MetricCardProps) => {
  const bgColor = color === "green" ? "bg-[#c4e538]" : "bg-[#3867d6]"
  const textColor = color === "green" ? "text-[#7a9e0c]" : "text-[#3867d6]"

  return (
    <div className="bg-white rounded-lg shadow-sm p-4 flex flex-col items-center">
      <div className={`${bgColor} p-3 rounded-lg mb-2`}>
        <LucideCctv className={color === "green" ? "text-[#7a9e0c]" : "text-white"} size={24} />
      </div>
      <h3 className={`${textColor} font-medium text-center`}>{title}</h3>
      <p className="text-gray-400 text-sm">{value}</p>
    </div>
  )
}

export default function TrafficMetrics() {
  return (
    <div className="grid grid-cols-3 gap-4 p-4 bg-[#f8fafc]">
      <MetricCard title="Density" value="47.43 m/s" color="green" />
      <MetricCard title="Speed Average" value="43.43 m/s" color="green" />
      <MetricCard title="Flow rate" value="47.43 m/s" color="blue" />
    </div>
  )
}

