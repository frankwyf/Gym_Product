import { StyleSheet, Text } from 'react-native'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'

export function RegisterScreen() {
  return (
    <Screen>
      <SectionCard title="Register" subtitle="注册页暂保留为占位页，后续可对接小程序 regist 流程与表单字段。">
        <Text style={styles.text}>The original mini-program has registration and password recovery flows. This screen is ready to be connected next.</Text>
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
