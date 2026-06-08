// notices.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
    coachcategoryId:0,
    notices: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    console.log('notices onLoad');
    var that = this;
    // get the posts from the server
    wx.request({
      url: that.data.server + 'until/notices',
      method : 'GET',
      success: function (res) {
        var topNotice = [];
        for (var i = 0; i < res.data.data.length; i++) {
          topNotice.push(res.data.data[i]);
        }
        that.setData({
          notices: topNotice
        });
      }
    });
    that.showZanTopTips("Tap to view details~");
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
      return{
        title:"News List",
        path: '/pages/notices/notices'
      }
  },
  //toptips
  showZanTopTips:function(content, options = {}) {
    let zanTopTips = this.data.zanTopTips || {};
    // clean existing timer
    if (zanTopTips.timer) {
      clearTimeout(zanTopTips.timer);
      zanTopTips.timer = undefined;
    }

    if (typeof options === 'number') {
      options = {
        duration: options
      };
    }

    // options default params set to 3000
    options = Object.assign({
      duration: 3000
    }, options);

    // set timer for hiding top tips
    let timer = setTimeout(() => {
      this.setData({
        'zanTopTips.show': false,
        'zanTopTips.timer': undefined
      });
    }, options.duration);

    // shwo topTips
    this.setData({
      zanTopTips: {
        show: true,
        content,
        options,
        timer
      }
    });
  },

  ContactCoachLongTap:function(e){
    var telnumber = e.currentTarget.dataset.telnumber;
    console.log(telnumber);
    wx.makePhoneCall({
      phoneNumber: telnumber,
    })
  }
})
