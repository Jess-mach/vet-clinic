import { useState } from 'react';
import type { Consultation } from '../types/consultation';
import { deleteConsultation, ApiError } from '../services/api';
import { ConfirmDeleteModal } from './ConfirmDeleteModal';
import { ErrorModal } from './ErrorModal';
import './ConsultationList.css';

interface ConsultationListProps {
  consultations: Consultation[];
  onConsultationClick: (consultation: Consultation) => void;
  loading?: boolean;
  onConsultationDeleted?: () => void;
}

export function ConsultationList({
  consultations,
  onConsultationClick,
  loading,
  onConsultationDeleted,
}: ConsultationListProps) {
  const [deleteConfirmModal, setDeleteConfirmModal] = useState<{
    isOpen: boolean;
    consultation: Consultation | null;
  }>({ isOpen: false, consultation: null });

  const [isDeleting, setIsDeleting] = useState(false);
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

  if (consultations.length === 0) {
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

  const handleDelete = (event: React.MouseEvent, consultation: Consultation) => {
    event.stopPropagation();
    setDeleteConfirmModal({ isOpen: true, consultation });
  };

  const handleConfirmDelete = async () => {
    if (!deleteConfirmModal.consultation) return;

    setIsDeleting(true);
    try {
      await deleteConsultation(deleteConfirmModal.consultation.id);
      setDeleteConfirmModal({ isOpen: false, consultation: null });

      if (onConsultationDeleted) {
        onConsultationDeleted();
      }
    } catch (err) {
      const apiError = err instanceof ApiError ? err : new ApiError(500, 'Erro desconhecido');
      setError({
        isOpen: true,
        message: apiError.detail,
        details: apiError.errorData?.errors,
      });
      setDeleteConfirmModal({ isOpen: false, consultation: null });
    } finally {
      setIsDeleting(false);
    }
  };

  const handleCancelDelete = () => {
    setDeleteConfirmModal({ isOpen: false, consultation: null });
  };

  return (
    <div className="consultation-list">
      <div className="consultation-list-header">
        <h2>Consultas Registradas ({consultations.length})</h2>
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
                      onClick={(e) => handleDelete(e, consultation)}
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
          deleteConfirmModal.consultation
            ? `Tem certeza que deseja deletar a consulta de "${deleteConfirmModal.consultation.animal.name}" em ${formatDate(deleteConfirmModal.consultation.consultationDate)}? Esta ação não pode ser desfeita.`
            : 'Tem certeza que deseja deletar esta consulta?'
        }
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
        isLoading={isDeleting}
      />

      <ErrorModal
        isOpen={error.isOpen}
        title="Erro ao Deletar Consulta"
        message={error.message}
        onClose={() => setError({ isOpen: false, message: '' })}
        details={error.details}
      />
    </div>
  );
}
