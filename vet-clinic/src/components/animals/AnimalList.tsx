import { useState } from 'react';
import type { Animal } from '../../types/animal';
import { deleteAnimal, ApiError } from '../../services/animalApi';
import { ConfirmDeleteModal } from '../shared/ConfirmDeleteModal';
import { ErrorModal } from '../shared/ErrorModal';
import { AnimalConsultationHistoryModal } from './AnimalConsultationHistoryModal';
import './AnimalList.css';

interface AnimalListProps {
  animals: Animal[];
  onAnimalClick: (animal: Animal) => void;
  loading?: boolean;
  onAnimalDeleted?: () => void;
}

export function AnimalList({ 
  animals, 
  onAnimalClick, 
  loading, 
  onAnimalDeleted 
}: AnimalListProps) {
  const [deleteConfirmModal, setDeleteConfirmModal] = useState<{
    isOpen: boolean;
    animal: Animal | null;
  }>({ isOpen: false, animal: null });
  
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState<{ isOpen: boolean; message: string; details?: Record<string, string> }>({
    isOpen: false,
    message: '',
  });
  const [consultationHistoryModalOpen, setConsultationHistoryModalOpen] = useState(false);
  const [selectedAnimalForHistory, setSelectedAnimalForHistory] = useState<Animal | null>(null);

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
    setDeleteConfirmModal({ isOpen: true, animal });
  };

  const handleViewHistory = (event: React.MouseEvent, animal: Animal) => {
    event.stopPropagation();
    setSelectedAnimalForHistory(animal);
    setConsultationHistoryModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!deleteConfirmModal.animal) return;

    setIsDeleting(true);
    try {
      await deleteAnimal(deleteConfirmModal.animal.id);
      setDeleteConfirmModal({ isOpen: false, animal: null });
      
      // Notify parent component to refresh the list
      if (onAnimalDeleted) {
        onAnimalDeleted();
      }
    } catch (err) {
      const apiError = err instanceof ApiError ? err : new ApiError(500, 'Erro desconhecido');
      setError({
        isOpen: true,
        message: apiError.detail,
        details: apiError.errorData?.errors,
      });
      setDeleteConfirmModal({ isOpen: false, animal: null });
    } finally {
      setIsDeleting(false);
    }
  };

  const handleCancelDelete = () => {
    setDeleteConfirmModal({ isOpen: false, animal: null });
  };

  return (
    <div className="animal-list">
      <div className="animal-list-header">
        <h2>Listagem de Pets ({animals.length})</h2>
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
              <th className="col-birth">Dt.Nascimento</th>
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
                      className="btn-action btn-history" 
                      onClick={(e) => handleViewHistory(e, animal)}
                      title="Ver histórico de consultas"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="18"
                        height="18"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      >
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                        <path d="M14 2v6h6" />
                        <path d="M16 13H8" />
                        <path d="M16 17H8" />
                        <path d="M10 9H8" />
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

      <ConfirmDeleteModal
        isOpen={deleteConfirmModal.isOpen}
        title="Confirmar Exclusão"
        message={
          deleteConfirmModal.animal
            ? `Tem certeza que deseja deletar o animal "${deleteConfirmModal.animal.name}"? Caso existam consultas vinculadas a este animal, ele será apenas inativado e não poderá ser utilizado em novos atendimentos.`
            : 'Tem certeza que deseja deletar este animal? Caso existam consultas vinculadas a este animal, ele será apenas inativado e não poderá ser utilizado em novos atendimentos.'
        }
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
        isLoading={isDeleting}
      />

      <ErrorModal
        isOpen={error.isOpen}
        title="Erro ao Deletar Animal"
        message={error.message}
        onClose={() => setError({ isOpen: false, message: '' })}
        details={error.details}
      />

      {selectedAnimalForHistory && (
        <AnimalConsultationHistoryModal
          isOpen={consultationHistoryModalOpen}
          animalId={selectedAnimalForHistory.id}
          animalName={selectedAnimalForHistory.name}
          onClose={() => {
            setConsultationHistoryModalOpen(false);
            setSelectedAnimalForHistory(null);
          }}
        />
      )}
    </div>
  );
}

