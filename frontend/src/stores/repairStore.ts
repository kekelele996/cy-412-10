import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { assignRepair, listRepairs, updateRepairStatus } from '../api/repair';
import { REPAIR_STATUS, type RepairStatus } from '../constants/repair';
import type { Repair } from '../types/repair';

export const useRepairStore = defineStore('repair', () => {
  const repairs = ref<Repair[]>([]);
  const loading = ref(false);
  const activeStatus = ref<RepairStatus | 'all'>('all');

  const pendingCount = computed(() => repairs.value.filter((item) => item.status === REPAIR_STATUS.PENDING).length);
  const processingCount = computed(() =>
    repairs.value.filter((item) => item.status === REPAIR_STATUS.ASSIGNED || item.status === REPAIR_STATUS.PROCESSING).length,
  );

  async function fetchRepairs(status: RepairStatus | 'all' = activeStatus.value) {
    loading.value = true;
    activeStatus.value = status;
    try {
      repairs.value = await listRepairs(status);
    } finally {
      loading.value = false;
    }
  }

  async function assign(id: number, handlerId: number) {
    const updated = await assignRepair(id, handlerId);
    repairs.value = repairs.value.map((item) => (item.id === id ? updated : item));
  }

  async function updateStatus(id: number, status: RepairStatus) {
    const updated = await updateRepairStatus(id, status);
    repairs.value = repairs.value.map((item) => (item.id === id ? updated : item));
  }

  return { repairs, loading, activeStatus, pendingCount, processingCount, fetchRepairs, assign, updateStatus };
});
