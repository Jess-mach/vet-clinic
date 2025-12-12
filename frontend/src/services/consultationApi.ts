import type {
  Consultation,
  ConsultationFilters,
  ConsultationRequest,
  ApiError as ConsultationApiErrorResponse,
  PaginatedResponse,
} from '../types/consultation';
import { ApiError, API_BASE_URL } from './apiClient';

export async function searchConsultations(
  filters?: ConsultationFilters
): Promise<PaginatedResponse<Consultation>> {
  const params = new URLSearchParams();

  // Filtros de busca
  if (filters?.animalName) {
    params.append('animalName', filters.animalName);
  }
  if (filters?.ownerName) {
    params.append('ownerName', filters.ownerName);
  }
  if (filters?.veterinarianName) {
    params.append('veterinarianName', filters.veterinarianName);
  }
  if (filters?.veterinarianId !== undefined) {
    params.append('veterinarianId', filters.veterinarianId.toString());
  }
  if (filters?.status) {
    params.append('status', filters.status);
  }
  if (filters?.reason) {
    params.append('reason', filters.reason);
  }
  if (filters?.description) {
    params.append('description', filters.description);
  }
  if (filters?.createdAtStart) {
    params.append('createdAtStart', filters.createdAtStart);
  }
  if (filters?.createdAtEnd) {
    params.append('createdAtEnd', filters.createdAtEnd);
  }

  // Parâmetros de paginação
  if (filters?.page !== undefined) {
    params.append('page', filters.page.toString());
  }
  if (filters?.size !== undefined) {
    params.append('size', filters.size.toString());
  }
  if (filters?.sort) {
    params.append('sort', filters.sort);
  }
  if (filters?.animalId !== undefined) {
    params.append('animalId', filters.animalId.toString());
  }

  const url = `${API_BASE_URL}/consultations${
    params.toString() ? `?${params.toString()}` : ''
  }`;

  try {
    const response = await fetch(url);

    if (!response.ok) {
      const error: ConsultationApiErrorResponse = await response.json();
      throw new ApiError(
        response.status,
        error.detail || 'Failed to search consultations',
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

export async function getConsultationById(
  id: number
): Promise<Consultation> {
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
      throw new ApiError(
        response.status,
        error.detail || 'Failed to get consultation',
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

export async function createConsultation(
  consultation: ConsultationRequest
): Promise<Consultation> {
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
        throw new ApiError(
          response.status,
          error.detail || 'Validation failed',
          error
        );
      }

      if (response.status === 404) {
        throw new ApiError(
          response.status,
          error.detail || 'Veterinarian not found',
          error
        );
      }

      if (response.status === 422) {
        throw new ApiError(
          response.status,
          error.detail || 'Business rule violation',
          error
        );
      }

      throw new ApiError(
        response.status,
        error.detail || 'Failed to create consultation',
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

export async function updateConsultation(
  id: number,
  consultation: ConsultationRequest
): Promise<Consultation> {
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
        throw new ApiError(
          response.status,
          error.detail || 'Validation failed',
          error
        );
      }

      if (response.status === 404) {
        const isVeterinarianNotFound =
          error.detail?.includes('Veterinarian not found');
        throw new ApiError(
          response.status,
          isVeterinarianNotFound ? error.detail : 'Consultation not found',
          error
        );
      }

      if (response.status === 422) {
        throw new ApiError(
          response.status,
          error.detail || 'Business rule violation',
          error
        );
      }

      throw new ApiError(
        response.status,
        error.detail || 'Failed to update consultation',
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

export async function cancelConsultation(
  id: number
): Promise<Consultation> {
  if (id <= 0) {
    throw new ApiError(400, 'ID must be greater than 0');
  }

  const url = `${API_BASE_URL}/consultations/${id}/cancel`;

  try {
    const response = await fetch(url, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const error: ConsultationApiErrorResponse = await response.json();

      if (response.status === 404) {
        throw new ApiError(404, 'Consultation not found', error);
      }

      if (response.status === 422) {
        throw new ApiError(
          422,
          error.detail || 'Business rule violation',
          error
        );
      }

      throw new ApiError(
        response.status,
        error.detail || 'Failed to cancel consultation',
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


