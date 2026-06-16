<script setup lang="ts">
import { Box } from '@element-plus/icons-vue';

defineProps<{ title?: string; description?: string }>();
</script>

<template>
  <div class="empty-state">
    <el-icon><Box /></el-icon>
    <strong>{{ title || '暂无数据' }}</strong>
    <span>{{ description || '调整筛选条件或稍后再试' }}</span>
  </div>
</template>

<style scoped>
.empty-state {
  min-height: 180px;
  display: grid;
  place-items: center;
  align-content: center;
  gap: 8px;
  border: 1px dashed #b9c5b3;
  border-radius: 8px;
  color: #6e7b68;
}

.empty-state .el-icon {
  font-size: 28px;
  color: #45624f;
}

.empty-state span {
  font-size: 13px;
}
</style>
