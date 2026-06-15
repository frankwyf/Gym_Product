import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import { Text } from 'react-native'
import { colors } from '../constants/theme'
import { useI18n } from '../hooks/useI18n'
import { CommunityScreen } from '../screens/community/CommunityScreen'
import { CoursesScreen } from '../screens/courses/CoursesScreen'
import { HomeScreen } from '../screens/home/HomeScreen'
import { ProfileScreen } from '../screens/profile/ProfileScreen'
import { ReservationScreen } from '../screens/reservation/ReservationScreen'
import type { MainTabParamList } from './types'

const Tab = createBottomTabNavigator<MainTabParamList>()

function icon(label: string) {
  return ({ focused }: { focused: boolean }) => <Text style={{ color: focused ? colors.primary : colors.textMuted }}>{label}</Text>
}

export function MainTabs() {
  const { t } = useI18n()

  return (
    <Tab.Navigator
      screenOptions={{
        headerStyle: { backgroundColor: colors.surface },
        headerTintColor: colors.text,
        tabBarStyle: { backgroundColor: colors.surface, borderTopColor: colors.border },
        tabBarActiveTintColor: colors.primary,
        tabBarInactiveTintColor: colors.textMuted,
        sceneStyle: { backgroundColor: colors.background }
      }}
    >
      <Tab.Screen name="Home" component={HomeScreen} options={{ tabBarIcon: icon(t('tab.home')), title: t('tab.home') }} />
      <Tab.Screen name="Reservation" component={ReservationScreen} options={{ tabBarIcon: icon(t('tab.reservation')), title: t('tab.reservation') }} />
      <Tab.Screen name="Community" component={CommunityScreen} options={{ tabBarIcon: icon(t('tab.community')), title: t('tab.community') }} />
      <Tab.Screen name="Courses" component={CoursesScreen} options={{ tabBarIcon: icon(t('tab.courses')), title: t('tab.courses') }} />
      <Tab.Screen name="Profile" component={ProfileScreen} options={{ tabBarIcon: icon(t('tab.profile')), title: t('tab.profile') }} />
    </Tab.Navigator>
  )
}
