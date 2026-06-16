<script setup lang="ts">
import { computed } from 'vue';
import type { AnnouncementCategory } from '../../types/announcement';

const props = defineProps<{ category: AnnouncementCategory }>();

const tag = computed(() => {
  const map = {
    notice: { text: '通知', type: 'info' },
    event: { text: '活动', type: 'success' },
    urgent: { text: '紧急', type: 'danger' },
  } as const;
  return map[props.category];
});
</script>

<template>
  <el-tag :type="tag.type" effect="light">{{ tag.text }}</el-tag>
</template>
