import { StyleSheet, Text } from 'react-native'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'

export function ForgotPasswordScreen() {
  return (
    <Screen>
      <SectionCard title="Forgot Password" subtitle="对应小程序 findpassword 流程。">
        <Text style={styles.text}>Password recovery API can be integrated next using the same backend route family as the mini-program.</Text>
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
