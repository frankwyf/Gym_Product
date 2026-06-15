import { useEffect, useState } from 'react'
import { Alert, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import { useI18n } from '../../hooks/useI18n'
import type { Account, CustomerProfile } from '../../types/models'

export function ProfileScreen({ navigation }: { navigation: any }) {
  const { token, setToken, cart, locale, setLocale } = useAppContext()
  const { t } = useI18n()
  const [profile, setProfile] = useState<CustomerProfile | null>(null)
  const [accounts, setAccounts] = useState<Account[]>([])

  useEffect(() => {
    if (!token) {
      return
    }
    Promise.all([gymApi.customerInfo(token), gymApi.accounts(token)])
      .then(([profileRes, accountRes]) => {
        setProfile(profileRes.data.customer)
        setAccounts(accountRes.data ?? [])
      })
      .catch(() => {
        setProfile(null)
      })
  }, [token])

  const handleLogout = async () => {
    if (token) {
      try {
        await gymApi.logout(token)
      } catch {
        // ignore network failure for local logout
      }
    }
    await setToken(null)
  }

  const handleUpgrade = async (account: Account) => {
    if (!token || !account.aid) {
      return
    }
    try {
      await gymApi.upgradeMembership(token, account.aid, 'silver member')
      Alert.alert('Upgrade', 'Membership upgrade request submitted.')
    } catch (error) {
      Alert.alert('Upgrade failed', String(error))
    }
  }

  return (
    <Screen>
      <SectionCard title={t('profile.title')} subtitle={t('profile.subtitle')}>
        <View style={styles.languageRow}>
          <Text style={styles.languageLabel}>{t('language.title')}</Text>
          <View style={styles.languageActions}>
            <PrimaryButton title={t('language.en')} secondary onPress={() => void setLocale('en')} disabled={locale === 'en'} />
            <PrimaryButton title={t('language.zh')} secondary onPress={() => void setLocale('zh')} disabled={locale === 'zh'} />
            <PrimaryButton title={t('language.ja')} secondary onPress={() => void setLocale('ja')} disabled={locale === 'ja'} />
          </View>
        </View>
        {token ? (
          <>
            <InfoRow label="Username" value={profile?.username} />
            <InfoRow label="Membership" value={profile?.membership} />
            <InfoRow label="Cart Items" value={cart.length} />
            <View style={styles.actions}>
              <PrimaryButton title={t('profile.wallet')} onPress={() => navigation.navigate('Wallet')} />
              <PrimaryButton title={t('profile.orders')} secondary onPress={() => navigation.navigate('Orders')} />
              <PrimaryButton title={t('profile.checkin')} secondary onPress={() => navigation.navigate('CheckIn')} />
              <PrimaryButton title={t('profile.addresses')} secondary onPress={() => navigation.navigate('Addresses')} />
              <PrimaryButton title={t('profile.logout')} secondary onPress={() => void handleLogout()} />
            </View>
          </>
        ) : (
          <>
            <Text style={styles.emptyText}>{t('profile.notLoggedIn')}</Text>
            <PrimaryButton title={t('profile.goLogin')} onPress={() => navigation.navigate('Login')} />
          </>
        )}
      </SectionCard>

      <SectionCard title="Accounts" subtitle="Wallet accounts and quick membership upgrade actions.">
        {accounts.map((account, index) => (
          <View key={`${account.aid ?? index}`} style={styles.accountCard}>
            <InfoRow label="Account" value={account.aid} />
            <InfoRow label="Balance" value={`¥${account.balance ?? 0}`} />
            <InfoRow label="Method" value={account.method ?? 'Unknown'} />
            <PrimaryButton title="Upgrade via this account" onPress={() => void handleUpgrade(account)} />
          </View>
        ))}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  languageRow: {
    gap: 8
  },
  languageLabel: {
    color: colors.text,
    fontWeight: '700'
  },
  languageActions: {
    flexDirection: 'row',
    gap: 8,
    flexWrap: 'wrap'
  },
  actions: {
    gap: 12
  },
  emptyText: {
    color: colors.textMuted,
    fontSize: 14
  },
  accountCard: {
    gap: 8,
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: 16
  }
})
