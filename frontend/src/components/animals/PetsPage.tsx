import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Animal, AnimalFilters, PaginatedResponse } from '../../types/animal';
import { searchAnimals, ApiError } from '../../services/animalApi';
import { AnimalList } from './AnimalList';
import { AnimalDetails } from './AnimalDetails';
import { AnimalFiltersComponent } from './AnimalFilters';
import { ErrorModal } from '../shared/ErrorModal';
import { PageSizeSelector } from '../shared/PageSizeSelector';
import { PaginationControls } from '../shared/PaginationControls';
import './PetsPage.css';

export function PetsPage() {
  const [pagination, setPagination] = useState<PaginatedResponse<Animal> | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedAnimalId, setSelectedAnimalId] = useState<number | null>(null);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [activeFilters, setActiveFilters] = useState<AnimalFilters>({});
  const navigate = useNavigate();

  const loadAnimals = async (page: number = 0, filters: AnimalFilters = {}, size: number = pageSize) => {
    try {
      setLoading(true);
      const data = await searchAnimals({
        ...filters,
        page,
        pageSize: size
      });
      setPagination(data);
      setCurrentPage(page);
      setActiveFilters(filters);
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Carregar', err.detail || 'Erro ao carregar animais. Tente novamente mais tarde.');
      } else {
        showErrorModal('Erro', 'Erro ao carregar animais. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAnimals(0, {});
  }, []);

  const handleSearch = async (filters: AnimalFilters) => {
    loadAnimals(0, filters);
  };

  const handleClearFilters = async () => {
    loadAnimals(0, {});
  };

  const handleNextPage = () => {
    if (pagination?.hasNext) {
      loadAnimals(currentPage + 1, activeFilters);
    }
  };

  const handlePreviousPage = () => {
    if (pagination?.hasPrevious) {
      loadAnimals(currentPage - 1, activeFilters);
    }
  };

  const handleAnimalClick = (animal: Animal) => {
    setSelectedAnimalId(animal.id);
  };

  const handleCloseDetails = () => {
    setSelectedAnimalId(null);
  };

  const handleCreateNew = () => {
    navigate('/cadastrar-animal');
  };

  const showErrorModal = (title: string, message: string) => {
    setErrorModalTitle(title);
    setErrorModalMessage(message);
    setErrorModalOpen(true);
  };

  const closeErrorModal = () => {
    setErrorModalOpen(false);
  };

  const handleSizeChange = (newSize: number) => {
    setPageSize(newSize);
    loadAnimals(0, activeFilters, newSize);
  };

  return (
    <div className="pets-page">
      <div className="pets-page-container">
        <div className="pets-page-actions">
          <AnimalFiltersComponent onSearch={handleSearch} onClear={handleClearFilters} />
          <button className="pets-page-create-btn btn btn-gradient" onClick={handleCreateNew}>
            Cadastrar novo pet
          </button>
        </div>
       
        <div className="pets-page-header">
          <div className="pets-page-header-content">
            {selectedAnimalId ? (
              <AnimalDetails animalId={selectedAnimalId} onClose={handleCloseDetails} />
            ) : (
              <>
                <AnimalList 
                  animals={pagination?.content || []} 
                  onAnimalClick={handleAnimalClick}
                  loading={loading}
                  onAnimalDeleted={() => loadAnimals(currentPage, activeFilters)}
                />
                {pagination && pagination.totalElements > 0 && (
                  <div className="pagination-wrapper">
                    <PaginationControls 
                      currentPage={pagination.pageNumber}
                      totalPages={pagination.totalPages}
                      totalElements={pagination.totalElements}
                      onPreviousPage={handlePreviousPage}
                      onNextPage={handleNextPage}
                      disabled={loading}
                      showFirstLastButtons={false}
                    />
                    <PageSizeSelector 
                      currentSize={pagination.pageSize}
                      onChange={handleSizeChange}
                      disabled={loading}
                    />
                  </div>
                )}

                
              </>
            )}
          </div>
        </div>

        <ErrorModal
          isOpen={errorModalOpen}
          title={errorModalTitle}
          message={errorModalMessage}
          onClose={closeErrorModal}
        />
      </div>
    </div>
  );
}
