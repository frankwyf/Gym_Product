import { useEffect, useMemo, useState } from 'react'
import { Pressable, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'
import type { Facility, Venue } from '../../types/models'

export function FacilityVenuesScreen({ route, navigation }: { route: any; navigation: any }) {
  const { t } = useI18n()
  const facilityId = Number(route.params?.facilityId ?? 0)
  const [facility, setFacility] = useState<Facility | null>(null)
  const [venues, setVenues] = useState<Venue[]>([])
  const [activeIndex, setActiveIndex] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadFacilityVenues = () => {
    if (!facilityId) {
      return
    }

    setLoading(true)
    setError(null)
    Promise.all([gymApi.specificFacility(facilityId), gymApi.venuesInfoByFacility(facilityId)])
      .then(([facilityRes, venuesRes]) => {
        setFacility(facilityRes.data ?? null)
        setVenues(venuesRes.data ?? [])
        setActiveIndex(0)
      })
      .catch(() => {
        setFacility(null)
        setVenues([])
        setError('Failed to load facility venues. Please try again.')
      })
      .finally(() => {
        setLoading(false)
      })
  }

  useEffect(() => {
    loadFacilityVenues()
  }, [facilityId])

  const categories = useMemo(() => {
    const names = venues.map((item) => item.vname).filter(Boolean) as string[]
    return [...new Set(names)]
  }, [venues])

  const visibleVenues = useMemo(() => {
    if (categories.length === 0) {
      return []
    }
    const selectedCategory = categories[activeIndex] ?? categories[0]
    return venues.filter((item) => item.vname === selectedCategory)
  }, [activeIndex, categories, venues])

  return (
    <Screen>
      <SectionCard title={route.params?.title ?? facility?.fname ?? 'Facility'} subtitle={t('facilityVenues.subtitle')}>
        <Text style={styles.meta}>{facility?.location ?? 'Location unavailable'}</Text>
        <Text style={styles.desc}>{facility?.description ?? 'No facility description.'}</Text>
        <PrimaryButton title={loading ? t('common.refreshing') : t('common.refresh')} secondary onPress={loadFacilityVenues} disabled={loading || !facilityId} />
      </SectionCard>

      <SectionCard title="Venue Categories" subtitle={`Found ${categories.length} category(s)`}>
        {!loading && categories.length === 0 ? <Text style={styles.empty}>{t('facilityVenues.emptyCategories')}</Text> : null}
        <View style={styles.tabs}>
          {categories.map((name, index) => (
            <Pressable key={`${name}-${index}`} style={[styles.tab, index === activeIndex ? styles.tabActive : null]} onPress={() => setActiveIndex(index)}>
              <Text style={[styles.tabText, index === activeIndex ? styles.tabTextActive : null]}>{name}</Text>
            </Pressable>
          ))}
        </View>
      </SectionCard>

      <SectionCard title="Available Venues" subtitle={loading ? t('facilityVenues.loading') : `Visible ${visibleVenues.length} venue(s)`}>
        {error ? <Text style={styles.error}>{t('facilityVenues.error')}</Text> : null}
        {!loading && visibleVenues.length === 0 ? <Text style={styles.empty}>{t('facilityVenues.emptyVenues')}</Text> : null}
        {visibleVenues.map((venue, index) => (
          <View key={`${venue.vid ?? index}`} style={styles.venueCard}>
            {/** Guard navigation when venue id is missing from backend payload. */}
            {(() => {
              const venueId = Number(venue.vid ?? 0)
              const hasValidVenueId = venueId > 0
              return (
                <>
                  <Text style={styles.venueTitle}>{venue.vname ?? `Venue ${index + 1}`}</Text>
                  <Text style={styles.venueMeta}>{`Price: ¥${venue.price ?? 0} / hour`}</Text>
                  <Text style={styles.venueMeta}>{`Capacity: ${venue.capacity ?? '-'}`}</Text>
                  <Text style={styles.venueDesc}>{venue.description ?? 'No venue description.'}</Text>
                  <Pressable
                    style={[styles.bookBtn, !hasValidVenueId ? styles.bookBtnDisabled : null]}
                    onPress={() => {
                      if (!hasValidVenueId) {
                        return
                      }
                      navigation.navigate('VenueDetail', {
                        venueId,
                        facilityId,
                        title: venue.vname ?? 'Venue'
                      })
                    }}
                  >
                    <Text style={styles.bookBtnText}>Book A Session</Text>
                  </Pressable>
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
  meta: {
    color: colors.text,
    fontWeight: '600'
  },
  desc: {
    color: colors.textMuted,
    lineHeight: 20
  },
  tabs: {
    flexDirection: 'row',
    gap: spacing.sm,
    flexWrap: 'wrap'
  },
  tab: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 999,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.xs
  },
  tabActive: {
    backgroundColor: colors.primary
  },
  tabText: {
    color: colors.textMuted,
    fontWeight: '700'
  },
  tabTextActive: {
    color: '#fff'
  },
  venueCard: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.xs
  },
  venueTitle: {
    color: colors.text,
    fontWeight: '700',
    fontSize: 15
  },
  venueMeta: {
    color: colors.textMuted,
    fontSize: 13
  },
  venueDesc: {
    color: colors.text,
    lineHeight: 20
  },
  bookBtn: {
    marginTop: spacing.xs,
    alignSelf: 'flex-start',
    backgroundColor: colors.primary,
    borderRadius: 12,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.xs
  },
  bookBtnDisabled: {
    opacity: 0.5
  },
  bookBtnText: {
    color: '#fff',
    fontWeight: '700'
  },
  empty: {
    color: colors.textMuted,
    lineHeight: 20
  },
  error: {
    color: colors.danger,
    lineHeight: 20
  }
})
