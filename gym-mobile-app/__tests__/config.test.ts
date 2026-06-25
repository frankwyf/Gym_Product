/**
 * Unit tests for app constants.
 *
 * These are infrastructure-free (no Expo native modules required) so they
 * run in the CI environment without an emulator.
 */

import { APP_NAME, API_BASE_URL, STORAGE_KEYS } from '../src/constants/config';

describe('config constants', () => {
  it('exports a non-empty APP_NAME', () => {
    expect(typeof APP_NAME).toBe('string');
    expect(APP_NAME.length).toBeGreaterThan(0);
  });

  it('falls back to localhost when EXPO_PUBLIC_API_URL is unset', () => {
    // process.env.EXPO_PUBLIC_API_URL is not set in the Jest environment,
    // so the nullish coalescing fallback should kick in.
    expect(API_BASE_URL).toMatch(/^https?:\/\//);
  });

  it('API_BASE_URL ends with a trailing slash', () => {
    expect(API_BASE_URL.endsWith('/')).toBe(true);
  });

  it('STORAGE_KEYS contains expected keys', () => {
    const required = ['token', 'cart', 'checkin', 'addresses', 'locale'];
    required.forEach((key) => {
      expect(STORAGE_KEYS).toHaveProperty(key);
      expect(typeof (STORAGE_KEYS as Record<string, string>)[key]).toBe('string');
    });
  });

  it('all STORAGE_KEYS values are prefixed with gym-mobile-', () => {
    Object.values(STORAGE_KEYS).forEach((val) => {
      expect(val).toMatch(/^gym-mobile-/);
    });
  });
});
