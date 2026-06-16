import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { login as loginApi, getProfile } from '../api/user';
import type { LoginRequest, User } from '../types/user';
import type { PermissionCode } from '../types/permission';
import { roleText } from '../utils/roleText';
import { USER_ROLE } from '../constants/user';

const defaultUser: User = {
  id: 1,
  phone: '13800000001',
  nickname: '林经理',
  avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200',
  role: USER_ROLE.ADMIN,
  building: '物业中心',
  unit: 'A',
  room: '001',
};

const defaultPermissions: PermissionCode[] = [
  'dashboard:view',
  'repair:view',
  'repair:create',
  'repair:assign',
  'repair:update',
  'payment:view',
  'payment:pay',
  'announcement:view',
  'announcement:publish',
  'user:profile',
  'operationLog:view',
];

export const useAuthStore = defineStore('auth', () => {
  const currentUser = ref<User | null>(defaultUser);
  const permissions = ref<PermissionCode[]>(defaultPermissions);
  const token = ref(localStorage.getItem('smartestate_token') || 'demo-token');

  const role = computed(() => currentUser.value?.role || USER_ROLE.RESIDENT);
  const roleTextValue = computed(() => roleText(role.value));

  function can(permission: PermissionCode | string) {
    return permissions.value.includes(permission as PermissionCode);
  }

  async function login(payload: LoginRequest) {
    const response = await loginApi(payload);
    token.value = response.token;
    currentUser.value = response.user;
    permissions.value = response.permissions as PermissionCode[];
    localStorage.setItem('smartestate_token', response.token);
  }

  async function bootstrap() {
    if (!token.value || token.value === 'demo-token') {
      await login({ phone: '13800000001', password: 'smartestate' });
      return;
    }
    try {
      currentUser.value = await getProfile();
    } catch {
      localStorage.removeItem('smartestate_token');
      token.value = 'demo-token';
      currentUser.value = defaultUser;
      await login({ phone: '13800000001', password: 'smartestate' });
    }
  }

  return {
    currentUser,
    permissions,
    token,
    role,
    roleText: roleTextValue,
    can,
    login,
    bootstrap,
  };
});
