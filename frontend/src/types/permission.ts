import type { UserRole } from '../constants/user';

export type PermissionCode =
  | 'dashboard:view'
  | 'repair:view'
  | 'repair:create'
  | 'repair:assign'
  | 'repair:update'
  | 'payment:view'
  | 'payment:pay'
  | 'announcement:view'
  | 'announcement:publish'
  | 'user:profile'
  | 'operationLog:view'
  | 'household:view'
  | 'household:manage';

export interface RoutePermissionMeta {
  permission: PermissionCode;
  roles?: UserRole[];
}
