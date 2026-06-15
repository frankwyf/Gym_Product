import React, { createContext, useEffect, useMemo, useState } from 'react'
import type { PropsWithChildren } from 'react'
import type { CartItem } from '../types/models'
import type { Locale } from '../i18n'
import { getStoredCart, getStoredLocale, getStoredToken, setStoredCart, setStoredLocale, setStoredToken } from '../utils/storage'

type AppContextValue = {
  token: string | null
  cart: CartItem[]
  locale: Locale
  initialized: boolean
  setToken: (token: string | null) => Promise<void>
  setCart: (items: CartItem[]) => Promise<void>
  setLocale: (locale: Locale) => Promise<void>
  addToCart: (item: CartItem) => Promise<void>
  clearCart: () => Promise<void>
}

export const AppContext = createContext<AppContextValue | null>(null)

export function AppProvider({ children }: PropsWithChildren) {
  const [token, updateToken] = useState<string | null>(null)
  const [cart, updateCart] = useState<CartItem[]>([])
  const [locale, updateLocale] = useState<Locale>('en')
  const [initialized, setInitialized] = useState(false)

  useEffect(() => {
    Promise.all([getStoredToken(), getStoredCart(), getStoredLocale()]).then(([storedToken, storedCart, storedLocale]) => {
      updateToken(storedToken)
      updateCart(storedCart)
      updateLocale(storedLocale ?? 'en')
      setInitialized(true)
    })
  }, [])

  const value = useMemo<AppContextValue>(() => ({
    token,
    cart,
    locale,
    initialized,
    setToken: async (nextToken) => {
      updateToken(nextToken)
      await setStoredToken(nextToken)
    },
    setCart: async (items) => {
      updateCart(items)
      await setStoredCart(items)
    },
    setLocale: async (nextLocale) => {
      updateLocale(nextLocale)
      await setStoredLocale(nextLocale)
    },
    addToCart: async (item) => {
      const nextItems = [...cart, item]
      updateCart(nextItems)
      await setStoredCart(nextItems)
    },
    clearCart: async () => {
      updateCart([])
      await setStoredCart([])
    }
  }), [cart, initialized, locale, token])

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>
}
