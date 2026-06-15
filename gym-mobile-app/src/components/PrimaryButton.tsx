import { Pressable, StyleSheet, Text } from 'react-native'
import { colors, spacing } from '../constants/theme'

export function PrimaryButton({ title, onPress, secondary = false, disabled = false }: { title: string; onPress?: () => void; secondary?: boolean; disabled?: boolean }) {
  return (
    <Pressable
      disabled={disabled}
      onPress={onPress}
      style={({ pressed }) => [
        styles.button,
        secondary ? styles.secondary : styles.primary,
        disabled ? styles.disabled : null,
        pressed ? styles.pressed : null
      ]}
    >
      <Text style={[styles.text, secondary ? styles.secondaryText : styles.primaryText]}>{title}</Text>
    </Pressable>
  )
}

const styles = StyleSheet.create({
  button: {
    borderRadius: 14,
    paddingVertical: spacing.sm,
    paddingHorizontal: spacing.md,
    alignItems: 'center'
  },
  primary: {
    backgroundColor: colors.primary
  },
  secondary: {
    backgroundColor: colors.surfaceAlt,
    borderWidth: 1,
    borderColor: colors.border
  },
  disabled: {
    opacity: 0.5
  },
  pressed: {
    opacity: 0.85
  },
  text: {
    fontSize: 15,
    fontWeight: '700'
  },
  primaryText: {
    color: '#fff'
  },
  secondaryText: {
    color: colors.text
  }
})
