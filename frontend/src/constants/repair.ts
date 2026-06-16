export const REPAIR_STATUS = {
  PENDING: 'pending',
  ASSIGNED: 'assigned',
  PROCESSING: 'processing',
  DONE: 'done',
  CLOSED: 'closed',
} as const;

export type RepairStatus = (typeof REPAIR_STATUS)[keyof typeof REPAIR_STATUS];

export const REPAIR_STATUS_TEXT: Record<RepairStatus, string> = {
  [REPAIR_STATUS.PENDING]: '待分配',
  [REPAIR_STATUS.ASSIGNED]: '已分配',
  [REPAIR_STATUS.PROCESSING]: '处理中',
  [REPAIR_STATUS.DONE]: '待确认',
  [REPAIR_STATUS.CLOSED]: '已关闭',
};

export const REPAIR_STATUS_COLOR: Record<RepairStatus, string> = {
  [REPAIR_STATUS.PENDING]: 'warning',
  [REPAIR_STATUS.ASSIGNED]: 'info',
  [REPAIR_STATUS.PROCESSING]: 'primary',
  [REPAIR_STATUS.DONE]: 'success',
  [REPAIR_STATUS.CLOSED]: 'default',
};

export const REPAIR_TYPE_TEXT: Record<string, string> = {
  water_power: '水电',
  furniture: '家具',
  public_facility: '公共设施',
  other: '其他',
};
