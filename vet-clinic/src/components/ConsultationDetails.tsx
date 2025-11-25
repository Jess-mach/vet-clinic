import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Consultation } from '../types/consultation';
import { getConsultationById, ApiError } from '../services/api';
import { printConsultationDetails } from './ConsultationDetailsPrint';
import { printConsultationRecipe } from './ConsultationRecipePrint';
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

  const handlePrintDetails = () => {
    if (consultation) {
      printConsultationDetails(consultation, (errorMessage) => {
        setError(errorMessage);
      });
    }
  };

  const handlePrintRecipe = () => {
    if (consultation) {
      printConsultationRecipe(consultation, (errorMessage) => {
        setError(errorMessage);
      });
    }
  };

  return (
    <div className="consultation-details-overlay" onClick={onClose}>
      <div className="consultation-details-modal" onClick={(e) => e.stopPropagation()}>
        <button className="consultation-details-close" onClick={onClose}>
          ×
        </button>

        {loading && (
          <div className="consultation-details-loading">
            <p>Carregando detalhes da consulta...</p>
          </div>
        )}

        {error && (
          <div className="consultation-details-error">
            <p>{error || 'Consulta não encontrada'}</p>
          </div>
        )}

        {consultation && !loading && !error && (
          <div className="consultation-details-content">

            <div className="consultation-details-header">
              <h2>{consultation.animal.name}</h2>
              <span className={`consultation-details-status ${getStatusBadgeClass(consultation.status)}`}>
                {consultation.status}
              </span>
            </div>

            <div className="consultation-details-sections">
              <section className="details-section">
                <h3>Informações Gerais</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Animal:</span>
                    <span className="detail-value">{consultation.animal.name}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Espécie:</span>
                    <span className="detail-value">{consultation.animal.species}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Raça:</span>
                    <span className="detail-value">{consultation.animal.breed || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Proprietário:</span>
                    <span className="detail-value">{consultation.animal.ownerName}</span>
                  </div>
                </div>
              </section>

              <section className="details-section">
                <h3>Dados da Consulta</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Data da Consulta:</span>
                    <span className="detail-value">{formatDate(consultation.consultationDate)}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Veterinário:</span>
                    <span className="detail-value">{consultation.veterinarianName}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Motivo:</span>
                    <span className="detail-value">{consultation.reason}</span>
                  </div>
                </div>
              </section>

              <section className="details-section">
                <h3>Descrição</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Descrição:</span>
                    <span className="detail-value">{consultation.description || 'Não informado'}</span>
                  </div>
                </div>
              </section>

              <section className="details-section">
                <h3>Diagnóstico e Tratamento</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Diagnóstico:</span>
                    <span className="detail-value">{consultation.diagnosis || 'Não informado'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Tratamento Prescrito:</span>
                    <span className="detail-value">{consultation.treatmentPrescribed || 'Não informado'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Observações:</span>
                    <span className="detail-value">{consultation.observations || 'Sem observações'}</span>
                  </div>
                </div>
              </section>

              {consultation.nextAppointmentDate && (
                <section className="details-section">
                  <h3>Próxima Consulta</h3>
                  <div className="details-grid">
                    <div className="detail-item">
                      <span className="detail-label">Data Agendada:</span>
                      <span className="detail-value">{formatDate(consultation.nextAppointmentDate)}</span>
                    </div>
                  </div>
                </section>
              )}

              <section className="details-section">
                <h3>Informações do Sistema</h3>
                <div className="details-grid">
                  <div className="detail-item">
                    <span className="detail-label">Criado em:</span>
                    <span className="detail-value">{formatDate(consultation.createdAt)}</span>
                  </div>
                  <div className="detail-item">
                    <span className="detail-label">Atualizado em:</span>
                    <span className="detail-value">{formatDate(consultation.updatedAt)}</span>
                  </div>
                </div>
              </section>
            </div>

            <div className="consultation-details-actions">
              <button
                type="button"
                className="btn btn-primary" 
                onClick={handlePrintDetails}
                title="Imprimir detalhes da consulta"
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
                  <path d="M6 9V2h12v7" />
                  <path d="M6 18h12v4H6z" />
                  <path d="M6 14h12a2 2 0 0 0 2-2v-3H4v3a2 2 0 0 0 2 2z" />
                </svg>
                Imprimir Detalhes
              </button>
              <button
                type="button"
                className="btn btn-primary" 
                onClick={handlePrintRecipe}
                title="Imprimir receita"
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
                  <polyline points="14 2 14 8 20 8" />
                  <line x1="16" y1="13" x2="8" y2="13" />
                  <line x1="16" y1="17" x2="8" y2="17" />
                  <polyline points="10 9 9 9 8 9" />
                </svg>
                Imprimir Receita
              </button>
              <button className="btn btn-primary" onClick={handleEdit}>
                ✏️ Editar
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

