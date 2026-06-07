//index.js
//获取应用实例
var app = getApp()
Page({
  data: {
    server : app.globalData.server,
    indicatorDots: true,
    autoplay: true,
    interval: 3000,
    duration: 1000,
    loadingHidden: false , // loading
    slides: [],
    userInfo: {},
    swiperCurrent: 0,
    selectCurrent:0,
    categories: [],
    activeCategoryId: 0,
    courses:[],
    scrollTop:"0",
    loadingMoreHidden:true
  },
  // this function is called when the page is loaded
  onShow() {

  },

  tabClick: function (e) {
    this.setData({
      activeCategoryId: e.currentTarget.id
    });
    this.getGoodsList(this.data.activeCategoryId);
  },
  //事件处理函数
  swiperchange: function(e) {
       this.setData({
        swiperCurrent: e.detail.current
    })
  },
  toDetailsTap:function(e){
    wx.navigateTo({
      url:"/pages/goods-details/goods-details?id="+e.currentTarget.dataset.id
    })
  },
  tapBanner: function(e) {
    if (e.currentTarget.dataset.id != 0) {
      wx.navigateTo({
        url: "/pages/goods-details/goods-details?id=" + e.currentTarget.dataset.id
      })
    }
  },
  bindTypeTap: function(e) {
     this.setData({
        selectCurrent: e.index
    })
  },
  scroll: function (e) {
    var that = this,scrollTop=that.data.scrollTop;
    that.setData({
      scrollTop:e.detail.scrollTop
    })
  },
  onLoad: function () {
    console.log('onLoad');
    var that = this;
    wx.setNavigationBarTitle({
      title: "GymMaster Courses"
    });
    wx.request({
      url: that.data.server + 'until/courseSlides',
      success: function(res) {
        if (res.data.code == 1) {
          that.setData({
            slides: res.data.data,
          });
        }
      }
    })
    wx.request({
      url: that.data.server + 'until/allCourses',
      method: 'GET',
      success: function(res) {
        var categories = [{id:0, name:"All"}];
        for (var i = 0; i < res.data.data.length; i++) {
            categories.push({id:i+1, name:res.data.data[i].type});
        }
        that.setData({
          categories: categories,
        });
      }
    })
    this.getGoodsList(0);
  },
  getGoodsList: function (categoryId) {
    if (categoryId == 0) {
      categoryId = "";
    }
    var that = this;
    wx.request({
      url: that.data.server + 'until/allCourses',
      method: 'GET',
      success: function(res) {
        that.setData({
          loadingMoreHidden:false,
        });
        var goods = [];
        // data from backend is corrupted
        if (res.data.code != 1 || res.data.data.length == 0) {
          that.setData({
            loadingMoreHidden:false,
          });
          return;
        }
        if (categoryId == 0) {
          for(var i=0;i<res.data.data.length;i++){
            goods.push(res.data.data[i]);
          }
          that.setData({
            courses:goods,
          });
        }
        else {
          for(var i=0;i<res.data.data.length;i++){
            if (res.data.data[i].type == that.data.categories[categoryId].name) {
              goods.push(res.data.data[i]);
            }
          }
          that.setData({
            courses:goods,
          });
        }
      }
    })
  }
})
