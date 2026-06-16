import type { UserRole } from '../constants/user';

export interface User {
  id: number;
  phone: string;
  nickname: string;
  avatar?: string;
  role: UserRole;
  building?: string;
  unit?: string;
  room?: string;
  createdAt?: string;
}

export interface LoginRequest {
  phone: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
  permissions: string[];
}
