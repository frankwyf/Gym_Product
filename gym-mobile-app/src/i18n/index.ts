import { dictionaries } from './translations'
import type { Locale } from './translations'

export type { Locale }

export function t(locale: Locale, key: string): string {
  const dict = dictionaries[locale] ?? dictionaries.en
  return dict[key] ?? dictionaries.en[key] ?? key
}
