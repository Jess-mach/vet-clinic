// ✅ Compatível com Backend Contract V0002
export interface AnimalBasicInfo {
  id: number;
  name: string;
  species: string;
  breed: string;
  ownerName: string;
}

export interface Consultation {
  id: number;
  animal: AnimalBasicInfo;
  consultationDate: string;
  veterinarianName: string;
  reason: string;
  description?: string;
  diagnosis?: string;
  treatmentPrescribed?: string;
  observations?: string;
  nextAppointmentDate?: string;
  status: 'COMPLETED' | 'SCHEDULED' | 'CANCELLED';
  createdAt: string;
  updatedAt: string;
}

export interface ConsultationFilters {
  animalId?: number;
}

export interface ConsultationRequest {
  animalId: number;
  consultationDate: string;
  veterinarianName: string;
  reason: string;
  description?: string;
  diagnosis?: string;
  treatmentPrescribed?: string;
  observations?: string;
  nextAppointmentDate?: string;
  status?: 'COMPLETED' | 'SCHEDULED' | 'CANCELLED';
}

export interface ApiError {
  type: string;
  title: string;
  status: number;
  detail: string;
  timestamp?: string;
  path?: string;
  errors?: Record<string, string>;
}

