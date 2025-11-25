import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAnimalById, ApiError } from '../services/api';
import type { Animal } from '../types/animal';
import { AnimalConsultationHistoryModal } from './AnimalConsultationHistoryModal';
import './AnimalDetails.css';

interface AnimalDetailsProps {
  animalId: number | null;
  onClose: () => void;
}

export function AnimalDetails({ animalId, onClose }: AnimalDetailsProps) {
  const navigate = useNavigate();
  const [animal, setAnimal] = useState<Animal | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [consultationHistoryModalOpen, setConsultationHistoryModalOpen] = useState(false);

  useEffect(() => {
    if (!animalId) {
      setAnimal(null);
      setError(null);
      return;
    }

    const fetchAnimal = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await getAnimalById(animalId);
        setAnimal(data);
      } catch (err) {
        if (err instanceof ApiError) {
          setError(err.detail);
        } else {
          setError('Erro ao carregar dados do animal');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchAnimal();
  }, [animalId]);

  if (!animalId) {
    return null;
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

  const formatDateTime = (dateString: string): string => {
    try {
      const date = new Date(dateString);
      return date.toLocaleString('pt-BR');
    } catch {
      return dateString;
    }
  };

  const handleEdit = () => {
    if (animal) {
      // Navega para a página de cadastro passando os dados do animal no location state
      navigate('/cadastrar-animal', { 
        state: { 
          animal: {
            id: animal.id,
            name: animal.name,
            species: animal.species,
            breed: animal.breed,
            gender: animal.gender,
            birthDate: animal.birthDate,
            color: animal.color,
            weight: animal.weight,
            microchipNumber: animal.microchipNumber,
            ownerName: animal.ownerName,
            ownerPhone: animal.ownerPhone,
            ownerEmail: animal.ownerEmail,
          }
        }
      });
    }
  };

  return (
    <div className="animal-details-overlay" onClick={onClose}>
      <div className="animal-details-modal" onClick={(e) => e.stopPropagation()}>
        <button className="animal-details-close" onClick={onClose}>
          ×
        </button>

        {loading && (
          <div className="animal-details-loading">
            <p>Carregando...</p>
          </div>
        )}

        {error && (
          <div className="animal-details-error">
            <p>{error}</p>
          </div>
        )}

        {animal && !loading && !error && (
          <div className="animal-details-content">
            <div className="animal-details-header">
              <h2>{animal.name}</h2>
              <span className="animal-details-species">{animal.species}</span>
            </div>

            <div className="animal-details-sections">
              <section className="details-section">
                <h3>Informações Básicas</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">ID:</span>
                    <span className="detail-value">{animal.id}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Raça:</span>
                    <span className="detail-value">{animal.breed || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Gênero:</span>
                    <span className="detail-value">{animal.gender}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Data de Nascimento:</span>
                    <span className="detail-value">{formatDate(animal.birthDate)}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Cor:</span>
                    <span className="detail-value">{animal.color || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Peso:</span>
                    <span className="detail-value">
                      {animal.weight ? `${animal.weight} kg` : 'N/A'}
                    </span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Microchip:</span>
                    <span className="detail-value">{animal.microchipNumber || 'N/A'}</span>
                  </div>
                </div>
              </section>

              <section className="details-section">
                <h3>Informações do Dono</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Nome:</span>
                    <span className="detail-value">{animal.ownerName}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Telefone:</span>
                    <span className="detail-value">{animal.ownerPhone || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Email:</span>
                    <span className="detail-value">{animal.ownerEmail || 'N/A'}</span>
                  </div>
                </div>
              </section>

              <section className="details-section">
                <h3>Informações do Sistema</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Criado em:</span>
                    <span className="detail-value">{formatDateTime(animal.createdAt)}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Atualizado em:</span>
                    <span className="detail-value">{formatDateTime(animal.updatedAt)}</span>
                  </div>
                </div>
              </section>
            </div>

            <div className="animal-details-actions">
              <button 
                className="btn btn-primary"  
                onClick={() => setConsultationHistoryModalOpen(true)}
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
                  style={{ marginRight: '8px', verticalAlign: 'middle' }}
                >
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                  <path d="M14 2v6h6" />
                  <path d="M16 13H8" />
                  <path d="M16 17H8" />
                  <path d="M10 9H8" />
                </svg>
                Histórico de Consultas
              </button>
              <button className="btn btn-primary" onClick={handleEdit}>
                ✏️ Editar
              </button>
            </div>
          </div>
        )}
      </div>

      {animal && (
        <AnimalConsultationHistoryModal
          isOpen={consultationHistoryModalOpen}
          animalId={animal.id}
          animalName={animal.name}
          onClose={() => setConsultationHistoryModalOpen(false)}
        />
      )}
    </div>
  );
}

