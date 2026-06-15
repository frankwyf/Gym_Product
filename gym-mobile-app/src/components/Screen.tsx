import type { PropsWithChildren } from 'react'
import { SafeAreaView, ScrollView, StyleSheet, View } from 'react-native'
import { colors, spacing } from '../constants/theme'

export function Screen({ children, scroll = true }: PropsWithChildren<{ scroll?: boolean }>) {
  const content = <View style={styles.inner}>{children}</View>

  return (
    <SafeAreaView style={styles.safeArea}>
      {scroll ? <ScrollView contentContainerStyle={styles.scrollContent}>{content}</ScrollView> : content}
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: colors.background
  },
  scrollContent: {
    paddingBottom: spacing.xl
  },
  inner: {
    padding: spacing.md,
    gap: spacing.md
  }
})
