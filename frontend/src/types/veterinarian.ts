export interface Veterinarian {
  id: number;
  name: string;
  specialtyCode: number;
  specialty: string;
  createdAt: string;
  updatedAt: string;
}

export interface VeterinarianFilters {
  name?: string;
  specialtyCode?: number;
}

export interface VeterinarianAvailabilityResponse {
  date: string;
  startTime: string;
  endTime: string;
  timezone: string;
}

