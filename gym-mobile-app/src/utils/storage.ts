import AsyncStorage from '@react-native-async-storage/async-storage'
import { STORAGE_KEYS } from '../constants/config'
import type { CartItem } from '../types/models'

export async function getStoredToken() {
  return AsyncStorage.getItem(STORAGE_KEYS.token)
}

export async function setStoredToken(token: string | null) {
  if (!token) {
    await AsyncStorage.removeItem(STORAGE_KEYS.token)
    return
  }
  await AsyncStorage.setItem(STORAGE_KEYS.token, token)
}

export async function getStoredCart(): Promise<CartItem[]> {
  const raw = await AsyncStorage.getItem(STORAGE_KEYS.cart)
  return raw ? (JSON.parse(raw) as CartItem[]) : []
}

export async function setStoredCart(items: CartItem[]) {
  await AsyncStorage.setItem(STORAGE_KEYS.cart, JSON.stringify(items))
}
