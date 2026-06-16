<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import RepairCard from '../components/common/RepairCard.vue';
import EmptyState from '../components/common/EmptyState.vue';
import PermissionButton from '../components/common/PermissionButton.vue';
import { useRepairStore } from '../stores/repairStore';
import { useUserStore } from '../stores/userStore';
import { REPAIR_STATUS, REPAIR_STATUS_TEXT, type RepairStatus } from '../constants/repair';

const repairStore = useRepairStore();
const userStore = useUserStore();

const statusOptions: Array<{ label: string; value: RepairStatus | 'all' }> = [
  { label: '全部', value: 'all' },
  ...Object.values(REPAIR_STATUS).map((status) => ({ label: REPAIR_STATUS_TEXT[status], value: status })),
];

async function assign(repairId: number, handlerId: number) {
  await repairStore.assign(repairId, handlerId);
  ElMessage.success('已分配处理人');
}

async function updateStatus(repairId: number, status: RepairStatus) {
  await repairStore.updateStatus(repairId, status);
  ElMessage.success('工单进度已更新');
}

onMounted(async () => {
  await Promise.all([repairStore.fetchRepairs(), userStore.fetchStaff()]);
});
</script>

<template>
  <section class="section-panel">
    <div class="filter-row">
      <el-segmented
        :model-value="repairStore.activeStatus"
        :options="statusOptions"
        @change="(status: string | number | boolean) => repairStore.fetchRepairs(status as RepairStatus | 'all')"
      />
      <PermissionButton permission="repair:create" plain>新建报修</PermissionButton>
    </div>
    <div v-if="repairStore.repairs.length" class="list-stack">
      <RepairCard
        v-for="repair in repairStore.repairs"
        :key="repair.id"
        :repair="repair"
        :staff-options="userStore.staff"
        @assign="assign"
        @status="updateStatus"
      />
    </div>
    <EmptyState v-else title="没有匹配的工单" description="切换状态筛选或新建报修" />
  </section>
</template>
