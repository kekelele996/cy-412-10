<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import PaymentItem from '../components/common/PaymentItem.vue';
import EmptyState from '../components/common/EmptyState.vue';
import StatCard from '../components/common/StatCard.vue';
import { usePaymentStore } from '../stores/paymentStore';
import { useAuthStore } from '../stores/authStore';
import { calculatePropertyFee } from '../utils/feeCalculator';

const paymentStore = usePaymentStore();
const authStore = useAuthStore();

const isCoResident = computed(() => authStore.currentUser?.role === 'co-resident');

async function pay(id: number) {
  await paymentStore.pay(id);
  ElMessage.success('支付宝沙箱支付成功');
}

onMounted(paymentStore.fetchPayments);
</script>

<template>
  <section>
    <div class="stats-row">
      <StatCard label="本月待缴" :value="`¥${paymentStore.unpaidAmount.toFixed(2)}`" hint="包含物业费、水电费" tone="amber" />
      <StatCard label="100㎡ 物业费估算" :value="`¥${calculatePropertyFee(100, 'property')}`" hint="工具函数计算" tone="green" />
      <StatCard label="停车月费" :value="`¥${calculatePropertyFee(1, 'parking')}`" hint="固定月租" tone="blue" />
      <StatCard label="账单数量" :value="paymentStore.payments.length" hint="历史账单" />
    </div>
    <section class="section-panel">
      <div class="section-title">
        <h2>{{ isCoResident ? '本户账单' : '账单列表' }}</h2>
      </div>
      <div v-if="paymentStore.payments.length" class="list-stack">
        <PaymentItem v-for="payment in paymentStore.payments" :key="payment.id" :payment="payment" @pay="pay" />
      </div>
      <EmptyState v-else title="暂无账单" description="物业生成账单后将在这里显示" />
    </section>
  </section>
</template>
