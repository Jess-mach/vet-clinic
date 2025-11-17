import React from 'react';
import './PageSizeSelector.css';

interface PageSizeSelectorProps {
  currentSize: number;
  onChange: (size: number) => void;
  options?: number[];
  disabled?: boolean;
}

export const PageSizeSelector: React.FC<PageSizeSelectorProps> = ({
  currentSize,
  onChange,
  options = [10, 20, 50, 100],
  disabled = false,
}) => {
  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newSize = parseInt(e.target.value, 10);
    onChange(newSize);
  };

  return (
    <div className="page-size-selector">
      <label htmlFor="page-size-selector-input" className="page-size-selector-label">
        Itens por página:
      </label>
      <select
        id="page-size-selector-input"
        className="page-size-selector-input"
        value={currentSize}
        onChange={handleChange}
        disabled={disabled}
      >
        {options.map((option) => (
          <option key={option} value={option}>
            {option}
          </option>
        ))}
      </select>
    </div>
  );
};

