import { useState } from 'react';
import type { Consultation } from '../types/consultation';
import { cancelConsultation, ApiError } from '../services/api';
import { ConfirmDeleteModal } from './ConfirmDeleteModal';
import { ErrorModal } from './ErrorModal';
import { PageSizeSelector } from './PageSizeSelector';
import { PaginationControls } from './PaginationControls';
import './ConsultationList.css';

interface ConsultationListProps {
  consultations: Consultation[];
  onConsultationClick: (consultation: Consultation) => void;
  loading?: boolean;
  onConsultationCancelled?: () => void;
  pagination?: {
    page: number;
    size: number;
    totalPages: number;
    totalElements: number;
  };
  onPageChange?: (page: number) => void;
  onSizeChange?: (size: number) => void;
}

export function ConsultationList({
  consultations,
  onConsultationClick,
  loading,
  onConsultationCancelled,
  pagination,
  onPageChange,
  onSizeChange,
}: ConsultationListProps) {
  const [cancelConfirmModal, setCancelConfirmModal] = useState<{
    isOpen: boolean;
    consultation: Consultation | null;
  }>({ isOpen: false, consultation: null });

  const [isCancelling, setIsCancelling] = useState(false);
  const [error, setError] = useState<{ isOpen: boolean; message: string; details?: Record<string, string> }>({
    isOpen: false,
    message: '',
  });

  if (loading) {
    return (
      <div className="consultation-list-loading">
        <p>Carregando consultas...</p>
      </div>
    );
  }

  const totalElements = pagination?.totalElements ?? consultations.length;
  const isEmpty = consultations.length === 0 && (!pagination || pagination.totalElements === 0);

  if (isEmpty) {
    return (
      <div className="consultation-list-empty">
        <p>Nenhuma consulta encontrada.</p>
      </div>
    );
  }

  const formatDate = (dateString: string | null): string => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('pt-BR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return dateString;
    }
  };

  const formatStatus = (status: string) => {
    const statusMap: Record<string, string> = {
      COMPLETED: 'Concluída',
      SCHEDULED: 'Agendada',
      CANCELLED: 'Cancelada',
    };
    return statusMap[status] || status;
  };

  const getStatusClass = (status: string) => {
    const statusClass: Record<string, string> = {
      COMPLETED: 'status-completed',
      SCHEDULED: 'status-scheduled',
      CANCELLED: 'status-cancelled',
    };
    return statusClass[status] || 'status-default';
  };

  const handleEdit = (event: React.MouseEvent, consultation: Consultation) => {
    event.stopPropagation();
    onConsultationClick(consultation);
  };

  const handleCancel = (event: React.MouseEvent, consultation: Consultation) => {
    event.stopPropagation();
    setCancelConfirmModal({ isOpen: true, consultation });
  };

  const handleConfirmCancel = async () => {
    if (!cancelConfirmModal.consultation) return;

    setIsCancelling(true);
    try {
      await cancelConsultation(cancelConfirmModal.consultation.id);
      setCancelConfirmModal({ isOpen: false, consultation: null });

      if (onConsultationCancelled) {
        onConsultationCancelled();
      }
    } catch (err) {
      const apiError = err instanceof ApiError ? err : new ApiError(500, 'Erro desconhecido');
      setError({
        isOpen: true,
        message: apiError.detail,
        details: apiError.errorData?.errors,
      });
      setCancelConfirmModal({ isOpen: false, consultation: null });
    } finally {
      setIsCancelling(false);
    }
  };

  const handleCloseCancelModal = () => {
    setCancelConfirmModal({ isOpen: false, consultation: null });
  };

  const handleFirstPage = () => {
    if (onPageChange && pagination) {
      onPageChange(0);
    }
  };

  const handlePreviousPage = () => {
    if (onPageChange && pagination && pagination.page > 0) {
      onPageChange(pagination.page - 1);
    }
  };

  const handleNextPage = () => {
    if (onPageChange && pagination && pagination.page < pagination.totalPages - 1) {
      onPageChange(pagination.page + 1);
    }
  };

  const handleLastPage = () => {
    if (onPageChange && pagination && pagination.totalPages > 0) {
      onPageChange(pagination.totalPages - 1);
    }
  };

  const handleSizeChange = (newSize: number) => {
    if (onSizeChange) {
      onSizeChange(newSize);
    }
  };

  return (
    <div className="consultation-list">
      <div className="consultation-list-header">
        <h2>Consultas Veterinárias ({totalElements})</h2>
      </div>
      <div className="consultation-table-container">
        <table className="consultation-table">
          <thead>
            <tr>
              <th className="col-id">ID</th>
              <th className="col-animal">Animal</th>
              <th className="col-owner">Proprietário</th>
              <th className="col-date">Data</th>
              <th className="col-veterinarian">Veterinário</th>
              <th className="col-reason">Motivo</th>
              <th className="col-status">Status</th>
              <th className="col-actions">Ações</th>
            </tr>
          </thead>
          <tbody>
            {consultations.map((consultation) => (
              <tr
                key={consultation.id}
                className="consultation-row"
                onClick={() => onConsultationClick(consultation)}
              >
                <td className="col-id">{consultation.id}</td>
                <td className="col-animal">
                  <span className="consultation-animal-badge">{consultation.animal.name}</span>
                </td>
                <td className="col-owner">{consultation.animal.ownerName}</td>
                <td className="col-date">{formatDate(consultation.consultationDate)}</td>
                <td className="col-veterinarian">{consultation.veterinarianName}</td>
                <td className="col-reason">{consultation.reason}</td>
                <td className="col-status">
                  <span className={`status-badge ${getStatusClass(consultation.status)}`}>
                    {formatStatus(consultation.status)}
                  </span>
                </td>
                <td className="col-actions">
                  <div className="action-buttons">
                    <button
                      className="btn-action btn-edit"
                      onClick={(e) => handleEdit(e, consultation)}
                      title="Ver Detalhes"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                      </svg>
                    </button>
                    <button
                      className="btn-action btn-delete"
                      onClick={(e) => handleCancel(e, consultation)}
                      title="Cancelar"
                      disabled={consultation.status === 'CANCELLED' || consultation.status === 'COMPLETED'}
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

      {pagination && pagination.totalPages > 0 && (
        <div className="consultation-pagination">
          <PaginationControls 
            currentPage={pagination.page}
            totalPages={pagination.totalPages}
            totalElements={pagination.totalElements}
            onFirstPage={handleFirstPage}
            onPreviousPage={handlePreviousPage}
            onNextPage={handleNextPage}
            onLastPage={handleLastPage}
            disabled={loading}
            showFirstLastButtons={true}
          />
          <PageSizeSelector 
            currentSize={pagination.size}
            onChange={handleSizeChange}
            disabled={loading}
          />
        </div>
      )}

      <ConfirmDeleteModal
        isOpen={cancelConfirmModal.isOpen}
        title="Confirmar Cancelamento"
        message={
          cancelConfirmModal.consultation
            ? `Tem certeza que deseja cancelar a consulta de "${cancelConfirmModal.consultation.animal.name}" em ${formatDate(cancelConfirmModal.consultation.consultationDate)}?`
            : 'Tem certeza que deseja cancelar esta consulta?'
        }
        onConfirm={handleConfirmCancel}
        onCancel={handleCloseCancelModal}
        isLoading={isCancelling}
      />

      <ErrorModal
        isOpen={error.isOpen}
        title="Erro ao Cancelar Consulta"
        message={error.message}
        onClose={() => setError({ isOpen: false, message: '' })}
        details={error.details}
      />
    </div>
  );
}
