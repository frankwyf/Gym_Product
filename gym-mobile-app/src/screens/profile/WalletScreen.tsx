import { useEffect, useState } from 'react'
import { Alert, StyleSheet, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import type { Account } from '../../types/models'

export function WalletScreen() {
  const { token } = useAppContext()
  const [accounts, setAccounts] = useState<Account[]>([])
  const [balance, setBalance] = useState('')
  const [method, setMethod] = useState('Credit Card')

  const loadAccounts = () => {
    if (!token) {
      return
    }
    gymApi.accounts(token).then((res) => setAccounts(res.data ?? [])).catch(() => setAccounts([]))
  }

  useEffect(() => {
    loadAccounts()
  }, [token])

  const createAccount = async () => {
    if (!token) {
      return
    }
    try {
      await gymApi.createAccount(token, {
        balance: Number(balance || 0),
        method,
        isActive: true
      })
      setBalance('')
      loadAccounts()
    } catch (error) {
      Alert.alert('Create account failed', String(error))
    }
  }

  const chargeAccount = async (account: Account) => {
    if (!token || !account.aid) {
      return
    }
    try {
      await gymApi.chargeAccount(token, account.aid, Number(balance || 0))
      loadAccounts()
    } catch (error) {
      Alert.alert('Charge failed', String(error))
    }
  }

  return (
    <Screen>
      <SectionCard title="Wallet" subtitle="账户创建、充值、删除逻辑映射自 wallet 页面。">
        <TextInput style={styles.input} placeholder="Balance" placeholderTextColor={colors.textMuted} keyboardType="numeric" value={balance} onChangeText={setBalance} />
        <TextInput style={styles.input} placeholder="Method" placeholderTextColor={colors.textMuted} value={method} onChangeText={setMethod} />
        <PrimaryButton title="Create Account" onPress={() => void createAccount()} />
      </SectionCard>

      <SectionCard title="Accounts" subtitle={`Loaded ${accounts.length} account(s)`}>
        {accounts.map((account, index) => (
          <View key={`${account.aid ?? index}`} style={styles.accountCard}>
            <InfoRow label="ID" value={account.aid} />
            <InfoRow label="Balance" value={`¥${account.balance ?? 0}`} />
            <InfoRow label="Method" value={account.method} />
            <View style={styles.actions}>
              <PrimaryButton title="Charge" onPress={() => void chargeAccount(account)} />
              <PrimaryButton title="Delete" secondary onPress={() => token && account.aid ? gymApi.deleteAccount(token, account.aid).then(loadAccounts) : undefined} />
            </View>
          </View>
        ))}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  input: {
    backgroundColor: colors.surfaceAlt,
    color: colors.text,
    borderRadius: 14,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.sm,
    borderWidth: 1,
    borderColor: colors.border
  },
  accountCard: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.sm
  },
  actions: {
    flexDirection: 'row',
    gap: spacing.sm,
    flexWrap: 'wrap'
  }
})
