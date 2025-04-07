"use client"

import type React from "react"

type Props = {
  selectedType: string
  onChangeType: (type: string) => void
}

const ChartToggle: React.FC<Props> = ({ selectedType, onChangeType }) => {
  const options = ["Speed Average", "Density Average", "Traffic Average"]

  return (
    <div className="flex flex-wrap justify-around mb-3 bg-(--its-yellow-bold) rounded-b-2xl">
      {options.map((option) => (
        <button
          key={option}
          onClick={() => onChangeType(option)}
          className={`px-3 py-4 w-1/3 text-sm rounded-b-2xl mb-6 ${
            selectedType === option ? "bg-(--its-yellow) text-black" : "bg-(--its-yellow-bold)"
          }`}
        >
          {option}
        </button>
      ))}
    </div>
  )
}

export default ChartToggle

