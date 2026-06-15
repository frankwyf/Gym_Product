import { NavigationContainer, DarkTheme } from '@react-navigation/native'
import { createNativeStackNavigator } from '@react-navigation/native-stack'
import { colors } from '../constants/theme'
import { useI18n } from '../hooks/useI18n'
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
import { FacilityVenuesScreen } from '../screens/reservation/FacilityVenuesScreen'
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
  const { t } = useI18n()

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
        <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} options={{ title: t('stack.forgotPassword') }} />
        <Stack.Screen name="CourseDetail" component={CourseDetailScreen} options={{ title: t('stack.courseDetail') }} />
        <Stack.Screen name="FacilityVenues" component={FacilityVenuesScreen} options={{ title: t('stack.facilityVenues') }} />
        <Stack.Screen name="VenueDetail" component={VenueDetailScreen} options={{ title: t('stack.venueDetail') }} />
        <Stack.Screen name="PostDetail" component={PostDetailScreen} options={{ title: t('stack.postDetail') }} />
        <Stack.Screen name="SendPost" component={SendPostScreen} options={{ title: t('stack.sendPost') }} />
        <Stack.Screen name="Wallet" component={WalletScreen} options={{ title: t('stack.wallet') }} />
        <Stack.Screen name="Orders" component={OrdersScreen} options={{ title: t('stack.orders') }} />
        <Stack.Screen name="Search" component={SearchScreen} options={{ title: t('stack.search') }} />
        <Stack.Screen name="Notices" component={NoticesScreen} options={{ title: t('stack.notices') }} />
        <Stack.Screen name="Video" component={VideoScreen} options={{ title: t('stack.video') }} />
        <Stack.Screen name="CheckIn" component={CheckInScreen} options={{ title: t('stack.checkin') }} />
        <Stack.Screen name="Addresses" component={AddressesScreen} options={{ title: t('stack.addresses') }} />
      </Stack.Navigator>
    </NavigationContainer>
  )
}
