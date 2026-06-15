import { useState } from 'react'
import { Alert, StyleSheet, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function RegisterScreen({ navigation }: { navigation: any }) {
  const { t } = useI18n()
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [captcha, setCaptcha] = useState('')
  const [serverCaptcha, setServerCaptcha] = useState('')
  const [loading, setLoading] = useState(false)

  const getCode = async () => {
    if (!emailPattern.test(email.trim())) {
      Alert.alert(t('auth.tips'), t('register.invalidEmail'))
      return
    }

    try {
      const result = await gymApi.getCaptcha(email.trim())
      if (result.code !== 1) {
        Alert.alert(t('register.getCodeFailed'), result.msg ?? 'Unknown error')
        return
      }
      setServerCaptcha(String(result.data ?? ''))
      Alert.alert(t('register.codeSent'), t('register.codeRequested'))
    } catch (error) {
      Alert.alert(t('register.getCodeFailed'), String(error))
    }
  }

  const submit = async () => {
    if (!firstName.trim() || !lastName.trim() || !username.trim() || !email.trim()) {
      Alert.alert(t('auth.tips'), t('register.fillRequired'))
      return
    }
    if (!emailPattern.test(email.trim())) {
      Alert.alert(t('auth.tips'), t('register.invalidEmail'))
      return
    }
    if (password.length < 6 || password.length > 20) {
      Alert.alert(t('auth.tips'), t('register.passwordRule'))
      return
    }
    if (password !== confirmPassword) {
      Alert.alert(t('auth.tips'), t('register.passwordMismatch'))
      return
    }
    if (!serverCaptcha || captcha.trim() !== serverCaptcha.trim()) {
      Alert.alert(t('auth.tips'), t('register.invalidCode'))
      return
    }

    try {
      setLoading(true)
      const result = await gymApi.register({
        firstName: firstName.trim(),
        lastName: lastName.trim(),
        username: username.trim(),
        email: email.trim(),
        password
      })
      if (result.code !== 1) {
        Alert.alert(t('register.registerFailed'), String(result.msg ?? result.data ?? 'Unknown error'))
        return
      }
      Alert.alert(t('register.success'), t('register.registerSuccess'), [{ text: 'OK', onPress: () => navigation.replace('Login') }])
    } catch (error) {
      Alert.alert(t('register.registerFailed'), String(error))
    } finally {
      setLoading(false)
    }
  }

  return (
    <Screen>
      <SectionCard title={t('register.title')} subtitle={t('register.subtitle')}>
        <TextInput style={styles.input} placeholder={t('register.firstName')} placeholderTextColor={colors.textMuted} value={firstName} onChangeText={setFirstName} />
        <TextInput style={styles.input} placeholder={t('register.lastName')} placeholderTextColor={colors.textMuted} value={lastName} onChangeText={setLastName} />
        <TextInput style={styles.input} placeholder={t('register.username')} placeholderTextColor={colors.textMuted} value={username} onChangeText={setUsername} autoCapitalize="none" />
        <TextInput style={styles.input} placeholder={t('register.email')} placeholderTextColor={colors.textMuted} value={email} onChangeText={setEmail} autoCapitalize="none" keyboardType="email-address" />
        <TextInput style={styles.input} placeholder={t('register.password')} placeholderTextColor={colors.textMuted} value={password} onChangeText={setPassword} secureTextEntry />
        <TextInput style={styles.input} placeholder={t('register.confirmPassword')} placeholderTextColor={colors.textMuted} value={confirmPassword} onChangeText={setConfirmPassword} secureTextEntry />
        <View style={styles.row}>
          <TextInput style={[styles.input, styles.flex]} placeholder={t('register.verificationCode')} placeholderTextColor={colors.textMuted} value={captcha} onChangeText={setCaptcha} autoCapitalize="none" />
          <PrimaryButton title={t('register.getCode')} secondary onPress={() => void getCode()} />
        </View>
        <PrimaryButton title={loading ? t('register.registering') : t('register.title')} onPress={() => void submit()} disabled={loading} />
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
