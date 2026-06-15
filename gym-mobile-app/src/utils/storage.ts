import AsyncStorage from '@react-native-async-storage/async-storage'
import { STORAGE_KEYS } from '../constants/config'
import type { CartItem, ShippingAddress } from '../types/models'

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

type CheckInStore = Record<string, number[]>

async function getStoredCheckInMap(): Promise<CheckInStore> {
  const raw = await AsyncStorage.getItem(STORAGE_KEYS.checkin)
  return raw ? (JSON.parse(raw) as CheckInStore) : {}
}

export async function getStoredCheckInDays(monthKey: string): Promise<number[]> {
  const map = await getStoredCheckInMap()
  return map[monthKey] ?? []
}

export async function setStoredCheckInDays(monthKey: string, days: number[]) {
  const map = await getStoredCheckInMap()
  map[monthKey] = days
  await AsyncStorage.setItem(STORAGE_KEYS.checkin, JSON.stringify(map))
}

export async function getStoredAddresses(): Promise<ShippingAddress[]> {
  const raw = await AsyncStorage.getItem(STORAGE_KEYS.addresses)
  return raw ? (JSON.parse(raw) as ShippingAddress[]) : []
}

export async function setStoredAddresses(addresses: ShippingAddress[]) {
  await AsyncStorage.setItem(STORAGE_KEYS.addresses, JSON.stringify(addresses))
}
