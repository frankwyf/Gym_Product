const { test, expect, chromium } = require('@playwright/test')
const { existsSync } = require('fs')

const browserAvailable = existsSync(chromium.executablePath())

test.skip(!browserAvailable, 'Playwright browser binary is not installed in current environment')

test('login page should render username/password form', async ({ page }) => {
  await page.goto('/login')
  await expect(page).toHaveURL(/\/login/)

  const inputCount = await page.locator('input').count()
  expect(inputCount).toBeGreaterThanOrEqual(2)
})
