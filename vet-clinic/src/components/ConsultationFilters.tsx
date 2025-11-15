import { useState } from 'react';
import type { ConsultationFilters } from '../types/consultation';
import './ConsultationFilters.css';

interface ConsultationFiltersComponentProps {
  onSearch: (filters: ConsultationFilters) => void;
  onClear: () => void;
}

export function ConsultationFiltersComponent({
  onSearch,
  onClear,
}: ConsultationFiltersComponentProps) {
  const [animalId, setAnimalId] = useState('');
  const [isExpanded, setIsExpanded] = useState(false);

  const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSearch({
      animalId: animalId ? parseInt(animalId) : undefined,
    });
  };

  const handleClear = () => {
    setAnimalId('');
    onClear();
  };

  const hasFilters = animalId;

  return (
    <form className="consultation-filters" onSubmit={handleSearch}>
      <button
        type="button"
        className="consultation-filters-toggle"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <span className="filter-icon">🔍</span>
        <span className="filter-text">Filtros</span>
        {hasFilters && <span className="filter-badge">1</span>}
        <span className={`arrow ${isExpanded ? 'expanded' : ''}`}>▼</span>
      </button>

      {isExpanded && (
        <div className="consultation-filters-content">
          <div className="filters-grid">
            <div className="filter-group">
              <label htmlFor="animalId" className="filter-label">
                🐾 ID do Animal
              </label>
              <input
                id="animalId"
                type="number"
                placeholder="Ex: 1, 2, 3..."
                value={animalId}
                onChange={(e) => setAnimalId(e.target.value)}
                className="filter-input"
                min="1"
              />
            </div>

            <div className="filter-group" style={{ opacity: 0.5, pointerEvents: 'none' }}>
              <label className="filter-label">
                💡 Filtros Avançados
              </label>
              <input
                type="text"
                placeholder="Em desenvolvimento..."
                disabled
                className="filter-input"
              />
              <small style={{ color: '#999', fontSize: '0.8rem' }}>Backend: apenas animalId suportado</small>
            </div>
          </div>

          <div className="filters-actions">
            <button type="submit" className="btn btn-primary">
              🔍 Buscar
            </button>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={handleClear}
              disabled={!hasFilters}
            >
              ✕ Limpar Filtros
            </button>
          </div>
        </div>
      )}
    </form>
  );
}

