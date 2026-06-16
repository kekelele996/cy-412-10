import { computed } from 'vue';
import { useAuthStore } from '../stores/authStore';
import type { PermissionCode } from '../types/permission';

export function usePermission(permissionCode: PermissionCode | string) {
  const authStore = useAuthStore();
  return computed(() => authStore.can(permissionCode));
}
