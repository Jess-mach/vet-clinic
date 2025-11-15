import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Animal, AnimalFilters } from '../types/animal';
import { searchAnimals, ApiError } from '../services/api';
import { AnimalList } from './AnimalList';
import { AnimalDetails } from './AnimalDetails';
import { AnimalFiltersComponent } from './AnimalFilters';
import { ErrorModal } from './ErrorModal';
import './PetsPage.css';

export function PetsPage() {
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedAnimalId, setSelectedAnimalId] = useState<number | null>(null);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  const navigate = useNavigate();

  const loadAnimals = async () => {
    try {
      setLoading(true);
      const data = await searchAnimals();
      setAnimals(data);
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
    loadAnimals();
  }, []);

  const handleSearch = async (filters: AnimalFilters) => {
    try {
      setLoading(true);
      const data = await searchAnimals(filters);
      setAnimals(data);
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Buscar', err.detail || 'Erro ao buscar animais. Tente novamente mais tarde.');
      } else {
        showErrorModal('Erro', 'Erro ao buscar animais. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleClearFilters = async () => {
    try {
      setLoading(true);
      const data = await searchAnimals();
      setAnimals(data);
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
            <div>
              <h1>Listagem de Pets</h1>
              <p className="pets-page-subtitle">Visualize todos os animais cadastrados</p>
              
            </div>
            {selectedAnimalId ? (
          <AnimalDetails animalId={selectedAnimalId} onClose={handleCloseDetails} />
        ) : (
          <AnimalList 
            animals={animals} 
            onAnimalClick={handleAnimalClick}
            loading={loading}
            onAnimalDeleted={loadAnimals}
          />
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
