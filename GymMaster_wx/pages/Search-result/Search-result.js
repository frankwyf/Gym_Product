// Search-result.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
    facilities:[],
    venues: [],
    course: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('result onload');
    var that = this;
    var searchTerm = options.term;
    wx.request({
      url: this.data.server + 'until/search?name=' + searchTerm,
      method: 'GET', // replace with the HTTP method used by your backend API
      success: function(res) {
        that.setData({
          facilities: res.data.data.facilities,
          venues: res.data.data.venues,
          course: res.data.data.courses,
        });
        console.log(that.data.course);
      },
      fail: function(res) {
        console.error(res); // log any errors that occur during the request
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
