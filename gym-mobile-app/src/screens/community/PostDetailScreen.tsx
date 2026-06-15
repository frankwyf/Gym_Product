import { useEffect, useState } from 'react'
import { Alert, StyleSheet, Text, TextInput, View } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'
import { useI18n } from '../../hooks/useI18n'
import type { Comment, Post } from '../../types/models'

export function PostDetailScreen({ route }: { route: any }) {
  const { t } = useI18n()
  const postId = Number(route.params?.postId ?? 0)
  const [post, setPost] = useState<Post | null>(null)
  const [comments, setComments] = useState<Comment[]>([])
  const [content, setContent] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { token } = useAppContext()

  useEffect(() => {
    if (!postId) {
      return
    }
    Promise.all([gymApi.postDetails(postId), gymApi.postComments(postId)])
      .then(([postRes, commentRes]) => {
        setPost(postRes.data)
        setComments(commentRes.data ?? [])
      })
      .catch(() => {
        setPost(null)
        setComments([])
      })
  }, [postId])

  const submitComment = async () => {
    if (!token) {
      Alert.alert(t('postDetail.loginRequired'), t('postDetail.loginFirst'))
      return
    }
    const trimmed = content.trim()
    if (!trimmed) {
      Alert.alert(t('auth.tips'), t('postDetail.empty'))
      return
    }
    try {
      setSubmitting(true)
      await gymApi.addComment(token, postId, trimmed)
      const commentRes = await gymApi.postComments(postId)
      setComments(commentRes.data ?? [])
      Alert.alert(t('postDetail.commentTitle'), t('postDetail.commentSuccess'))
      setContent('')
    } catch (error) {
      Alert.alert(t('postDetail.commentFailed'), String(error))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Screen>
      <SectionCard title={route.params?.title ?? t('stack.postDetail')} subtitle={post?.type ?? t('postDetail.subtitleDefault')}>
        <Text style={styles.content}>{post?.content ?? t('postDetail.loading')}</Text>
      </SectionCard>

      <SectionCard title={t('postDetail.comments')} subtitle={`Loaded ${comments.length} comment(s)`}>
        {comments.map((comment, index) => (
          <Text key={`${comment.id ?? index}`} style={styles.commentText}>{comment.content ?? `Comment #${index + 1}`}</Text>
        ))}
      </SectionCard>

      <SectionCard title={t('postDetail.addComment')} subtitle={t('postDetail.addSubtitle')}>
        <TextInput
          style={styles.input}
          placeholder={t('postDetail.placeholder')}
          placeholderTextColor={colors.textMuted}
          value={content}
          onChangeText={setContent}
          multiline
        />
        <PrimaryButton title={submitting ? t('postDetail.sending') : t('postDetail.send')} onPress={() => void submitComment()} disabled={submitting} />
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  content: {
    color: colors.text,
    lineHeight: 22
  },
  commentText: {
    color: colors.textMuted,
    lineHeight: 20
  },
  input: {
    minHeight: 120,
    backgroundColor: colors.surfaceAlt,
    color: colors.text,
    borderRadius: 14,
    padding: spacing.md,
    borderWidth: 1,
    borderColor: colors.border,
    textAlignVertical: 'top'
  }
})
