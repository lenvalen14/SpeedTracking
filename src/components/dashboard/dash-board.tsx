"use client"

import { useState, useEffect } from "react"
import { Bell, Grid, FileText, Shield, PenTool, User, Settings, Search } from "lucide-react"
import Header from "./header"
import SpeedcChart from "./speed-chart"
import LocationSearch from "./location-search"
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
              <div className="flex-1  overflow-auto bg-red-100 h-full">
                  <div className="grid grid-flow-col grid-rows-8 bg-yellow-100 h-full">
                      <div className="col-span-2 row-span-1 bg-yellow-200">
                      <Header />
                      </div>
                      <div className="col-span-2 row-span-5 bg-green-200">
                        <SpeedcChart/>
                      </div>
                      <div className="col-span-2 row-span-2 bg-blue-200">c</div>
                      <div className="col-span-2 row-span-8 bg-pink-200">
                        <LocationSearch/>
                      </div>
                  </div>
              </div>
          </div>
    </>
  )
}

