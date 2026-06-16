import type { User } from './user';

export type HouseMemberRelation = 'owner' | 'co-resident';
export type HouseMemberStatus = 'active' | 'removed';

export interface HouseMember {
  id: number;
  building: string;
  unit: string;
  room: string;
  userId: number;
  relation: HouseMemberRelation;
  invitedBy?: number;
  status: HouseMemberStatus;
  createdAt: string;
  user?: User;
}
