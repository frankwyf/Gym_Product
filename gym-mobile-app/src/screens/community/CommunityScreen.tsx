import { useEffect, useState } from 'react'
import { Pressable, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'
import type { Post } from '../../types/models'

const themes = ['ALL', 'Customer', 'Coach', 'Employee', 'Manager']

export function CommunityScreen({ navigation }: { navigation: any }) {
  const { t } = useI18n()
  const [posts, setPosts] = useState<Post[]>([])
  const [activeTheme, setActiveTheme] = useState('ALL')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadPosts = () => {
    setLoading(true)
    setError(null)
    gymApi.allPosts()
      .then((res) => setPosts(res.data ?? []))
      .catch(() => {
        setPosts([])
        setError('Failed to load posts. Please try again.')
      })
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadPosts()
  }, [])

  const visiblePosts = activeTheme === 'ALL' ? posts : posts.filter((post) => post.type === activeTheme)

  return (
    <Screen>
      <SectionCard title={t('community.title')} subtitle={t('community.subtitle')}>
        <View style={styles.filterRow}>
          {themes.map((theme) => (
            <Pressable key={theme} style={[styles.filter, activeTheme === theme ? styles.filterActive : null]} onPress={() => setActiveTheme(theme)}>
              <Text style={[styles.filterText, activeTheme === theme ? styles.filterTextActive : null]}>{theme}</Text>
            </Pressable>
          ))}
        </View>
        <PrimaryButton title="Create Post" onPress={() => navigation.navigate('SendPost')} />
        <PrimaryButton title={loading ? t('common.refreshing') : t('community.refreshFeed')} secondary onPress={loadPosts} disabled={loading} />
      </SectionCard>

      <SectionCard title={t('community.title')} subtitle={loading ? t('community.loading') : `Visible posts: ${visiblePosts.length}`}>
        {error ? <Text style={styles.error}>{t('community.error')}</Text> : null}
        {!loading && visiblePosts.length === 0 ? <Text style={styles.empty}>{t('community.empty')}</Text> : null}
        {visiblePosts.map((post, index) => (
          <Pressable
            key={`${post.pid ?? index}`}
            style={styles.postItem}
            onPress={() => {
              const postId = Number(post.pid ?? 0)
              if (!postId) {
                return
              }
              navigation.navigate('PostDetail', { postId, title: 'Post detail' })
            }}
          >
            <Text style={styles.postType}>{post.type ?? 'General'}</Text>
            <Text style={styles.postContent} numberOfLines={3}>{post.content ?? 'No content'}</Text>
          </Pressable>
        ))}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  filterRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: spacing.sm
  },
  filter: {
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.xs,
    borderRadius: 999,
    backgroundColor: colors.surfaceAlt
  },
  filterActive: {
    backgroundColor: colors.primary
  },
  filterText: {
    color: colors.textMuted,
    fontWeight: '700'
  },
  filterTextActive: {
    color: '#fff'
  },
  postItem: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.xs
  },
  postType: {
    color: colors.accent,
    fontWeight: '700'
  },
  postContent: {
    color: colors.text,
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
