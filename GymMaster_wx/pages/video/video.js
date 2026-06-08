// video.js
var app = getApp();
Page({
  FindOutMore(){
    wx.navigateTo({
      url: '../articles-detail/index?id=-1'
    })
  },


  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    wx.getNetworkType({
      success: function (res) {
        var networkType = res.networkType; // 返回网络类型2g，3g，4g，wifi
        if (networkType != "wifi") {
          console.log(networkType);
          wx.showModal({
            title: 'Notice',
            content: 'The current network is not wifi, please be careful of your data usage.',
            showCancel:false,
            confirmText:'Continue',
          })
        }
      }
    });
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
  videoErrorCallback: function (e) {
    console.log(e.detail.errMsg);
    wx.showModal({
        title: 'Error',
        content: 'The video is not available now, please try again later.',
        showCancel: false,
        confirmText: 'Noted',
    })
  },
  playvideo: function(){
  },

  /**播放视屏 */
  play(e) {
    //执行全屏方法
    var videoContext = wx.createVideoContext('myvideo', this);
    videoContext.requestFullScreen();
    this.setData({
      fullScreen:true
    })
  },
  /**关闭视屏 */
  closeVideo() {
    //执行退出全屏方法
    var videoContext = wx.createVideoContext('myvideo', this);
    videoContext.exitFullScreen();
  },
  /**视屏进入、退出全屏 */
  fullScreen(e){
    var isFull = e.detail.fullScreen;
    //视屏全屏时显示加载video，非全屏时，不显示加载video
    this.setData({
      fullScreen:isFull
    })
  }

})
