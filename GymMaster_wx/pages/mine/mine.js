// mine.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
    phoneNumber:'Enter your phone number',
    userInfo: {},
    locale: 'en',
    i18n: {},
    localeOptions: [
      { code: 'en', label: 'English' },
      { code: 'zh', label: '中文' },
      { code: 'ja', label: '日本語' }
    ],
    items: [
      {
        icon: '../../images/mine/Fingerprint.png',
        text: 'Daily checkout',
        path: '/pages/signin/sign-in'
      },
      {
        icon: '../../images/mine/ok.png',
        text: 'Check out Shopping cart',
        path: '/pages/shop-cart/index'
      },
      {
        icon: '../../images/mine/order.png',
        text: 'All events',
        path: '/pages/order-list/index'
      },
      {
        icon: '../../images/mine/wallet.jpg',
        text: 'My wallet',
        path: '/pages/wallet/wallet'
      },
      {
        icon: '../../images/mine/support.png',
        text: 'Contact us',
        path: '13800000000',
      },
    ],
    settings: [
      {
        icon: '../../images/mine/About-Us.png',
        text: 'About us',
        path: '/pages/articles-detail/index?id=-1'
      },
    ],
    membership: 'Free Trial',
    upgradeOptions: [],
    accounts: [],
    upgradeOptions: ['free trail', 'copper member', 'silver member', 'gold member'],
    upgradePrices: {
      'free trail': 0,
      'copper member': 10,
      'silver member': 20,
      'gold member': 30
    },
    upgradeDiscounts: {
      'free trail': 0,
      'copper member': 20,
      'silver member': 40,
      'gold member': 70
    },
    showUpgradeModal: false,
    selectedAccountId: null,
    upgradeLevel: null
  },

  syncI18n: function() {
    this.setData({
      locale: app.globalData.locale,
      i18n: app.getMessages()
    });
  },

 onShow() {
    this.syncI18n();
    app.applyTabBarLanguage();
    this.onLoad();
 },
  onLoad: function (options) {
    this.syncI18n();
    var that = this;
    wx.request({
      url: that.data.server + 'account/page',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data);
        if (res.data.code == 1){
          that.setData({
            accounts: res.data.data,
          });
        }
        else {
          wx.showToast({
            title: 'Failed to load accounts',
            icon: 'none',
            duration: 2000
          });
        }
      }
    })
    // check whether the user has logged in
    if (!app.globalData.userInfo) {
      wx.redirectTo({
        url: '/pages/login/index'
      })
    }
    // get user info from backend
    wx.request({
      url: this.data.server + 'customer/CheckInformation',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data.data.customer);
        that.setData({
          profile: res.data.data.customer.profile,
          username: res.data.data.customer.username,
          membership : res.data.data.customer.membership,
        });
        const membershipLevelIndex = that.getMembershipLevelIndex(that.data.membership);
        that.setData({
          upgradeOptions: that.getUpgradeOptions(membershipLevelIndex)
        });
      }
    })
  },

  changeLanguage: function(e) {
    const locale = e.currentTarget.dataset.locale;
    if (!locale) {
      return;
    }
    app.setLanguage(locale);
    app.applyTabBarLanguage();
    this.syncI18n();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  showUpgradeModal(event) {
    const upgradeLevel = event.currentTarget.dataset.level;
    this.setData({
      showUpgradeModal: true,
      upgradeLevel,
      selectedAccountId: null
    });
  },

  onAccountSelect(event) {
    const accountId = event.currentTarget.dataset.id;
    this.setData({ selectedAccountId: accountId });
  },

  onUpgradeCancel() {
    this.setData({ showUpgradeModal: false });
  },

  onUpgradeConfirm() {
    var aid = parseInt(this.data.selectedAccountId);
    console.log(aid)
      wx.request({
        url: this.data.server + 'customer/vipMem?aid=' + aid + '&type=' + this.data.upgradeLevel,
        method: 'POST',
        header: {
            'content-type': 'application/json',
            'token': app.globalData.userInfo
        },
        success(res) {
          console.log(res.data);
          if (res.data.code == 1){
            wx.showToast({
              title: 'Upgrade success',
              icon: 'success',
              duration: 2000
            });
          }else {
            wx.showModal({
                title: 'Upgrade failed',
                content: res.data.msg,
                showCancel: false,
                confirmText: 'OK',
            })
          }
        },
        fail(err) {
          console.error(err);
        }
      });
    this.setData({ showUpgradeModal: false });
  },
  logout: function () {
    // Query server for logout
    wx.request({
      url: this.data.server + 'loginCus/logout',
      method: 'GET',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data);
        if (res.data.code == 1){
          // Clean global data of the app
          app.globalData.userInfo = null;

          // Redirect to home page
          wx.redirectTo({
            url: '../home/home'
          })
        }
        else {
            wx.showToast({
                title: 'Logout failed',
                icon: 'none',
                duration: 2000
            });
        }
      }
    });
  },

  onShareAppMessage: function () {
    return {
      title: "GymMaster",
      path:"/pages/home/home"
    }
  },

  navigateTo:function(e) {
    const index = e.currentTarget.dataset.index;
    const path = e.currentTarget.dataset.path;
    switch (index) {
      case 4:
        wx.makePhoneCall({
          phoneNumber: path
        })
        break;
      default:
        console.log(path);
        // console.log(typeof path);
        wx.navigateTo({
          url: path
        });
    };
  },
  getMembershipLevelIndex: function(membershipLevel) {
    const membershipLevels = ['free trial', 'copper member', 'silver member', 'gold member'];
    console.log(membershipLevels.indexOf(membershipLevel));
    return membershipLevels.indexOf(membershipLevel);
  },

  getUpgradeOptions: function(membershipLevelIndex) {
    const upgradeOptions = ['copper member', 'silver member', 'gold member'];
    if (membershipLevelIndex === -1) {
      return upgradeOptions;
    }
    return upgradeOptions.slice(membershipLevelIndex, upgradeOptions.length);
  },


})

