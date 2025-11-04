import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
    plugins: [vue()],
    server: {
        host: '0.0.0.0',  // ✅ 允许所有网络访问
        port: 5173,
        strictPort: true,  // 如果端口被占用直接退出
        // ✅ 允许所有主机（开发环境）
        allowedHosts: true
    }
})
