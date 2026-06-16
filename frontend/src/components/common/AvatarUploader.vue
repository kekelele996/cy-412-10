<script setup lang="ts">
import { ref, watch } from 'vue';
import { Upload } from '@element-plus/icons-vue';

const props = defineProps<{ modelValue?: string }>();
const emit = defineEmits<{ 'update:modelValue': [value: string] }>();

const avatar = ref(props.modelValue || '');

watch(
  () => props.modelValue,
  (value) => {
    avatar.value = value || '';
  },
);

function mockUpload() {
  const next = `https://api.dicebear.com/9.x/initials/svg?seed=${Date.now()}`;
  avatar.value = next;
  emit('update:modelValue', next);
}
</script>

<template>
  <button class="avatar-uploader" type="button" @click="mockUpload">
    <img v-if="avatar" :src="avatar" alt="" />
    <el-icon v-else><Upload /></el-icon>
    <span>更换头像</span>
  </button>
</template>

<style scoped>
.avatar-uploader {
  width: 128px;
  height: 128px;
  border: 1px solid #cfdac8;
  border-radius: 8px;
  background: #f9fbf4;
  display: grid;
  place-items: center;
  align-content: center;
  gap: 8px;
  cursor: pointer;
}

.avatar-uploader img {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  object-fit: cover;
}

.avatar-uploader span {
  color: #526156;
  font-size: 13px;
}
</style>
