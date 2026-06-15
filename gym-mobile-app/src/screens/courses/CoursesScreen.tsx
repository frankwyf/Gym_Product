import { useEffect, useMemo, useState } from 'react'
import { Pressable, StyleSheet, Text, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'
import type { Course } from '../../types/models'

export function CoursesScreen({ navigation }: { navigation: any }) {
  const { t } = useI18n()
  const [courses, setCourses] = useState<Course[]>([])
  const [activeCategory, setActiveCategory] = useState('all')
  const [term, setTerm] = useState('')

  useEffect(() => {
    gymApi.allCourses().then((res) => setCourses(res.data ?? [])).catch(() => setCourses([]))
  }, [])

  const categories = useMemo(() => ['all', ...new Set(courses.map((item) => item.type).filter(Boolean) as string[])], [courses])
  const filtered = useMemo(() => {
    const normalized = term.trim().toLowerCase()
    return courses.filter((course) => {
      const typeMatch = activeCategory === 'all' || course.type === activeCategory
      const haystack = [
        course.name,
        course.cname,
        course.type,
        String(course.price ?? ''),
        course.profile
      ].filter(Boolean).join(' ').toLowerCase()
      const termMatch = !normalized || haystack.includes(normalized)
      return typeMatch && termMatch
    })
  }, [activeCategory, courses, term])

  return (
    <Screen>
      <SectionCard title={t('courses.title')} subtitle={t('courses.subtitle')}>
        <TextInput
          placeholder={t('courses.searchCourse')}
          placeholderTextColor={colors.textMuted}
          style={styles.input}
          value={term}
          onChangeText={setTerm}
        />
        <View style={styles.chips}>
          {categories.map((category) => (
            <Pressable key={category} onPress={() => setActiveCategory(category)} style={[styles.chip, activeCategory === category ? styles.chipActive : null]}>
              <Text style={[styles.chipText, activeCategory === category ? styles.chipTextActive : null]}>{category === 'all' ? t('courses.all') : category}</Text>
            </Pressable>
          ))}
        </View>
      </SectionCard>

      <SectionCard title={t('courses.courseList')} subtitle={`Showing ${filtered.length} item(s)`}>
        {filtered.map((course, index) => (
          <Pressable
            key={`${course.id ?? course.cid ?? index}`}
            style={styles.listItem}
            onPress={() => navigation.navigate('CourseDetail', { courseId: Number(course.id ?? course.cid ?? 0), title: String(course.name ?? course.cname ?? 'Course') })}
          >
            <Text style={styles.name}>{course.name ?? course.cname ?? t('courses.unnamed')}</Text>
            <Text style={styles.meta}>{`${course.type ?? 'General'} · ¥${course.price ?? 0}`}</Text>
          </Pressable>
        ))}
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
  chips: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: spacing.sm
  },
  chip: {
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.xs,
    borderRadius: 999,
    backgroundColor: colors.surfaceAlt
  },
  chipActive: {
    backgroundColor: colors.primary
  },
  chipText: {
    color: colors.textMuted,
    fontWeight: '600'
  },
  chipTextActive: {
    color: '#fff'
  },
  listItem: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: 4
  },
  name: {
    color: colors.text,
    fontSize: 16,
    fontWeight: '700'
  },
  meta: {
    color: colors.textMuted,
    fontSize: 13
  }
})
