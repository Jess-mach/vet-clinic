export interface Animal {
  id: number;
  name: string;
  species: string;
  breed: string | null;
  gender: string;
  birthDate: string | null;
  color: string | null;
  weight: number | null;
  microchipNumber: string | null;
  ownerName: string;
  ownerPhone: string | null;
  ownerEmail: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface AnimalFilters {
  name?: string;
  species?: string;
  ownerName?: string;
}

export interface AnimalRequest {
  name: string;
  species: string;
  breed?: string | null;
  gender: string;
  birthDate?: string | null;
  color?: string | null;
  weight?: number | null;
  microchipNumber?: string | null;
  ownerName: string;
  ownerPhone?: string | null;
  ownerEmail?: string | null;
}

export interface ApiError {
  type: string;
  title: string;
  status: number;
  detail: string;
  timestamp: string;
  path: string;
  errors?: Record<string, string>;
}


