import { useEffect, useState } from 'react'
import { Alert, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import type { Account, CustomerProfile } from '../../types/models'

export function ProfileScreen({ navigation }: { navigation: any }) {
  const { token, setToken, cart } = useAppContext()
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
      <SectionCard title="Profile" subtitle="个人中心迁移：账号资料、会员等级、钱包账户、订单和登出。">
        {token ? (
          <>
            <InfoRow label="Username" value={profile?.username} />
            <InfoRow label="Membership" value={profile?.membership} />
            <InfoRow label="Cart Items" value={cart.length} />
            <View style={styles.actions}>
              <PrimaryButton title="Wallet" onPress={() => navigation.navigate('Wallet')} />
              <PrimaryButton title="Orders" secondary onPress={() => navigation.navigate('Orders')} />
              <PrimaryButton title="Daily Check-In" secondary onPress={() => navigation.navigate('CheckIn', { title: 'Daily Check-In', description: 'Maps to pages/signin/sign-in and keeps the original daily check-in feature boundary.' })} />
              <PrimaryButton title="Addresses" secondary onPress={() => navigation.navigate('Addresses', { title: 'Addresses', description: 'Maps to pages/select-address and pages/address-add for shipping address management.' })} />
              <PrimaryButton title="Logout" secondary onPress={() => void handleLogout()} />
            </View>
          </>
        ) : (
          <>
            <Text style={styles.emptyText}>You are not logged in.</Text>
            <PrimaryButton title="Go to Login" onPress={() => navigation.navigate('Login')} />
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
