import { request } from '../utils/request';
import type { Repair, RepairPayload } from '../types/repair';
import type { RepairStatus } from '../constants/repair';

export const listRepairs = (status?: RepairStatus | 'all') =>
  request.get<never, Repair[]>('/repairs', { params: { status: status === 'all' ? undefined : status } });
export const createRepair = (data: RepairPayload) => request.post<never, Repair>('/repairs', data);
export const assignRepair = (id: number, handlerId: number) =>
  request.put<never, Repair>(`/repairs/${id}/assign`, { handlerId });
export const updateRepairStatus = (id: number, status: RepairStatus) =>
  request.put<never, Repair>(`/repairs/${id}/status`, { status });
