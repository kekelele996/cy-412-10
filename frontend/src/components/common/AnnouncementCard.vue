<script setup lang="ts">
import type { Announcement } from '../../types/announcement';
import CategoryTag from './CategoryTag.vue';

defineProps<{ announcement: Announcement }>();
defineEmits<{ read: [id: number] }>();
</script>

<template>
  <article class="announcement-card" @click="$emit('read', announcement.id)">
    <div class="announcement-card__head">
      <CategoryTag :category="announcement.category" />
      <el-tag v-if="announcement.top" type="danger" effect="plain">置顶</el-tag>
    </div>
    <h3>{{ announcement.title }}</h3>
    <p>{{ announcement.content }}</p>
    <span>{{ announcement.publishAt }} · 已读 {{ announcement.readCount }}</span>
  </article>
</template>

<style scoped>
.announcement-card {
  cursor: pointer;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #dfe7d8;
  background: #fff;
}

.announcement-card__head {
  display: flex;
  justify-content: space-between;
}

.announcement-card h3 {
  margin: 12px 0 8px;
  font-size: 17px;
}

.announcement-card p {
  color: #3e4b44;
  line-height: 1.6;
}

.announcement-card span {
  color: #778273;
  font-size: 13px;
}
</style>
