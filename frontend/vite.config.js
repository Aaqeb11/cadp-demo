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
        target: "https://f5baba88e8cc18.lhr.life",
        changeOrigin: true,
      },
    },
  },
});
