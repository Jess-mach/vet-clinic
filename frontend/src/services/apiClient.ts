const API_BASE_URL = 'http://localhost:8080/api';

export class ApiError extends Error {
  constructor(
    public status: number,
    public detail: string,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    public errorData?: any
  ) {
    super(detail);
    this.name = 'ApiError';
  }
}

export { API_BASE_URL };


