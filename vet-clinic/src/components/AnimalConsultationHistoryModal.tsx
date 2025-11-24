import { useState, useEffect } from 'react';
import React from 'react';
import type { Consultation, ConsultationFilters } from '../types/consultation';
import { searchConsultations, cancelConsultation, ApiError } from '../services/api';
import { ErrorModal } from './ErrorModal';
import { ConfirmDeleteModal } from './ConfirmDeleteModal';
import { PageSizeSelector } from './PageSizeSelector';
import { PaginationControls } from './PaginationControls';
import { printConsultations } from './AnimalConsultationHistoryPrint';
import './AnimalConsultationHistoryModal.css';
import './ConsultationList.css';

interface AnimalConsultationHistoryModalProps {
  isOpen: boolean;
  animalId: number;
  animalName: string;
  onClose: () => void;
}

export function AnimalConsultationHistoryModal({
  isOpen,
  animalId,
  animalName,
  onClose,
}: AnimalConsultationHistoryModalProps) {
  const [consultations, setConsultations] = useState<Consultation[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  
  // Estado de paginação
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Estado para cancelamento
  const [cancelConfirmModal, setCancelConfirmModal] = useState<{
    isOpen: boolean;
    consultation: Consultation | null;
  }>({ isOpen: false, consultation: null });
  const [isCancelling, setIsCancelling] = useState(false);

  // Estado para controlar quais consultas estão expandidas
  const [expandedConsultations, setExpandedConsultations] = useState<Set<number>>(new Set());

  const showErrorModal = (title: string, message: string) => {
    setErrorModalTitle(title);
    setErrorModalMessage(message);
    setErrorModalOpen(true);
  };

  const loadConsultations = async (pageNum: number, pageSize: number) => {
    if (!animalId) return;
    
    try {
      setLoading(true);
      
      const filters: ConsultationFilters = {
        animalId,
        page: pageNum,
        size: pageSize,
        sort: 'consultationDate,desc',
      };
      
      const data = await searchConsultations(filters);
      setConsultations(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setPage(data.number);
      setSize(data.size);
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Carregar', err.detail || 'Erro ao carregar histórico de consultas. Tente novamente mais tarde.');
      } else {
        showErrorModal('Erro', 'Erro ao carregar histórico de consultas. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isOpen && animalId) {
      loadConsultations(0, 10);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen, animalId]);

  const handlePageChange = async (newPage: number) => {
    await loadConsultations(newPage, size);
  };

  const handleSizeChange = async (newSize: number) => {
    setSize(newSize);
    await loadConsultations(0, newSize);
  };

  const handleConsultationClick = (consultation: Consultation) => {
    // Alterna a expansão dos detalhes apenas para consultas concluídas
    if (consultation.status === 'COMPLETED') {
      setExpandedConsultations(prev => {
        const newSet = new Set(prev);
        if (newSet.has(consultation.id)) {
          newSet.delete(consultation.id);
        } else {
          newSet.add(consultation.id);
        }
        return newSet;
      });
    }
  };

  const isExpanded = (consultationId: number) => {
    return expandedConsultations.has(consultationId);
  };

  const handleCancel = (event: React.MouseEvent, consultation: Consultation) => {
    event.stopPropagation();
    event.preventDefault();
    setCancelConfirmModal({ isOpen: true, consultation });
  };

  const handleConfirmCancel = async () => {
    if (!cancelConfirmModal.consultation) return;

    setIsCancelling(true);
    try {
      await cancelConsultation(cancelConfirmModal.consultation.id);
      setCancelConfirmModal({ isOpen: false, consultation: null });
      loadConsultations(page, size);
    } catch (err) {
      const apiError = err instanceof ApiError ? err : new ApiError(500, 'Erro desconhecido');
      showErrorModal('Erro ao Cancelar', apiError.detail || 'Erro ao cancelar consulta.');
      setCancelConfirmModal({ isOpen: false, consultation: null });
    } finally {
      setIsCancelling(false);
    }
  };

  const handleCloseCancelModal = () => {
    setCancelConfirmModal({ isOpen: false, consultation: null });
  };

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

  const handleFirstPage = () => {
    if (page > 0) {
      handlePageChange(0);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      handlePageChange(page - 1);
    }
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      handlePageChange(page + 1);
    }
  };

  const handleLastPage = () => {
    if (totalPages > 0) {
      handlePageChange(totalPages - 1);
    }
  };

  const handlePrintAll = async () => {
    try {
      setLoading(true);
      // Buscar todas as consultas do animal para impressão
      const allFilters: ConsultationFilters = {
        animalId,
        page: 0,
        size: 10000, // Número grande para pegar todas
        sort: 'consultationDate,desc',
      };
      
      const data = await searchConsultations(allFilters);
      const allConsultations = data.content;
      
      if (allConsultations.length === 0) {
        showErrorModal('Impressão', 'Não há consultas para imprimir.');
        return;
      }
      
      printConsultations(allConsultations, (errorMessage) => {
        showErrorModal('Erro ao Imprimir', errorMessage);
      });
    } catch (err) {
      if (err instanceof ApiError) {
        showErrorModal('Erro ao Imprimir', err.detail || 'Erro ao carregar consultas para impressão.');
      } else {
        showErrorModal('Erro', 'Erro ao preparar impressão. Tente novamente mais tarde.');
      }
    } finally {
      setLoading(false);
    }
  };

  const closeErrorModal = () => {
    setErrorModalOpen(false);
  };

  if (!isOpen) {
    return null;
  }

  return (
    <>
      <div className="animal-consultation-history-modal-overlay" onClick={onClose}>
        <div className="animal-consultation-history-modal" onClick={(e) => e.stopPropagation()}>
          <div className="animal-consultation-history-modal-header">
            <h2>📋 Histórico de Consultas - {animalName}</h2>
            <button className="animal-consultation-history-modal-close" onClick={onClose}>
              ×
            </button>
          </div>

          <div className="animal-consultation-history-modal-content">
            {loading ? (
              <div className="consultation-list-loading">
                <p>Carregando consultas...</p>
              </div>
            ) : consultations.length === 0 ? (
              <div className="consultation-list-empty">
                <p>Nenhuma consulta encontrada.</p>
              </div>
            ) : (
              <>
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
                        <React.Fragment key={consultation.id}>
                          <tr
                            className={`consultation-row ${consultation.status === 'COMPLETED' ? 'consultation-row-expandable' : ''} ${isExpanded(consultation.id) ? 'consultation-row-expanded' : ''}`}
                            onClick={(e) => {
                              // Só expande se não clicar diretamente em um botão
                              const target = e.target as HTMLElement;
                              if (target.tagName !== 'BUTTON' && !target.closest('button')) {
                                handleConsultationClick(consultation);
                              }
                            }}
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
                                {consultation.status === 'COMPLETED' && (
                                  <span className="consultation-expand-icon" title="Clique para ver detalhes">
                                    <svg
                                      xmlns="http://www.w3.org/2000/svg"
                                      width="16"
                                      height="16"
                                      viewBox="0 0 24 24"
                                      fill="none"
                                      stroke="currentColor"
                                      strokeWidth="2"
                                      strokeLinecap="round"
                                      strokeLinejoin="round"
                                    >
                                      <polyline points="6 9 12 15 18 9"></polyline>
                                    </svg>
                                  </span>
                                )}
                                <button
                                  className="btn-action btn-delete"
                                  onClick={(e) => handleCancel(e, consultation)}
                                  title="Cancelar consulta"
                                  disabled={
                                    consultation.status === 'CANCELLED' ||
                                    consultation.status === 'COMPLETED'
                                  }
                                >
                                  ✖
                                </button>
                              </div>
                            </td>
                          </tr>
                          {consultation.status === 'COMPLETED' && isExpanded(consultation.id) && (
                            <tr className="consultation-details-row">
                              <td colSpan={8} className="consultation-details-cell">
                                <div className="consultation-details-content">
                                  {consultation.diagnosis && (
                                    <div className="consultation-details-section">
                                      <span className="consultation-details-label">🩺 Diagnóstico:</span>
                                      <span className="consultation-details-text">{consultation.diagnosis}</span>
                                    </div>
                                  )}
                                  {consultation.treatmentPrescribed && (
                                    <div className="consultation-details-section">
                                      <span className="consultation-details-label">💊 Tratamento:</span>
                                      <span className="consultation-details-text">{consultation.treatmentPrescribed}</span>
                                    </div>
                                  )}
                                  {consultation.observations && (
                                    <div className="consultation-details-section">
                                      <span className="consultation-details-label">📝 Observações:</span>
                                      <span className="consultation-details-text">{consultation.observations}</span>
                                    </div>
                                  )}
                                </div>
                              </td>
                            </tr>
                          )}
                        </React.Fragment>
                      ))}
                    </tbody>
                  </table>
                </div>

                {totalPages > 0 && (
                  <div className="consultation-pagination">
                    <PaginationControls 
                      currentPage={page}
                      totalPages={totalPages}
                      totalElements={totalElements}
                      onFirstPage={handleFirstPage}
                      onPreviousPage={handlePreviousPage}
                      onNextPage={handleNextPage}
                      onLastPage={handleLastPage}
                      disabled={loading}
                      showFirstLastButtons={true}
                    />
                    <PageSizeSelector 
                      currentSize={size}
                      onChange={handleSizeChange}
                      disabled={loading}
                    />
                  </div>
                )}
              </>
            )}
          </div>

          <div className="animal-consultation-history-modal-footer">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={handlePrintAll}
              disabled={loading || totalElements === 0}
              title="Imprimir histórico completo de consultas"
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
              Imprimir Histórico
            </button>
            <button className="btn btn-secondary" onClick={onClose}>
              Fechar
            </button>
          </div>
        </div>
      </div>

      <ErrorModal
        isOpen={errorModalOpen}
        title={errorModalTitle}
        message={errorModalMessage}
        onClose={closeErrorModal}
      />

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
    </>
  );
}

