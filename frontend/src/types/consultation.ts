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
  veterinarianId: number;      // NOVO: ID do veterinário (obrigatório)
  veterinarianName: string;   // Mantido para compatibilidade (leitura)
  reason: string;
  reasonCode: number;
  description?: string;
  diagnosis?: string;
  treatmentPrescribed?: string;
  observations?: string;
  nextAppointmentDate?: string;
  status: 'COMPLETED' | 'SCHEDULED' | 'CANCELLED';
  createdAt: string;
  updatedAt: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface ConsultationFilters {
  animalName?: string;
  ownerName?: string;
  veterinarianName?: string;  // Mantido para busca por nome
  veterinarianId?: number;     // NOVO: Filtro por ID do veterinário
  status?: 'COMPLETED' | 'SCHEDULED' | 'CANCELLED';
  reason?: string;
  description?: string;
  createdAtStart?: string;
  createdAtEnd?: string;
  page?: number;
  size?: number;
  sort?: string;
  animalId?: number;
}

export interface ConsultationRequest {
  animalId: number;
  consultationDate: string;
  veterinarianId: number;  // ALTERADO: era veterinarianName (string)
  reason?: string;
  reasonCode: number;
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
  timestamp: string;
  path: string;
  errors?: Record<string, string>;
}

