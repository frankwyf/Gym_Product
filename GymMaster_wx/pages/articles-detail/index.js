// index.js
var WxParse = require('../../wxParse/wxParse.js');
var app = getApp()
Page({
  FindOutMore(){
    wx.navigateTo({
      url: '../video/video'
    })
  },
  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
    id:0,
    article:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options.id);
    if (options.id == -1) {
      // assume the API response data is stored in a variable called 'responseData'
      var responseData = {
        content: "Welcome to GymMaster, " +
            "your ultimate sports center for a healthy lifestyle! " +
            "We offer a range of facilities and services including a fully equipped gym, " +
            "cardio machines, state-of-the-art swimming pool, and group fitness classes. " +
            "Our certified trainers provide personalized training programs and one-on-one coaching sessions. " +
            "Join our friendly and supportive community today and start your fitness journey with GymMaster!",
        noticeDate: "2023-02-24T05:47:53.000+00:00",
        noticeMedia: "GymMaster.jpg",
        publisherType: "System",
        title: "About us"
      };
      var article = {
        content: "",
        noticeDate: "",
        noticeMedia: "",
        publisherType: "",
        title: ""
      };
      article.content = responseData.content;
      article.noticeDate = responseData.noticeDate;
      article.noticeMedia = responseData.noticeMedia;
      article.publisherType = responseData.publisherType;
      article.title = responseData.title;
// set the data to the global data
      this.setData({
        article:article
      });
      console.log(article);
      WxParse.wxParse('FitnessArticle', 'html',responseData.content, that, 10);
    }
    else {
      console.log('backend Notice onLoad');
      var that = this;
      wx.request({
        url: that.data.server + 'until/getNotice?noticeId=' + options.id,
        method: 'GET',
        data: {
          id: options.id
        },
        success: function (res) {
          console.log(res.data);
          that.setData({
            article: res.data.data,
            id: options.id,
          });
          WxParse.wxParse('FitnessArticle', 'html', res.data.data.content, that, 10);
        }
      });
    }
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
    console.log(this.data.article);
    return {
      title: this.data.article.title,
      path: '/pages/articles-detail/index?id=' + this.data.id,
      success: function (res) {
        // 转发成功
      },
      fail: function (res) {
        // 转发失败
      }
    }
  }
})
