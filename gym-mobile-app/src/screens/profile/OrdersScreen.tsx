import { useEffect, useMemo, useState } from 'react'
import { Alert, Pressable, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import type { Account, CartItem } from '../../types/models'

export function OrdersScreen() {
  const { token, cart, clearCart } = useAppContext()
  const [accounts, setAccounts] = useState<Account[]>([])
  const [selectedAccountId, setSelectedAccountId] = useState<number | null>(null)
  const [paying, setPaying] = useState(false)
  const [paidReservations, setPaidReservations] = useState<CartItem[]>([])
  const [bills, setBills] = useState<unknown[]>([])

  const selectedAccount = useMemo(() => {
    if (!selectedAccountId) {
      return null
    }
    return accounts.find((account) => Number(account.aid ?? 0) === selectedAccountId) ?? null
  }, [accounts, selectedAccountId])

  const total = useMemo(() => cart.reduce((sum, item) => {
    const price = Number(item.price ?? 0)
    const amount = Number(item.amount ?? 1)
    return sum + price * amount
  }, 0), [cart])

  const refreshRemoteData = () => {
    if (!token) {
      return
    }

    Promise.all([gymApi.accounts(token), gymApi.paidReservations(token), gymApi.bills(token)])
      .then(([accountsRes, reservationRes, billRes]) => {
        const nextAccounts = accountsRes.data ?? []
        setAccounts(nextAccounts)
        setPaidReservations(reservationRes.data ?? [])
        setBills(billRes.data ?? [])

        if (nextAccounts.length > 0 && !selectedAccountId) {
          setSelectedAccountId(Number(nextAccounts[0]?.aid ?? 0) || null)
        }
      })
      .catch(() => {
        setAccounts([])
        setPaidReservations([])
        setBills([])
      })
  }

  useEffect(() => {
    if (!token) {
      return
    }
    refreshRemoteData()
  }, [token])

  const payNow = async () => {
    if (!token) {
      Alert.alert('Login required', 'Please login first.')
      return
    }
    if (cart.length === 0) {
      Alert.alert('Tips', 'No unpaid reservations in cart.')
      return
    }
    if (!selectedAccountId) {
      Alert.alert('Tips', 'Please select an account.')
      return
    }
    if (!selectedAccount) {
      Alert.alert('Tips', 'Selected account is no longer available. Please re-select.')
      return
    }
    const accountBalance = Number(selectedAccount.balance ?? 0)
    if (!Number.isNaN(accountBalance) && accountBalance < total) {
      Alert.alert('Insufficient balance', 'Selected account balance is lower than order total.')
      return
    }

    try {
      setPaying(true)
      const result = await gymApi.payBill(token, selectedAccountId, total, cart)
      if (result.code === 0) {
        Alert.alert('Payment failed', result.msg ?? 'Unknown backend rejection')
        return
      }
      await clearCart()
      Alert.alert('Success', 'Order paid successfully.')
      refreshRemoteData()
    } catch (error) {
      Alert.alert('Payment failed', String(error))
    } finally {
      setPaying(false)
    }
  }

  const formatPaidItem = (item: CartItem) => {
    const name = item.name ?? 'Reservation'
    const amount = Number(item.amount ?? 1)
    const price = Number(item.price ?? 0)
    return `${name} · x${amount} · ¥${price}`
  }

  const formatBill = (bill: unknown) => {
    if (bill && typeof bill === 'object') {
      const anyBill = bill as Record<string, unknown>
      const bid = anyBill.bid ?? anyBill.id ?? '-'
      const totalValue = anyBill.total ?? anyBill.money ?? anyBill.amount ?? '-'
      const status = anyBill.status ?? anyBill.state ?? 'unknown'
      return `Bill #${String(bid)} · total ${String(totalValue)} · ${String(status)}`
    }
    return String(bill)
  }

  return (
    <Screen>
      <SectionCard title="Unpaid Reservations" subtitle="Maps from local cart storage in the mini-program.">
        {cart.map((item, index) => (
          <View key={`${item.name ?? index}`} style={styles.rowCard}>
            <InfoRow label="Name" value={item.name} />
            <InfoRow label="Amount" value={item.amount} />
            <InfoRow label="Price" value={`¥${item.price ?? 0}`} />
          </View>
        ))}
        <InfoRow label="Total" value={`¥${total.toFixed(2)}`} />
      </SectionCard>

      <SectionCard title="Select Account & Pay" subtitle="迁移自 to-pay-order：账号选择 + 结算调用。">
        {accounts.map((account, index) => {
          const aid = Number(account.aid ?? 0)
          const active = selectedAccountId === aid
          return (
            <Pressable key={`${aid || index}`} onPress={() => setSelectedAccountId(aid)} style={[styles.accountItem, active ? styles.accountItemActive : null]}>
              <Text style={styles.accountTitle}>{`Account ${account.aid ?? '-'}`}</Text>
              <Text style={styles.accountMeta}>{`${account.method ?? 'Unknown'} · Balance ¥${account.balance ?? 0}`}</Text>
            </Pressable>
          )
        })}
        <PrimaryButton title={paying ? 'Paying...' : 'Pay Now'} onPress={() => void payNow()} disabled={paying || cart.length === 0 || !selectedAccountId} />
      </SectionCard>

      <SectionCard title="Paid Reservations" subtitle={`Loaded ${paidReservations.length} item(s)`}>
        {paidReservations.map((item, index) => (
          <Text key={`${item.name ?? index}`} style={styles.text}>{formatPaidItem(item)}</Text>
        ))}
      </SectionCard>

      <SectionCard title="Bills" subtitle={`Loaded ${bills.length} bill record(s)`}>
        {bills.map((bill, index) => (
          <Text key={index} style={styles.text}>{formatBill(bill)}</Text>
        ))}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  rowCard: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.xs
  },
  accountItem: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 14,
    padding: spacing.md,
    gap: 4,
    borderWidth: 1,
    borderColor: colors.border
  },
  accountItemActive: {
    borderColor: colors.primary
  },
  accountTitle: {
    color: colors.text,
    fontWeight: '700'
  },
  accountMeta: {
    color: colors.textMuted,
    fontSize: 13
  },
  text: {
    color: colors.textMuted,
    lineHeight: 20
  }
})
