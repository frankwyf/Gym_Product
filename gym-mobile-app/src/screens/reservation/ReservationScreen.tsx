import { useEffect, useMemo, useState } from 'react'
import { Pressable, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import type { CartItem, Facility } from '../../types/models'

export function ReservationScreen({ navigation }: { navigation: any }) {
  const [facilities, setFacilities] = useState<Facility[]>([])
  const { addToCart, cart } = useAppContext()

  useEffect(() => {
    gymApi.allVenues().then((res) => setFacilities(res.data ?? [])).catch(() => setFacilities([]))
  }, [])

  const summary = useMemo(() => cart.reduce((acc, item) => acc + Number(item.price ?? 0) * Number(item.amount ?? 1), 0), [cart])

  const quickAdd = async (facility: Facility) => {
    const cartItem: CartItem = {
      facility: Number(facility.fid ?? 0),
      name: String(facility.fname ?? 'Venue Reservation'),
      price: 0,
      amount: 1,
      active: true,
      type: 'venues'
    }
    await addToCart(cartItem)
  }

  return (
    <Screen>
      <SectionCard title="Reservation" subtitle="预约页迁移：设施列表、购物车暂存、场馆详情跳转。">
        <Text style={styles.summary}>{`Cart items: ${cart.length} · Estimated total: ¥${summary.toFixed(2)}`}</Text>
      </SectionCard>

      <SectionCard title="Facilities" subtitle="Maps from facilities / all-venues / venues flow in the mini-program.">
        {facilities.map((facility, index) => (
          <View key={`${facility.fid ?? index}`} style={styles.item}>
            <View style={styles.itemTextWrap}>
              <Text style={styles.itemTitle}>{facility.fname ?? `Facility ${index + 1}`}</Text>
              <Text style={styles.itemSubtitle}>Choose venue, time slot and quantity in detail screen.</Text>
            </View>
            <View style={styles.actions}>
              <PrimaryButton title="Detail" secondary onPress={() => navigation.navigate('VenueDetail', { facilityId: Number(facility.fid ?? 0), title: String(facility.fname ?? 'Venue') })} />
              <PrimaryButton title="Quick Add" onPress={() => void quickAdd(facility)} />
            </View>
          </View>
        ))}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  summary: {
    color: colors.text,
    fontSize: 15,
    fontWeight: '600'
  },
  item: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.sm
  },
  itemTextWrap: {
    gap: 4
  },
  itemTitle: {
    color: colors.text,
    fontSize: 16,
    fontWeight: '700'
  },
  itemSubtitle: {
    color: colors.textMuted,
    fontSize: 13
  },
  actions: {
    flexDirection: 'row',
    gap: spacing.sm,
    flexWrap: 'wrap'
  }
})
