// coaches-detail.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    server: app.globalData.server,
    shopNum: 0,
    venue: {},
    reservationAmount: 1, // default value
    reservationAmountOptions: [1, 2, 3, 4, 5], // options for picker
    timeList: [], // date list
    occupy: [[], [], [], [], [], [], []], // occupy list, stores the va
    yyDay:7, // open reservation for the next 7 days
    //预约时间段
    hourList: [
      {hour: "9:00",n: 8,isShow: true},
      {hour: "10:00",n: 9,isShow: true},
      {hour: "11:00",n: 10,isShow: true},
      {hour: "15:00",n: 11,isShow: true},
      {hour: "16:00",n: 12,isShow: true},
      {hour: "17:00",n: 13,isShow: true},
      {hour: "18:00",n: 14,isShow: true},
      {hour: "19:00",n: 15,isShow: true}
    ],
    //是否显示
    timeShow: false,
    currentTab: 0,
    //选择时间
    chooseHour: "",
    //选择日期
    chooseTime: "",
    hourIndex: -1,
    //预约时间
    yyTime:'',
    activedateid : 0,
    modalContent: "",
    modalHidden: true
  },

  onLoad: function (options) {
    console.log("onLoad");
    wx.getStorage({
      key: 'shopCarInfo',
      success: function(res) {
        console.log("aa");
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
    // check whether the user has logged in
    if (!app.globalData.userInfo) {
      wx.redirectTo({
        url: '/pages/login/index'
      })
      // show error message
      wx.showToast({
        title: 'Please login first!',
        icon: 'none',
        duration: 5000
      })
    }
    console.log('venue reservation onLoad');
    const venueId = options.vid;
    const that = this;
    that.setData({
      fid:options.fid,
      vid: venueId
    })
    wx.request({
      url: that.data.server + 'venue/getById?vid=' + venueId,
      data: {
        id: venueId
      },
      method: 'GET',
      success: function (res) {
        const venue= res.data.data[0].venue;
        that.setData({
          venue:venue
        })
        console.log(that.data.venue)
      }
    });

    // get shop car info
    wx.getStorage({
      key: 'shopCarInfo',
      success: function(res) {
        console.log(res.data)
        that.setData({
          shopCarInfo:res.data,
          shopNum:res.data.shopNum
        });
      }
    })


    Date.prototype.Format = function (format) {
      var o = {
        "M+": this.getMonth() + 1,  //month
        "d+": this.getDate(),     //day
        "h+": this.getHours(),    //hour
        "m+": this.getMinutes(),  //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
      }
      if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
      }
      for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
          format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
      }
      return format;
    }
    Date.prototype.DateAdd = function (interval, number) {
      number = parseInt(number);
      var date = new Date(this.getTime());
      switch (interval) {
        case "y": date.setFullYear(this.getFullYear() + number); break;
        case "m": date.setMonth(this.getMonth() + number); break;
        case "d": date.setDate(this.getDate() + number); break;
        case "w": date.setDate(this.getDate() + 7 * number); break;
        case "h": date.setHours(this.getHours() + number); break;
        case "n": date.setMinutes(this.getMinutes() + number); break;
        case "s": date.setSeconds(this.getSeconds() + number); break;
        case "l": date.setMilliseconds(this.getMilliseconds() + number); break;
      }
      return date;
    }

    var dateList = [];
    var now = new Date();
    for (var i = 0; i < this.data.yyDay; i++) {
      var d = {};
      var day = new Date().DateAdd('d', i).getDay();
      if (day == 1) { var w = "Monday" }
      if (day == 2) { var w = "Tuesday" }
      if (day == 3) { var w = "Wednesday" }
      if (day == 4) { var w = "Thursday" }
      if (day == 5) { var w = "Friday" }
      if (day == 6) { var w = "Saturday" }
      if (day == 0) { var w = "Sunday" }
      d.name = w;
      d.date = new Date().DateAdd('d', i).Format("MM-dd");
      dateList.push(d)
    }
    this.setData({
      timeList: dateList
    });
    // initial the time list of hour
    // current time
    var hour = new Date().getHours();
    if (hour > 12) {
      hour = hour - 4;
    }
    // read from backend and set the time list
    wx.request({
        url: that.data.server + 'venue/getById?vid=' + venueId,
      success: function (res) {
          // store the 7x8 matrix of vacancies
        var vacancies = [[], [], [], [], [], [], []];
        for (var i = 0; i < 8; i++) {
          for (var j = 0; j < 7; j++) {
            vacancies[j][i] = res.data.data[j].cap[i];
          }
        }
        // set the vacancies to global data occupy
        that.setData({
          occupy: vacancies
        })
        // set the time list first
        for (var i = 0; i < 8; i++) {
          if (that.data.occupy[0][i] == 0) {
            that.data.hourList[i].isShow = false;
          }
        }
      },
    })
    for (var i = 0; i < this.data.hourList.length; i++) {
      var list = this.data.hourList;
      // passed hours are not available
      if (this.data.hourList[i].n <= hour ) {
        list[i].isShow = false;
        this.setData({
          hourList: list
        })
      }
    }
  },




  handleReservationAmountChange: function(event) {
    const value = event.detail.value;
    this.setData({
      reservationAmount: this.data.reservationAmountOptions[value]
    });
    console.log(this.data.reservationAmount);
  },

  // 购物车
  addShopCar:function(){
    // 加入购物车
    var shopCarMap = {};
    shopCarMap.date = this.data.chooseTime;
    shopCarMap.facility = this.data.fid;
    shopCarMap.venue = this.data.vid;
    shopCarMap.period = this.data.chooseHourn;
    shopCarMap.amount = this.data.reservationAmount;
    shopCarMap.type = "venues";
    shopCarMap.pic = this.data.venue.profile;
    shopCarMap.name = this.data.venue.vname;
    shopCarMap.price = this.data.venue.price;

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

  // click on shopping cart or checkout
  goShopCar:function () {
    wx.navigateTo({
      url: "/pages/shop-cart/index"
    });
  },

// show modal
  showTimeModel: function () {
    // refresh the page every time
    this.setData({
      timeShow: !this.data.timeShow,
      chooseTime: this.data.timeList[0].date,
    });
  },

  // press anywhere to close model
  modelCancel: function () {
    this.setData({
      timeShow: !this.data.timeShow,
      chooseTime: this.data.timeList[0].date,
    });
  },
  // when click on date banner
  timeClick: function (e) {
    // if it is nit today, show every time points
    // only consider backend reservation restriction
    if (e.currentTarget.dataset.index != 0) {
      var list = this.data.hourList;
      for (var i = 0; i < 8; i++) {
        if (this.data.occupy[e.currentTarget.dataset.index][i] != 0) {
          list[i].isShow = true;
        }
      }
      this.setData({
        hourList: list
      })
    } else {
      // today - judge whether it is over the current time point
      var hour = new Date().getHours();
      if (hour > 12) {
        hour = hour - 4;
      }
      for (var i = 0; i < this.data.hourList.length; i++) {
        var list = this.data.hourList;
        if (this.data.hourList[i].n <= hour) {
          list[i].isShow = false;
          this.setData({
            hourList: list
          })
        }
      }
    }
    this.setData({
      currentTab: e.currentTarget.dataset.index,
      chooseTime: this.data.timeList[e.currentTarget.dataset.index].date,
      yyTime: '',
      chooseHour: "",
      hourIndex: -1
    });
    console.log(this.data.chooseTime)
  },
  // 时间选择
  hourClick: function (e) {
    var that = this;
    // if the time point is not available, show toast
    if (!e.currentTarget.dataset.isshow) {
      // show toast
        wx.showToast({
          title: 'Not available',
          duration: 2000,
          icon: 'error',
        })
      return false;
    }
    // update chooseTime and chooseHour data properties
    var chooseTime = this.data.chooseTime;
    var chooseHour = this.data.hourList[e.currentTarget.dataset.index].n-7;
    console.log(chooseHour);
    this.setData({
      chooseTime: chooseTime,
      chooseHourn: chooseHour,
    });
    console.log(this.data.chooseTime)
    console.log(this.data.chooseHour)
    this.setData({
      hourIndex: e.currentTarget.dataset.index,
      chooseHour: this.data.hourList[e.currentTarget.dataset.index].hour,
    });
    var chooseTime = new Date().getFullYear() + "-" + this.data.chooseTime + " " + this.data.chooseHour
    that.setData({
      yyTime: chooseTime,
      timeShow: !this.data.timeShow
    })
  },
})
