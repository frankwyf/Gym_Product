export type Locale = 'en' | 'zh' | 'ja'

type Dictionary = Record<string, string>

const en: Dictionary = {
  'language.title': 'Language',
  'language.en': 'English',
  'language.zh': 'Chinese',
  'language.ja': 'Japanese',

  'tab.home': 'Home',
  'tab.reservation': 'Book',
  'tab.community': 'Feed',
  'tab.courses': 'Class',
  'tab.profile': 'Me',

  'stack.forgotPassword': 'Forgot Password',
  'stack.courseDetail': 'Course Detail',
  'stack.facilityVenues': 'Facility Venues',
  'stack.venueDetail': 'Venue Detail',
  'stack.postDetail': 'Post Detail',
  'stack.sendPost': 'Create Post',
  'stack.wallet': 'Wallet',
  'stack.orders': 'Orders',
  'stack.search': 'Search',
  'stack.notices': 'Notices',
  'stack.video': 'Video',
  'stack.checkin': 'Daily Check-In',
  'stack.addresses': 'Addresses',

  'profile.title': 'Profile',
  'profile.subtitle': 'User info, membership, wallet, orders and sign out.',
  'profile.notLoggedIn': 'You are not logged in.',
  'profile.goLogin': 'Go to Login',
  'profile.wallet': 'Wallet',
  'profile.orders': 'Orders',
  'profile.checkin': 'Daily Check-In',
  'profile.addresses': 'Addresses',
  'profile.logout': 'Logout',

  'common.refresh': 'Refresh',
  'common.refreshing': 'Refreshing...',
  'common.retry': 'Retry',

  'notices.title': 'Notices',
  'notices.subtitle': 'Show historical notices and expand details.',
  'notices.empty': 'No notices available.',
  'notices.error': 'Failed to load notices. Please try again.',

  'community.title': 'Community',
  'community.subtitle': 'Theme feed, post detail and create post.',
  'community.refreshFeed': 'Refresh Feed',
  'community.loading': 'Loading posts...',
  'community.empty': 'No posts found for this theme.',
  'community.error': 'Failed to load posts. Please try again.',

  'reservation.title': 'Reservation',
  'reservation.subtitle': 'Facility list and reservation draft cart.',
  'reservation.refreshFacilities': 'Refresh Facilities',
  'reservation.loadingFacilities': 'Loading facilities...',
  'reservation.emptyFacilities': 'No facilities available right now.',
  'reservation.errorFacilities': 'Failed to load facilities. Please try again.',

  'facilityVenues.subtitle': 'Facility details, venue categories and booking entry.',
  'facilityVenues.loading': 'Loading venues...',
  'facilityVenues.emptyCategories': 'No venue categories available for this facility.',
  'facilityVenues.emptyVenues': 'No venues are currently available.',
  'facilityVenues.error': 'Failed to load facility venues. Please try again.'
}

const zh: Dictionary = {
  'language.title': '语言',
  'language.en': '英文',
  'language.zh': '中文',
  'language.ja': '日文',

  'tab.home': '首页',
  'tab.reservation': '预约',
  'tab.community': '社区',
  'tab.courses': '课程',
  'tab.profile': '我的',

  'stack.forgotPassword': '找回密码',
  'stack.courseDetail': '课程详情',
  'stack.facilityVenues': '设施场馆',
  'stack.venueDetail': '场馆详情',
  'stack.postDetail': '帖子详情',
  'stack.sendPost': '发布帖子',
  'stack.wallet': '钱包',
  'stack.orders': '订单',
  'stack.search': '搜索',
  'stack.notices': '公告',
  'stack.video': '视频',
  'stack.checkin': '每日签到',
  'stack.addresses': '地址管理',

  'profile.title': '个人中心',
  'profile.subtitle': '账号资料、会员等级、钱包、订单与登出。',
  'profile.notLoggedIn': '当前未登录。',
  'profile.goLogin': '前往登录',
  'profile.wallet': '钱包',
  'profile.orders': '订单',
  'profile.checkin': '每日签到',
  'profile.addresses': '地址管理',
  'profile.logout': '退出登录',

  'common.refresh': '刷新',
  'common.refreshing': '刷新中...',
  'common.retry': '重试',

  'notices.title': '公告',
  'notices.subtitle': '展示历史公告并支持展开查看。',
  'notices.empty': '暂无公告。',
  'notices.error': '公告加载失败，请重试。',

  'community.title': '社区',
  'community.subtitle': '帖子分栏、详情与发帖入口。',
  'community.refreshFeed': '刷新动态',
  'community.loading': '动态加载中...',
  'community.empty': '当前分类暂无帖子。',
  'community.error': '帖子加载失败，请重试。',

  'reservation.title': '预约',
  'reservation.subtitle': '设施列表与预约购物车草稿。',
  'reservation.refreshFacilities': '刷新设施',
  'reservation.loadingFacilities': '设施加载中...',
  'reservation.emptyFacilities': '当前暂无可预约设施。',
  'reservation.errorFacilities': '设施加载失败，请重试。',

  'facilityVenues.subtitle': '设施介绍、场馆分类与预约入口。',
  'facilityVenues.loading': '场馆加载中...',
  'facilityVenues.emptyCategories': '该设施暂无场馆分类。',
  'facilityVenues.emptyVenues': '当前暂无可用场馆。',
  'facilityVenues.error': '场馆信息加载失败，请重试。'
}

