//index.js
//获取应用实例
var app = getApp()

Page({
  data: {
    server: app.globalData.server,
    mallName:wx.getStorageSync('mallName'),
    goodsList:[],
    allGoodsPrice:0,
    yunPrice:0,
    showModal: false,
    accounts: [],
    selectedAccount: null,
    goodsJsonStr:""
  },
  onLoad: function (e) {
    var that = this;
    wx.request({
      url: that.data.server + 'account/page',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data);
        if (res.data.code == 1){
          that.setData({
            accounts: res.data.data,
          });
        }
        else {
          wx.showToast({
            title: 'Failed to load accounts',
            icon: 'none',
            duration: 2000
          });
        }
      }
    })
    var shopList = [];
    var shopCarInfoMem = wx.getStorageSync('shopCarInfo');
    if (shopCarInfoMem && shopCarInfoMem.shopList) {
      shopList = shopCarInfoMem.shopList
    }
    var isNeedLogistics = 0;
    var allGoodsPrice = 0;

    var goodsJsonStr = "[";
    for (var i =0; i < shopList.length; i++) {
      var carShopBean = shopList[i];
      console.log(carShopBean);
      if (carShopBean.logisticsType > 0) {
        isNeedLogistics = 1;
      }
      allGoodsPrice += carShopBean.price * carShopBean.amount

      var goodsJsonStrTmp = '';
      if (i > 0){
        goodsJsonStrTmp = ",";
      }
      goodsJsonStrTmp += '{"goodsId":'+ carShopBean.goodsId +',"number":'+ carShopBean.number +',"propertyChildIds":"'+ carShopBean.propertyChildIds +'","logisticsType":'+ carShopBean.logisticsType +'}';
      goodsJsonStr += goodsJsonStrTmp;
    }
    goodsJsonStr += "]";
    that.setData({
      goodsList:shopList,
      allGoodsPrice:allGoodsPrice,
    });

  },
  createOrder:function (e) {
    if (this.data.selectedAccount == null){
      wx.showToast({
        title: 'Please select an account',
        icon: 'none',
        duration: 2000
      });
      return;
    }
    wx.showLoading();
    var that = this;
    console.log(that.data.selectedAccount);
    console.log(that.data.goodsList);
    console.log(that.data.allGoodsPrice);
    wx.request({
      url: that.data.server + 'bill/pay?aid='+that.data.selectedAccount + '&total=' + that.data.allGoodsPrice,
      method:'POST',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      data: {
        goodlist:that.data.goodsList,
      }, // 设置请求的 参数
      success: (res) =>{
        wx.hideLoading();
        console.log(res.data);
        if (res.data.code == 0) {
          wx.showModal({
            title: 'Error',
            content: res.data.msg,
            showCancel: false
          })
          return;
        }
        //清空购物车数据
        wx.removeStorageSync('shopCarInfo');
        //下单成功，跳转到订单管理界面
        wx.redirectTo({
          url: "/pages/order-list/index"
        });
      }
    })
  },
  showModal() {
    this.setData({ showModal: true });
  },
  onCancel() {
    this.setData({ showModal: false });
  },
  onAccountSelect(event) {
    const accountId = event.currentTarget.dataset.id;
    const selectedAccount = accountId;
    this.setData({ selectedAccount: selectedAccount });
  },
  onConfirm() {
    const selectedAccount = this.data.selectedAccount;
    console.log(selectedAccount)
    this.setData({ showModal: false });
  },

})
