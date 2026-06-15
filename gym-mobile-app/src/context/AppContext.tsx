import React, { createContext, useEffect, useMemo, useState } from 'react'
import type { PropsWithChildren } from 'react'
import type { CartItem } from '../types/models'
import { getStoredCart, getStoredToken, setStoredCart, setStoredToken } from '../utils/storage'

type AppContextValue = {
  token: string | null
  cart: CartItem[]
  initialized: boolean
  setToken: (token: string | null) => Promise<void>
  setCart: (items: CartItem[]) => Promise<void>
  addToCart: (item: CartItem) => Promise<void>
  clearCart: () => Promise<void>
}

export const AppContext = createContext<AppContextValue | null>(null)

export function AppProvider({ children }: PropsWithChildren) {
  const [token, updateToken] = useState<string | null>(null)
  const [cart, updateCart] = useState<CartItem[]>([])
  const [initialized, setInitialized] = useState(false)

  useEffect(() => {
    Promise.all([getStoredToken(), getStoredCart()]).then(([storedToken, storedCart]) => {
      updateToken(storedToken)
      updateCart(storedCart)
      setInitialized(true)
    })
  }, [])

  const value = useMemo<AppContextValue>(() => ({
    token,
    cart,
    initialized,
    setToken: async (nextToken) => {
      updateToken(nextToken)
      await setStoredToken(nextToken)
    },
    setCart: async (items) => {
      updateCart(items)
      await setStoredCart(items)
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
  }), [cart, initialized, token])

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>
}
