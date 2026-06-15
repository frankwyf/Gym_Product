import { useEffect, useMemo, useState } from 'react'
import { Pressable, StyleSheet, Text, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import type { Course } from '../../types/models'

export function CoursesScreen({ navigation }: { navigation: any }) {
  const [courses, setCourses] = useState<Course[]>([])
  const [activeCategory, setActiveCategory] = useState('All')
  const [term, setTerm] = useState('')

  useEffect(() => {
    gymApi.allCourses().then((res) => setCourses(res.data ?? [])).catch(() => setCourses([]))
  }, [])

  const categories = useMemo(() => ['All', ...new Set(courses.map((item) => item.type).filter(Boolean) as string[])], [courses])
  const filtered = useMemo(() => {
    return courses.filter((course) => {
      const typeMatch = activeCategory === 'All' || course.type === activeCategory
      const termMatch = !term || JSON.stringify(course).toLowerCase().includes(term.toLowerCase())
      return typeMatch && termMatch
    })
  }, [activeCategory, courses, term])

  return (
    <Screen>
      <SectionCard title="Courses" subtitle="课程页迁移：分类筛选 + 搜索 + 详情跳转。">
        <TextInput
          placeholder="Search course"
          placeholderTextColor={colors.textMuted}
          style={styles.input}
          value={term}
          onChangeText={setTerm}
        />
        <View style={styles.chips}>
          {categories.map((category) => (
            <Pressable key={category} onPress={() => setActiveCategory(category)} style={[styles.chip, activeCategory === category ? styles.chipActive : null]}>
              <Text style={[styles.chipText, activeCategory === category ? styles.chipTextActive : null]}>{category}</Text>
            </Pressable>
          ))}
        </View>
      </SectionCard>

      <SectionCard title="Course List" subtitle={`Showing ${filtered.length} item(s)`}>
        {filtered.map((course, index) => (
          <Pressable
            key={`${course.id ?? course.cid ?? index}`}
            style={styles.listItem}
            onPress={() => navigation.navigate('CourseDetail', { courseId: Number(course.id ?? course.cid ?? 0), title: String(course.name ?? course.cname ?? 'Course') })}
          >
            <Text style={styles.name}>{course.name ?? course.cname ?? 'Unnamed course'}</Text>
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
