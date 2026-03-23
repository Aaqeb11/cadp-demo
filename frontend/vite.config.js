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
        target: "https://6113c1302ee6ee.lhr.life",
        changeOrigin: true,
      },
    },
  },
});
