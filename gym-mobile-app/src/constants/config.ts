export const APP_NAME = 'Gym Product Mobile'
export const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL ?? 'http://localhost:8087/'
export const STORAGE_KEYS = {
  token: 'gym-mobile-token',
  cart: 'gym-mobile-cart',
  checkin: 'gym-mobile-checkin',
  addresses: 'gym-mobile-addresses',
  locale: 'gym-mobile-locale'
} as const
