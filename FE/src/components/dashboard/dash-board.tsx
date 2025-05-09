"use client"

import { useState, useEffect } from "react"
import { Bell, Grid, FileText, Shield, PenTool, User, Settings, Search } from "lucide-react"
import Header from "./header"
import SpeedChart from "./speed-chart"
import LocationSearch from "./location-search"
import TrafficCards from "./traffic-card"
import TrafficMetrics from "./traffic-metrics"
// import SpeedChart from "@/components/speed-chart"
// import CameraCard from "@/components/camera-card"
// import MetricCard from "@/components/metric-card"
// import LocationList from "@/components/location-list"
// import TopLoadingBar from "@/components/top-loading-bar"

export default function Dashboard() {


  const locations = [
    { name: "Pham Van Dong", time: "08 Oct 14h41" },
    { name: "Pham Van Dong", time: "08 Oct 14h41" },
    { name: "Pham Van Dong", time: "08 Oct 14h41" },
    { name: "Pham Van Dong", time: "08 Oct 14h41" },
    { name: "Pham Van Dong", time: "08 Oct 14h41" },
    { name: "Pham Van Dong", time: "08 Oct 14h41" },
  ]


  return (
    <>
    {/* dash-board */}
          <div className="h-screen"> 
              <div className="flex-1  overflow-auto h-full">
                  <div className="grid grid-flow-col grid-rows-8  h-full">
                      <div className="col-span-2 row-span-1 ">
                      <Header />
                      </div>
                      <div className="col-span-2 row-span-5  flex">
                      <div className="flex-3">
                        <SpeedChart />
                      </div>
                      <div className="flex-1">
                        <TrafficCards />
                      </div>
                    </div>
                      <div className="col-span-2 row-span-2 ">
                        <TrafficMetrics/>
                      </div>
                      <div className="col-span-2 row-span-8 ">
                        <LocationSearch/>
                      </div>
                  </div>
              </div>
          </div>
    </>
  )
}

