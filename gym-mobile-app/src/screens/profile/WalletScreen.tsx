import { useEffect, useState } from 'react'
import { Alert, StyleSheet, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import { useI18n } from '../../hooks/useI18n'
import type { Account } from '../../types/models'

export function WalletScreen() {
  const { t } = useI18n()
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
    const numericBalance = Number(balance || 0)
    if (Number.isNaN(numericBalance) || numericBalance < 0) {
      Alert.alert(t('wallet.invalidBalanceTitle'), t('wallet.invalidBalanceMsg'))
      return
    }
    if (!method.trim()) {
      Alert.alert(t('wallet.invalidMethodTitle'), t('wallet.invalidMethodMsg'))
      return
    }
    try {
      await gymApi.createAccount(token, {
        balance: numericBalance,
        method: method.trim(),
        isActive: true
      })
      setBalance('')
      loadAccounts()
    } catch (error) {
      Alert.alert(t('wallet.createFailed'), String(error))
    }
  }

  const chargeAccount = async (account: Account) => {
    if (!token || !account.aid) {
      return
    }
    const numericBalance = Number(balance || 0)
    if (Number.isNaN(numericBalance) || numericBalance <= 0) {
      Alert.alert(t('wallet.invalidAmountTitle'), t('wallet.invalidAmountMsg'))
      return
    }
    try {
      await gymApi.chargeAccount(token, account.aid, numericBalance)
      loadAccounts()
    } catch (error) {
      Alert.alert(t('wallet.chargeFailed'), String(error))
    }
  }

  const deleteAccount = async (account: Account) => {
    if (!token || !account.aid) {
      return
    }

    try {
      await gymApi.deleteAccount(token, account.aid)
      loadAccounts()
    } catch (error) {
      Alert.alert(t('wallet.deleteFailed'), String(error))
    }
  }

  return (
    <Screen>
      <SectionCard title={t('wallet.title')} subtitle={t('wallet.subtitle')}>
        <TextInput style={styles.input} placeholder={t('wallet.balance')} placeholderTextColor={colors.textMuted} keyboardType="numeric" value={balance} onChangeText={setBalance} />
        <TextInput style={styles.input} placeholder={t('wallet.method')} placeholderTextColor={colors.textMuted} value={method} onChangeText={setMethod} />
        <PrimaryButton title={t('wallet.createAccount')} onPress={() => void createAccount()} />
      </SectionCard>

      <SectionCard title={t('wallet.accounts')} subtitle={`Loaded ${accounts.length} account(s)`}>
        {accounts.map((account, index) => (
          <View key={`${account.aid ?? index}`} style={styles.accountCard}>
            <InfoRow label="ID" value={account.aid} />
            <InfoRow label="Balance" value={`¥${account.balance ?? 0}`} />
            <InfoRow label="Method" value={account.method} />
            <View style={styles.actions}>
              <PrimaryButton title={t('wallet.charge')} onPress={() => void chargeAccount(account)} />
              <PrimaryButton title={t('wallet.delete')} secondary onPress={() => void deleteAccount(account)} />
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
