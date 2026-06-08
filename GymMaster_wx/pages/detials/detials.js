const app = getApp();
import bData from '../../service/mock.js'
Page({
  data: {
    contentHeight: app.globalData.windowHeight,
    item: {title:"测试好友分享"}
  },
  // 页面加载
  onLoad: function (options) {
    wx.showShareMenu({
      withShareTicket: true // 要求小程序返回分享目标信息
    });
    var id = options.id;
    var i = bData.getItemById(id);

    this.setData({ item: i, id: id});
  },
  onShareAppMessage: function (ops) {
    var url = '/pages/detials/detials' + "?id=" + this.data.id
    return {
      title: this.data.item.title,
      path: url,
      success: function (res) {
        wx.showToast({
          title: '分享成功',
        })
      },
      fail: function (res) {
        wx.showToast({
          title: '分享失败',
        })
      }
    }
  },
});
