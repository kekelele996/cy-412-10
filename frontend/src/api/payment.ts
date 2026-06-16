import { request } from '../utils/request';
import type { Payment } from '../types/payment';

export const listPayments = () => request.get<never, Payment[]>('/payments');
export const payPayment = (id: number) => request.post<never, Payment>(`/payments/${id}/pay`, {});
export const generateBill = (userId: number, month: string) =>
  request.post<never, Payment>('/payments/generate', { userId, month, feeType: 'property' });
