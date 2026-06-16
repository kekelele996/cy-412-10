import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Dashboard from '../pages/Dashboard.vue';
import Repairs from '../pages/Repairs.vue';
import Payments from '../pages/Payments.vue';
import Announcements from '../pages/Announcements.vue';
import Profile from '../pages/Profile.vue';
import Household from '../pages/Household.vue';
import { registerGuards } from './guards';
import { USER_ROLE } from '../constants/user';

const routes: RouteRecordRaw[] = [
  { path: '/', redirect: '/dashboard' },
  {
    path: '/dashboard',
    component: Dashboard,
    meta: { title: '物业工作台', permission: 'dashboard:view', roles: [USER_ROLE.STAFF, USER_ROLE.ADMIN, USER_ROLE.RESIDENT, USER_ROLE.CO_RESIDENT] },
  },
  {
    path: '/repairs',
    component: Repairs,
    meta: { title: '报修管理', permission: 'repair:view', roles: [USER_ROLE.STAFF, USER_ROLE.ADMIN, USER_ROLE.RESIDENT, USER_ROLE.CO_RESIDENT] },
  },
  {
    path: '/payments',
    component: Payments,
    meta: { title: '费用缴纳', permission: 'payment:view', roles: [USER_ROLE.RESIDENT, USER_ROLE.ADMIN, USER_ROLE.STAFF, USER_ROLE.CO_RESIDENT] },
  },
  {
    path: '/announcements',
    component: Announcements,
    meta: {
      title: '社区公告',
      permission: 'announcement:view',
      roles: [USER_ROLE.RESIDENT, USER_ROLE.STAFF, USER_ROLE.ADMIN, USER_ROLE.CO_RESIDENT],
    },
  },
  {
    path: '/household',
    component: Household,
    meta: { title: '住户管理', permission: 'household:view', roles: [USER_ROLE.RESIDENT, USER_ROLE.STAFF, USER_ROLE.ADMIN, USER_ROLE.CO_RESIDENT] },
  },
  {
    path: '/profile',
    component: Profile,
    meta: { title: '个人中心', permission: 'user:profile', roles: [USER_ROLE.RESIDENT, USER_ROLE.STAFF, USER_ROLE.ADMIN, USER_ROLE.CO_RESIDENT] },
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});

registerGuards(router);
