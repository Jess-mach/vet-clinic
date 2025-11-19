import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { createConsultation,  updateConsultation, ApiError} from '../services/api';
import type { Consultation, ConsultationRequest } from '../types/consultation';
import type { Animal } from '../types/animal';
import { ErrorModal } from './ErrorModal';
import { AnimalSearchModal } from './AnimalSearchModal';
import './CreateConsultation.css';


export function CreateConsultation() {
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const veterinarianFromUrl = searchParams.get('veterinarian') || '';
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  const [errorModalDetails, setErrorModalDetails] = useState<Record<string, string>>({});
  const [animalSearchModalOpen, setAnimalSearchModalOpen] = useState(false);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [editingConsultationId, setEditingConsultationId] = useState<number | null>(null);
  const [consultation, setConsultation] = useState<Consultation | null>(null);

  const [formData, setFormData] = useState<ConsultationRequest>({
    animalId: 0,
    consultationDate: '',
    veterinarianName: veterinarianFromUrl, // Pré-preenche com o valor da URL
    reasonCode: 0,
    description: '',
    diagnosis: '',
    treatmentPrescribed: '',
    observations: '',
    nextAppointmentDate: '',
    status: 'SCHEDULED',
  });

  const CONSULTATION_REASON_OPTIONS = [
    { value: 1, label: 'Consulta com clinico geral' },
    { value: 2, label: 'Consulta com oftalmologista' },
    { value: 3, label: 'Consulta com cardiologista' },
    { value: 4, label: 'Consulta com ortopedista' },
    { value: 5, label: 'Consulta com neurologista' },
    { value: 6, label: 'Exames' },
    { value: 7, label: 'Exame de imagem (raio-x, ultrassom, etc.)' },
    { value: 8, label: 'Vacinação' },
    { value: 9, label: 'Cirurgia' },
    { value: 10, label: 'Retorno' },
    { value: 11, label: 'Emergência' },
  ];

  const VETERINARIAN_OPTIONS = [
    // General practitioner
    { value: 1, label: 'Dr. Amelia Rivers' },
    { value: 2, label: 'Dr. Noah Bennett' },
    { value: 3, label: 'Dr. Olivia Carter' },
    { value: 4, label: 'Dr. Ethan Walker' },
    { value: 5, label: 'Dr. Sophia Hayes' },
    { value: 6, label: 'Dr. Lucas Griffin' },
    { value: 7, label: 'Dr. Harper Collins' },
    { value: 8, label: 'Dr. Mason Clarke' },
    { value: 9, label: 'Dr. Isla Morgan' },
    { value: 10, label: 'Dr. Leo Harrison' },
    { value: 11, label: 'Dr. Aria Mitchell' },
    { value: 12, label: 'Dr. Daniel Brooks' },
    { value: 13, label: 'Dr. Chloe Parker' },
    { value: 14, label: 'Dr. Henry Coleman' },
    { value: 15, label: 'Dr. Avery Scott' },
  ];

  const getAutoVeterinarianByReason = (reasonCode: number): string | undefined => {
    switch (reasonCode) {
      // General practitioner and generic exams
      case 1: // Consulta com clinico geral
      case 6: // Exames
      case 7: // Exame de imagem
      case 8: // Vacinação
      case 10: // Retorno
        return 'Luna Lovegood';
      case 2: // Oftalmologista
        return 'Dra. Hermione Granger';
      case 3: // Cardiologista
        return 'Dr. Minerva McGonagall';
      case 4: // Ortopedista
        return 'Dr. Remus Lupin';
      case 5: // Neurologista
        return 'Dra. Albus Dumbledore';
      // Cirurgia (9) e Emergência (11) -> usuário escolhe manualmente
      case 9:
      case 11:
      default:
        return undefined;
    }
  };


  useEffect(() => {
    

    const state = location.state as { consultation?: Consultation } | null;


    if (state?.consultation) {
        setConsultation(state?.consultation);
        setIsEditMode(true);
        setEditingConsultationId(state.consultation.id);
        setSelectedAnimal(state.consultation.animal as Animal);
        setFormData({
          animalId: state.consultation.animal.id,
          consultationDate: toInputDateTime(state.consultation.consultationDate),
          veterinarianName: state.consultation.veterinarianId,
          reasonCode: (state.consultation.reasonCode ?? 0) as number,
          description: state.consultation.description || '',
          diagnosis: state.consultation.diagnosis || '',
          treatmentPrescribed: state.consultation.treatmentPrescribed || '',
          observations: state.consultation.observations || '',
          nextAppointmentDate: toInputDateTime(state.consultation.nextAppointmentDate ?? ''),
          status: state.consultation.status,
        });
      }
    }, [location.state]);
  
  const toInputDateTime = (isoDateTime: string | null | undefined): string => {
    if (!isoDateTime) return '';
    // Backend envia com segundos; input datetime-local espera até minutos
    return isoDateTime.substring(0, 16);
  };

  const formatDateTimeForBackend = (dateTime: string): string => {
    if (!dateTime) return '';
    if (dateTime.length === 16) {
      return `${dateTime}:00`;
    }
    return dateTime;
  };



  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;

    if (name === 'reasonCode') {
      const numericReasonCode = value === '' ? 0 : Number(value);

      setFormData(prev => {
        const updated = { ...prev, reasonCode: numericReasonCode };
        const autoVet = getAutoVeterinarianByReason(numericReasonCode);

        if (autoVet) {
          updated.veterinarianName = autoVet;
        }

        return updated;
      });

      if (fieldErrors.reasonCode) {
        setFieldErrors(prev => {
          const newErrors = { ...prev };
          delete newErrors.reasonCode;
          return newErrors;
        });
      }

      setGeneralError(null);
      return;
    }

    if (value === '') {
      setFormData(prev => ({ ...prev, [name]: undefined }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }

    if (fieldErrors[name]) {
      setFieldErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
    setGeneralError(null);
  };

  const handleSelectAnimal = (animal: Animal) => {
    setSelectedAnimal(animal);
    setFormData(prev => ({ ...prev, animalId: animal.id }));
    
    // Clear animal field error
    if (fieldErrors.animalId) {
      setFieldErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors.animalId;
        return newErrors;
      });
    }
  };

  const handleRemoveAnimal = () => {
    setSelectedAnimal(null);
    setFormData(prev => ({ ...prev, animalId: 0 }));
  };

  const validateForm = (): boolean => {
    const errors: Record<string, string> = {};

    // Required fields
    if (!selectedAnimal || formData.animalId <= 0) {
      errors.animalId = 'Selecione um animal';
    }

    if (!formData.consultationDate || formData.consultationDate.trim() === '') {
      errors.consultationDate = 'Data da consulta é obrigatória';
    }

    if (!formData.veterinarianName || formData.veterinarianName.trim() === '') {
      errors.veterinarianName = 'Nome do veterinário é obrigatório';
    } else if (formData.veterinarianName.length > 100) {
      errors.veterinarianName = 'Nome do veterinário deve ter no máximo 100 caracteres';
    }

    if (!formData.reasonCode || formData.reasonCode === 0) {
      errors.reasonCode = 'Motivo da consulta é obrigatório';
    } 

    // Optional field validations
    if (formData.description && formData.description.length > 5000) {
      errors.description = 'Descrição deve ter no máximo 5000 caracteres';
    }

    if (formData.diagnosis && formData.diagnosis.length > 255) {
      errors.diagnosis = 'Diagnóstico deve ter no máximo 255 caracteres';
    }

    if (formData.treatmentPrescribed && formData.treatmentPrescribed.length > 5000) {
      errors.treatmentPrescribed = 'Tratamento prescrito deve ter no máximo 5000 caracteres';
    }

    if (formData.observations && formData.observations.length > 5000) {
      errors.observations = 'Observações devem ter no máximo 5000 caracteres';
    }

    if (formData.status && formData.status.length > 20) {
      errors.status = 'Status deve ter no máximo 20 caracteres';
    }

    setFieldErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setGeneralError(null);
    setSuccess(false);

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      // Prepare data - formato ISO 8601 com segundos
      const formatDateTimeForBackend = (dateTime: string): string => {
        if (!dateTime) return '';
        // datetime-local retorna "YYYY-MM-DDTHH:mm"
        // Backend espera "YYYY-MM-DDTHH:mm:ss"
        if (dateTime.length === 16) {
          return dateTime + ':00';
        }
        return dateTime;
      };

      const requestData: ConsultationRequest = {
        animalId: formData.animalId,
        consultationDate: formatDateTimeForBackend(formData.consultationDate),
        veterinarianName: formData.veterinarianName.trim(),
        reasonCode: formData.reasonCode,
        description: formData.description?.trim() || undefined,
        diagnosis: formData.diagnosis?.trim() || undefined,
        treatmentPrescribed: formData.treatmentPrescribed?.trim() || undefined,
        observations: formData.observations?.trim() || undefined,
        nextAppointmentDate: formData.nextAppointmentDate 
          ? formatDateTimeForBackend(formData.nextAppointmentDate) 
          : undefined,
        status: formData.status as 'COMPLETED' | 'SCHEDULED' | 'CANCELLED' || undefined,
      };

      if (isEditMode && editingConsultationId) {
        // Update existing animal

        await updateConsultation(editingConsultationId, requestData);
        setSuccess(true);
        
        // Redirect after 2 seconds
        setTimeout(() => {
          navigate('/pets');
        }, 2000);
      } else {
        // Create new animal
        
          await createConsultation(requestData);
          setSuccess(true);
          
          // Redirect after 2 seconds
          setTimeout(() => {
            navigate('/consultas');
          }, 2000);

      }

    } catch (error) {
      if (error instanceof ApiError) {
        if (error.status === 400 && error.errorData?.errors) {
          // Validation errors from backend
          setFieldErrors(error.errorData.errors);
          showErrorModal(
            'Erros de Validação',
            'Por favor, corrija os erros no formulário',
            error.errorData.errors
          );
        } else if (error.status === 404) {
          // Animal not found
          showErrorModal(
            'Animal Não Encontrado',
            error.detail || 'O animal selecionado não foi encontrado ou não está ativo.'
          );
        } else if (error.status === 422) {
          // Business rule violation
          showErrorModal(
            'Erro na Operação',
            error.detail || 'Não foi possível criar a consulta. Tente novamente.'
          );
        } else {
          showErrorModal(
            'Erro ao Salvar',
            error.detail || 'Erro inesperado ao criar a consulta. Tente novamente.'
          );
        }
      } else {
        showErrorModal(
          'Erro',
          'Erro inesperado ao criar a consulta. Tente novamente.'
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/consultas');
  };

  const showErrorModal = (
    title: string,
    message: string,
    details?: Record<string, string>
  ) => {
    setErrorModalTitle(title);
    setErrorModalMessage(message);
    setErrorModalDetails(details || {});
    setErrorModalOpen(true);
  };

  const closeErrorModal = () => {
    setErrorModalOpen(false);
  };

  const handleCreateNew = () => {
    navigate('/cadastrar-animal');
  };

  // Get current date and time in format for datetime-local input
  const getCurrentDateTime = (): string => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  };


  return (
    <div className="create-consultation-container">
      <div className="create-consultation-content">
        <div className="create-consultation-header">
          <h1>{isEditMode ? '✏️ Editar Consulta' : '📅 Agendar Consulta'} </h1>
          <p>Preencha os dados abaixo para agendar uma nova consulta</p>
        </div>

        {success && (
          <div className="create-consultation-success">
            <p>✓ Consulta agendada com sucesso! Redirecionando...</p>
          </div>
        )}

        {generalError && (
          <div className="create-consultation-error">
            <p>{generalError}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="create-consultation-form">
          {/* Animal Selection */}
          <section className="form-section">
            <div className="form-group">
              <label>
                Selecione o Pet <span className="required">*</span>
              </label>
              {!selectedAnimal ? (
                <div className="animal-select-buttons">
                  <button
                    type="button"
                    className="animal-select-btn"
                    onClick={() => setAnimalSearchModalOpen(true)}
                    disabled={loading}
                  >
                    <span className="animal-select-icon">🔍</span>
                    <span>Buscar Pet</span>
                  </button>
                  <button className="pets-page-create-btn btn btn-gradient" onClick={handleCreateNew}>
                    Cadastrar novo pet
                  </button>
                </div>
              ) : (
                <div className="selected-animal-card">
                  <div className="selected-animal-icon">🐾</div>
                  <div className="selected-animal-info">
                    <h3>{selectedAnimal.name}</h3>
                    <p className="selected-animal-species">
                      {selectedAnimal.species === 'Dog' ? '🐕 Cão' : 
                       selectedAnimal.species === 'Cat' ? '🐈 Gato' : 
                       selectedAnimal.species === 'Bird' ? '🦜 Ave' : 
                       selectedAnimal.species === 'Rabbit' ? '🐰 Coelho' : 
                       '🐾 ' + selectedAnimal.species}
                      {selectedAnimal.breed && ` - ${selectedAnimal.breed}`}
                    </p>
                    <p className="selected-animal-owner">👤 {selectedAnimal.ownerName}</p>
                  </div>
                 {!isEditMode && <button
                    type="button"
                    className="remove-animal-btn"
                    onClick={handleRemoveAnimal}
                    disabled={loading || (isEditMode)}
                    title="Remover animal selecionado"
                  >
                    ×
                  </button>}
                </div>
              )}
              {fieldErrors.animalId && (
                <span className="field-error">{fieldErrors.animalId}</span>
              )}
            </div>
          </section>

          {/* Consultation Information */}
          <section className="form-section">
            <h2>Informações da Consulta</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="consultationDate">
                  Data e Hora da Consulta <span className="required">*</span>
                </label>
                <input
                  type="datetime-local"
                  id="consultationDate"
                  name="consultationDate"
                  value={formData.consultationDate}
                  onChange={handleChange}
                  min={getCurrentDateTime()}
                  required
                  disabled={
                    consultation?.status === 'CANCELLED' ||
                    consultation?.status === 'COMPLETED'
                  }
                />
                {fieldErrors.consultationDate && (
                  <span className="field-error">{fieldErrors.consultationDate}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="veterinarianName">
                  Nome do Veterinário <span className="required">*</span>
                </label>
                <select
                  id="veterinarianName"
                  name="veterinarianName"
                  value={formData.veterinarianName}
                  onChange={handleChange}
                  required
                  disabled={loading}
                >
                  <option value="">Selecione o veterinário</option>
                  {VETERINARIAN_OPTIONS.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
                {fieldErrors.veterinarianName && (
                  <span className="field-error">{fieldErrors.veterinarianName}</span>
                )}
              </div>

              <div className="form-group form-group-full">
                <label htmlFor="reasonCode">
                  Tipo de Consulta <span className="required">*</span>
                </label>
                <select
                  id="reasonCode"
                  name="reasonCode"
                  value={formData.reasonCode}
                  onChange={handleChange}
                  required
                  disabled={loading}
                >

                <option value="">Selecione um tipo</option>
                {CONSULTATION_REASON_OPTIONS.map((option) => (
                  <option key={option.value} value={option.value}>{option.label}</option>
                ))}
                </select>

                {fieldErrors.reasonCode && (
                  <span className="field-error">{fieldErrors.reasonCode}</span>
                )}
              </div>

              <div className="form-group form-group-full">
                <label htmlFor="description">Descrição da Consulta</label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  maxLength={5000}
                  rows={4}
                  placeholder="Descreva os sintomas, histórico ou informações adicionais..."
                  disabled={loading}
                />
                {fieldErrors.description && (
                  <span className="field-error">{fieldErrors.description}</span>
                )}
              </div>
            </div>
          </section>

          {/* Clinical Information */}
          <section className="form-section">
            <h2>Informações Clínicas</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="diagnosis">Diagnóstico</label>
                <input
                  type="text"
                  id="diagnosis"
                  name="diagnosis"
                  value={formData.diagnosis}
                  onChange={handleChange}
                  maxLength={255}
                  placeholder="Diagnóstico (se aplicável)"
                  disabled={!isEditMode}
                />
                {fieldErrors.diagnosis && (
                  <span className="field-error">{fieldErrors.diagnosis}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="status">Status</label>
                <select
                  id="status"
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                  disabled={!isEditMode ||
                    (consultation?.status === 'CANCELLED' ||
                    consultation?.status === 'COMPLETED')
                  }
                >
                  <option value="SCHEDULED">Agendada</option>
                  <option value="COMPLETED">Concluída</option>
                  <option value="CANCELLED">Cancelada</option>
                </select>
                {fieldErrors.status && (
                  <span className="field-error">{fieldErrors.status}</span>
                )}
              </div>

              <div className="form-group form-group-full">
                <label htmlFor="treatmentPrescribed">Tratamento Prescrito</label>
                <textarea
                  id="treatmentPrescribed"
                  name="treatmentPrescribed"
                  value={formData.treatmentPrescribed}
                  onChange={handleChange}
                  maxLength={5000}
                  rows={4}
                  placeholder="Descreva o tratamento prescrito, medicações, dosagens..."
                  disabled={!isEditMode}
                />
                {fieldErrors.treatmentPrescribed && (
                  <span className="field-error">{fieldErrors.treatmentPrescribed}</span>
                )}
              </div>

              <div className="form-group form-group-full">
                <label htmlFor="observations">Observações</label>
                <textarea
                  id="observations"
                  name="observations"
                  value={formData.observations}
                  onChange={handleChange}
                  maxLength={5000}
                  rows={4}
                  placeholder="Observações adicionais sobre a consulta..."
                  disabled={!isEditMode}
                />
                {fieldErrors.observations && (
                  <span className="field-error">{fieldErrors.observations}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="nextAppointmentDate">Próximo Agendamento</label>
                <input
                  type="datetime-local"
                  id="nextAppointmentDate"
                  name="nextAppointmentDate"
                  value={formData.nextAppointmentDate}
                  onChange={handleChange}
                  min={getCurrentDateTime()}
                  disabled={!isEditMode}
                />
                {fieldErrors.nextAppointmentDate && (
                  <span className="field-error">{fieldErrors.nextAppointmentDate}</span>
                )}
              </div>
            </div>
          </section>

          <div className="form-actions">
            <button
              type="button"
              onClick={handleCancel}
              className="btn btn-secondary"
              disabled={loading}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {isEditMode ? 'Salvar' : 'Agendar'}
            </button>
          </div>
        </form>
      </div>

      <AnimalSearchModal
        isOpen={animalSearchModalOpen}
        onClose={() => setAnimalSearchModalOpen(false)}
        onSelectAnimal={handleSelectAnimal}
      />

      <ErrorModal
        isOpen={errorModalOpen}
        title={errorModalTitle}
        message={errorModalMessage}
        details={errorModalDetails}
        onClose={closeErrorModal}
      />
    </div>
  );
}

