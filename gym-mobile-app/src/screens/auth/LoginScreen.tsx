import { useState } from 'react'
import { Alert, StyleSheet, Text, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import { useI18n } from '../../hooks/useI18n'

export function LoginScreen({ navigation }: { navigation: any }) {
  const { t } = useI18n()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const { setToken } = useAppContext()

  const submit = async () => {
    if (!username.trim() || !password.trim()) {
      Alert.alert(t('auth.tips'), t('auth.inputUsernamePassword'))
      return
    }

    try {
      setLoading(true)
      const result = await gymApi.login(username.trim(), password.trim())
      if (result.code !== 1 || !result.data?.token) {
        Alert.alert(t('auth.loginFailed'), result.msg ?? t('auth.loginWrong'))
        return
      }
      await setToken(result.data.token)
      navigation.replace('MainTabs')
    } catch (error) {
      Alert.alert(t('auth.loginFailed'), String(error))
    } finally {
      setLoading(false)
    }
  }

  return (
    <Screen>
      <SectionCard title={t('auth.loginTitle')} subtitle={t('auth.loginSubtitle')}>
        <TextInput placeholder={t('auth.account')} placeholderTextColor={colors.textMuted} style={styles.input} value={username} onChangeText={setUsername} autoCapitalize="none" />
        <TextInput placeholder={t('auth.password')} placeholderTextColor={colors.textMuted} style={styles.input} value={password} onChangeText={setPassword} secureTextEntry />
        <PrimaryButton title={loading ? t('auth.loggingIn') : t('auth.login')} onPress={() => void submit()} disabled={loading} />
        <View style={styles.links}>
          <Text style={styles.link} onPress={() => navigation.navigate('Register')}>{t('auth.register')}</Text>
          <Text style={styles.link} onPress={() => navigation.navigate('ForgotPassword')}>{t('auth.forgotPassword')}</Text>
        </View>
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
  links: {
    flexDirection: 'row',
    justifyContent: 'space-between'
  },
  link: {
    color: colors.accent,
    fontWeight: '700'
  }
})
