import Vue from 'vue'
import { messages } from './messages'

const STORAGE_KEY = 'gym-ui-locale'
const initialLocale = localStorage.getItem(STORAGE_KEY)
const locale = initialLocale === 'zh' || initialLocale === 'ja' || initialLocale === 'en' ? initialLocale : 'en'

const state = Vue.observable({
  locale
})

function t(key) {
  const scoped = messages[state.locale] || messages.en
  const value = scoped[key] || messages.en[key]
  if (value) {
    return value
  }
  if (process && process.env && process.env.NODE_ENV !== 'production') {
    // Surface missing keys during development without changing production behavior.
    console.warn(`[i18n] Missing key: ${key} (locale=${state.locale})`)
  }
  return key
}

const i18n = {
  get locale() {
    return state.locale
  },
  setLocale(nextLocale) {
    if (nextLocale !== 'en' && nextLocale !== 'zh' && nextLocale !== 'ja') {
      return
    }
    state.locale = nextLocale
    localStorage.setItem(STORAGE_KEY, nextLocale)
  },
  t
}

const I18nPlugin = {
  install(VueCtor) {
    VueCtor.prototype.$i18n = i18n
    VueCtor.prototype.$tr = (key) => t(key)
  }
}

export { i18n, I18nPlugin }
