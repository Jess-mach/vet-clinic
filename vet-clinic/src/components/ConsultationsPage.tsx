import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Consultation, ConsultationFilters } from '../types/consultation';
import { searchConsultations, ApiError } from '../services/api';
import { ConsultationList } from './ConsultationList';
import { ConsultationDetails } from './ConsultationDetails';
import { ConsultationFiltersComponent } from './ConsultationFilters';
import { ErrorModal } from './ErrorModal';
import './ConsultationsPage.css';

export function ConsultationsPage() {
  const [consultations, setConsultations] = useState<Consultation[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedConsultationId, setSelectedConsultationId] = useState<number | null>(null);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  const navigate = useNavigate();

  const loadConsultations = async () => {
    try {
      setLoading(true);
      const data = await searchConsultations();
      setConsultations(data);
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Carregar', err.detail || 'Erro ao carregar consultas. Tente novamente mais tarde.');
      } else {
        showErrorModal('Erro', 'Erro ao carregar consultas. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadConsultations();
  }, []);

  const handleSearch = async (filters: ConsultationFilters) => {
    try {
      setLoading(true);
      const data = await searchConsultations(filters);
      setConsultations(data);
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Buscar', err.detail || 'Erro ao buscar consultas. Tente novamente mais tarde.');
      } else {
        showErrorModal('Erro', 'Erro ao buscar consultas. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleClearFilters = async () => {
    try {
      setLoading(true);
      const data = await searchConsultations();
      setConsultations(data);
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Carregar', err.detail || 'Erro ao carregar consultas. Tente novamente mais tarde.');
      } else {
        showErrorModal('Erro', 'Erro ao carregar consultas. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleConsultationClick = (consultation: Consultation) => {
    setSelectedConsultationId(consultation.id);
  };

  const handleCloseDetails = () => {
    setSelectedConsultationId(null);
  };

  const handleCreateNew = () => {
    navigate('/cadastrar-consulta');
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
    <div className="consultations-page">
      <div className="consultations-page-container">
        <div className="consultations-page-actions">
          <ConsultationFiltersComponent onSearch={handleSearch} onClear={handleClearFilters} />
          <button className="consultations-page-create-btn btn btn-gradient" onClick={handleCreateNew}>
            Agendar consulta
          </button>
        </div>

        <div className="consultations-page-header">
          <div className="consultations-page-header-content">
            <div>
              <h1>Consultas Veterinárias</h1>
              <p className="consultations-page-subtitle">Gerenciamento de consultas e histórico</p>
            </div>
            <ConsultationList
              consultations={consultations}
              onConsultationClick={handleConsultationClick}
              loading={loading}
              onConsultationDeleted={loadConsultations}
            />
          </div>
        </div>

        {selectedConsultationId && (
          <ConsultationDetails consultationId={selectedConsultationId} onClose={handleCloseDetails} />
        )}

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