const ja: Dictionary = {
  'language.title': '言語',
  'language.en': '英語',
  'language.zh': '中国語',
  'language.ja': '日本語',

  'tab.home': 'ホーム',
  'tab.reservation': '予約',
  'tab.community': 'コミュニティ',
  'tab.courses': 'コース',
  'tab.profile': 'マイ',

  'stack.forgotPassword': 'パスワード再設定',
  'stack.courseDetail': 'コース詳細',
  'stack.facilityVenues': '施設と会場',
  'stack.venueDetail': '会場詳細',
  'stack.postDetail': '投稿詳細',
  'stack.sendPost': '投稿作成',
  'stack.wallet': 'ウォレット',
  'stack.orders': '注文',
  'stack.search': '検索',
  'stack.notices': 'お知らせ',
  'stack.video': '動画',
  'stack.checkin': 'デイリーチェックイン',
  'stack.addresses': '住所',

  'profile.title': 'プロフィール',
  'profile.subtitle': 'アカウント、会員情報、ウォレット、注文、ログアウト。',
  'profile.notLoggedIn': 'ログインしていません。',
  'profile.goLogin': 'ログインへ',
  'profile.wallet': 'ウォレット',
  'profile.orders': '注文',
  'profile.checkin': 'デイリーチェックイン',
  'profile.addresses': '住所',
  'profile.logout': 'ログアウト',

  'common.refresh': '更新',
  'common.refreshing': '更新中...',
  'common.retry': '再試行',

  'notices.title': 'お知らせ',
  'notices.subtitle': '過去のお知らせを表示し、詳細を展開できます。',
  'notices.empty': 'お知らせはありません。',
  'notices.error': 'お知らせの取得に失敗しました。再試行してください。',

  'community.title': 'コミュニティ',
  'community.subtitle': 'テーマ別フィード、詳細、投稿作成。',
  'community.refreshFeed': 'フィードを更新',
  'community.loading': '投稿を読み込み中...',
  'community.empty': 'このテーマの投稿はありません。',
  'community.error': '投稿の取得に失敗しました。再試行してください。',

  'reservation.title': '予約',
  'reservation.subtitle': '施設一覧と予約カート下書き。',
  'reservation.refreshFacilities': '施設を更新',
  'reservation.loadingFacilities': '施設を読み込み中...',
  'reservation.emptyFacilities': '現在利用可能な施設がありません。',
  'reservation.errorFacilities': '施設の取得に失敗しました。再試行してください。',

  'facilityVenues.subtitle': '施設詳細、会場カテゴリ、予約入口。',
  'facilityVenues.loading': '会場を読み込み中...',
  'facilityVenues.emptyCategories': 'この施設には会場カテゴリがありません。',
  'facilityVenues.emptyVenues': '現在利用可能な会場がありません。',
  'facilityVenues.error': '会場情報の取得に失敗しました。再試行してください。'
}

export const dictionaries: Record<Locale, Dictionary> = { en, zh, ja }
