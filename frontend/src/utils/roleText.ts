import { REPAIR_STATUS_TEXT, type RepairStatus } from '../constants/repair';
import { USER_ROLE_TEXT, type UserRole } from '../constants/user';

export function roleText(role: UserRole) {
  return USER_ROLE_TEXT[role] || role;
}

export function relationText(relation: string) {
  return { owner: '业主', 'co-resident': '同住人' }[relation] || relation;
}

export function repairStatusText(status: RepairStatus) {
  return REPAIR_STATUS_TEXT[status] || status;
}

export function feeStatusText(status: string) {
  return {
    unpaid: '待缴费',
    paid: '已缴费',
    overdue: '已逾期',
  }[status] || status;
}
