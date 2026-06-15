const { messages } = require('../src/i18n/messages')

const locales = ['en', 'zh', 'ja']
const base = messages.en || {}
const baseKeys = Object.keys(base)

let hasError = false

for (const locale of locales) {
  if (!messages[locale]) {
    console.error(`[i18n] Missing locale object: ${locale}`)
    hasError = true
    continue
  }

  const keys = Object.keys(messages[locale])
  const missing = baseKeys.filter((key) => !(key in messages[locale]))
  const extra = keys.filter((key) => !(key in base))

  if (missing.length > 0) {
    console.error(`[i18n] ${locale} missing keys (${missing.length}):`)
    missing.forEach((key) => console.error(`  - ${key}`))
    hasError = true
  }

  if (extra.length > 0) {
    console.warn(`[i18n] ${locale} has extra keys (${extra.length}):`)
    extra.forEach((key) => console.warn(`  - ${key}`))
  }
}

if (hasError) {
  process.exit(1)
}

console.log('[i18n] Key check passed for en/zh/ja.')
