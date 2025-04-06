import React from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

type Props = {
  dataType: string;
  timeRange: string;
};

const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

const generateDummyData = (type: string): number[] => {
  switch (type) {
    case 'Speed Average':
      return [40, 55, 45, 50, 47, 53, 49, 48, 52, 44, 43, 42];
    case 'Density Average':
      return [20, 25, 22, 23, 26, 28, 27, 26, 25, 22, 21, 20];
    case 'Traffic Average':
      return [100, 120, 110, 130, 125, 140, 138, 135, 120, 110, 108, 105];
    default:
      return [];
  }
};

const LineChart: React.FC<Props> = ({ dataType, timeRange }) => {
  const data = {
    labels: months,
    datasets: [
      {
        label: dataType,
        data: generateDummyData(dataType),
        borderColor: 'blue',
        backgroundColor: 'rgba(0,0,255,0.1)',
        tension: 0.4,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  return <div style={{ width: '90%', height: '250px' }}>
        <Line data={data} options={{ ...options, maintainAspectRatio: false }} />
        </div>;
};

export default LineChart;
