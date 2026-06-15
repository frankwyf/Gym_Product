// home.js
var app = getApp()
Page({

  /**
   * initial data of the home page
   */
  data: {
    server : app.globalData.server,
    latitude: 30.0,
    longitude: 108.0,
    autoplay: true,
    interval: 3000,
    duration: 1000,
    coaches:[],
    facilities:[],
    articles:[],
    slides: [],
    notices: [],
    i18n: {},
  },

  syncI18n: function () {
    this.setData({ i18n: app.getMessages() });
  },

  popup: function (e) {
    console.log(e);
    wx.makePhoneCall({
        phoneNumber: e.currentTarget.dataset.phone,
    })
  },

  /**
   * when the page is loaded, this function is called
   * it will get the data from the server and set the banner and the categories
   * the banner is set in the data
   */
  onLoad: function (options) {
    console.log('home onLoad');
    var that = this;
    this.syncI18n();
    // get the banner from storage
    wx.setNavigationBarTitle({
      title: app.t('navHome')
    });

    // get current location
    wx.getLocation({
      isHighAccuracy : true,
        success :(res) =>{
          console.log('get current location:');
          console.log(res);
          that.setData({
            latitude: res.latitude,
            longitude: res.longitude,
          })
        }
    });

    // get the head slides from the server
    wx.request({
      url: that.data.server + 'until/homeslides',
      method : 'GET',
      success: function (res) {
        var data = res.data
        if (data.code == 1){ // code 1 means success from backend
            that.setData({
              slides: res.data.data,
            });
        }
      }
    });

    // get the posts from the server
    wx.request({
      url: that.data.server + 'until/notices',
      method : 'GET',
      success: function (res) {
        var topNotice = [];
        for (var i = 0; i < 2; i++) {
          console.log(res.data.data[i]);
          topNotice.push(res.data.data[i]);
        }
        that.setData({
          notices: topNotice
        });
      }
    });

    // get facility pictures from the server
    wx.request({
      url: that.data.server + 'until/facilities',
      method : 'GET',
      success: function (res) {
        var data = res.data
        if (data.code == 1){ // code 1 means success from backend
            that.setData({
              facilities: res.data.data,
            });
        }
      }
    })

    // get coach information from the server
    wx.request({
        url: that.data.server + 'until/coaches',
      success: function (res) {
        var data = res.data
        if (data.code == 1){ // code 1 means success from backend
            that.setData({
              coaches: res.data.data,
            });
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    // show a toast when page is ready
    wx.showToast({
      title: app.t('welcome'),
        icon: 'success',
        duration: 2000,
        mask: true,
        success: function () {
            console.log('show toast success');
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
    // unload the page
    console.log('onUnload');
    // wx.switchTab({
    //     url: '/pages/home/home',
    // }
    // )
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // refresh home page
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
    return {
      title:"GymMaster"
    }
  },
  //事件处理函数
  swiperchange: function (e) {
    //console.log(e.detail.current)
    this.setData({
      swiperCurrent: e.detail.current
    })
  },

  coachswiperchange: function (e) {
    //console.log(e.detail.current)
    this.setData({
      swiperCurrent: e.detail.current
    })
  },

  showmap:function (){
    this.openLocationFun(this.data.latitude,this.data.longitude,15,"GymMaster","");
  },
  showvideo:function (){
    wx.navigateTo({
      url: '/pages/video/video',
    })
    wx.getNetworkType({
      success: function (res) {
        var networkType = res.networkType; // 返回网络类型2g，3g，4g，wifi
        if (networkType != "wifi"){
          wx.showModal({
            title: 'Tips',
            content: 'It is not wifi you are using, continue to play?',
            success: function (res) {
              if (res.confirm) {
                console.log('user conform to play video under mobile network')
              }
            }
          })
        }
        console.log(networkType);
      }
    });
  },
  /**
 * 使用微信内置地图查看位置
 * 1、latitude：     纬度，范围为-90~90，负数表示南纬 必填
 * 2、longitude：    经度，范围为-180~180，负数表示西经 必填
 * 3、scale：        缩放比例，范围1~28，默认为28 选填
 * 4、name：         位置名 选填
 * 5、address：      地址的详细说明 选填
 * 6、cbSuccessFun： 接口调用成功的回调函数 选填
 * 7、cbFailFun：    接口调用失败的回调函数 选填
 * 8、cbCompleteFun：接口调用结束的回调函数（调用成功、失败都会执行） 选填
 */
  openLocationFun: function(latitude, longitude, scale, name, address, cbSuccessFun, cbFailFun, cbCompleteFun){
    var openObj= {};
    openObj.latitude = latitude;
    openObj.longitude = longitude;
    openObj.scale = 15;
    if(scale>0 && scale < 29) {
      openObj.scale = scale;
    }
    if(name) {
      openObj.name = name;
    }
    if(address) {
      openObj.address = address;
    }
    openObj.success = function (res) {
      if (cbSuccessFun) {
        cbSuccessFun();
      }
    }
    openObj.fail = function (res) {
      if (cbFailFun) {
        cbFailFun();
      } else {
        console.log("openLocation fail:" + res.errMsg);
      }
    }
    openObj.complete = function (res) {
      if (cbCompleteFun) {
        cbCompleteFun();
      }
    }
    wx.openLocation(openObj);
  },
  historyNotice:function (){
    wx.navigateTo({
      url: "/pages/notices/notices"
    })
  },
  toAllCoachesTap:function (){
    wx.navigateTo({
      url: "/pages/coaches/coaches?coachid=" + this.data.coachcategoryid
    })
  },
  toAllVenuesTap:function (){
    wx.navigateTo({
      url: "/pages/all-venues/all-venues"
    })
  },
  searchForm: function(event) {
    const searchTerm = event.detail.value.term;
    console.log(searchTerm);
    wx.redirectTo({
        url: '/pages/Search-result/Search-result?term=' + searchTerm,
    })
  }
})
