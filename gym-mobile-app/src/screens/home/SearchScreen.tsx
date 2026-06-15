import { useEffect, useMemo, useState } from 'react'
import { Alert, Pressable, StyleSheet, Text, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import type { Course, Facility, Venue } from '../../types/models'

export function SearchScreen({ navigation, route }: { navigation: any; route: any }) {
  const [term, setTerm] = useState(String(route.params?.term ?? ''))
  const [facilities, setFacilities] = useState<Facility[]>([])
  const [venues, setVenues] = useState<Venue[]>([])
  const [courses, setCourses] = useState<Course[]>([])
  const [loading, setLoading] = useState(false)

  const total = useMemo(() => facilities.length + venues.length + courses.length, [facilities.length, venues.length, courses.length])

  const runSearch = async () => {
    if (!term.trim()) {
      Alert.alert('Tips', 'Please enter a keyword')
      return
    }

    try {
      setLoading(true)
      const result = await gymApi.search(term.trim())
      setFacilities(result.data?.facilities ?? [])
      setVenues(result.data?.venues ?? [])
      setCourses(result.data?.courses ?? [])
    } catch (error) {
      Alert.alert('Search failed', String(error))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (term.trim()) {
      void runSearch()
    }
  }, [])

  return (
    <Screen>
      <SectionCard title="Search" subtitle="迁移自 Search-result：按关键词聚合课程、设施、场馆。">
        <TextInput
          style={styles.input}
          value={term}
          onChangeText={setTerm}
          placeholder="Search keyword"
          placeholderTextColor={colors.textMuted}
          autoCapitalize="none"
          onSubmitEditing={() => void runSearch()}
        />
        <PrimaryButton title={loading ? 'Searching...' : 'Search'} onPress={() => void runSearch()} disabled={loading} />
      </SectionCard>

      <SectionCard title="Results" subtitle={`Total ${total} item(s)`}>
        <View style={styles.group}>
          <Text style={styles.groupTitle}>{`Facilities (${facilities.length})`}</Text>
          {facilities.map((facility, index) => (
            <Pressable
              key={`${facility.fid ?? index}`}
              style={styles.item}
              onPress={() => navigation.navigate('MainTabs', { screen: 'Reservation' })}
            >
              <Text style={styles.itemTitle}>{facility.fname ?? `Facility ${index + 1}`}</Text>
              <Text style={styles.itemMeta}>Open reservation list</Text>
            </Pressable>
          ))}
        </View>

        <View style={styles.group}>
          <Text style={styles.groupTitle}>{`Venues (${venues.length})`}</Text>
          {venues.map((venue, index) => (
            <Pressable
              key={`${venue.vid ?? index}`}
              style={styles.item}
              onPress={() => navigation.navigate('VenueDetail', { venueId: Number(venue.vid ?? 0), title: venue.vname ?? 'Venue' })}
            >
              <Text style={styles.itemTitle}>{venue.vname ?? `Venue ${index + 1}`}</Text>
              <Text style={styles.itemMeta}>{`¥${venue.price ?? 0}`}</Text>
            </Pressable>
          ))}
        </View>

        <View style={styles.group}>
          <Text style={styles.groupTitle}>{`Courses (${courses.length})`}</Text>
          {courses.map((course, index) => (
            <Pressable
              key={`${course.id ?? course.cid ?? index}`}
              style={styles.item}
              onPress={() => navigation.navigate('CourseDetail', { courseId: Number(course.id ?? course.cid ?? 0), title: course.name ?? course.cname ?? 'Course' })}
            >
              <Text style={styles.itemTitle}>{course.name ?? course.cname ?? `Course ${index + 1}`}</Text>
              <Text style={styles.itemMeta}>{`${course.type ?? 'General'} · ¥${course.price ?? 0}`}</Text>
            </Pressable>
          ))}
        </View>
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
  group: {
    gap: spacing.xs
  },
  groupTitle: {
    color: colors.text,
    fontWeight: '700',
    fontSize: 15
  },
  item: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 14,
    padding: spacing.md,
    gap: 4
  },
  itemTitle: {
    color: colors.text,
    fontWeight: '600',
    fontSize: 14
  },
  itemMeta: {
    color: colors.textMuted,
    fontSize: 12
  }
})
