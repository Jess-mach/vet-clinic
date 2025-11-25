import { useState, useEffect } from 'react';
import type { Animal } from '../../types/animal';
import { searchAnimals, ApiError } from '../../services/animalApi';
import './AnimalSearchModal.css';

interface AnimalSearchModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSelectAnimal: (animal: Animal) => void;
}

export function AnimalSearchModal({
  isOpen,
  onClose,
  onSelectAnimal,
}: AnimalSearchModalProps) {
  const [searchTerm, setSearchTerm] = useState('');
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Load initial 5 animals when modal opens
  useEffect(() => {
    if (isOpen) {
      setSearchTerm('');
      setError(null);
      loadAnimals('');
    }
  }, [isOpen]);

  // Auto search when user types 3+ characters
  useEffect(() => {
    if (isOpen && searchTerm.length >= 3) {
      const delayDebounce = setTimeout(() => {
        loadAnimals(searchTerm);
      }, 500); // Debounce de 500ms

      return () => clearTimeout(delayDebounce);
    } else if (isOpen && searchTerm.length === 0) {
      // Load initial animals when search is cleared
      loadAnimals('');
    }
  }, [searchTerm, isOpen]);

  const loadAnimals = async (term: string) => {
    setLoading(true);
    setError(null);

    try {
      const response = await searchAnimals({
        name: term.trim() || undefined,
        page: 0,
        pageSize: term.trim() ? 50 : 5, // 5 initial, 50 when searching
      });
      
      setAnimals(response.content);
      
      if (response.content.length === 0) {
        setError(term ? 'Nenhum pet encontrado com esse nome' : 'Nenhum pet cadastrado');
      }
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.detail || 'Erro ao buscar pets');
      } else {
        setError('Erro ao buscar pets. Tente novamente.');
      }
      setAnimals([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchTermChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleSelectAnimal = (animal: Animal) => {
    onSelectAnimal(animal);
    onClose();
  };

  const getSpeciesDisplay = (species: string): string => {
    const speciesMap: Record<string, string> = {
      'Dog': '🐕 Cão',
      'Cat': '🐈 Gato',
      'Bird': '🦜 Ave',
      'Rabbit': '🐰 Coelho',
    };
    return speciesMap[species] || `🐾 ${species}`;
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="animal-search-modal-overlay" onClick={onClose}>
      <div className="animal-search-modal" onClick={(e) => e.stopPropagation()}>
        <div className="animal-search-modal-header">
          <h2>🔍 Buscar Pet</h2>
          <button className="animal-search-modal-close" onClick={onClose}>
            ×
          </button>
        </div>

        <div className="animal-search-modal-content">
          <div className="animal-search-form">
            <div className="search-input-group">
              <label htmlFor="animalSearch">Nome do Pet</label>
              <div className="search-input-wrapper">
                <input
                  id="animalSearch"
                  type="text"
                  placeholder="Digite no mínimo 3 caracteres para buscar..."
                  value={searchTerm}
                  onChange={handleSearchTermChange}
                  disabled={loading}
                  autoFocus
                />
                {searchTerm.length > 0 && searchTerm.length < 3 && (
                  <small className="search-hint-text">
                    Digite mais {3 - searchTerm.length} caractere(s) para buscar
                  </small>
                )}
              </div>
            </div>
          </div>

          {loading && (
            <div className="search-loading">
              <div className="loading-spinner"></div>
              <p>Buscando pets...</p>
            </div>
          )}

          {!loading && error && (
            <div className="search-error-message">
              <span>⚠️</span> {error}
            </div>
          )}

          {!loading && !error && animals.length > 0 && (
            <div className="animals-table-container">
              <p className="results-count">
                {searchTerm.length >= 3 ? (
                  <>{animals.length} {animals.length === 1 ? 'pet encontrado' : 'pets encontrados'}</>
                ) : (
                  <>Exibindo os 5 primeiros pets cadastrados</>
                )}
              </p>
              <div className="animals-table-wrapper">
                <table className="animals-table">
                  <thead>
                    <tr>
                      <th>Nome</th>
                      <th>Espécie</th>
                      <th>Raça</th>
                      <th>Proprietário</th>
                      <th>Ação</th>
                    </tr>
                  </thead>
                  <tbody>
                    {animals.map((animal) => (
                      <tr key={animal.id} className="animal-row">
                        <td className="animal-name">
                          <span className="animal-icon-small">🐾</span>
                          <strong>{animal.name}</strong>
                        </td>
                        <td>{getSpeciesDisplay(animal.species)}</td>
                        <td>{animal.breed || '-'}</td>
                        <td>
                          <span className="owner-icon">👤</span>
                          {animal.ownerName}
                        </td>
                        <td>
                          <button
                            type="button"
                            className="btn btn-primary btn-small select-btn"
                            onClick={() => handleSelectAnimal(animal)}
                          >
                            Selecionar
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>

        <div className="animal-search-modal-footer">
          <button className="btn btn-primary"  onClick={onClose}>
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
