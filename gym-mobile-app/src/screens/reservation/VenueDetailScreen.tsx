import { useEffect, useState } from 'react'
import { StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import type { Venue } from '../../types/models'

export function VenueDetailScreen({ route, navigation }: { route: any; navigation: any }) {
  const [venue, setVenue] = useState<Venue | null>(null)
  const { addToCart } = useAppContext()
  const venueId = Number(route.params?.venueId ?? 0)

  useEffect(() => {
    if (!venueId) {
      return
    }
    gymApi.venueById(venueId)
      .then((res) => setVenue(res.data?.[0]?.venue ?? null))
      .catch(() => setVenue(null))
  }, [venueId])

  const addReservation = async () => {
    if (!venue) {
      return
    }
    await addToCart({
      venue: venueId,
      name: venue.vname,
      price: venue.price,
      amount: 1,
      active: true,
      type: 'venues'
    })
    navigation.navigate('Orders')
  }

  return (
    <Screen>
      <SectionCard title={route.params?.title ?? 'Venue Detail'} subtitle="迁移自 venues 页面，保留场馆信息与预约加入购物车入口。">
        <InfoRow label="Venue" value={venue?.vname} />
        <InfoRow label="Price" value={venue?.price ? `¥${venue.price}` : '-'} />
        <Text style={styles.text}>Time slot matrix and occupancy rules from the mini-program can be layered onto this screen in the next pass.</Text>
        <View style={styles.actions}>
          <PrimaryButton title="Add Reservation" onPress={() => void addReservation()} />
          <PrimaryButton title="Back to Reservation" secondary onPress={() => navigation.goBack()} />
        </View>
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  text: {
    color: colors.textMuted,
    lineHeight: 22
  },
  actions: {
    gap: 12
  }
})
