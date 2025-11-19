import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Consultation } from '../types/consultation';
import { getConsultationById, ApiError } from '../services/api';
import './ConsultationDetails.css';

interface ConsultationDetailsProps {
  consultationId: number;
  onClose: () => void;
}

export function ConsultationDetails({ consultationId, onClose }: ConsultationDetailsProps) {
  const [consultation, setConsultation] = useState<Consultation | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    const loadConsultation = async () => {
      try {
        setLoading(true);
        const data = await getConsultationById(consultationId);
        setConsultation(data);
        setError(null);
      } catch (err) {
        if (err instanceof ApiError) {
          setError(err.detail || 'Erro ao carregar detalhes da consulta');
        } else {
          setError('Erro ao carregar detalhes da consulta');
        }
      } finally {
        setLoading(false);
      }
    };

    loadConsultation();
  }, [consultationId]);

  const formatDate = (dateString: string | null) => {
    if (!dateString) return 'N/A';
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

  const handleEdit = () => {
    if (consultation) {
      navigate(`/consultas/${consultation.id}/editar`, { state: { consultation: consultation } });
    }
  };

  if (loading) {
    return (
      <div className="consultation-details">
        <div className="consultation-details-container">
          <button className="btn btn-icon consultation-close-btn" onClick={onClose} title="Fechar">
            ✕
          </button>
          <div className="consultation-details-loading">
            <div className="spinner"></div>
            <p>Carregando detalhes da consulta...</p>
          </div>
        </div>
      </div>
    );
  }

  if (error || !consultation) {
    return (
      <div className="consultation-details">
        <div className="consultation-details-container">
          <button className="btn btn-icon consultation-close-btn" onClick={onClose} title="Fechar">
            ✕
          </button>
          <div className="consultation-details-error">
            <p className="error-icon">⚠️</p>
            <p className="error-message">{error || 'Consulta não encontrada'}</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="consultation-details">
      <div className="consultation-details-container">
        <button className="btn btn-icon consultation-close-btn" onClick={onClose} title="Fechar">
          ✕
        </button>

        <div className="consultation-details-header">
          <div>
            <h2 className="consultation-details-title">{consultation.animal.name}</h2>
            <p className="consultation-details-subtitle">Consulta Veterinária</p>
          </div>
          <span className={`consultation-status-large ${getStatusBadgeClass(consultation.status)}`}>
            {consultation.status}
          </span>
        </div>

        <div className="consultation-details-content">
          <div className="details-section">
            <h3 className="section-title">📋 Informações Gerais</h3>
            <div className="details-grid">
              <div className="detail-row">
                <span className="detail-label">Animal:</span>
                <span className="detail-value">{consultation.animal.name}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Espécie:</span>
                <span className="detail-value">{consultation.animal.species}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Raça:</span>
                <span className="detail-value">{consultation.animal.breed || 'N/A'}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Proprietário:</span>
                <span className="detail-value">{consultation.animal.ownerName}</span>
              </div>
            </div>
          </div>

          <div className="details-section">
            <h3 className="section-title">🩺 Dados da Consulta</h3>
            <div className="details-grid">
              <div className="detail-row">
                <span className="detail-label">Data da Consulta:</span>
                <span className="detail-value">{formatDate(consultation.consultationDate)}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Veterinário:</span>
                <span className="detail-value">{consultation.veterinarianName}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Motivo:</span>
                <span className="detail-value">{consultation.reason}</span>
              </div>
            </div>
          </div>

          <div className="details-section">
            <h3 className="section-title">📝 Descrição</h3>
            <div className="details-grid">
              <div className="detail-row full-width">
                <span className="detail-label">Descrição:</span>
                <span className="detail-value">{consultation.description || 'Não informado'}</span>
              </div>
            </div>
          </div>

          <div className="details-section">
            <h3 className="section-title">🩺 Diagnóstico e Tratamento</h3>
            <div className="details-grid">
              <div className="detail-row full-width">
                <span className="detail-label">Diagnóstico:</span>
                <span className="detail-value">{consultation.diagnosis || 'Não informado'}</span>
              </div>
              <div className="detail-row full-width">
                <span className="detail-label">Tratamento Prescrito:</span>
                <span className="detail-value">{consultation.treatmentPrescribed || 'Não informado'}</span>
              </div>
              <div className="detail-row full-width">
                <span className="detail-label">Observações:</span>
                <span className="detail-value">{consultation.observations || 'Sem observações'}</span>
              </div>
            </div>
          </div>

          {consultation.nextAppointmentDate && (
            <div className="details-section">
              <h3 className="section-title">📅 Próxima Consulta</h3>
              <div className="details-grid">
                <div className="detail-row">
                  <span className="detail-label">Data Agendada:</span>
                  <span className="detail-value">{formatDate(consultation.nextAppointmentDate)}</span>
                </div>
              </div>
            </div>
          )}

          <div className="details-section">
            <h3 className="section-title">⏱️ Auditoria</h3>
            <div className="details-grid">
              <div className="detail-row">
                <span className="detail-label">Criado em:</span>
                <span className="detail-value">{formatDate(consultation.createdAt)}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Atualizado em:</span>
                <span className="detail-value">{formatDate(consultation.updatedAt)}</span>
              </div>
            </div>
          </div>
        </div>

        {/* Botão para ir para a página de agendamento/edição da consulta */}
        <div className="consultation-details-footer">
          <button
            className="btn btn-primary"
            onClick={handleEdit}
          >
            Editar consulta
          </button>
        </div>
      </div>
    </div>
  );
}

