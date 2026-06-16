<script setup lang="ts">
import { computed } from 'vue';
import { REPAIR_STATUS_COLOR, REPAIR_STATUS_TEXT, type RepairStatus } from '../../constants/repair';

const props = defineProps<{ status: RepairStatus }>();

const label = computed(() => REPAIR_STATUS_TEXT[props.status]);
const type = computed(() => REPAIR_STATUS_COLOR[props.status] as 'success' | 'info' | 'warning' | 'primary' | undefined);
</script>

<template>
  <el-tag :type="type" effect="light" round>{{ label }}</el-tag>
</template>
