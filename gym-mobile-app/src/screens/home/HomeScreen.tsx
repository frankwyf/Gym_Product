import { useEffect, useState } from 'react'
import { Linking, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import type { Coach, Facility, Notice, Slide } from '../../types/models'

export function HomeScreen({ navigation }: { navigation: any }) {
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
      <SectionCard title="Gym Product" subtitle="小程序首页迁移为原生 App 首页，保留轮播、公告、设施、教练和快速入口。">
        <View style={styles.hero}>
          <Text style={styles.heroTitle}>Train smarter, book faster.</Text>
          <Text style={styles.heroText}>Home tab mirrors the mini-program: slides, notices, facility showcase, coaches, location and video access.</Text>
        </View>
        <View style={styles.buttonRow}>
          <PrimaryButton title="Open Video" onPress={() => navigation.navigate('Video')} />
          <PrimaryButton title="Call Gym" secondary onPress={() => Linking.openURL('tel:13800000000')} />
        </View>
      </SectionCard>

      <SectionCard title="Slides" subtitle={`Loaded ${slides.length} banner items from until/homeslides`}>
        {slides.slice(0, 3).map((slide, index) => (
          <Text key={`${slide.id ?? index}`} style={styles.itemText}>{`#${index + 1} ${String(slide.title ?? slide.image ?? slide.profile ?? 'slide')}`}</Text>
        ))}
      </SectionCard>

      <SectionCard title="Notices" subtitle="Top 2 announcements, same as mini-program home page.">
        {notices.map((notice, index) => (
          <Text key={`${notice.nid ?? index}`} style={styles.itemText}>{notice.title ?? notice.content ?? `Notice ${index + 1}`}</Text>
        ))}
        <PrimaryButton title="All Notices" secondary onPress={() => navigation.navigate('Notices')} />
      </SectionCard>

      <SectionCard title="Facilities & Coaches" subtitle="Quick overview before jumping to reservation or detail pages.">
        <Text style={styles.itemText}>{`Facilities: ${facilities.length}`}</Text>
        <Text style={styles.itemText}>{`Coaches: ${coaches.length}`}</Text>
        <View style={styles.buttonRow}>
          <PrimaryButton title="All Venues" onPress={() => navigation.navigate('VenueDetail', { title: 'Venue detail' })} />
          <PrimaryButton title="Community" secondary onPress={() => navigation.navigate('MainTabs', { screen: 'Community' })} />
          <PrimaryButton title="Search" secondary onPress={() => navigation.navigate('Search', { term: '' })} />
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
