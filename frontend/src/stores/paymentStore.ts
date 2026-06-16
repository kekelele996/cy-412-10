import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { listPayments, payPayment } from '../api/payment';
import type { Payment } from '../types/payment';

export const usePaymentStore = defineStore('payment', () => {
  const payments = ref<Payment[]>([]);
  const loading = ref(false);

  const unpaidAmount = computed(() =>
    payments.value.filter((item) => item.status !== 'paid').reduce((sum, item) => sum + Number(item.amount), 0),
  );

  async function fetchPayments() {
    loading.value = true;
    try {
      payments.value = await listPayments();
    } finally {
      loading.value = false;
    }
  }

  async function pay(id: number) {
    const updated = await payPayment(id);
    payments.value = payments.value.map((item) => (item.id === id ? updated : item));
  }

  return { payments, loading, unpaidAmount, fetchPayments, pay };
});
