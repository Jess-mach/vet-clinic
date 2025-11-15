import type { Animal } from '../types/animal';
import './AnimalList.css';

interface AnimalListProps {
  animals: Animal[];
  onAnimalClick: (animal: Animal) => void;
  loading?: boolean;
}

export function AnimalList({ animals, onAnimalClick, loading }: AnimalListProps) {
  if (loading) {
    return (
      <div className="animal-list-loading">
        <p>Carregando animais...</p>
      </div>
    );
  }

  if (animals.length === 0) {
    return (
      <div className="animal-list-empty">
        <p>Nenhum animal encontrado.</p>
      </div>
    );
  }

  const formatDate = (dateString: string | null): string => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('pt-BR');
    } catch {
      return dateString;
    }
  };

  return (
    <div className="animal-list">
      <h2>Animais Cadastrados ({animals.length})</h2>
      <div className="animal-grid">
        {animals.map((animal) => (
          <div
            key={animal.id}
            className="animal-card"
            onClick={() => onAnimalClick(animal)}
          >
            <div className="animal-card-header">
              <h3>{animal.name}</h3>
              <span className="animal-species">{animal.species}</span>
            </div>
            <div className="animal-card-body">
              <div className="animal-info-row">
                <span className="info-label">Raça:</span>
                <span className="info-value">{animal.breed || 'N/A'}</span>
              </div>
              <div className="animal-info-row">
                <span className="info-label">Gênero:</span>
                <span className="info-value">{animal.gender}</span>
              </div>
              <div className="animal-info-row">
                <span className="info-label">Data de Nascimento:</span>
                <span className="info-value">{formatDate(animal.birthDate)}</span>
              </div>
              <div className="animal-info-row">
                <span className="info-label">Dono:</span>
                <span className="info-value">{animal.ownerName}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

