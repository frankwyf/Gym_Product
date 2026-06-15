import { useEffect, useState } from 'react'
import { Pressable, StyleSheet, Text, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import type { Post } from '../../types/models'

const themes = ['ALL', 'Customer', 'Coach', 'Employee', 'Manager']

export function CommunityScreen({ navigation }: { navigation: any }) {
  const [posts, setPosts] = useState<Post[]>([])
  const [activeTheme, setActiveTheme] = useState('ALL')

  useEffect(() => {
    gymApi.allPosts().then((res) => setPosts(res.data ?? [])).catch(() => setPosts([]))
  }, [])

  const visiblePosts = activeTheme === 'ALL' ? posts : posts.filter((post) => post.type === activeTheme)

  return (
    <Screen>
      <SectionCard title="Community" subtitle="社区页迁移：分主题帖子流、详情、发帖入口。">
        <View style={styles.filterRow}>
          {themes.map((theme) => (
            <Pressable key={theme} style={[styles.filter, activeTheme === theme ? styles.filterActive : null]} onPress={() => setActiveTheme(theme)}>
              <Text style={[styles.filterText, activeTheme === theme ? styles.filterTextActive : null]}>{theme}</Text>
            </Pressable>
          ))}
        </View>
        <PrimaryButton title="Create Post" onPress={() => navigation.navigate('SendPost')} />
      </SectionCard>

      <SectionCard title="Posts" subtitle={`Visible posts: ${visiblePosts.length}`}>
        {visiblePosts.map((post, index) => (
          <Pressable
            key={`${post.pid ?? index}`}
            style={styles.postItem}
            onPress={() => navigation.navigate('PostDetail', { postId: Number(post.pid ?? 0), title: 'Post detail' })}
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
  }
})
