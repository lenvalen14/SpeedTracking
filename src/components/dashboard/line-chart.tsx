import type React from "react"
import { Line } from "react-chartjs-2"
import { Chart as ChartJS, LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend } from "chart.js"

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend)

type Props = {
  dataType: string
  timeRange: string
}

const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
const weeks = ["Week 1", "Week 2", "Week 3", "Week 4"]

const generateMonthlyData = (type: string): number[] => {
  switch (type) {
    case "Speed Average":
      return [40, 55, 45, 50, 47, 53, 49, 48, 52, 44, 43, 42]
    case "Density Average":
      return [20, 25, 22, 23, 26, 28, 27, 26, 25, 22, 21, 20]
    case "Traffic Average":
      return [100, 120, 110, 130, 125, 140, 138, 135, 120, 110, 108, 105]
    default:
      return []
  }
}

const generateWeeklyData = (type: string): number[] => {
  switch (type) {
    case "Speed Average":
      return [45, 50, 47, 53]
    case "Density Average":
      return [22, 26, 28, 25]
    case "Traffic Average":
      return [110, 125, 140, 120]
    default:
      return []
  }
}

const LineChart: React.FC<Props> = ({ dataType, timeRange }) => {
  const labels = timeRange === "Weekly" ? weeks : months
  const chartData = timeRange === "Weekly" ? generateWeeklyData(dataType) : generateMonthlyData(dataType)

  const data = {
    labels: labels,
    datasets: [
      {
        label: dataType,
        data: chartData,
        borderColor: "blue",
        backgroundColor: "rgba(0,0,255,0.1)",
        tension: 0.4,
      },
    ],
  }

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  }

  return (
    <div style={{ width: "100%", height: "250px", backgroundColor: "#D2FF52" }}>
      <Line data={data} options={{ ...options, maintainAspectRatio: false }} />
    </div>
  )
}

export default LineChart

