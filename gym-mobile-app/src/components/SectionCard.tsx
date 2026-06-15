import type { PropsWithChildren } from 'react'
import { StyleSheet, Text, View } from 'react-native'
import { colors, spacing } from '../constants/theme'

export function SectionCard({ title, subtitle, children }: PropsWithChildren<{ title: string; subtitle?: string }>) {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>{title}</Text>
      {subtitle ? <Text style={styles.subtitle}>{subtitle}</Text> : null}
      <View style={styles.content}>{children}</View>
    </View>
  )
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.surface,
    borderRadius: 20,
    padding: spacing.md,
    borderWidth: 1,
    borderColor: colors.border,
    gap: spacing.sm
  },
  title: {
    color: colors.text,
    fontSize: 20,
    fontWeight: '700'
  },
  subtitle: {
    color: colors.textMuted,
    fontSize: 13
  },
  content: {
    gap: spacing.sm
  }
})
