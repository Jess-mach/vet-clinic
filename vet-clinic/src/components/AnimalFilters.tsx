import { useState } from 'react';
import type { AnimalFilters } from '../types/animal';
import './AnimalFilters.css';

interface AnimalFiltersProps {
  onSearch: (filters: AnimalFilters) => void;
  onClear: () => void;
}

export function AnimalFiltersComponent({ onSearch, onClear }: AnimalFiltersProps) {
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

  return (
    <form onSubmit={handleSubmit} className="animal-filters">
      <div className="animal-filters-header">
        <div className="animal-filters-title">
          <h3>🔍 Filtros de Busca</h3>
          <p>Refine sua busca pelos animais cadastrados</p>
        </div>
      </div>

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
      </div>

      <div className="filters-actions">
        <button type="submit" className="btn-primary">
          🔍 Buscar
        </button>
        <button type="button" onClick={handleClear} className="btn-secondary">
          ✕ Limpar Filtros
        </button>
      </div>
    </form>
  );
}


