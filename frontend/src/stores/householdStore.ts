import { defineStore } from 'pinia';
import { ref } from 'vue';
import { addHouseMember, listAllHouseholds, listMyHousehold, removeHouseMember } from '../api/houseMember';
import type { HouseMember } from '../types/houseMember';

export const useHouseholdStore = defineStore('household', () => {
  const myMembers = ref<HouseMember[]>([]);
  const allMembers = ref<HouseMember[]>([]);
  const loading = ref(false);

  async function fetchMyHousehold() {
    loading.value = true;
    try {
      myMembers.value = await listMyHousehold();
    } finally {
      loading.value = false;
    }
  }

  async function fetchAllHouseholds() {
    loading.value = true;
    try {
      allMembers.value = await listAllHouseholds();
    } finally {
      loading.value = false;
    }
  }

  async function addMember(phone: string) {
    const member = await addHouseMember(phone);
    myMembers.value = [...myMembers.value, member];
    return member;
  }

  async function removeMember(id: number) {
    await removeHouseMember(id);
    myMembers.value = myMembers.value.filter((m) => m.id !== id);
  }

  return { myMembers, allMembers, loading, fetchMyHousehold, fetchAllHouseholds, addMember, removeMember };
});
