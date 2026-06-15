import { dictionaries } from './translations'
import type { Locale } from './translations'

export type { Locale }

export function t(locale: Locale, key: string): string {
  const dict = dictionaries[locale] ?? dictionaries.en
  const value = dict[key] ?? dictionaries.en[key]
  if (value) {
    return value
  }

  if (typeof __DEV__ !== 'undefined' && __DEV__) {
    console.warn(`[i18n] Missing key: ${key} (locale=${locale})`)
  }
  return key
}
