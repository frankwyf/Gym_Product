import { useEffect, useState } from 'react'
import { Pressable, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import { useI18n } from '../../hooks/useI18n'
import type { Venue } from '../../types/models'

export function VenueDetailScreen({ route, navigation }: { route: any; navigation: any }) {
  const { t } = useI18n()
  const [venue, setVenue] = useState<Venue | null>(null)
  const [caps, setCaps] = useState<number[]>([])
  const [selectedPeriod, setSelectedPeriod] = useState<number>(0)
  const { addToCart } = useAppContext()
  const venueId = Number(route.params?.venueId ?? 0)

  useEffect(() => {
    if (!venueId) {
      return
    }
    gymApi.venueById(venueId)
      .then((res) => {
        setVenue(res.data?.[0]?.venue ?? null)
        const capacityList = res.data?.[0]?.cap ?? []
        setCaps(capacityList)
        const firstAvailable = capacityList.findIndex((value) => value > 0)
        setSelectedPeriod(firstAvailable >= 0 ? firstAvailable : 0)
      })
      .catch(() => {
        setVenue(null)
        setCaps([])
      })
  }, [venueId])

  const addReservation = async () => {
    if (!venue) {
      return
    }
    await addToCart({
      venue: venueId,
      name: venue.vname,
      price: venue.price,
      period: selectedPeriod,
      amount: 1,
      active: true,
      type: 'venues'
    })
    navigation.navigate('Orders')
  }

  return (
    <Screen>
      <SectionCard title={route.params?.title ?? t('stack.venueDetail')} subtitle={t('venueDetail.subtitle')}>
        <InfoRow label={t('venueDetail.venue')} value={venue?.vname} />
        <InfoRow label={t('venueDetail.price')} value={venue?.price ? `¥${venue.price}` : '-'} />
        <Text style={styles.text}>{t('venueDetail.tip')}</Text>
        <View style={styles.periodWrap}>
          {caps.map((cap, index) => {
            const selectable = cap > 0
            const active = selectedPeriod === index
            return (
              <Pressable
                key={index}
                onPress={() => {
                  if (selectable) {
                    setSelectedPeriod(index)
                  }
                }}
                style={[styles.periodItem, active ? styles.periodItemActive : null, !selectable ? styles.periodItemDisabled : null]}
              >
                <Text style={[styles.periodText, active ? styles.periodTextActive : null]}>{`${t('venueDetail.period')} ${index + 1}`}</Text>
                <Text style={styles.periodCap}>{`${t('venueDetail.available')}: ${cap}`}</Text>
              </Pressable>
            )
          })}
        </View>
        <View style={styles.actions}>
          <PrimaryButton title={t('venueDetail.addReservation')} onPress={() => void addReservation()} disabled={!venue || (caps.length > 0 && caps[selectedPeriod] <= 0)} />
          <PrimaryButton title={t('venueDetail.backReservation')} secondary onPress={() => navigation.goBack()} />
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
  periodWrap: {
    gap: 8
  },
  periodItem: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 12,
    padding: 12,
    gap: 4,
    borderWidth: 1,
    borderColor: colors.border
  },
  periodItemActive: {
    borderColor: colors.primary
  },
  periodItemDisabled: {
    opacity: 0.45
  },
  periodText: {
    color: colors.text,
    fontWeight: '700'
  },
  periodTextActive: {
    color: colors.primary
  },
  periodCap: {
    color: colors.textMuted,
    fontSize: 12
  },
  actions: {
    gap: 12
  }
})
