const { defineConfig } = require("@playwright/test");

module.exports = defineConfig({
  testDir: "./tests/e2e",
  timeout: 30000,
  use: {
    baseURL: "http://127.0.0.1:8081",
    headless: true,
  },
  webServer: {
    command: "npm run dev -- --port 8081",
    port: 8081,
    reuseExistingServer: true,
    timeout: 120000,
  },
});
