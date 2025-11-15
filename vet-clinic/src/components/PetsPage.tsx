import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Animal, AnimalFilters } from '../types/animal';
import { searchAnimals, ApiError } from '../services/api';
import { AnimalList } from './AnimalList';
import { AnimalDetails } from './AnimalDetails';
import { AnimalFiltersComponent } from './AnimalFilters';
import './PetsPage.css';

export function PetsPage() {
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedAnimalId, setSelectedAnimalId] = useState<number | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const loadAnimals = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await searchAnimals();
        setAnimals(data);
      } catch (err) {
        if (err instanceof ApiError) {
          setError(`Erro ao carregar animais: ${err.detail}`);
        } else {
          setError('Erro ao carregar animais. Tente novamente mais tarde.');
        }
      } finally {
        setLoading(false);
      }
    };

    loadAnimals();
  }, []);

  const handleSearch = async (filters: AnimalFilters) => {
    try {
      setLoading(true);
      setError(null);
      const data = await searchAnimals(filters);
      setAnimals(data);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(`Erro ao buscar animais: ${err.detail}`);
      } else {
        setError('Erro ao buscar animais. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleClearFilters = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await searchAnimals();
      setAnimals(data);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(`Erro ao carregar animais: ${err.detail}`);
      } else {
        setError('Erro ao carregar animais. Tente novamente mais tarde.');
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

  return (
    <div className="pets-page">

      <div className="pets-page-container">
      <button className="pets-page-create-btn btn btn-primary" onClick={handleCreateNew}>
            Cadastrar novo pet
          </button>
        <div className="pets-page-header">
          <div className="pets-page-header-content">
            <div>
              <h1>Listagem de Pets</h1>
              <p className="pets-page-subtitle">Visualize todos os animais cadastrados</p>
              <AnimalFiltersComponent onSearch={handleSearch} onClear={handleClearFilters} />
              {selectedAnimalId ? (
          <AnimalDetails animalId={selectedAnimalId} onClose={handleCloseDetails} />
        ) : (
          <AnimalList 
            animals={animals} 
            onAnimalClick={handleAnimalClick}
            loading={loading}
          />
        )}
            </div>
          </div>

        </div>

        {error && (
          <div className="pets-page-error">
            <p>{error}</p>
          </div>
        )}

        


      </div>
    </div>
  );
}
