export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  userId: number;
  email: string;
  fullName: string;
  companyId: number | null; // null = B2C, ID = B2B
  token: string;
}
