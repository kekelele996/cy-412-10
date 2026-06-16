import axios from 'axios';
import { ElMessage } from 'element-plus';

export const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('smartestate_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

request.interceptors.response.use(
  (response) => {
    const payload = response.data;
    if (payload && typeof payload === 'object' && 'success' in payload) {
      if (payload.success) {
        return payload.data;
      }
      throw new Error(payload.message || '请求失败，请稍后重试');
    }
    return payload;
  },
  (error) => {
    const message = error.response?.data?.message || '请求失败，请稍后重试';
    ElMessage.error(message);
    return Promise.reject(error);
  },
);
