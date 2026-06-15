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
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const { addToCart, cart } = useAppContext()

  const loadFacilities = () => {
    setLoading(true)
    setError(null)
    gymApi.allVenues()
      .then((res) => setFacilities(res.data ?? []))
      .catch(() => {
        setFacilities([])
        setError('Failed to load facilities. Please try again.')
      })
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadFacilities()
  }, [])

  const summary = useMemo(() => cart.reduce((acc, item) => acc + Number(item.price ?? 0) * Number(item.amount ?? 1), 0), [cart])

  const quickAdd = async (facility: Facility) => {
    const facilityId = Number(facility.fid ?? 0)
    if (!facilityId) {
      return
    }

    const cartItem: CartItem = {
      facility: facilityId,
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

      <SectionCard title="Facilities" subtitle={loading ? 'Loading facilities...' : 'Maps from facilities / all-venues / venues flow in the mini-program.'}>
        <PrimaryButton title={loading ? 'Refreshing...' : 'Refresh Facilities'} secondary onPress={loadFacilities} disabled={loading} />
        {error ? <Text style={styles.error}>{error}</Text> : null}
        {!loading && facilities.length === 0 ? <Text style={styles.empty}>No facilities available right now.</Text> : null}
        {facilities.map((facility, index) => (
          <View key={`${facility.fid ?? index}`} style={styles.item}>
            {/** Keep action buttons safe when backend returns invalid IDs. */}
            {(() => {
              const facilityId = Number(facility.fid ?? 0)
              const hasValidFacilityId = facilityId > 0
              return (
                <>
                  <View style={styles.itemTextWrap}>
                    <Text style={styles.itemTitle}>{facility.fname ?? `Facility ${index + 1}`}</Text>
                    <Text style={styles.itemSubtitle}>Choose venue, time slot and quantity in detail screen.</Text>
                  </View>
                  <View style={styles.actions}>
                    <PrimaryButton
                      title="Detail"
                      secondary
                      onPress={() => navigation.navigate('FacilityVenues', { facilityId, title: String(facility.fname ?? 'Facility') })}
                      disabled={!hasValidFacilityId}
                    />
                    <PrimaryButton title="Quick Add" onPress={() => void quickAdd(facility)} disabled={!hasValidFacilityId} />
                  </View>
                </>
              )
            })()}
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
  empty: {
    color: colors.textMuted,
    fontSize: 13
  },
  error: {
    color: colors.danger,
    fontSize: 13
  },
  actions: {
    flexDirection: 'row',
    gap: spacing.sm,
    flexWrap: 'wrap'
  }
})
