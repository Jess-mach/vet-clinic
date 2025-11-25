import { useState, useEffect } from 'react';
import type { FormEvent } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { createAnimal, updateAnimal, ApiError } from '../services/api';
import type { AnimalRequest, Animal } from '../types/animal';
import { ErrorModal } from './ErrorModal';
import { AnimalConsultationHistoryModal } from './AnimalConsultationHistoryModal';
import './CreateAnimal.css';

export function CreateAnimal() {
  const navigate = useNavigate();
  const location = useLocation();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [editingAnimalId, setEditingAnimalId] = useState<number | null>(null);
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorModalTitle, setErrorModalTitle] = useState('Erro');
  const [errorModalMessage, setErrorModalMessage] = useState('');
  const [errorModalDetails, setErrorModalDetails] = useState<Record<string, string>>({});
  const [consultationHistoryModalOpen, setConsultationHistoryModalOpen] = useState(false);

  const [formData, setFormData] = useState<AnimalRequest>({
    name: '',
    species: '',
    breed: null,
    gender: '',
    birthDate: null,
    color: null,
    weight: null,
    microchipNumber: null,
    ownerName: '',
    ownerPhone: null,
    ownerEmail: null,
  });

  // Detect if we're in edit mode and load the animal data
  useEffect(() => {
    const state = location.state as { animal?: Animal } | null;
    if (state?.animal) {
      setIsEditMode(true);
      setEditingAnimalId(state.animal.id);
      setFormData({
        name: state.animal.name,
        species: state.animal.species,
        breed: state.animal.breed,
        gender: state.animal.gender,
        birthDate: state.animal.birthDate,
        color: state.animal.color,
        weight: state.animal.weight,
        microchipNumber: state.animal.microchipNumber,
        ownerName: state.animal.ownerName,
        ownerPhone: state.animal.ownerPhone,
        ownerEmail: state.animal.ownerEmail,
      });
    }
  }, [location.state]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    
    if (value === '') {
      setFormData(prev => ({ ...prev, [name]: null }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
    
    // Clear field error when user starts typing
    if (fieldErrors[name]) {
      setFieldErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
    setGeneralError(null);
  };

  const handleWeightChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (value === '') {
      setFormData(prev => ({ ...prev, weight: null }));
    } else {
      const numValue = parseFloat(value);
      if (!isNaN(numValue)) {
        setFormData(prev => ({ ...prev, weight: numValue }));
      }
    }
    
    if (fieldErrors.weight) {
      setFieldErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors.weight;
        return newErrors;
      });
    }
  };

  const validateForm = (): boolean => {
    const errors: Record<string, string> = {};

    // Required fields
    if (!formData.name || formData.name.trim() === '') {
      errors.name = 'Nome é obrigatório';
    } else if (formData.name.length > 100) {
      errors.name = 'Nome deve ter no máximo 100 caracteres';
    }

    if (!formData.species || formData.species.trim() === '') {
      errors.species = 'Espécie é obrigatória';
    } else if (formData.species.length > 50) {
      errors.species = 'Espécie deve ter no máximo 50 caracteres';
    }

    if (!formData.gender || formData.gender.trim() === '') {
      errors.gender = 'Gênero é obrigatório';
    } else if (formData.gender.length > 20) {
      errors.gender = 'Gênero deve ter no máximo 20 caracteres';
    }

    if (!formData.ownerName || formData.ownerName.trim() === '') {
      errors.ownerName = 'Nome do dono é obrigatório';
    } else if (formData.ownerName.length > 100) {
      errors.ownerName = 'Nome do dono deve ter no máximo 100 caracteres';
    }

    // Optional field validations
    if (formData.breed && formData.breed.length > 100) {
      errors.breed = 'Raça deve ter no máximo 100 caracteres';
    }

    if (formData.color && formData.color.length > 50) {
      errors.color = 'Cor deve ter no máximo 50 caracteres';
    }

    if (formData.microchipNumber && formData.microchipNumber.length > 50) {
      errors.microchipNumber = 'Número do microchip deve ter no máximo 50 caracteres';
    }

    if (formData.ownerPhone && formData.ownerPhone.length > 20) {
      errors.ownerPhone = 'Telefone deve ter no máximo 20 caracteres';
    } else if (formData.ownerPhone) {
      const phonePattern = /^[\d\s\-\(\)\+]+$/;
      if (!phonePattern.test(formData.ownerPhone)) {
        errors.ownerPhone = 'Telefone deve conter apenas números, espaços, hífens, parênteses e +';
      }
    }

    if (formData.ownerEmail) {
      if (formData.ownerEmail.length > 100) {
        errors.ownerEmail = 'Email deve ter no máximo 100 caracteres';
      } else {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(formData.ownerEmail)) {
          errors.ownerEmail = 'Email deve ter um formato válido';
        }
      }
    }

    if (formData.weight !== null && formData.weight !== undefined) {
      if (formData.weight < 0.01 || formData.weight > 999.99) {
        errors.weight = 'Peso deve estar entre 0.01 e 999.99 kg';
      }
      const weightStr = formData.weight.toString();
      const parts = weightStr.split('.');
      if (parts[0].length > 3) {
        errors.weight = 'Peso deve ter no máximo 3 dígitos inteiros';
      }
      if (parts[1] && parts[1].length > 2) {
        errors.weight = 'Peso deve ter no máximo 2 casas decimais';
      }
    }

    if (formData.birthDate) {
      const date = new Date(formData.birthDate);
      const today = new Date();
      today.setHours(23, 59, 59, 999);
      if (date > today) {
        errors.birthDate = 'Data de nascimento não pode ser futura';
      }
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
      // Prepare data - convert empty strings to null for optional fields
      const requestData: AnimalRequest = {
        name: formData.name.trim(),
        species: formData.species.trim(),
        gender: formData.gender.trim(),
        ownerName: formData.ownerName.trim(),
        breed: formData.breed?.trim() || null,
        color: formData.color?.trim() || null,
        microchipNumber: formData.microchipNumber?.trim() || null,
        ownerPhone: formData.ownerPhone?.trim() || null,
        ownerEmail: formData.ownerEmail?.trim() || null,
        birthDate: formData.birthDate || null,
        weight: formData.weight || null,
      };

      if (isEditMode && editingAnimalId) {
        // Update existing animal
        await updateAnimal(editingAnimalId, requestData);
        setSuccess(true);
        
        // Redirect after 2 seconds
        setTimeout(() => {
          navigate('/pets');
        }, 2000);
      } else {
        // Create new animal
        await createAnimal(requestData);
        setSuccess(true);
        
        // Reset form after 2 seconds and redirect
        setTimeout(() => {
          navigate('/');
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
          // Animal not found (edit mode only)
          showErrorModal(
            'Animal Não Encontrado',
            error.detail || 'O animal que você está tentando editar não existe mais.'
          );
        } else if (error.status === 422) {
          // Business rule violation
          showErrorModal(
            'Erro na Operação',
            error.detail || 'Não foi possível salvar o animal. Tente novamente.'
          );
        } else {
          showErrorModal(
            'Erro ao Salvar',
            error.detail || 'Erro inesperado ao salvar o animal. Tente novamente.'
          );
        }
      } else {
        showErrorModal(
          'Erro',
          'Erro inesperado ao salvar o animal. Tente novamente.'
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/');
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

  return (
    <div className="create-animal-container">
      <div className="create-animal-content">
        <div className="create-animal-header">
          <div className="create-animal-header-content">
            <div>
              <h1>{isEditMode ? '✏️ Editar Pet' : 'Cadastre seu Pet'}</h1>
              <p>
                {isEditMode
                  ? 'Atualize os dados do animal abaixo'
                  : 'Preencha os dados abaixo para cadastrar um novo animal na clínica'}
              </p>
            </div>
            {isEditMode && editingAnimalId && (
              <button
                type="button"
                className="btn btn-secondary create-animal-history-btn"
                onClick={() => setConsultationHistoryModalOpen(true)}
                disabled={loading}
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
            )}
          </div>
        </div>

        {success && (
          <div className="create-animal-success">
            <p>
              ✓ Animal {isEditMode ? 'atualizado' : 'cadastrado'} com sucesso! Redirecionando...
            </p>
          </div>
        )}

        {generalError && (
          <div className="create-animal-error">
            <p>{generalError}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="create-animal-form">
          <section className="form-section">
            <h2>Informações do Animal</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="name">
                  Nome do Animal <span className="required">*</span>
                </label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  maxLength={100}
                  required
                  disabled={loading}
                />
                {fieldErrors.name && (
                  <span className="field-error">{fieldErrors.name}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="species">
                  Espécie <span className="required">*</span>
                </label>
                <select
                  id="species"
                  name="species"
                  value={formData.species}
                  onChange={handleChange}
                  required
                  disabled={loading}
                >
                  <option value="">Selecione...</option>
                  <option value="Dog">Cão</option>
                  <option value="Cat">Gato</option>
                  <option value="Bird">Ave</option>
                  <option value="Rabbit">Coelho</option>
                  <option value="Other">Outro</option>
                </select>
                {fieldErrors.species && (
                  <span className="field-error">{fieldErrors.species}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="breed">Raça</label>
                <input
                  type="text"
                  id="breed"
                  name="breed"
                  value={formData.breed || ''}
                  onChange={handleChange}
                  maxLength={100}
                  disabled={loading}
                />
                {fieldErrors.breed && (
                  <span className="field-error">{fieldErrors.breed}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="gender">
                  Gênero <span className="required">*</span>
                </label>
                <select
                  id="gender"
                  name="gender"
                  value={formData.gender}
                  onChange={handleChange}
                  required
                  disabled={loading}
                >
                  <option value="">Selecione...</option>
                  <option value="Male">Macho</option>
                  <option value="Female">Fêmea</option>
                  <option value="Neutered">Castrado</option>
                  <option value="Spayed">Esterilizado</option>
                </select>
                {fieldErrors.gender && (
                  <span className="field-error">{fieldErrors.gender}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="birthDate">Data de Nascimento</label>
                <input
                  type="date"
                  id="birthDate"
                  name="birthDate"
                  value={formData.birthDate || ''}
                  onChange={handleChange}
                  max={new Date().toISOString().split('T')[0]}
                  disabled={loading}
                />
                {fieldErrors.birthDate && (
                  <span className="field-error">{fieldErrors.birthDate}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="color">Cor</label>
                <input
                  type="text"
                  id="color"
                  name="color"
                  value={formData.color || ''}
                  onChange={handleChange}
                  maxLength={50}
                  disabled={loading}
                />
                {fieldErrors.color && (
                  <span className="field-error">{fieldErrors.color}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="weight">Peso (kg)</label>
                <input
                  type="number"
                  id="weight"
                  name="weight"
                  value={formData.weight || ''}
                  onChange={handleWeightChange}
                  min="0.01"
                  max="999.99"
                  step="0.01"
                  disabled={loading}
                />
                {fieldErrors.weight && (
                  <span className="field-error">{fieldErrors.weight}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="microchipNumber">Número do Microchip</label>
                <input
                  type="text"
                  id="microchipNumber"
                  name="microchipNumber"
                  value={formData.microchipNumber || ''}
                  onChange={handleChange}
                  maxLength={50}
                  disabled={loading}
                />
                {fieldErrors.microchipNumber && (
                  <span className="field-error">{fieldErrors.microchipNumber}</span>
                )}
              </div>
            </div>
          </section>

          <section className="form-section">
            <h2>Informações do Dono</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="ownerName">
                  Nome do Dono <span className="required">*</span>
                </label>
                <input
                  type="text"
                  id="ownerName"
                  name="ownerName"
                  value={formData.ownerName}
                  onChange={handleChange}
                  maxLength={100}
                  required
                  disabled={loading}
                />
                {fieldErrors.ownerName && (
                  <span className="field-error">{fieldErrors.ownerName}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="ownerPhone">Telefone</label>
                <input
                  type="text"
                  id="ownerPhone"
                  name="ownerPhone"
                  value={formData.ownerPhone || ''}
                  onChange={handleChange}
                  maxLength={20}
                  disabled={loading}
                />
                {fieldErrors.ownerPhone && (
                  <span className="field-error">{fieldErrors.ownerPhone}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="ownerEmail">Email</label>
                <input
                  type="email"
                  id="ownerEmail"
                  name="ownerEmail"
                  value={formData.ownerEmail || ''}
                  onChange={handleChange}
                  maxLength={100}
                  disabled={loading}
                />
                {fieldErrors.ownerEmail && (
                  <span className="field-error">{fieldErrors.ownerEmail}</span>
                )}
              </div>
            </div>
          </section>

          <div className="form-actions">
            <button
              type="button"
              onClick={handleCancel}
              className="btn btn-primary" 
              disabled={loading}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading 
                ? isEditMode 
                  ? 'Atualizando...' 
                  : 'Cadastrando...' 
                : isEditMode 
                  ? 'Salvar' 
                  : 'Salvar'}
            </button>
          </div>
        </form>
      </div>

      <ErrorModal
        isOpen={errorModalOpen}
        title={errorModalTitle}
        message={errorModalMessage}
        details={errorModalDetails}
        onClose={closeErrorModal}
      />

      {isEditMode && editingAnimalId && (
        <AnimalConsultationHistoryModal
          isOpen={consultationHistoryModalOpen}
          animalId={editingAnimalId}
          animalName={formData.name || 'Animal'}
          onClose={() => setConsultationHistoryModalOpen(false)}
        />
      )}
    </div>
  );
}

