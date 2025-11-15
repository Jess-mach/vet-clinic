export interface Consultation {
  id: number;
  animalId: number;
  animalName: string;
  ownerName: string;
  ownerPhone: string | null;
  ownerEmail: string | null;
  consultationDate: string;
  consultationType: string;
  reason: string;
  diagnosis: string | null;
  treatment: string | null;
  notes: string | null;
  veterinarian: string;
  status: string;
  nextConsultationDate: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ConsultationFilters {
  animalName?: string;
  ownerName?: string;
  status?: string;
  veterinarian?: string;
}

export interface ConsultationRequest {
  animalId: number;
  consultationDate: string;
  consultationType: string;
  reason: string;
  diagnosis?: string | null;
  treatment?: string | null;
  notes?: string | null;
  veterinarian: string;
  status: string;
  nextConsultationDate?: string | null;
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

