import { useEffect, useMemo, useState } from 'react'
import { Alert, Pressable, StyleSheet, Text, View } from 'react-native'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { getStoredCheckInDays, setStoredCheckInDays } from '../../utils/storage'

function getMonthKey(date: Date) {
  return `${date.getFullYear()}-${date.getMonth() + 1}`
}

function getDaysInMonth(date: Date) {
  return new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate()
}

function getFirstWeekday(date: Date) {
  return new Date(date.getFullYear(), date.getMonth(), 1).getDay()
}

export function CheckInScreen() {
  const [monthDate, setMonthDate] = useState(new Date())
  const [signedDays, setSignedDays] = useState<number[]>([])

  const monthKey = useMemo(() => getMonthKey(monthDate), [monthDate])
  const currentDate = useMemo(() => new Date(), [])

  useEffect(() => {
    getStoredCheckInDays(monthKey).then((days) => setSignedDays(days))
  }, [monthKey])

  const signToday = async () => {
    const today = new Date()
    const isCurrentMonth = today.getFullYear() === monthDate.getFullYear() && today.getMonth() === monthDate.getMonth()
    if (!isCurrentMonth) {
      setMonthDate(today)
      return
    }

    const day = today.getDate()
    if (signedDays.includes(day)) {
      Alert.alert('Already checked in', 'Today has been checked in already.')
      return
    }

    const next = [...signedDays, day].sort((a, b) => a - b)
    setSignedDays(next)
    await setStoredCheckInDays(monthKey, next)
    Alert.alert('Success', 'Check-in completed.')
  }

  const previousMonth = () => {
    const next = new Date(monthDate)
    next.setMonth(next.getMonth() - 1)
    setMonthDate(next)
  }

  const nextMonth = () => {
    const next = new Date(monthDate)
    next.setMonth(next.getMonth() + 1)
    setMonthDate(next)
  }

  const daysInMonth = getDaysInMonth(monthDate)
  const firstWeekday = getFirstWeekday(monthDate)
  const monthGrid = [...Array(firstWeekday).fill(0), ...Array.from({ length: daysInMonth }, (_, idx) => idx + 1)]

  return (
    <Screen>
      <SectionCard title="Daily Check-In" subtitle="迁移自 sign-in 页面，按月打卡并持久化记录。">
        <View style={styles.monthHeader}>
          <PrimaryButton title="Prev" secondary onPress={previousMonth} />
          <Text style={styles.monthText}>{`${monthDate.getFullYear()}-${monthDate.getMonth() + 1}`}</Text>
          <PrimaryButton title="Next" secondary onPress={nextMonth} />
        </View>

        <View style={styles.weekRow}>
          {['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'].map((label) => (
            <Text key={label} style={styles.weekLabel}>{label}</Text>
          ))}
        </View>

        <View style={styles.grid}>
          {monthGrid.map((day, index) => {
            if (!day) {
              return <View key={`empty-${index}`} style={styles.emptyCell} />
            }

            const isToday = currentDate.getFullYear() === monthDate.getFullYear() && currentDate.getMonth() === monthDate.getMonth() && currentDate.getDate() === day
            const isSigned = signedDays.includes(day)

            return (
              <Pressable key={day} style={[styles.dayCell, isToday ? styles.todayCell : null, isSigned ? styles.signedCell : null]}>
                <Text style={[styles.dayText, isSigned ? styles.signedText : null]}>{isToday ? 'Today' : day}</Text>
              </Pressable>
            )
          })}
        </View>

        <PrimaryButton title="Check In Today" onPress={() => void signToday()} />
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  monthHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: spacing.sm
  },
  monthText: {
    color: colors.text,
    fontWeight: '700',
    fontSize: 16
  },
  weekRow: {
    flexDirection: 'row',
    justifyContent: 'space-between'
  },
  weekLabel: {
    color: colors.textMuted,
    width: '14.2%',
    textAlign: 'center',
    fontSize: 12,
    fontWeight: '600'
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: spacing.xs
  },
  emptyCell: {
    width: '13%',
    height: 42
  },
  dayCell: {
    width: '13%',
    minHeight: 42,
    borderRadius: 12,
    backgroundColor: colors.surfaceAlt,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 4
  },
  todayCell: {
    borderColor: colors.primary,
    borderWidth: 1
  },
  signedCell: {
    backgroundColor: colors.primary
  },
  dayText: {
    color: colors.text,
    fontSize: 12,
    fontWeight: '700'
  },
  signedText: {
    color: '#fff'
  }
})
