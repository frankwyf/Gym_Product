import { useEffect, useState } from 'react'
import { Pressable, StyleSheet, Text } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'
import type { Notice } from '../../types/models'

export function NoticesScreen() {
  const { t } = useI18n()
  const [notices, setNotices] = useState<Notice[]>([])
  const [expandedNoticeId, setExpandedNoticeId] = useState<number | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadNotices = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await gymApi.notices()
      setNotices(response.data ?? [])
    } catch {
      setNotices([])
      setError('Failed to load notices. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadNotices()
  }, [])

  return (
    <Screen>
      <SectionCard title={t('notices.title')} subtitle={t('notices.subtitle')}>
        <PrimaryButton title={loading ? t('common.refreshing') : t('common.refresh')} secondary onPress={() => void loadNotices()} disabled={loading} />
      </SectionCard>

      <SectionCard title={t('notices.title')} subtitle={`Loaded ${notices.length} notice(s)`}>
        {error ? <Text style={styles.error}>{t('notices.error')}</Text> : null}
        {!loading && notices.length === 0 ? <Text style={styles.empty}>{t('notices.empty')}</Text> : null}
        {notices.map((notice, index) => {
          const nid = Number(notice.nid ?? index)
          const isExpanded = expandedNoticeId === nid
          return (
            <Pressable
              key={nid}
              style={styles.noticeItem}
              onPress={() => setExpandedNoticeId(isExpanded ? null : nid)}
            >
              <Text style={styles.noticeTitle}>{notice.title ?? `Notice ${index + 1}`}</Text>
              <Text numberOfLines={isExpanded ? 0 : 2} style={styles.noticeContent}>
                {notice.content ?? 'No content'}
              </Text>
            </Pressable>
          )
        })}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  noticeItem: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.xs
  },
  noticeTitle: {
    color: colors.text,
    fontSize: 15,
    fontWeight: '700'
  },
  noticeContent: {
    color: colors.textMuted,
    lineHeight: 20
  },
  empty: {
    color: colors.textMuted,
    lineHeight: 20
  },
  error: {
    color: colors.danger,
    lineHeight: 20
  }
})
