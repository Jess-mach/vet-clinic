import type { Animal, AnimalFilters, ApiError as ApiErrorResponse } from '../types/animal';

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

