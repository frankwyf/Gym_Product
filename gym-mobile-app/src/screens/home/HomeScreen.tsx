import { useEffect, useState } from 'react'
import { Linking, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'
import type { Coach, Facility, Notice, Slide } from '../../types/models'

export function HomeScreen({ navigation }: { navigation: any }) {
  const { t } = useI18n()
  const [slides, setSlides] = useState<Slide[]>([])
  const [notices, setNotices] = useState<Notice[]>([])
  const [facilities, setFacilities] = useState<Facility[]>([])
  const [coaches, setCoaches] = useState<Coach[]>([])

  useEffect(() => {
    Promise.all([
      gymApi.homeSlides(),
      gymApi.notices(),
      gymApi.facilities(),
      gymApi.coaches()
    ])
      .then(([slideRes, noticeRes, facilityRes, coachRes]) => {
        setSlides(slideRes.data ?? [])
        setNotices((noticeRes.data ?? []).slice(0, 2))
        setFacilities(facilityRes.data ?? [])
        setCoaches(coachRes.data ?? [])
      })
      .catch(() => {
        setSlides([])
      })
  }, [])

  return (
    <Screen>
      <SectionCard title={t('home.title')} subtitle={t('home.subtitle')}>
        <View style={styles.hero}>
          <Text style={styles.heroTitle}>{t('home.heroTitle')}</Text>
          <Text style={styles.heroText}>{t('home.heroText')}</Text>
        </View>
        <View style={styles.buttonRow}>
          <PrimaryButton title={t('home.openVideo')} onPress={() => navigation.navigate('Video')} />
          <PrimaryButton title={t('home.callGym')} secondary onPress={() => Linking.openURL('tel:13800000000')} />
        </View>
      </SectionCard>

      <SectionCard title={t('home.slides')} subtitle={`Loaded ${slides.length} banner items from until/homeslides`}>
        {slides.slice(0, 3).map((slide, index) => (
          <Text key={`${slide.id ?? index}`} style={styles.itemText}>{`#${index + 1} ${String(slide.title ?? slide.image ?? slide.profile ?? 'slide')}`}</Text>
        ))}
      </SectionCard>

      <SectionCard title={t('home.notices')} subtitle={t('home.noticesSubtitle')}>
        {notices.map((notice, index) => (
          <Text key={`${notice.nid ?? index}`} style={styles.itemText}>{notice.title ?? notice.content ?? `Notice ${index + 1}`}</Text>
        ))}
        <PrimaryButton title={t('home.allNotices')} secondary onPress={() => navigation.navigate('Notices')} />
      </SectionCard>

      <SectionCard title={t('home.facilitiesCoaches')} subtitle={t('home.facilitiesCoachesSubtitle')}>
        <Text style={styles.itemText}>{`Facilities: ${facilities.length}`}</Text>
        <Text style={styles.itemText}>{`Coaches: ${coaches.length}`}</Text>
        <View style={styles.buttonRow}>
          <PrimaryButton title={t('home.allVenues')} onPress={() => navigation.navigate('MainTabs', { screen: 'Reservation' })} />
          <PrimaryButton title={t('home.community')} secondary onPress={() => navigation.navigate('MainTabs', { screen: 'Community' })} />
          <PrimaryButton title={t('home.search')} secondary onPress={() => navigation.navigate('Search', { term: '' })} />
        </View>
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  hero: {
    gap: 8
  },
  heroTitle: {
    color: colors.text,
    fontSize: 26,
    fontWeight: '800'
  },
  heroText: {
    color: colors.textMuted,
    fontSize: 14,
    lineHeight: 22
  },
  buttonRow: {
    flexDirection: 'row',
    gap: 12,
    flexWrap: 'wrap'
  },
  itemText: {
    color: colors.text,
    fontSize: 14
  }
})
