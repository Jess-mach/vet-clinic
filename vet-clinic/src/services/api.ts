import type { Animal, AnimalFilters, AnimalRequest, ApiError as ApiErrorResponse } from '../types/animal';
import type { Consultation, ConsultationFilters, ConsultationRequest, ApiError as ConsultationApiErrorResponse } from '../types/consultation';

const API_BASE_URL = 'http://localhost:8080/api';

export class ApiError extends Error {
  constructor(
    public status: number,
    public detail: string,
    public errorData?: ApiErrorResponse
  ) {
    super(detail);
    this.name = 'ApiError';
  }
}

export async function searchAnimals(
  filters?: AnimalFilters
): Promise<Animal[]> {
  const params = new URLSearchParams();
  
  if (filters?.name) {
    params.append('name', filters.name);
  }
  if (filters?.species) {
    params.append('species', filters.species);
  }
  if (filters?.ownerName) {
    params.append('ownerName', filters.ownerName);
  }

  const url = `${API_BASE_URL}/animals${params.toString() ? `?${params.toString()}` : ''}`;
  
  try {
    const response = await fetch(url);
    
    if (!response.ok) {
      const error: ApiErrorResponse = await response.json();
      throw new ApiError(response.status, error.detail || 'Failed to search animals', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function getAnimalById(id: number): Promise<Animal> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/animals/${id}`;
  
  try {
    const response = await fetch(url);
    
    if (!response.ok) {
      if (response.status === 404) {
        throw new ApiError(404, 'Animal not found');
      }
      const error: ApiErrorResponse = await response.json();
      throw new ApiError(response.status, error.detail || 'Failed to get animal', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function createAnimal(animal: AnimalRequest): Promise<Animal> {
  const url = `${API_BASE_URL}/animals`;
  
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(animal),
    });
    
    if (!response.ok) {
      const error: ApiErrorResponse = await response.json();
      
      if (response.status === 400) {
        // Validation errors - includes errors object
        throw new ApiError(response.status, error.detail || 'Validation failed', error);
      }
      
      if (response.status === 422) {
        // Business rule violation
        throw new ApiError(response.status, error.detail || 'Business rule violation', error);
      }
      
      throw new ApiError(response.status, error.detail || 'Failed to create animal', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function updateAnimal(id: number, animal: AnimalRequest): Promise<Animal> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/animals/${id}`;
  
  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(animal),
    });
    
    if (!response.ok) {
      const error: ApiErrorResponse = await response.json();
      
      if (response.status === 400) {
        // Validation errors - includes errors object
        throw new ApiError(response.status, error.detail || 'Validation failed', error);
      }
      
      if (response.status === 404) {
        // Animal not found
        throw new ApiError(response.status, 'Animal not found', error);
      }
      
      if (response.status === 422) {
        // Business rule violation
        throw new ApiError(response.status, error.detail || 'Business rule violation', error);
      }
      
      throw new ApiError(response.status, error.detail || 'Failed to update animal', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function deleteAnimal(id: number): Promise<void> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/animals/${id}`;
  
  try {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (!response.ok) {
      const error: ApiErrorResponse = await response.json();
      
      if (response.status === 404) {
        throw new ApiError(404, 'Animal not found', error);
      }
      
      throw new ApiError(response.status, error.detail || 'Failed to delete animal', error);
    }
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

// ==================== CONSULTATION FUNCTIONS ====================

export async function searchConsultations(
  filters?: ConsultationFilters
): Promise<Consultation[]> {
  const params = new URLSearchParams();
  
  // Backend suporta: GET /api/consultations?animalId={id}
  if (filters?.animalId) {
    params.append('animalId', filters.animalId.toString());
  }

  const url = `${API_BASE_URL}/consultations${params.toString() ? `?${params.toString()}` : ''}`;
  
  try {
    const response = await fetch(url);
    
    if (!response.ok) {
      const error: ConsultationApiErrorResponse = await response.json();
      throw new ApiError(response.status, error.detail || 'Failed to search consultations', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function getConsultationById(id: number): Promise<Consultation> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/consultations/${id}`;
  
  try {
    const response = await fetch(url);
    
    if (!response.ok) {
      if (response.status === 404) {
        throw new ApiError(404, 'Consultation not found');
      }
      const error: ConsultationApiErrorResponse = await response.json();
      throw new ApiError(response.status, error.detail || 'Failed to get consultation', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function createConsultation(consultation: ConsultationRequest): Promise<Consultation> {
  const url = `${API_BASE_URL}/consultations`;
  
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(consultation),
    });
    
    if (!response.ok) {
      const error: ConsultationApiErrorResponse = await response.json();
      
      if (response.status === 400) {
        throw new ApiError(response.status, error.detail || 'Validation failed', error);
      }
      
      if (response.status === 422) {
        throw new ApiError(response.status, error.detail || 'Business rule violation', error);
      }
      
      throw new ApiError(response.status, error.detail || 'Failed to create consultation', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function updateConsultation(id: number, consultation: ConsultationRequest): Promise<Consultation> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/consultations/${id}`;
  
  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(consultation),
    });
    
    if (!response.ok) {
      const error: ConsultationApiErrorResponse = await response.json();
      
      if (response.status === 400) {
        throw new ApiError(response.status, error.detail || 'Validation failed', error);
      }
      
      if (response.status === 404) {
        throw new ApiError(response.status, 'Consultation not found', error);
      }
      
      if (response.status === 422) {
        throw new ApiError(response.status, error.detail || 'Business rule violation', error);
      }
      
      throw new ApiError(response.status, error.detail || 'Failed to update consultation', error);
    }
    
    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function deleteConsultation(id: number): Promise<void> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/consultations/${id}`;
  
  try {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (!response.ok) {
      const error: ConsultationApiErrorResponse = await response.json();
      
      if (response.status === 404) {
        throw new ApiError(404, 'Consultation not found', error);
      }
      
      throw new ApiError(response.status, error.detail || 'Failed to delete consultation', error);
    }
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

