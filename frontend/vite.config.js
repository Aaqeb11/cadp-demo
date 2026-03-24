import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5173,
    allowedHosts: ["all", "legend-rhizocephalous-magaret.ngrok-free.dev"],
    proxy: {
      "/api": {
        target: "https://6fdf6db5826f0b.lhr.life",
        changeOrigin: true,
      },
    },
  },
});
