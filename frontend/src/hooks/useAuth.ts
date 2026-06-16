import { storeToRefs } from 'pinia';
import { useAuthStore } from '../stores/authStore';

export function useAuth() {
  const authStore = useAuthStore();
  const refs = storeToRefs(authStore);
  return { ...refs, can: authStore.can, login: authStore.login, bootstrap: authStore.bootstrap };
}
