import { computed, type Ref } from 'vue';
import { REPAIR_STATUS } from '../constants/repair';
import type { Repair } from '../types/repair';

export function useRepairStats(repairs: Ref<Repair[]>) {
  return computed(() => ({
    pending: repairs.value.filter((item) => item.status === REPAIR_STATUS.PENDING).length,
    assigned: repairs.value.filter((item) => item.status === REPAIR_STATUS.ASSIGNED).length,
    processing: repairs.value.filter((item) => item.status === REPAIR_STATUS.PROCESSING).length,
    done: repairs.value.filter((item) => item.status === REPAIR_STATUS.DONE).length,
    closed: repairs.value.filter((item) => item.status === REPAIR_STATUS.CLOSED).length,
  }));
}
