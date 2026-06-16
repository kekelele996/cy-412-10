import type { RepairStatus } from '../constants/repair';
import type { User } from './user';

export interface Repair {
  id: number;
  userId: number;
  title: string;
  description: string;
  type: 'water_power' | 'furniture' | 'public_facility' | 'other';
  images?: string;
  status: RepairStatus;
  handlerId?: number;
  rating?: number;
  createdAt: string;
  updatedAt: string;
  user?: User;
  handler?: User;
}

export interface RepairPayload {
  title: string;
  description: string;
  type: Repair['type'];
}
