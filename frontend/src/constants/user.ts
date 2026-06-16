export const USER_ROLE = {
  RESIDENT: 'resident',
  STAFF: 'staff',
  ADMIN: 'admin',
} as const;

export type UserRole = (typeof USER_ROLE)[keyof typeof USER_ROLE];

export const USER_ROLE_TEXT: Record<UserRole, string> = {
  [USER_ROLE.RESIDENT]: '业主',
  [USER_ROLE.STAFF]: '物业',
  [USER_ROLE.ADMIN]: '管理员',
};
