<script setup lang="ts">
import { reactive, watch } from 'vue';
import { ElMessage } from 'element-plus';
import AvatarUploader from '../components/common/AvatarUploader.vue';
import { useAuthStore } from '../stores/authStore';
import { useUserStore } from '../stores/userStore';
import { roleText } from '../utils/roleText';

const authStore = useAuthStore();
const userStore = useUserStore();

const form = reactive({
  nickname: authStore.currentUser?.nickname || '',
  avatar: authStore.currentUser?.avatar || '',
  building: authStore.currentUser?.building || '',
  unit: authStore.currentUser?.unit || '',
  room: authStore.currentUser?.room || '',
});

watch(
  () => authStore.currentUser,
  (user) => {
    if (user) {
      Object.assign(form, {
        nickname: user.nickname,
        avatar: user.avatar,
        building: user.building,
        unit: user.unit,
        room: user.room,
      });
    }
  },
);

async function save() {
  const updated = await userStore.saveProfile(form);
  authStore.currentUser = updated;
  ElMessage.success('资料已保存');
}
</script>

<template>
  <section class="section-panel profile-panel">
    <AvatarUploader v-model="form.avatar" />
    <el-form label-position="top" class="profile-form">
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" />
      </el-form-item>
      <el-form-item label="角色">
        <el-input :model-value="authStore.currentUser ? roleText(authStore.currentUser.role) : ''" disabled />
      </el-form-item>
      <div class="form-row">
        <el-form-item label="楼栋">
          <el-input v-model="form.building" />
        </el-form-item>
        <el-form-item label="单元">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="房号">
          <el-input v-model="form.room" />
        </el-form-item>
      </div>
      <el-button type="primary" :loading="userStore.saving" @click="save">保存资料</el-button>
    </el-form>
  </section>
</template>

<style scoped>
.profile-panel {
  display: grid;
  grid-template-columns: 160px minmax(0, 1fr);
  gap: 28px;
  align-items: start;
}

.profile-form {
  max-width: 760px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

@media (max-width: 760px) {
  .profile-panel,
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
