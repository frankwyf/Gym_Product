import { StyleSheet, Text } from 'react-native'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors } from '../../constants/theme'

export function FeatureStubScreen({ route }: { route: any }) {
  return (
    <Screen>
      <SectionCard title={route.params?.title ?? 'Feature'} subtitle="Migrated feature boundary placeholder">
        <Text style={styles.text}>{route.params?.description ?? 'This feature exists in the original mini-program and is now represented as a dedicated native route for the next implementation pass.'}</Text>
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
