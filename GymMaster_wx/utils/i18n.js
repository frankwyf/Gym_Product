const STORAGE_KEY = 'gymmaster_locale';

const messages = {
  en: {
    appName: 'GymMaster',
    navHome: 'GymMaster - Home',
    welcome: 'Welcome to GymMaster',
    langEnglish: 'English',
    langChinese: 'Chinese',
    langJapanese: 'Japanese',
    aboutUs: 'About Us',
    currentAddress: 'Your current Address',
    ourEnvironment: 'Our Environment',
    videoDemo: 'Real Video Demo',
    ourFacilities: 'Our Facilities',
    bookVenues: 'Book Venues',
    ourCoaches: 'Our Coaches',
    allCoaches: 'All Coaches',
    recentNotices: 'Recent Notices',
    historyNotices: 'History Notices',
    searchPlaceholder: 'Search for venue/facilities',
    search: 'Search',
    profileTapHint: 'Tap to view/update',
    logout: 'Logout',
    currentMembership: 'Your Current Membership',
    upgradeMembership: 'Upgrade Membership',
    cancel: 'Cancel',
    confirm: 'Confirm'
    ,tabHome: 'Home'
    ,tabAppointment: 'Appointment'
    ,tabCommunity: 'Community'
    ,tabCourses: 'Courses'
    ,tabMe: 'Me'
  },
  zh: {
    appName: '健身管家',
    navHome: '健身管家 - 首页',
    welcome: '欢迎使用健身管家',
    langEnglish: '英文',
    langChinese: '中文',
    langJapanese: '日文',
    aboutUs: '关于我们',
    currentAddress: '当前位置',
    ourEnvironment: '训练环境',
    videoDemo: '视频演示',
    ourFacilities: '设施场馆',
    bookVenues: '预约场馆',
    ourCoaches: '教练团队',
    allCoaches: '全部教练',
    recentNotices: '最新公告',
    historyNotices: '历史公告',
    searchPlaceholder: '搜索场馆/设施',
    search: '搜索',
    profileTapHint: '点击查看/修改',
    logout: '退出登录',
    currentMembership: '当前会员等级',
    upgradeMembership: '会员升级',
    cancel: '取消',
    confirm: '确认'
    ,tabHome: '首页'
    ,tabAppointment: '预约'
    ,tabCommunity: '社区'
    ,tabCourses: '课程'
    ,tabMe: '我的'
  },
  ja: {
    appName: 'ジムマスター',
    navHome: 'ジムマスター - ホーム',
    welcome: 'ジムマスターへようこそ',
    langEnglish: '英語',
    langChinese: '中国語',
    langJapanese: '日本語',
    aboutUs: '私たちについて',
    currentAddress: '現在地',
    ourEnvironment: 'トレーニング環境',
    videoDemo: '動画デモ',
    ourFacilities: '施設',
    bookVenues: '会場予約',
    ourCoaches: 'コーチ',
    allCoaches: 'すべてのコーチ',
    recentNotices: '最新のお知らせ',
    historyNotices: '過去のお知らせ',
    searchPlaceholder: '施設/会場を検索',
    search: '検索',
    profileTapHint: 'タップして表示/更新',
    logout: 'ログアウト',
    currentMembership: '現在の会員ランク',
    upgradeMembership: '会員アップグレード',
    cancel: 'キャンセル',
    confirm: '確認'
    ,tabHome: 'ホーム'
    ,tabAppointment: '予約'
    ,tabCommunity: 'コミュニティ'
    ,tabCourses: 'コース'
    ,tabMe: 'マイ'
  }
};

function normalize(locale) {
  return locale === 'zh' || locale === 'ja' || locale === 'en' ? locale : 'en';
}

function getLocale() {
  const value = wx.getStorageSync(STORAGE_KEY);
  return normalize(value || 'en');
}

function setLocale(locale) {
  const next = normalize(locale);
  wx.setStorageSync(STORAGE_KEY, next);
  return next;
}

function t(locale, key) {
  const scoped = messages[normalize(locale)] || messages.en;
  return scoped[key] || messages.en[key] || key;
}

function bundle(locale) {
  const key = normalize(locale);
  return messages[key] || messages.en;
}

module.exports = {
  getLocale,
  setLocale,
  t,
  bundle
};
