import { useState } from 'react';
import type { Consultation } from '../types/consultation';
import { ConfirmDeleteModal } from './ConfirmDeleteModal';
import './ConsultationList.css';

interface ConsultationListProps {
  consultations: Consultation[];
  onConsultationClick: (consultation: Consultation) => void;
  loading: boolean;
  onConsultationDeleted: () => void;
}

export function ConsultationList({
  consultations,
  onConsultationClick,
  loading,
  onConsultationDeleted,
}: ConsultationListProps) {
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [selectedConsultationId, setSelectedConsultationId] = useState<number | null>(null);

  const handleDeleteClick = (e: React.MouseEvent, id: number) => {
    e.stopPropagation();
    setSelectedConsultationId(id);
    setDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (selectedConsultationId === null) return;

    try {
      const { deleteConsultation } = await import('../services/api');
      await deleteConsultation(selectedConsultationId);
      setDeleteModalOpen(false);
      setSelectedConsultationId(null);
      onConsultationDeleted();
    } catch (error) {
      console.error('Error deleting consultation:', error);
      setDeleteModalOpen(false);
      setSelectedConsultationId(null);
    }
  };

  const formatDate = (dateString: string) => {
    try {
      return new Date(dateString).toLocaleDateString('pt-BR', {
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

  const getStatusBadgeClass = (status: string) => {
    const statusLower = status.toLowerCase();
    if (statusLower === 'completed') return 'status-completed';
    if (statusLower === 'scheduled') return 'status-scheduled';
    if (statusLower === 'cancelled') return 'status-cancelled';
    return 'status-default';
  };

  if (loading) {
    return (
      <div className="consultation-list">
        <div className="consultation-list-loading">
          <div className="spinner"></div>
          <p>Carregando consultas...</p>
        </div>
      </div>
    );
  }

  if (consultations.length === 0) {
    return (
      <div className="consultation-list">
        <div className="consultation-list-empty">
          <p className="empty-icon">📋</p>
          <p className="empty-title">Nenhuma consulta encontrada</p>
          <p className="empty-description">Comece adicionando uma nova consulta ao sistema</p>
        </div>
      </div>
    );
  }

  return (
    <>
      <div className="consultation-list">
        {consultations.map((consultation) => (
          <div
            key={consultation.id}
            className="consultation-card"
            onClick={() => onConsultationClick(consultation)}
          >
            <div className="consultation-card-header">
              <div className="consultation-info">
                <h3 className="consultation-animal-name">{consultation.animalName}</h3>
                <p className="consultation-owner">👤 {consultation.ownerName}</p>
              </div>
              <span className={`consultation-status ${getStatusBadgeClass(consultation.status)}`}>
                {consultation.status}
              </span>
            </div>

            <div className="consultation-details">
              <div className="consultation-detail-item">
                <span className="detail-label">📅 Data:</span>
                <span className="detail-value">{formatDate(consultation.consultationDate)}</span>
              </div>
              <div className="consultation-detail-item">
                <span className="detail-label">🩺 Tipo:</span>
                <span className="detail-value">{consultation.consultationType}</span>
              </div>
              <div className="consultation-detail-item">
                <span className="detail-label">👨‍⚕️ Veterinário:</span>
                <span className="detail-value">{consultation.veterinarian}</span>
              </div>
              <div className="consultation-detail-item">
                <span className="detail-label">📝 Motivo:</span>
                <span className="detail-value">{consultation.reason}</span>
              </div>
            </div>

            <div className="consultation-card-actions">
              <button
                className="btn btn-icon"
                onClick={(e) => {
                  e.stopPropagation();
                  onConsultationClick(consultation);
                }}
                title="Ver detalhes"
              >
                👁️
              </button>
              <button
                className="btn btn-icon btn-icon-danger"
                onClick={(e) => handleDeleteClick(e, consultation.id)}
                title="Deletar consulta"
              >
                🗑️
              </button>
            </div>
          </div>
        ))}
      </div>

      <ConfirmDeleteModal
        isOpen={deleteModalOpen}
        title="Confirmar exclusão"
        message="Tem certeza que deseja deletar esta consulta? Esta ação não pode ser desfeita."
        onConfirm={handleConfirmDelete}
        onCancel={() => {
          setDeleteModalOpen(false);
          setSelectedConsultationId(null);
        }}
      />
    </>
  );
}

