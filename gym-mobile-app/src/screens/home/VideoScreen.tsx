import { Video, ResizeMode } from 'expo-av'
import { useRef } from 'react'
import { Alert, Linking, StyleSheet, Text, View } from 'react-native'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { API_BASE_URL } from '../../constants/config'
import { colors, spacing } from '../../constants/theme'

const VIDEO_URL = `${API_BASE_URL}Environment/About.mp4`

export function VideoScreen() {
  const videoRef = useRef<Video | null>(null)

  const play = async () => {
    try {
      await videoRef.current?.playAsync()
    } catch (error) {
      Alert.alert('Video unavailable', String(error))
    }
  }

  const openInBrowser = async () => {
    await Linking.openURL(VIDEO_URL)
  }

  return (
    <Screen>
      <SectionCard title="Video" subtitle="迁移自 video 页面，保留场馆介绍视频播放能力。">
        <Text style={styles.text}>If your network is not Wi-Fi, video playback may consume mobile data.</Text>
        <Video
          ref={videoRef}
          source={{ uri: VIDEO_URL }}
          style={styles.video}
          useNativeControls
          resizeMode={ResizeMode.CONTAIN}
          onError={(error) => Alert.alert('Video error', JSON.stringify(error))}
        />
        <View style={styles.actions}>
          <PrimaryButton title="Play" onPress={() => void play()} />
          <PrimaryButton title="Open Source URL" secondary onPress={() => void openInBrowser()} />
        </View>
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  text: {
    color: colors.textMuted,
    lineHeight: 20
  },
  video: {
    width: '100%',
    height: 220,
    borderRadius: 14,
    backgroundColor: '#000'
  },
  actions: {
    flexDirection: 'row',
    gap: spacing.sm,
    flexWrap: 'wrap'
  }
})
