<script setup lang="ts">
import { computed } from 'vue';
import { useAuthStore } from '../../stores/authStore';
import type { PermissionCode } from '../../types/permission';

const props = withDefaults(
  defineProps<{
    permission: PermissionCode | string;
    type?: 'primary' | 'success' | 'warning' | 'danger' | 'info';
    size?: 'small' | 'default' | 'large';
    plain?: boolean;
  }>(),
  {
    type: 'primary',
    size: 'default',
    plain: false,
  },
);

const authStore = useAuthStore();
const visible = computed(() => authStore.can(props.permission));
</script>

<template>
  <el-button v-if="visible" :type="type" :size="size" :plain="plain">
    <slot />
  </el-button>
</template>
