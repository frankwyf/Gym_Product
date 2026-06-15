import { useState } from 'react'
import { Alert, StyleSheet, Text, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'

export function LoginScreen({ navigation }: { navigation: any }) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const { setToken } = useAppContext()

  const submit = async () => {
    if (!username.trim() || !password.trim()) {
      Alert.alert('Tips', 'Please input username and password')
      return
    }

    try {
      setLoading(true)
      const result = await gymApi.login(username.trim(), password.trim())
      if (result.code !== 1 || !result.data?.token) {
        Alert.alert('Login failed', result.msg ?? 'Username or password is wrong')
        return
      }
      await setToken(result.data.token)
      navigation.replace('MainTabs')
    } catch (error) {
      Alert.alert('Login failed', String(error))
    } finally {
      setLoading(false)
    }
  }

  return (
    <Screen>
      <SectionCard title="Login" subtitle="迁移自小程序登录页，保留账号密码登录流程。">
        <TextInput placeholder="Account" placeholderTextColor={colors.textMuted} style={styles.input} value={username} onChangeText={setUsername} autoCapitalize="none" />
        <TextInput placeholder="Password" placeholderTextColor={colors.textMuted} style={styles.input} value={password} onChangeText={setPassword} secureTextEntry />
        <PrimaryButton title={loading ? 'Login...' : 'Login'} onPress={() => void submit()} disabled={loading} />
        <View style={styles.links}>
          <Text style={styles.link} onPress={() => navigation.navigate('Register')}>Register</Text>
          <Text style={styles.link} onPress={() => navigation.navigate('ForgotPassword')}>Forgot password</Text>
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
