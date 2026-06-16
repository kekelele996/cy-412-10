import type { Router } from 'vue-router';
import { useAuthStore } from '../stores/authStore';
import type { PermissionCode } from '../types/permission';
import type { UserRole } from '../constants/user';

export function registerGuards(router: Router) {
  router.beforeEach(async (to) => {
    const authStore = useAuthStore();
    await authStore.bootstrap();

    const permission = to.meta.permission as PermissionCode | undefined;
    const roles = to.meta.roles as UserRole[] | undefined;

    if (permission && !authStore.can(permission)) {
      return '/dashboard';
    }
    if (roles?.length && authStore.currentUser && !roles.includes(authStore.currentUser.role)) {
      return '/dashboard';
    }
    return true;
  });
}
