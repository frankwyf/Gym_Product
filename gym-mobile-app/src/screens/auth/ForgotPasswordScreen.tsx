import { useState } from 'react'
import { Alert, StyleSheet, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function ForgotPasswordScreen({ navigation }: { navigation: any }) {
  const { t } = useI18n()
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [captcha, setCaptcha] = useState('')
  const [serverCaptcha, setServerCaptcha] = useState('')
  const [loading, setLoading] = useState(false)

  const getCode = async () => {
    if (!emailPattern.test(email.trim())) {
      Alert.alert(t('auth.tips'), t('forgot.invalidEmail'))
      return
    }

    try {
      const emails = await gymApi.getEmails()
      if (!emails.includes(email.trim())) {
        Alert.alert(t('auth.tips'), t('forgot.emailNotRegistered'))
        return
      }

      const result = await gymApi.getCaptchaReset(email.trim(), username.trim())
      if (result.code !== 1) {
        Alert.alert(t('forgot.getCodeFailed'), result.msg ?? 'Unknown error')
        return
      }
      setServerCaptcha(String(result.data ?? ''))
      Alert.alert(t('forgot.codeSent'), t('forgot.codeRequested'))
    } catch (error) {
      Alert.alert(t('forgot.getCodeFailed'), String(error))
    }
  }

  const submit = async () => {
    if (!username.trim() || !email.trim()) {
      Alert.alert(t('auth.tips'), t('forgot.usernameEmailRequired'))
      return
    }
    if (!emailPattern.test(email.trim())) {
      Alert.alert(t('auth.tips'), t('forgot.invalidEmail'))
      return
    }
    if (password.length < 6 || password.length > 20) {
      Alert.alert(t('auth.tips'), t('forgot.passwordRule'))
      return
    }
    if (password !== confirmPassword) {
      Alert.alert(t('auth.tips'), t('forgot.passwordMismatch'))
      return
    }
    if (!serverCaptcha || captcha.trim() !== serverCaptcha.trim()) {
      Alert.alert(t('auth.tips'), t('forgot.invalidCode'))
      return
    }

    try {
      setLoading(true)
      const result = await gymApi.resetPassword(username.trim(), password)
      if (result.code !== 1) {
        Alert.alert(t('forgot.resetFailed'), result.msg ?? 'Unknown error')
        return
      }
      Alert.alert(t('register.success'), t('forgot.resetSuccess'), [{ text: 'OK', onPress: () => navigation.replace('Login') }])
    } catch (error) {
      Alert.alert(t('forgot.resetFailed'), String(error))
    } finally {
      setLoading(false)
    }
  }

  return (
    <Screen>
      <SectionCard title={t('forgot.title')} subtitle={t('forgot.subtitle')}>
        <TextInput style={styles.input} placeholder={t('forgot.username')} placeholderTextColor={colors.textMuted} value={username} onChangeText={setUsername} autoCapitalize="none" />
        <TextInput style={styles.input} placeholder={t('forgot.email')} placeholderTextColor={colors.textMuted} value={email} onChangeText={setEmail} autoCapitalize="none" keyboardType="email-address" />
        <TextInput style={styles.input} placeholder={t('forgot.newPassword')} placeholderTextColor={colors.textMuted} value={password} onChangeText={setPassword} secureTextEntry />
        <TextInput style={styles.input} placeholder={t('forgot.confirmPassword')} placeholderTextColor={colors.textMuted} value={confirmPassword} onChangeText={setConfirmPassword} secureTextEntry />
        <View style={styles.row}>
          <TextInput style={[styles.input, styles.flex]} placeholder={t('forgot.verificationCode')} placeholderTextColor={colors.textMuted} value={captcha} onChangeText={setCaptcha} autoCapitalize="none" />
          <PrimaryButton title={t('forgot.getCode')} secondary onPress={() => void getCode()} />
        </View>
        <PrimaryButton title={loading ? t('forgot.submitting') : t('forgot.submit')} onPress={() => void submit()} disabled={loading} />
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
  row: {
    flexDirection: 'row',
    gap: spacing.sm,
    alignItems: 'center'
  },
  flex: {
    flex: 1
  }
})
