const { test, expect } = require('@playwright/test');
const { injectAxe, checkA11y } = require('axe-playwright');

test.describe('Accessibility Testing', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('https://www.zigwheels.com');
    await injectAxe(page);
  });

  test('Zigwheels - Accessibility Check', async ({ page }) => {
    await checkA11y(page);
  });
});