//app.js
const i18n = require('./utils/i18n');

App({
  onLaunch: function () {
    console.log('GymMaster App onLaunch');
    wx.setStorageSync('mallName', 'GymMaster');
    this.globalData.locale = i18n.getLocale();
    this.applyTabBarLanguage();
  },

  applyTabBarLanguage: function() {
    const labels = this.getMessages();
    const texts = [
      labels.tabHome,
      labels.tabAppointment,
      labels.tabCommunity,
      labels.tabCourses,
      labels.tabMe
    ];

    texts.forEach((text, index) => {
      wx.setTabBarItem({
        index,
        text
      });
    });
  },

  setLanguage: function(locale) {
    this.globalData.locale = i18n.setLocale(locale);
    this.applyTabBarLanguage();
    return this.globalData.locale;
  },

  t: function(key) {
    return i18n.t(this.globalData.locale, key);
  },

  getMessages: function() {
    return i18n.bundle(this.globalData.locale);
  },

  globalData:{
    userInfo: null,
    locale: 'en',
    server: "http://172.20.10.2:8087/"
    //server: "http://localhost:8087/"
    //server: "https://162.14.64.131:8309/"
  }
})
