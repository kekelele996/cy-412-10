<script setup lang="ts">
import type { Repair } from '../../types/repair';
import { REPAIR_STATUS, type RepairStatus } from '../../constants/repair';
import { REPAIR_TYPE_TEXT } from '../../constants/repair';
import RepairStatusBadge from './RepairStatusBadge.vue';
import PermissionButton from './PermissionButton.vue';

defineProps<{
  repair: Repair;
  staffOptions?: Array<{ id: number; nickname: string }>;
}>();

defineEmits<{
  assign: [repairId: number, handlerId: number];
  status: [repairId: number, status: RepairStatus];
}>();
</script>

<template>
  <article class="repair-card">
    <div class="repair-card__head">
      <div>
        <strong>{{ repair.title }}</strong>
        <span>{{ REPAIR_TYPE_TEXT[repair.type] }} · {{ repair.user?.nickname || `用户 ${repair.userId}` }}</span>
      </div>
      <RepairStatusBadge :status="repair.status" />
    </div>
    <p>{{ repair.description }}</p>
    <div class="repair-card__actions">
      <el-select
        v-if="staffOptions?.length"
        placeholder="处理人"
        size="small"
        :model-value="repair.handlerId"
        style="width: 132px"
        @change="(value: number) => $emit('assign', repair.id, value)"
      >
        <el-option v-for="staff in staffOptions" :key="staff.id" :label="staff.nickname" :value="staff.id" />
      </el-select>
      <PermissionButton permission="repair:update" size="small" @click="$emit('status', repair.id, REPAIR_STATUS.PROCESSING)">
        开始处理
      </PermissionButton>
      <PermissionButton permission="repair:update" size="small" type="success" @click="$emit('status', repair.id, REPAIR_STATUS.DONE)">
        标记完成
      </PermissionButton>
    </div>
  </article>
</template>

<style scoped>
.repair-card {
  border: 1px solid #dfe7d8;
  border-radius: 8px;
  background: #fff;
  padding: 16px;
}

.repair-card__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.repair-card strong,
.repair-card span {
  display: block;
}

.repair-card span {
  color: #778273;
  font-size: 13px;
  margin-top: 5px;
}

.repair-card p {
  color: #3e4b44;
  line-height: 1.65;
}

.repair-card__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>
