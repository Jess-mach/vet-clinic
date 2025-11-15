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
  const [animalName, setAnimalName] = useState('');
  const [ownerName, setOwnerName] = useState('');
  const [status, setStatus] = useState('');
  const [veterinarian, setVeterinarian] = useState('');
  const [isExpanded, setIsExpanded] = useState(false);

  const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSearch({
      animalName: animalName || undefined,
      ownerName: ownerName || undefined,
      status: status || undefined,
      veterinarian: veterinarian || undefined,
    });
  };

  const handleClear = () => {
    setAnimalName('');
    setOwnerName('');
    setStatus('');
    setVeterinarian('');
    onClear();
  };

  const hasFilters = animalName || ownerName || status || veterinarian;

  return (
    <form className="consultation-filters" onSubmit={handleSearch}>
      <button
        type="button"
        className="consultation-filters-toggle"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <span className="filter-icon">🔍</span>
        <span className="filter-text">Filtros</span>
        {hasFilters && <span className="filter-badge">{[animalName, ownerName, status, veterinarian].filter(Boolean).length}</span>}
        <span className={`arrow ${isExpanded ? 'expanded' : ''}`}>▼</span>
      </button>

      {isExpanded && (
        <div className="consultation-filters-content">
          <div className="filters-grid">
            <div className="filter-group">
              <label htmlFor="animalName" className="filter-label">
                🐾 Nome do Animal
              </label>
              <input
                id="animalName"
                type="text"
                placeholder="Ex: Rex, Fluffy..."
                value={animalName}
                onChange={(e) => setAnimalName(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="ownerName" className="filter-label">
                👤 Proprietário
              </label>
              <input
                id="ownerName"
                type="text"
                placeholder="Ex: João Silva..."
                value={ownerName}
                onChange={(e) => setOwnerName(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="status" className="filter-label">
                ✓ Status
              </label>
              <select value={status} onChange={(e) => setStatus(e.target.value)} className="filter-select">
                <option value="">Todos os status</option>
                <option value="COMPLETED">Concluída</option>
                <option value="SCHEDULED">Agendada</option>
                <option value="CANCELLED">Cancelada</option>
              </select>
            </div>

            <div className="filter-group">
              <label htmlFor="veterinarian" className="filter-label">
                👨‍⚕️ Veterinário
              </label>
              <input
                id="veterinarian"
                type="text"
                placeholder="Ex: Dr. Silva..."
                value={veterinarian}
                onChange={(e) => setVeterinarian(e.target.value)}
                className="filter-input"
              />
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

