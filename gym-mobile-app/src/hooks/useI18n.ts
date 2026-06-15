import { useAppContext } from './useAppContext'
import { t as translate } from '../i18n'

export function useI18n() {
  const { locale, setLocale } = useAppContext()
  return {
    locale,
    setLocale,
    t: (key: string) => translate(locale, key)
  }
}
