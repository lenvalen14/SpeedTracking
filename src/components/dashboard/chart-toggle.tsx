type Props = {
    selectedType: string;
    onChangeType: (type: string) => void;
  };
  
  const ChartToggle: React.FC<Props> = ({ selectedType, onChangeType }) => {
    const options = ['Speed Average', 'Density Average', 'Traffic Average'];
  
    return (
      <div className="flex justify-around">
        {options.map((option) => (
          <button
            key={option}
            onClick={() => onChangeType(option)}
            className={`px-4 py-2 rounded ${
              selectedType === option ? 'bg-green-500 text-white' : 'bg-gray-200'
            }`}
          >
            {option}
          </button>
        ))}
      </div>
    );
  };
  
  export default ChartToggle;
  