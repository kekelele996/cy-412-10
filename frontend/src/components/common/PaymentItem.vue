<script setup lang="ts">
import type { Payment } from '../../types/payment';
import { feeStatusText } from '../../utils/roleText';
import PermissionButton from './PermissionButton.vue';

defineProps<{ payment: Payment }>();
defineEmits<{ pay: [id: number] }>();

const feeTypeText: Record<string, string> = {
  property: '物业费',
  parking: '停车费',
  utilities: '水电费',
};
</script>

<template>
  <article class="payment-item">
    <div>
      <strong>{{ feeTypeText[payment.feeType] }}</strong>
      <span>{{ payment.month }} · {{ feeStatusText(payment.status) }}</span>
    </div>
    <div class="payment-item__right">
      <strong>¥{{ Number(payment.amount).toFixed(2) }}</strong>
      <PermissionButton v-if="payment.status !== 'paid'" permission="payment:pay" size="small" @click="$emit('pay', payment.id)">
        支付宝沙箱支付
      </PermissionButton>
    </div>
  </article>
</template>

<style scoped>
.payment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px;
  border: 1px solid #dfe7d8;
  border-radius: 8px;
  background: #fff;
}

.payment-item span {
  display: block;
  color: #778273;
  margin-top: 5px;
  font-size: 13px;
}

.payment-item__right {
  display: grid;
  justify-items: end;
  gap: 8px;
}
</style>
