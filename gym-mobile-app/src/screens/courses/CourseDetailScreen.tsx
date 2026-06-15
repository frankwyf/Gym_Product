import { useEffect, useState } from 'react'
import { Alert, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { InfoRow } from '../../components/InfoRow'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import { useI18n } from '../../hooks/useI18n'
import type { Coach, Course } from '../../types/models'

export function CourseDetailScreen({ route, navigation }: { route: any; navigation: any }) {
  const { t } = useI18n()
  const courseId = Number(route.params?.courseId ?? 0)
  const [course, setCourse] = useState<Course | null>(null)
  const [coach, setCoach] = useState<Coach | null>(null)
  const [content, setContent] = useState('')
  const [loading, setLoading] = useState(false)
  const { addToCart } = useAppContext()

  useEffect(() => {
    if (!courseId) {
      return
    }

    setLoading(true)
    gymApi.specificCourse(courseId)
      .then((res) => {
        setCourse(res.data?.course ?? null)
        setCoach(res.data?.coach ?? null)
        setContent(String(res.data?.content ?? ''))
      })
      .catch(() => {
        setCourse(null)
        setCoach(null)
        setContent('')
      })
      .finally(() => setLoading(false))
  }, [courseId])

  const addReservation = async () => {
    if (!course) {
      return
    }

    await addToCart({
      date: String(course.time ?? ''),
      facility: Number(course.courseFacility ?? 0),
      venue: Number(course.courseVenue ?? 0),
      period: 0,
      amount: 1,
      type: 'courses',
      pic: String(course.cover ?? ''),
      name: String(course.type ?? course.name ?? course.cname ?? 'Course'),
      price: Number(course.price ?? 0),
      active: true
    })
    Alert.alert(t('register.success'), t('courseDetail.added'))
    navigation.navigate('Orders')
  }

  return (
    <Screen>
      <SectionCard title={route.params?.title ?? t('stack.courseDetail')} subtitle={t('courseDetail.subtitle')}>
        <InfoRow label={t('courseDetail.course')} value={course?.name ?? course?.cname} />
        <InfoRow label={t('courseDetail.type')} value={course?.type} />
        <InfoRow label={t('courseDetail.price')} value={course?.price ? `¥${course.price}` : '-'} />
        <InfoRow label={t('courseDetail.coach')} value={coach?.cname} />
        {content ? <Text style={styles.text}>{content}</Text> : <Text style={styles.text}>{loading ? t('courseDetail.loading') : t('courseDetail.empty')}</Text>}
        <View style={styles.actions}>
          <PrimaryButton title={t('courseDetail.addToCart')} onPress={() => void addReservation()} disabled={!course} />
          <PrimaryButton title={t('courseDetail.back')} secondary onPress={() => navigation.goBack()} />
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
