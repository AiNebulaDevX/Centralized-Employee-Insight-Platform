import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 8080,  // Changed to 8080
    open: true,
    host: true,
    strictPort: false  // Changed to false to allow automatic port fallback
  }
})
