import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 18412,
    proxy: {
      '/api': {
        target: 'http://localhost:19412',
        changeOrigin: true,
      },
    },
  },
});
