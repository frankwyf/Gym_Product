import * as ImagePicker from 'expo-image-picker'
import { useState } from 'react'
import { Alert, Image, StyleSheet, TextInput } from 'react-native'
import { gymApi } from '../../api/gymApi'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useAppContext } from '../../hooks/useAppContext'

export function SendPostScreen({ navigation }: { navigation: any }) {
  const { token } = useAppContext()
  const [content, setContent] = useState('')
  const [imageUri, setImageUri] = useState<string | null>(null)

  const pickImage = async () => {
    const permission = await ImagePicker.requestMediaLibraryPermissionsAsync()
    if (!permission.granted) {
      Alert.alert('Permission', 'Media library access is required.')
      return
    }
    const result = await ImagePicker.launchImageLibraryAsync({ mediaTypes: ['images'], quality: 0.7 })
    if (!result.canceled) {
      setImageUri(result.assets[0]?.uri ?? null)
    }
  }

  const submit = async () => {
    if (!token) {
      Alert.alert('Login required', 'Please login first.')
      return
    }
    try {
      let uploadedMedia: string | undefined
      if (imageUri) {
        uploadedMedia = await gymApi.uploadPostImage(token, imageUri)
      }
      await gymApi.addPost(token, content, uploadedMedia)
      Alert.alert('Post', 'Post created successfully.')
      navigation.goBack()
    } catch (error) {
      Alert.alert('Post failed', String(error))
    }
  }

  return (
    <Screen>
      <SectionCard title="Create Post" subtitle="迁移自 send 页面，保留图片上传 + 发帖流程。">
        <TextInput
          style={styles.input}
          placeholder="Enter content of the post"
          placeholderTextColor={colors.textMuted}
          multiline
          value={content}
          onChangeText={setContent}
        />
        {imageUri ? <Image source={{ uri: imageUri }} style={styles.preview} /> : null}
        <PrimaryButton title="Choose Image" secondary onPress={() => void pickImage()} />
        <PrimaryButton title="Submit Post" onPress={() => void submit()} />
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  input: {
    minHeight: 160,
    backgroundColor: colors.surfaceAlt,
    color: colors.text,
    borderRadius: 14,
    padding: spacing.md,
    borderWidth: 1,
    borderColor: colors.border,
    textAlignVertical: 'top'
  },
  preview: {
    width: '100%',
    height: 220,
    borderRadius: 16
  }
})
