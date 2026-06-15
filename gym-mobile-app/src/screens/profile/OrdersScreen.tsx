import { useEffect, useState } from 'react'
import { StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import type { CartItem } from '../../types/models'

export function OrdersScreen() {
  const { token, cart } = useAppContext()
  const [paidReservations, setPaidReservations] = useState<CartItem[]>([])
  const [bills, setBills] = useState<unknown[]>([])

  useEffect(() => {
    if (!token) {
      return
    }
    Promise.all([gymApi.paidReservations(token), gymApi.bills(token)])
      .then(([reservationRes, billRes]) => {
        setPaidReservations(reservationRes.data ?? [])
        setBills(billRes.data ?? [])
      })
      .catch(() => {
        setPaidReservations([])
        setBills([])
      })
  }, [token])

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
      </SectionCard>

      <SectionCard title="Paid Reservations" subtitle={`Loaded ${paidReservations.length} item(s)`}>
        {paidReservations.map((item, index) => (
          <Text key={`${item.name ?? index}`} style={styles.text}>{JSON.stringify(item)}</Text>
        ))}
      </SectionCard>

      <SectionCard title="Bills" subtitle={`Loaded ${bills.length} bill record(s)`}>
        {bills.map((bill, index) => (
          <Text key={index} style={styles.text}>{JSON.stringify(bill)}</Text>
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
  text: {
    color: colors.textMuted,
    lineHeight: 20
  }
})
