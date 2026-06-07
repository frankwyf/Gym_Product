// Search-result.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
    facilities:[],
    starFacilities: [],
    bestsales : [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('appointment onload');
    var that = this;
    wx.request({
      url: that.data.server + 'until/facilities',
      method : 'GET',
      success :(res)=> {
        var recommend = [];
        var facilities = [];
        var bestsales = [];
        for (var i = 0; i < res.data.data.length; i++) {
          if (res.data.data[i].recommend === "Yes"){
            recommend.push(res.data.data[i]);
          }
          facilities.push(res.data.data[i]);
        }
        console.log(recommend);
        // first three are the best sales
        for (var i = 0; i < 3; i++) {
          bestsales.push(res.data.data[i]);
        }
        that.setData({
          facilities: facilities,
          starFacilities: recommend,
          bestsales : bestsales,
        });
        console.log("facilities and starFacilities are loaded.")
      }
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // encourage reservation with a message
    wx.showModal({
      title: 'Before Reservation',
      content: '\n1.GymMaster is opened 7/24, 9:00 ~ 12:00 & 15:00-20:00.\n' +
          '2.Reservations must be paid to get an entrance QR code.\n' +
          '3.We kindly ask that you arrive on time to avoid any delays or disruptions\n' +
          '4.To make reservation, please log in to your account on our website or mobile app.\n',
      showCancel: false,
      confirmText: 'Got it',
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // refresh the page
    this.onLoad();

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
})
