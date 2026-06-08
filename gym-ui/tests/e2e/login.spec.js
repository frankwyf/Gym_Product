const { test, expect } = require("@playwright/test");

test("login page should render username/password form", async ({ page }) => {
  await page.goto("/login");
  await expect(page).toHaveURL(/\/login/);

  const inputCount = await page.locator("input").count();
  expect(inputCount).toBeGreaterThanOrEqual(2);
});
