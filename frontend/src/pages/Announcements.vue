<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import AnnouncementCard from '../components/common/AnnouncementCard.vue';
import EmptyState from '../components/common/EmptyState.vue';
import PermissionButton from '../components/common/PermissionButton.vue';
import { listAnnouncements, markAnnouncementRead, publishAnnouncement } from '../api/announcement';
import type { Announcement, AnnouncementCategory } from '../types/announcement';

const announcements = ref<Announcement[]>([]);
const form = reactive({
  title: '',
  content: '',
  category: 'notice' as AnnouncementCategory,
  top: false,
});

async function fetchAnnouncements() {
  announcements.value = await listAnnouncements();
}

async function read(id: number) {
  await markAnnouncementRead(id);
  await fetchAnnouncements();
}

async function publish() {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写公告标题和内容');
    return;
  }
  await publishAnnouncement(form);
  Object.assign(form, { title: '', content: '', category: 'notice', top: false });
  await fetchAnnouncements();
  ElMessage.success('公告已发布');
}

onMounted(fetchAnnouncements);
</script>

<template>
  <div class="page-grid two-col">
    <section class="section-panel">
      <div class="section-title">
        <h2>公告列表</h2>
      </div>
      <div v-if="announcements.length" class="list-stack">
        <AnnouncementCard v-for="announcement in announcements" :key="announcement.id" :announcement="announcement" @read="read" />
      </div>
      <EmptyState v-else title="暂无公告" />
    </section>

    <section class="section-panel">
      <div class="section-title">
        <h2>物业发布</h2>
      </div>
      <el-form label-position="top">
        <el-form-item label="标题">
          <el-input v-model="form.title" maxlength="80" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="通知" value="notice" />
            <el-option label="活动" value="event" />
            <el-option label="紧急" value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.top">置顶公告</el-checkbox>
        </el-form-item>
        <PermissionButton permission="announcement:publish" @click="publish">发布公告</PermissionButton>
      </el-form>
    </section>
  </div>
</template>
