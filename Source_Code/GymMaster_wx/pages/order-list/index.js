// pages/customer/customer.js
const app = getApp();
Page({
  data: {
    server: app.globalData.server,
    tabs: ["Unpaid", "Paid", "Bills"],
    activeTab: 0,
    unpaidReservations: [],
    paidReservations: [],
    bills: [],
    uid: 0,
    isModalShow: false,
    modalItem: null,
    time:["9:00", "10:00", "11:00", "15:00", "16:00", "17:00", "18:00", "19:00"],
    isModalShowun: false,
    modalItemun: null
  },

  /**
   * Lifecycle function--Called when page load
   */
  onLoad: function (options) {
    var that = this;
    // get user id
    wx.request({
        url: app.globalData.server + 'customer/getuid',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
        success: function (res) {
          that.setData({
            uid: res.data.data
          });
        }
    })
    this.getUnpaidReservations()
    this.getPaidReservations()
    this.getBills()
  },
  switchTab: function(e) {
    var index = e.currentTarget.dataset.index;
    this.setData({
      activeTab: index
    });
  },

  /**
   * Get unpaid reservations from local storage
   */
  getUnpaidReservations() {
    var that = this;
    var shopCarInfoMem = wx.getStorageSync('shopCarInfo');
    var shopList = [];
    if (shopCarInfoMem && shopCarInfoMem.shopList) {
      shopList = shopCarInfoMem.shopList
    }
    that.setData({
        unpaidReservations: shopList
    })
    console.log("aa")
     for (var i = 0; i < that.data.unpaidReservations.length; i++) {
       console.log(that.data.unpaidReservations[i])
     }
  },
  sendAll: function(e) {
    wx.request({
      url: app.globalData.server + 'student/export/getAll?id='+this.data.uid,
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
    })
  },

  /**
   * Get paid reservations from server
   */
  getPaidReservations() {
    var that = this;
    wx.request({
      url: app.globalData.server + 'reservation/getPaid',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success:function (res ){
        console.log("paid "+res.data.data)
        that.setData({
          paidReservations: res.data.data
        })
      }
    })
  },

  /**
   * Get bills from server
   */
  getBills() {
    var that = this;
    wx.request({
      url: app.globalData.server + 'bill/showall',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success:function (res ) {
        console.log(res.data.data)
        that.setData({
          bills: res.data.data
        })
      }
    })
  },

  /**
   * Handle tab switch event
   */
  handleTabClick(e) {
    const index = e.detail.index
    this.setData({
      activeTab: index
    })
  },

  /**
   * Lifecycle function--Called when page is unloaded
   */
  onUnload: function () {
    this.setData({
      unpaidReservations: [],
      paidReservations: [],
      bills: []
    })
  },
  showModal: function(e) {
    const item = e.currentTarget.dataset.item;
    this.setData({
      isModalShow: true,
      modalItem: item,
    });
  },
  hideModal: function() {
    this.setData({
      isModalShow: false,
      modalItem: null,
    });
  },
  showModalun: function(e) {
    const item = e.currentTarget.dataset.item;
    this.setData({
      isModalShowun: true,
      modalItemun: item,
    });
  },
  hideModalun: function() {
    this.setData({
      isModalShowun: false,
      modalItemun: null,
    });
  },
  sendToServer: function(event) {
    console.log(event.currentTarget.dataset.bid);
    const bid = event.currentTarget.dataset.bid.toString();
    wx.request({
      url: app.globalData.server + 'student/export/sendBill?x=' + bid,
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data.data);
        wx.showModal({
            title: 'System Message',
            content: res.data.data,
            showCancel: false,
            confirmText: 'OK',
        })
      }
    })
  }
})
