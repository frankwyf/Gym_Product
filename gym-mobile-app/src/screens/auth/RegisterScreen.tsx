import { useState } from 'react'
import { Alert, StyleSheet, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function RegisterScreen({ navigation }: { navigation: any }) {
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
      Alert.alert('Tips', 'Email format is incorrect')
      return
    }

    try {
      const result = await gymApi.getCaptcha(email.trim())
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
    if (!firstName.trim() || !lastName.trim() || !username.trim() || !email.trim()) {
      Alert.alert('Tips', 'Please complete all required fields')
      return
    }
    if (!emailPattern.test(email.trim())) {
      Alert.alert('Tips', 'Email format is incorrect')
      return
    }
    if (password.length < 6 || password.length > 20) {
      Alert.alert('Tips', 'Password length must be 6-20')
      return
    }
    if (password !== confirmPassword) {
      Alert.alert('Tips', 'The two passwords are different')
      return
    }
    if (!serverCaptcha || captcha.trim() !== serverCaptcha.trim()) {
      Alert.alert('Tips', 'Enter the right verification code')
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
        Alert.alert('Register failed', String(result.msg ?? result.data ?? 'Unknown error'))
        return
      }
      Alert.alert('Success', 'Register success', [{ text: 'OK', onPress: () => navigation.replace('Login') }])
    } catch (error) {
      Alert.alert('Register failed', String(error))
    } finally {
      setLoading(false)
    }
  }

  return (
    <Screen>
      <SectionCard title="Register" subtitle="迁移自 regist 页面：邮箱验证码 + 用户注册。">
        <TextInput style={styles.input} placeholder="First Name" placeholderTextColor={colors.textMuted} value={firstName} onChangeText={setFirstName} />
        <TextInput style={styles.input} placeholder="Last Name" placeholderTextColor={colors.textMuted} value={lastName} onChangeText={setLastName} />
        <TextInput style={styles.input} placeholder="Username" placeholderTextColor={colors.textMuted} value={username} onChangeText={setUsername} autoCapitalize="none" />
        <TextInput style={styles.input} placeholder="Email" placeholderTextColor={colors.textMuted} value={email} onChangeText={setEmail} autoCapitalize="none" keyboardType="email-address" />
        <TextInput style={styles.input} placeholder="Password" placeholderTextColor={colors.textMuted} value={password} onChangeText={setPassword} secureTextEntry />
        <TextInput style={styles.input} placeholder="Confirm Password" placeholderTextColor={colors.textMuted} value={confirmPassword} onChangeText={setConfirmPassword} secureTextEntry />
        <View style={styles.row}>
          <TextInput style={[styles.input, styles.flex]} placeholder="Verification Code" placeholderTextColor={colors.textMuted} value={captcha} onChangeText={setCaptcha} autoCapitalize="none" />
          <PrimaryButton title="Get Code" secondary onPress={() => void getCode()} />
        </View>
        <PrimaryButton title={loading ? 'Registering...' : 'Register'} onPress={() => void submit()} disabled={loading} />
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
