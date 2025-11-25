import type {
  Veterinarian,
  VeterinarianFilters,
  VeterinarianAvailabilityResponse,
} from '../types/veterinarian';
import { ApiError, API_BASE_URL } from './apiClient';

export async function getVeterinarians(
  filters?: VeterinarianFilters
): Promise<Veterinarian[]> {
  const params = new URLSearchParams();

  if (filters?.name) {
    params.append('name', filters.name);
  }

  if (filters?.specialtyCode !== undefined) {
    params.append('specialtyCode', filters.specialtyCode.toString());
  }

  const url = `${API_BASE_URL}/veterinarians${
    params.toString() ? `?${params.toString()}` : ''
  }`;

  try {
    const response = await fetch(url);

    if (!response.ok) {
      const error = await response.json();
      throw new ApiError(
        response.status,
        error.detail || 'Failed to get veterinarians',
        error
      );
    }

    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export async function getVeterinarianAvailability(
  veterinarianId: number,
  date?: string
): Promise<VeterinarianAvailabilityResponse[]> {
  if (veterinarianId <= 0) {
    throw new ApiError(400, 'Veterinarian ID must be greater than 0');
  }

  const params = new URLSearchParams();
  if (date) {
    params.append('date', date);
  }

  const url = `${API_BASE_URL}/veterinarians/${veterinarianId}/availability${
    params.toString() ? `?${params.toString()}` : ''
  }`;

  try {
    const response = await fetch(url);

    if (!response.ok) {
      const error = await response.json();

      if (response.status === 404) {
        throw new ApiError(404, 'Veterinarian not found', error);
      }

      throw new ApiError(
        response.status,
        error.detail || 'Failed to get veterinarian availability',
        error
      );
    }

    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(500, 'Network error or server unavailable');
  }
}

export { ApiError } from './apiClient';


