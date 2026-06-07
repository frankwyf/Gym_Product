// coaches-detail.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    fid: 0,
    server: app.globalData.server,
    modalContent: "",
    modalHidden: true,
    categories: [],
    activeCategoryId: 0,
    courses:[],
    scrollTop:"0",
    loadingMoreHidden:true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('facility and venues onLoad');
    const facilityId = options.id;
    this.setData({
      fid:options.id
    })
    const that = this;
    wx.request({
      url: that.data.server + 'until/specificFacility?facilityID=' + facilityId,
      data: {
        id: facilityId
      },
      method: 'GET',
      success: function (res) {
        const facility= res.data.data;
        that.setData({
          facility,
          facilityId
        })
      }
    });
    wx.request({
      url: that.data.server + 'until/venuesInfo?facilityID=' + facilityId,
      method: 'GET',
      success: function(res) {
        var categories = [];
        for (var i = 0; i < res.data.data.length; i++) {
          categories.push({id:i, name:res.data.data[i].vname});
        }
        that.setData({
          categories: categories,
        });
      }
    })
    this.getVenueList(0,facilityId);
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

  },

  tabClick: function (e) {
    console.log(this.data.facilityId)
    this.setData({
      activeCategoryId: e.currentTarget.id
    });
    this.getVenueList(this.data.activeCategoryId, this.data.facilityId);
  },

  getVenueList: function (categoryId, facilityId) {
    console.log(facilityId)
    var that = this;
    wx.request({
      url: that.data.server + 'until/venuesInfo?facilityID=' + facilityId,
      method: 'GET',
      success: function(res) {
        that.setData({
          loadingMoreHidden:true,
        });
        var goods = [];
        // data from backend is corrupted
        if (res.data.code != 1 || res.data.data.length == 0) {
          that.setData({
            loadingMoreHidden:false,
          });
          return;
        }
        for(var i=0;i<res.data.data.length;i++){
          if (res.data.data[i].vname == that.data.categories[categoryId].name) {
            goods.push(res.data.data[i]);
          }
        }
        that.setData({
          venues:goods,
        });
        console.log(that.data.venues);
      }
    })
  }
})
