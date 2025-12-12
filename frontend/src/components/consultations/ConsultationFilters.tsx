import { useState, useEffect } from 'react';
import type { ConsultationFilters } from '../../types/consultation';
import type { Veterinarian } from '../../types/veterinarian';
import { getVeterinarians } from '../../services/veterinarianApi';
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
  const [veterinarianName, setVeterinarianName] = useState('');
  const [veterinarianId, setVeterinarianId] = useState<number | ''>('');
  const [status, setStatus] = useState<'COMPLETED' | 'SCHEDULED' | 'CANCELLED' | ''>('');
  const [reason, setReason] = useState('');
  const [description, setDescription] = useState('');
  const [createdAtStart, setCreatedAtStart] = useState('');
  const [createdAtEnd, setCreatedAtEnd] = useState('');
  const [isExpanded, setIsExpanded] = useState(false);
  const [veterinarians, setVeterinarians] = useState<Veterinarian[]>([]);
  const [loadingVeterinarians, setLoadingVeterinarians] = useState(false);

  // Load veterinarians for filter dropdown
  useEffect(() => {
    const fetchVeterinarians = async () => {
      setLoadingVeterinarians(true);
      try {
        const data = await getVeterinarians();
        setVeterinarians(data);
      } catch (error) {
        console.error('Error fetching veterinarians:', error);
      } finally {
        setLoadingVeterinarians(false);
      }
    };
    
    if (isExpanded) {
      fetchVeterinarians();
    }
  }, [isExpanded]);

  const formatDateTimeForBackend = (dateTimeLocal: string): string => {
    // datetime-local retorna formato "YYYY-MM-DDTHH:mm"
    // Backend espera formato ISO 8601 completo "YYYY-MM-DDTHH:mm:ss"
    if (!dateTimeLocal) return '';
    // Se não tiver segundos, adiciona :00
    if (dateTimeLocal.length === 16) {
      return dateTimeLocal + ':00';
    }
    return dateTimeLocal;
  };

  const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const filters: ConsultationFilters = {};
    
    if (animalName.trim()) filters.animalName = animalName.trim();
    if (ownerName.trim()) filters.ownerName = ownerName.trim();
    if (veterinarianName.trim()) filters.veterinarianName = veterinarianName.trim();
    if (veterinarianId && veterinarianId !== '') {
      filters.veterinarianId = typeof veterinarianId === 'number' ? veterinarianId : Number(veterinarianId);
    }
    if (status) filters.status = status as 'COMPLETED' | 'SCHEDULED' | 'CANCELLED';
    if (reason.trim()) filters.reason = reason.trim();
    if (description.trim()) filters.description = description.trim();
    if (createdAtStart) filters.createdAtStart = formatDateTimeForBackend(createdAtStart);
    if (createdAtEnd) filters.createdAtEnd = formatDateTimeForBackend(createdAtEnd);
    
    // Resetar para página 0 ao aplicar filtros
    filters.page = 0;
    filters.size = 10;
    filters.sort = 'consultationDate,desc';
    
    onSearch(filters);
  };

  const handleClear = () => {
    setAnimalName('');
    setOwnerName('');
    setVeterinarianName('');
    setVeterinarianId('');
    setStatus('');
    setReason('');
    setDescription('');
    setCreatedAtStart('');
    setCreatedAtEnd('');
    onClear();
  };

  const countActiveFilters = () => {
    let count = 0;
    if (animalName.trim()) count++;
    if (ownerName.trim()) count++;
    if (veterinarianName.trim()) count++;
    if (veterinarianId && veterinarianId !== '') count++;
    if (status) count++;
    if (reason.trim()) count++;
    if (description.trim()) count++;
    if (createdAtStart) count++;
    if (createdAtEnd) count++;
    return count;
  };

  const hasFilters = countActiveFilters() > 0;

  return (
    <form className="consultation-filters" onSubmit={handleSearch}>
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
              <label htmlFor="animalName" className="filter-label">
                🐾 Nome do Animal
              </label>
              <input
                id="animalName"
                type="text"
                placeholder="Ex: Rex, Max..."
                value={animalName}
                onChange={(e) => setAnimalName(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="ownerName" className="filter-label">
                👤 Nome do Proprietário
              </label>
              <input
                id="ownerName"
                type="text"
                placeholder="Ex: João, Maria..."
                value={ownerName}
                onChange={(e) => setOwnerName(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="veterinarianName" className="filter-label">
                🩺 Nome do Veterinário
              </label>
              <input
                id="veterinarianName"
                type="text"
                placeholder="Ex: Dr. Silva..."
                value={veterinarianName}
                onChange={(e) => setVeterinarianName(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="veterinarianId" className="filter-label">
                🩺 Veterinário (ID)
              </label>
              <select
                id="veterinarianId"
                value={veterinarianId || ''}
                onChange={(e) => setVeterinarianId(e.target.value === '' ? '' : Number(e.target.value))}
                className="filter-select"
                disabled={loadingVeterinarians}
              >
                <option value="">Todos</option>
                {veterinarians.map((vet) => (
                  <option key={vet.id} value={vet.id}>
                    {vet.name} - {vet.specialty}
                  </option>
                ))}
              </select>
            </div>

            <div className="filter-group">
              <label htmlFor="status" className="filter-label">
                📋 Status
              </label>
              <select
                id="status"
                value={status}
                onChange={(e) => setStatus(e.target.value as 'COMPLETED' | 'SCHEDULED' | 'CANCELLED' | '')}
                className="filter-select"
              >
                <option value="">Todos</option>
                <option value="COMPLETED">Concluída</option>
                <option value="SCHEDULED">Agendada</option>
                <option value="CANCELLED">Cancelada</option>
              </select>
            </div>

            <div className="filter-group">
              <label htmlFor="reason" className="filter-label">
                📝 Motivo
              </label>
              <input
                id="reason"
                type="text"
                placeholder="Ex: Checkup, Vacinação..."
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="description" className="filter-label">
                📄 Descrição
              </label>
              <input
                id="description"
                type="text"
                placeholder="Buscar na descrição..."
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="createdAtStart" className="filter-label">
                📅 Data Inicial
              </label>
              <input
                id="createdAtStart"
                type="datetime-local"
                value={createdAtStart}
                onChange={(e) => setCreatedAtStart(e.target.value)}
                className="filter-input"
              />
            </div>

            <div className="filter-group">
              <label htmlFor="createdAtEnd" className="filter-label">
                📅 Data Final
              </label>
              <input
                id="createdAtEnd"
                type="datetime-local"
                value={createdAtEnd}
                onChange={(e) => setCreatedAtEnd(e.target.value)}
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
              className="btn btn-primary" 
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
