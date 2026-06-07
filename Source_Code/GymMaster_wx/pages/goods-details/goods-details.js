//index.js
//获取应用实例
var app = getApp();
var WxParse = require('../../wxParse/wxParse.js');

Page({
  data: {
    server : app.globalData.server,
    autoplay: false,
    interval: 5000,
    duration: 1000,
    goodsDetail:{},
    coachDetail:{},
    swiperCurrent: 0,
    hasMoreSelect:true,
    shopNum:0,
    hideShopPopup:true,
    buyNumber:0,
    buyNumMin:0,
    buyNumMax:0,

    propertyChildIds:"",
    propertyChildNames:"",
    canSubmit:false, //  选中规格尺寸时候是否允许加入购物车
    shopCarInfo:{},
    modalContent:"",
    modalHidden:true
  },

  //事件处理函数
  swiperchange: function(e) {
      //console.log(e.detail.current)
       this.setData({
        swiperCurrent: e.detail.current
    })
  },
  onLoad: function (e) {
    console.log('course details onLoad');
    console.log(e.id);
    var that = this;
    // get shop car info
    wx.getStorage({
      key: 'shopCarInfo',
      success: function(res) {
        console.log(res.data)
        that.setData({
          shopCarInfo:res.data,
          shopNum:res.data.shopNum
        });
      },
      fail: function(res) {
        that.setData({
          shopCarInfo: {shopNum:0,shopList:[]},
          shopNum:0
        });
      }
    })
    wx.request({
      url: that.data.server + 'until/specificCourse?courseID=' + e.id,
      success: function(res) {
        var selectSizeTemp = "";
        that.setData({
            goodsDetail:res.data.data.course,
            coachDetail:res.data.data.coach,
        })
        console.log(that.data.goodsDetail);
        WxParse.wxParse('article', 'html', res.data.data.content, that, 15);
      }
    })


  },
  addShopCar:function(){
    // check whether the user has logged in
    if (!app.globalData.userInfo) {
      wx.redirectTo({
        url: '/pages/login/index'
      })
      // show error message
      wx.showToast({
        title: 'Please login first!',
        icon: 'none',
        duration: 2000
      })
    }
    // 加入购物车
    var shopCarMap = {};
    shopCarMap.date = this.data.goodsDetail.time;
    shopCarMap.facility = this.data.goodsDetail.courseFacility;
    shopCarMap.venue = this.data.goodsDetail.courseVenue;
    shopCarMap.period = 0;
    shopCarMap.amount = 1;
    shopCarMap.type = "courses";
    shopCarMap.pic = this.data.goodsDetail.cover;
    shopCarMap.name = this.data.goodsDetail.type;
    shopCarMap.price = this.data.goodsDetail.price;

    var shopCarInfo = this.data.shopCarInfo;

    if (!shopCarInfo.shopNum){
      shopCarInfo.shopNum = 0;
    }
    if (!shopCarInfo.shopList){
      shopCarInfo.shopList = [];
    }

    shopCarInfo.shopNum = shopCarInfo.shopNum + 1;
    shopCarInfo.shopList.push(shopCarMap);
    this.setData({
      shopCarInfo:shopCarInfo,
      shopNum:shopCarInfo.shopNum
    });

    // 写入本地存储
    wx.setStorage({
      key:"shopCarInfo",
      data:shopCarInfo
    })
    wx.showToast({
      title: 'Successfully added to cart',
      icon: 'success',
      duration: 2000
    })
    console.log(shopCarInfo);
  },

  goShopCar:function () {
    wx.navigateTo({
      url: "/pages/shop-cart/index"
    });
  },
  tobuy:function(){
    if (this.data.goodsDetail.properties && !this.data.canSubmit) {
      this.bindGuiGeTap();
      return;
    }

    //临时代码，根据商户需求更改
    this.setData({
      modalContent: '请确认您预定的课程信息\r\n课程名称: ' + this.data.goodsDetail.basicInfo.name + '\r\n上课时间: ' + this.data.propertyChildNames + '\r\n请检查无误后点击确认',
      modalHidden: false
    });

    if(this.data.buyNumber < 1){
      wx.showModal({
        title: '提示',
        content: '课程爆满了哦~',
        showCancel:false
      })
      return;
    }
    this.addShopCar();
    this.goShopCar();
  },
  modalHide:function () {
    this.setData({
      modalContent: '',
      modalHidden: true
    });
    wx.showModal({
      title: '提示',
      content: '具体预定方式待与商家沟通后继续开发。谢谢配合。',
    })
  },
  modalHideCancle:function (){
    this.setData({
      modalContent: '',
      modalHidden: true
    });
  },
  onShareAppMessage: function () {
    return {
      title: this.data.goodsDetail.basicInfo.name,
      path: '/pages/goods-details/index?id=' + this.data.goodsDetail.basicInfo.id,
      success: function (res) {
        // 转发成功
      },
      fail: function (res) {
        // 转发失败
      }
    }
  }
})
