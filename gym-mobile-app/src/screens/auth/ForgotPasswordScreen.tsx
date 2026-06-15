import { useState } from 'react'
import { Alert, StyleSheet, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function ForgotPasswordScreen({ navigation }: { navigation: any }) {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [captcha, setCaptcha] = useState('')
  const [serverCaptcha, setServerCaptcha] = useState('')
  const [loading, setLoading] = useState(false)

  const getCode = async () => {
    if (!emailPattern.test(email.trim())) {
      Alert.alert('Tips', 'Enter a valid email address')
      return
    }

    try {
      const emails = await gymApi.getEmails()
      if (!emails.includes(email.trim())) {
        Alert.alert('Tips', 'The email is not registered')
        return
      }

      const result = await gymApi.getCaptchaReset(email.trim(), username.trim())
      if (result.code !== 1) {
        Alert.alert('Get code failed', result.msg ?? 'Unknown error')
        return
      }
      setServerCaptcha(String(result.data ?? ''))
      Alert.alert('Code sent', 'Verification code has been requested from backend.')
    } catch (error) {
      Alert.alert('Get code failed', String(error))
    }
  }

  const submit = async () => {
    if (!username.trim() || !email.trim()) {
      Alert.alert('Tips', 'Username and email are required')
      return
    }
    if (!emailPattern.test(email.trim())) {
      Alert.alert('Tips', 'Enter a valid email address')
      return
    }
    if (password.length < 6 || password.length > 20) {
      Alert.alert('Tips', 'Password length is 6-20')
      return
    }
    if (password !== confirmPassword) {
      Alert.alert('Tips', 'The two passwords are different')
      return
    }
    if (!serverCaptcha || captcha.trim() !== serverCaptcha.trim()) {
      Alert.alert('Tips', 'Enter the correct verification code')
      return
    }

    try {
      setLoading(true)
      const result = await gymApi.resetPassword(username.trim(), password)
      if (result.code !== 1) {
        Alert.alert('Reset failed', result.msg ?? 'Unknown error')
        return
      }
      Alert.alert('Success', 'Password reset success', [{ text: 'OK', onPress: () => navigation.replace('Login') }])
    } catch (error) {
      Alert.alert('Reset failed', String(error))
    } finally {
      setLoading(false)
    }
  }

  return (
    <Screen>
      <SectionCard title="Forgot Password" subtitle="对应小程序 findpassword 流程。">
        <TextInput style={styles.input} placeholder="Username" placeholderTextColor={colors.textMuted} value={username} onChangeText={setUsername} autoCapitalize="none" />
        <TextInput style={styles.input} placeholder="Email" placeholderTextColor={colors.textMuted} value={email} onChangeText={setEmail} autoCapitalize="none" keyboardType="email-address" />
        <TextInput style={styles.input} placeholder="New Password" placeholderTextColor={colors.textMuted} value={password} onChangeText={setPassword} secureTextEntry />
        <TextInput style={styles.input} placeholder="Confirm Password" placeholderTextColor={colors.textMuted} value={confirmPassword} onChangeText={setConfirmPassword} secureTextEntry />
        <View style={styles.row}>
          <TextInput style={[styles.input, styles.flex]} placeholder="Verification Code" placeholderTextColor={colors.textMuted} value={captcha} onChangeText={setCaptcha} autoCapitalize="none" />
          <PrimaryButton title="Get Code" secondary onPress={() => void getCode()} />
        </View>
        <PrimaryButton title={loading ? 'Submitting...' : 'Submit'} onPress={() => void submit()} disabled={loading} />
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
