"use client"

import { useState } from "react"
import { Search } from "lucide-react"

export default function Header() {
  const [query, setQuery] = useState("")

  // Handle search when the Search icon is clicked
  const handleSearchClick = () => {
    alert(`Search for: ${query}`) 
  }

  // Handle search when the Enter key is pressed
  const handleKeyDown = (e: { key: string }) => {
    if (e.key === "Enter") {
      alert(`Search for: ${query}`) 
    }
  }

  return (
    <div className="flex items-center justify-between px-4 py-2 h-full bg-white rounded-md shadow-sm">
      <h1 className="text-4xl font-bold text-blue-600">Dashboard</h1>

      <div className="flex items-center gap-4">
        {/* Search box */}
        <div className="flex items-center bg-gradient-to-r from-green-100 to-white rounded-full px-4 py-2">
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={handleKeyDown} // Trigger search on Enter key
            className="outline-none"
            placeholder="Search..."
          />
          <Search
            className="w-4 h-4 text-black cursor-pointer"
            onClick={handleSearchClick} // Trigger search on Search icon click
          />
        </div>

        {/* Avatar */}
        <img
          src="/logo_google.png"
          alt="User Avatar"
          className="w-16 h-16 rounded-full border"
        />
      </div>
    </div>
  )
}
