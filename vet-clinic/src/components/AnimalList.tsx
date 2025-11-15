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

  const handleEdit = (event: React.MouseEvent, animal: Animal) => {
    event.stopPropagation();
    onAnimalClick(animal);
  };

  const handleDelete = (event: React.MouseEvent, animal: Animal) => {
    event.stopPropagation();
    if (window.confirm(`Tem certeza que deseja deletar o animal ${animal.name}?`)) {
      console.log('Deletar animal:', animal.id);
      // TODO: Implementar chamada à API de exclusão
    }
  };

  return (
    <div className="animal-list">
      <div className="animal-list-header">
        <h2>Animais Cadastrados ({animals.length})</h2>
      </div>
      <div className="animal-table-container">
        <table className="animal-table">
          <thead>
            <tr>
              <th className="col-id">ID</th>
              <th className="col-name">Nome</th>
              <th className="col-species">Espécie</th>
              <th className="col-breed">Raça</th>
              <th className="col-gender">Gênero</th>
              <th className="col-birth">Data Nascimento</th>
              <th className="col-owner">Dono</th>
              <th className="col-actions">Ações</th>
            </tr>
          </thead>
          <tbody>
            {animals.map((animal) => (
              <tr 
                key={animal.id} 
                className="animal-row"
                onClick={() => onAnimalClick(animal)}
              >
                <td className="col-id">{animal.id}</td>
                <td className="col-name">
                  <span className="animal-name-badge">{animal.name}</span>
                </td>
                <td className="col-species">
                  <span className="animal-species-badge">{animal.species}</span>
                </td>
                <td className="col-breed">{animal.breed || 'N/A'}</td>
                <td className="col-gender">{animal.gender}</td>
                <td className="col-birth">{formatDate(animal.birthDate)}</td>
                <td className="col-owner">{animal.ownerName}</td>
                <td className="col-actions">
                  <div className="action-buttons">
                    <button 
                      className="btn-action btn-edit" 
                      onClick={(e) => handleEdit(e, animal)}
                      title="Editar"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                      </svg>
                    </button>
                    <button 
                      className="btn-action btn-delete" 
                      onClick={(e) => handleDelete(e, animal)}
                      title="Deletar"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <polyline points="3 6 5 6 21 6"></polyline>
                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                        <line x1="10" y1="11" x2="10" y2="17"></line>
                        <line x1="14" y1="11" x2="14" y2="17"></line>
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

