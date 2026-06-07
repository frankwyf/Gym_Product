//app.js
App({
  onLaunch: function () {
    console.log('GymMaster App onLaunch');
    var that = this;
    wx.setStorageSync('mallName', "GymMaster");
  },

  globalData:{
    userInfo: null,
    server: "http://172.20.10.2:8087/"
    //server: "http://localhost:8087/"
    //server: "https://162.14.64.131:8309/"
  }
})
