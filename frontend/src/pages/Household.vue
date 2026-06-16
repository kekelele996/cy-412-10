<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import EmptyState from '../components/common/EmptyState.vue';
import PermissionButton from '../components/common/PermissionButton.vue';
import { useHouseholdStore } from '../stores/householdStore';
import { useAuthStore } from '../stores/authStore';
import { relationText } from '../utils/roleText';
import { USER_ROLE } from '../constants/user';

const householdStore = useHouseholdStore();
const authStore = useAuthStore();

const addPhone = ref('');
const adding = ref(false);

const isResident = computed(() => authStore.currentUser?.role === USER_ROLE.RESIDENT);
const isStaffOrAdmin = computed(() =>
  authStore.currentUser?.role === USER_ROLE.STAFF || authStore.currentUser?.role === USER_ROLE.ADMIN,
);

const groupedByHouse = computed(() => {
  const groups: Record<string, typeof householdStore.allMembers> = {};
  for (const member of householdStore.allMembers) {
    const key = `${member.building}-${member.unit}-${member.room}`;
    if (!groups[key]) groups[key] = [];
    groups[key].push(member);
  }
  return groups;
});

async function addMember() {
  if (!addPhone.value.trim()) {
    ElMessage.warning('请输入同住人手机号');
    return;
  }
  adding.value = true;
  try {
    await householdStore.addMember(addPhone.value.trim());
    addPhone.value = '';
    ElMessage.success('同住人已添加');
  } catch {
    ElMessage.error('添加失败，请检查手机号是否正确');
  } finally {
    adding.value = false;
  }
}

async function removeMember(id: number) {
  await householdStore.removeMember(id);
  ElMessage.success('已移除同住人');
}

onMounted(async () => {
  if (isStaffOrAdmin.value) {
    await householdStore.fetchAllHouseholds();
  } else {
    await householdStore.fetchMyHousehold();
  }
});
</script>

<template>
  <section>
    <section v-if="isResident" class="section-panel">
      <div class="section-title">
        <h2>我的房屋住户</h2>
      </div>
      <div v-if="householdStore.myMembers.length" class="list-stack">
        <div v-for="member in householdStore.myMembers" :key="member.id" class="member-card">
          <div class="member-card__left">
            <img v-if="member.user?.avatar" :src="member.user.avatar" alt="" class="member-avatar" />
            <div v-else class="member-avatar member-avatar--placeholder">{{ member.user?.nickname?.[0] || '?' }}</div>
            <div>
              <strong>{{ member.user?.nickname || '未知' }}</strong>
              <span>{{ member.user?.phone }} · {{ relationText(member.relation) }}</span>
            </div>
          </div>
          <el-button
            v-if="member.relation === 'co-resident'"
            type="danger"
            size="small"
            plain
            @click="removeMember(member.id)"
          >
            移除
          </el-button>
        </div>
      </div>
      <EmptyState v-else title="暂无住户信息" description="请联系物业绑定房屋" />
    </section>

    <section v-if="isResident" class="section-panel" style="margin-top: 18px">
      <div class="section-title">
        <h2>添加同住人</h2>
      </div>
      <div class="add-form">
        <el-input
          v-model="addPhone"
          placeholder="输入同住人手机号"
          maxlength="11"
          clearable
          @keyup.enter="addMember"
        />
        <PermissionButton permission="household:manage" :loading="adding" @click="addMember">
          添加同住人
        </PermissionButton>
      </div>
    </section>

    <section v-if="isStaffOrAdmin" class="section-panel">
      <div class="section-title">
        <h2>住户关系（按房屋）</h2>
      </div>
      <div v-if="Object.keys(groupedByHouse).length" class="list-stack">
        <div v-for="(members, houseKey) in groupedByHouse" :key="houseKey" class="house-group">
          <div class="house-group__title">{{ members[0]?.building }} {{ members[0]?.unit }} {{ members[0]?.room }}</div>
          <div v-for="member in members" :key="member.id" class="member-card member-card--inline">
            <div class="member-card__left">
              <img v-if="member.user?.avatar" :src="member.user.avatar" alt="" class="member-avatar" />
              <div v-else class="member-avatar member-avatar--placeholder">{{ member.user?.nickname?.[0] || '?' }}</div>
              <div>
                <strong>{{ member.user?.nickname || '未知' }}</strong>
                <span>{{ relationText(member.relation) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <EmptyState v-else title="暂无住户关系" description="业主添加同住人后将在这里显示" />
    </section>
  </section>
</template>

<style scoped>
.member-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 14px 16px;
  border: 1px solid #dfe7d8;
  border-radius: 8px;
  background: #fff;
}

.member-card__left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.member-card__left strong {
  display: block;
}

.member-card__left span {
  display: block;
  color: #778273;
  margin-top: 3px;
  font-size: 13px;
}

.member-avatar {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: cover;
}

.member-avatar--placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e8f0e4;
  color: #45624f;
  font-size: 16px;
  font-weight: 600;
}

.add-form {
  display: flex;
  gap: 12px;
  align-items: center;
}

.add-form .el-input {
  max-width: 280px;
}

.house-group {
  border: 1px solid #dfe7d8;
  border-radius: 8px;
  background: #fff;
  overflow: hidden;
}

.house-group__title {
  padding: 10px 16px;
  background: #f2f5ef;
  font-weight: 600;
  font-size: 14px;
  color: #45624f;
  border-bottom: 1px solid #dfe7d8;
}

.member-card--inline {
  border: none;
  border-radius: 0;
  border-bottom: 1px solid #f2f5ef;
}

.member-card--inline:last-child {
  border-bottom: none;
}

@media (max-width: 760px) {
  .add-form {
    flex-direction: column;
    align-items: stretch;
  }

  .add-form .el-input {
    max-width: none;
  }
}
</style>
