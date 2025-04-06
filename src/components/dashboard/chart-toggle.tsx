"use client"

import type React from "react"

type Props = {
  selectedType: string
  onChangeType: (type: string) => void
}

const ChartToggle: React.FC<Props> = ({ selectedType, onChangeType }) => {
  const options = ["Speed Average", "Density Average", "Traffic Average"]

  return (
    <div className="flex flex-wrap justify-around gap-2 mb-3">
      {options.map((option) => (
        <button
          key={option}
          onClick={() => onChangeType(option)}
          className={`px-3 py-1.5 text-sm rounded ${
            selectedType === option ? "bg-green-500 text-white" : "bg-gray-200"
          }`}
        >
          {option}
        </button>
      ))}
    </div>
  )
}

export default ChartToggle

