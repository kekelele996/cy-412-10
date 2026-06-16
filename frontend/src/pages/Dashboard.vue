<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import * as echarts from 'echarts';
import StatCard from '../components/common/StatCard.vue';
import RepairStatusBadge from '../components/common/RepairStatusBadge.vue';
import AnnouncementCard from '../components/common/AnnouncementCard.vue';
import EmptyState from '../components/common/EmptyState.vue';
import { useRepairStore } from '../stores/repairStore';
import { usePaymentStore } from '../stores/paymentStore';
import { useHouseholdStore } from '../stores/householdStore';
import { useAuthStore } from '../stores/authStore';
import { listAnnouncements, markAnnouncementRead } from '../api/announcement';
import { useRepairStats } from '../hooks/useRepairStats';
import { relationText } from '../utils/roleText';
import { USER_ROLE } from '../constants/user';
import type { Announcement } from '../types/announcement';

const repairStore = useRepairStore();
const paymentStore = usePaymentStore();
const householdStore = useHouseholdStore();
const authStore = useAuthStore();
const announcements = ref<Announcement[]>([]);
const chartEl = ref<HTMLDivElement | null>(null);
const repairStats = useRepairStats(ref(repairStore.repairs));

const isStaffOrAdmin = computed(() =>
  authStore.currentUser?.role === USER_ROLE.STAFF || authStore.currentUser?.role === USER_ROLE.ADMIN,
);

const householdCount = computed(() => {
  const houses = new Set(householdStore.allMembers.map((m) => `${m.building}-${m.unit}-${m.room}`));
  return houses.size;
});

async function refresh() {
  await Promise.all([
    repairStore.fetchRepairs(),
    paymentStore.fetchPayments(),
    isStaffOrAdmin.value ? householdStore.fetchAllHouseholds() : Promise.resolve(),
  ]);
  announcements.value = await listAnnouncements();
}

function renderChart() {
  if (!chartEl.value) return;
  const chart = echarts.init(chartEl.value);
  chart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 32, right: 18, bottom: 28, top: 24 },
    xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六'] },
    yAxis: { type: 'value' },
    series: [
      {
        name: '工单',
        type: 'line',
        smooth: true,
        data: [4, 7, 5, 9, 6, repairStore.repairs.length],
        areaStyle: { color: 'rgba(69, 98, 79, 0.12)' },
        itemStyle: { color: '#45624f' },
      },
    ],
  });
}

async function readAnnouncement(id: number) {
  await markAnnouncementRead(id);
  announcements.value = await listAnnouncements();
}

onMounted(async () => {
  await refresh();
  renderChart();
});

watch(() => repairStore.repairs.length, renderChart);
</script>

<template>
  <section>
    <div class="stats-row">
      <StatCard label="待分配工单" :value="repairStats.pending" hint="需物业派单" tone="amber" />
      <StatCard label="处理中工单" :value="repairStats.processing" hint="含已分配" tone="blue" />
      <StatCard label="本月待收" :value="`¥${paymentStore.unpaidAmount.toFixed(2)}`" hint="支付宝沙箱模拟" tone="green" />
      <StatCard v-if="isStaffOrAdmin" label="登记住户" :value="householdCount" hint="已绑定房屋" tone="red" />
      <StatCard v-else label="公告阅读" :value="announcements.reduce((sum, item) => sum + item.readCount, 0)" hint="累计阅读数" tone="red" />
    </div>

    <div class="page-grid two-col">
      <section class="section-panel">
        <div class="section-title">
          <h2>工单趋势</h2>
          <el-button size="small" @click="refresh">刷新</el-button>
        </div>
        <div ref="chartEl" class="chart"></div>
        <div class="mini-repairs">
          <div v-for="repair in repairStore.repairs.slice(0, 3)" :key="repair.id">
            <span>{{ repair.title }}</span>
            <RepairStatusBadge :status="repair.status" />
          </div>
        </div>
      </section>

      <section v-if="isStaffOrAdmin" class="section-panel">
        <div class="section-title">
          <h2>住户概览</h2>
        </div>
        <div v-if="householdStore.allMembers.length" class="mini-household">
          <div v-for="member in householdStore.allMembers.slice(0, 5)" :key="member.id" class="mini-household__row">
            <span>{{ member.building }} {{ member.unit }} {{ member.room }}</span>
            <span>{{ member.user?.nickname || '未知' }} · {{ relationText(member.relation) }}</span>
          </div>
        </div>
        <EmptyState v-else title="暂无住户" />
      </section>

      <section v-else class="section-panel">
        <div class="section-title">
          <h2>最新公告</h2>
        </div>
        <div v-if="announcements.length" class="list-stack">
          <AnnouncementCard
            v-for="announcement in announcements.slice(0, 3)"
            :key="announcement.id"
            :announcement="announcement"
            @read="readAnnouncement"
          />
        </div>
        <EmptyState v-else title="暂无公告" />
      </section>
    </div>
  </section>
</template>

<style scoped>
.chart {
  width: 100%;
  height: 280px;
}

.mini-repairs {
  display: grid;
  gap: 10px;
  margin-top: 14px;
}

.mini-repairs div {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-top: 1px solid #e5ecdf;
}

.mini-household {
  display: grid;
  gap: 0;
}

.mini-household__row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-top: 1px solid #e5ecdf;
  font-size: 14px;
}

.mini-household__row span:last-child {
  color: #778273;
  font-size: 13px;
}
</style>
