import { request } from '../utils/request';
import type { HouseMember } from '../types/houseMember';

export const listMyHousehold = () => request.get<never, HouseMember[]>('/household');
export const listAllHouseholds = () => request.get<never, HouseMember[]>('/household/all');
export const addHouseMember = (phone: string) => request.post<never, HouseMember>('/household', { phone });
export const removeHouseMember = (id: number) => request.delete<never, HouseMember>(`/household/${id}`);
