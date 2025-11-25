import { useState } from 'react';
import type { AnimalFilters } from '../types/animal';
import './AnimalFilters.css';

interface AnimalFiltersProps {
  onSearch: (filters: AnimalFilters) => void;
  onClear: () => void;
}

export function AnimalFiltersComponent({ onSearch, onClear }: AnimalFiltersProps) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [filters, setFilters] = useState<AnimalFilters>({
    name: '',
    species: '',
    ownerName: '',
  });

  const handleChange = (field: keyof AnimalFilters, value: string) => {
    setFilters((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const activeFilters: AnimalFilters = {};
    
    if (filters.name?.trim()) {
      activeFilters.name = filters.name.trim();
    }
    if (filters.species?.trim()) {
      activeFilters.species = filters.species.trim();
    }
    if (filters.ownerName?.trim()) {
      activeFilters.ownerName = filters.ownerName.trim();
    }
    
    onSearch(activeFilters);
  };

  const handleClear = () => {
    setFilters({
      name: '',
      species: '',
      ownerName: '',
    });
    onClear();
  };

  const countActiveFilters = () => {
    let count = 0;
    if (filters.name?.trim()) count++;
    if (filters.species?.trim()) count++;
    if (filters.ownerName?.trim()) count++;
    return count;
  };

  const hasFilters = countActiveFilters() > 0;

  return (
    <form onSubmit={handleSubmit} className="consultation-filters">
      <button
        type="button"
        className="consultation-filters-toggle"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <span className="filter-icon">🔍</span>
        <span className="filter-text">Filtros</span>
        {hasFilters && <span className="filter-badge">{countActiveFilters()}</span>}
        <span className={`arrow ${isExpanded ? 'expanded' : ''}`}>▼</span>
      </button>
      
      {isExpanded && (
        <div className="consultation-filters-content">
          <div className="filters-grid">
            <div className="filter-group">
              <label htmlFor="name">Nome do Animal</label>
              <input
                id="name"
                type="text"
                placeholder="Digite o nome do animal"
                value={filters.name || ''}
                onChange={(e) => handleChange('name', e.target.value)}
              />
            </div>

            <div className="filter-group">
              <label htmlFor="species">Espécie</label>
              <input
                id="species"
                type="text"
                placeholder="Ex: Cachorro, Gato, Pássaro"
                value={filters.species || ''}
                onChange={(e) => handleChange('species', e.target.value)}
              />
            </div>

            <div className="filter-group">
              <label htmlFor="ownerName">Nome do Dono</label>
              <input
                id="ownerName"
                type="text"
                placeholder="Digite o nome do dono"
                value={filters.ownerName || ''}
                onChange={(e) => handleChange('ownerName', e.target.value)}
              />
            </div>
            <div className="filters-actions">
              <button type="submit" className="btn btn-primary">
                🔍 Buscar
              </button>
              <button type="button" onClick={handleClear} className="btn btn-primary" >
                ✕ Limpar Filtros
              </button>
            </div>
          </div>
      </div>)}

    </form>
  );
}


