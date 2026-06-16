import { defineStore } from 'pinia';
import { ref } from 'vue';
import { listStaff, updateProfile } from '../api/user';
import type { User } from '../types/user';

export const useUserStore = defineStore('user', () => {
  const staff = ref<User[]>([]);
  const saving = ref(false);

  async function fetchStaff() {
    staff.value = await listStaff();
  }

  async function saveProfile(profile: Partial<User>) {
    saving.value = true;
    try {
      return await updateProfile(profile);
    } finally {
      saving.value = false;
    }
  }

  return { staff, saving, fetchStaff, saveProfile };
});
