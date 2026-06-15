import { StyleSheet, Text } from 'react-native'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'

export function CourseDetailScreen({ route }: { route: any }) {
  return (
    <Screen>
      <SectionCard title={route.params?.title ?? 'Course Detail'} subtitle="详情页占位骨架，对应小程序 goods-details。">
        <Text style={styles.text}>The detail screen is wired into navigation. Next iteration can bind the course detail endpoint once backend response shape is confirmed.</Text>
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  text: {
    color: colors.textMuted,
    lineHeight: 22
  }
})
