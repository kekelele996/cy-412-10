import { request } from '../utils/request';
import type { LoginRequest, LoginResponse, User } from '../types/user';

export const login = (data: LoginRequest) => request.post<never, LoginResponse>('/auth/login', data);
export const getProfile = () => request.get<never, User>('/users/profile');
export const updateProfile = (data: Partial<User>) => request.put<never, User>('/users/profile', data);
export const listStaff = () => request.get<never, User[]>('/users/staff');
