import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Consultation, ConsultationFilters } from '../../types/consultation';
import { searchConsultations, ApiError } from '../../services/consultationApi';
import { ConsultationList } from './ConsultationList';
import { ConsultationDetails } from './ConsultationDetails';
import { ConsultationFiltersComponent } from './ConsultationFilters';
import { ErrorModal } from '../shared/ErrorModal';
import { printConsultationDetails } from './ConsultationDetailsPrint';
import './ConsultationsPage.css';

export function ConsultationsPage() {
  const [consultations, setConsultations] = useState<Consultation[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedConsultationId, setSelectedConsultationId] = useState<number | null>(null);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  
  // Estado de paginação
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  
  // Estado para manter filtros atuais
  const [currentFilters, setCurrentFilters] = useState<ConsultationFilters>({});
  
  const navigate = useNavigate();

  const loadConsultations = async (filters?: ConsultationFilters, pageNum?: number, pageSize?: number) => {
    try {
      setLoading(true);
      const filtersToUse = filters || currentFilters;
      const pageToUse = pageNum !== undefined ? pageNum : page;
      const sizeToUse = pageSize !== undefined ? pageSize : size;
      
      const searchFilters: ConsultationFilters = {
        ...filtersToUse,
        page: pageToUse,
        size: sizeToUse,
        sort: filtersToUse.sort || 'consultationDate,desc',
      };
      
      const data = await searchConsultations(searchFilters);
      setConsultations(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setPage(data.number);
      setSize(data.size);
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
    setCurrentFilters(filters);
    await loadConsultations(filters, 0, filters.size || size);
  };

  const handleClearFilters = async () => {
    const emptyFilters: ConsultationFilters = {};
    setCurrentFilters(emptyFilters);
    await loadConsultations(emptyFilters, 0, size);
  };

  const handlePageChange = async (newPage: number) => {
    await loadConsultations(currentFilters, newPage, size);
  };

  const handleSizeChange = async (newSize: number) => {
    await loadConsultations(currentFilters, 0, newSize);
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

  const handlePrintDetails = (consultation: Consultation) => {
    printConsultationDetails(consultation, (errorMessage) => {
      showErrorModal('Erro ao Imprimir', errorMessage);
    });
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
        <ConsultationList
              consultations={consultations}
              onConsultationClick={handleConsultationClick}
              loading={loading}
              onConsultationCancelled={() => loadConsultations(currentFilters, page, size)}
              onPrintDetails={handlePrintDetails}
              pagination={{
                page,
                size,
                totalPages,
                totalElements,
              }}
              onPageChange={handlePageChange}
              onSizeChange={handleSizeChange}
            />
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

