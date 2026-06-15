import { NavigationContainer, DarkTheme } from '@react-navigation/native'
import { createNativeStackNavigator } from '@react-navigation/native-stack'
import { colors } from '../constants/theme'
import { useAppContext } from '../hooks/useAppContext'
import { LoginScreen } from '../screens/auth/LoginScreen'
import { RegisterScreen } from '../screens/auth/RegisterScreen'
import { ForgotPasswordScreen } from '../screens/auth/ForgotPasswordScreen'
import { PostDetailScreen } from '../screens/community/PostDetailScreen'
import { SendPostScreen } from '../screens/community/SendPostScreen'
import { CourseDetailScreen } from '../screens/courses/CourseDetailScreen'
import { NoticesScreen } from '../screens/home/NoticesScreen'
import { SearchScreen } from '../screens/home/SearchScreen'
import { VideoScreen } from '../screens/home/VideoScreen'
import { AddressesScreen } from '../screens/profile/AddressesScreen'
import { CheckInScreen } from '../screens/profile/CheckInScreen'
import { OrdersScreen } from '../screens/profile/OrdersScreen'
import { WalletScreen } from '../screens/profile/WalletScreen'
import { VenueDetailScreen } from '../screens/reservation/VenueDetailScreen'
import { MainTabs } from './MainTabs'
import type { RootStackParamList } from './types'

const Stack = createNativeStackNavigator<RootStackParamList>()

const theme = {
  ...DarkTheme,
  colors: {
    ...DarkTheme.colors,
    background: colors.background,
    card: colors.surface,
    border: colors.border,
    text: colors.text,
    primary: colors.primary
  }
}

export function RootNavigator() {
  const { initialized } = useAppContext()

  if (!initialized) {
    return null
  }

  return (
    <NavigationContainer theme={theme}>
      <Stack.Navigator
        screenOptions={{
          headerStyle: { backgroundColor: colors.surface },
          headerTintColor: colors.text,
          contentStyle: { backgroundColor: colors.background }
        }}
      >
        <Stack.Screen name="MainTabs" component={MainTabs} options={{ headerShown: false }} />
        <Stack.Screen name="Login" component={LoginScreen} />
        <Stack.Screen name="Register" component={RegisterScreen} />
        <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} options={{ title: 'Forgot Password' }} />
        <Stack.Screen name="CourseDetail" component={CourseDetailScreen} options={{ title: 'Course Detail' }} />
        <Stack.Screen name="VenueDetail" component={VenueDetailScreen} options={{ title: 'Venue Detail' }} />
        <Stack.Screen name="PostDetail" component={PostDetailScreen} options={{ title: 'Post Detail' }} />
        <Stack.Screen name="SendPost" component={SendPostScreen} options={{ title: 'Create Post' }} />
        <Stack.Screen name="Wallet" component={WalletScreen} />
        <Stack.Screen name="Orders" component={OrdersScreen} />
        <Stack.Screen name="Search" component={SearchScreen} options={{ title: 'Search' }} />
        <Stack.Screen name="Notices" component={NoticesScreen} options={{ title: 'Notices' }} />
        <Stack.Screen name="Video" component={VideoScreen} options={{ title: 'Video' }} />
        <Stack.Screen name="CheckIn" component={CheckInScreen} options={{ title: 'Daily Check-In' }} />
        <Stack.Screen name="Addresses" component={AddressesScreen} options={{ title: 'Addresses' }} />
      </Stack.Navigator>
    </NavigationContainer>
  )
}
