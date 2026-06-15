import { useEffect, useState } from 'react'
import { Pressable, StyleSheet, Text } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import type { Notice } from '../../types/models'

export function NoticesScreen() {
  const [notices, setNotices] = useState<Notice[]>([])
  const [expandedNoticeId, setExpandedNoticeId] = useState<number | null>(null)

  const loadNotices = async () => {
    try {
      const response = await gymApi.notices()
      setNotices(response.data ?? [])
    } catch {
      setNotices([])
    }
  }

  useEffect(() => {
    void loadNotices()
  }, [])

  return (
    <Screen>
      <SectionCard title="Notices" subtitle="迁移自 notices 页面，展示历史公告并可展开查看内容。">
        <PrimaryButton title="Refresh" secondary onPress={() => void loadNotices()} />
      </SectionCard>

      <SectionCard title="Historical Notices" subtitle={`Loaded ${notices.length} notice(s)`}>
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
  }
})
