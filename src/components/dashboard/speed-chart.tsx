'use client';

import { useState } from 'react';
import ChartToggle from '@/components/dashboard/chart-toggle';
import LineChart from '@/components/dashboard/line-chart';

export default function SpeedcChart() {
  const [selectedType, setSelectedType] = useState('Speed Average');
  const [timeRange, setTimeRange] = useState<'Monthly' | 'Weekly'>('Monthly');

  return (
    <div className="h-full p-4 bg-lime-100 rounded-lg flex flex-col justify-between">
        {/* selection */}
        <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold">Violations</h2>
            <select
            value={timeRange}
            onChange={(e) => setTimeRange(e.target.value as 'Monthly' | 'Weekly')}
            className="border border-gray-300 rounded px-2 py-1"
            >
            <option value="Monthly">Monthly</option>
            <option value="Weekly">Weekly</option>
            </select>
      </div>
        {/* chart toggle */}
      {/* <div className="flex-1">
        <LineChart dataType={selectedType} timeRange={timeRange} />
      </div> */}

    </div>
  );
}
