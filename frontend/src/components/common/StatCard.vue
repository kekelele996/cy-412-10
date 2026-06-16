<script setup lang="ts">
defineProps<{
  label: string;
  value: string | number;
  hint?: string;
  tone?: 'green' | 'amber' | 'blue' | 'red';
}>();
</script>

<template>
  <div class="stat-card" :class="tone">
    <span>{{ label }}</span>
    <strong>{{ value }}</strong>
    <small>{{ hint }}</small>
  </div>
</template>

<style scoped>
.stat-card {
  min-height: 112px;
  border-radius: 8px;
  padding: 18px;
  background: #ffffff;
  border: 1px solid #dfe7d8;
  display: grid;
  align-content: space-between;
}

.stat-card span {
  color: #667263;
  font-size: 13px;
}

.stat-card strong {
  font-size: 32px;
  letter-spacing: 0;
}

.stat-card small {
  color: #7d8878;
}

.stat-card.green {
  background: #eef8de;
}

.stat-card.amber {
  background: #fff3d1;
}

.stat-card.blue {
  background: #e8f0f4;
}

.stat-card.red {
  background: #ffe7df;
}
</style>
