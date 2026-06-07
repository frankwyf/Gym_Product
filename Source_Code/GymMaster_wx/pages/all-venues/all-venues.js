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
    everyday:[],
    activeCategoryId: 0,
    today_venues:[],
    venues:[],
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
    console.log(e.currentTarget.dataset.id);
    wx.navigateTo({
      url:"/pages/venues/venues?vid="+e.currentTarget.dataset.id
    })
  },
  tapBanner: function(e) {
    // get the data-id of the banner
    console.log('tapBanner');
    console.log(e.currentTarget.dataset.id);
    if (e.currentTarget.dataset.id != 0) {
      wx.navigateTo({
        url: "/pages/venues/venues?vid=" + e.currentTarget.dataset.id
      })
    }
  },
  bindTypeTap: function(e) {
     this.setData({
        selectCurrent: e.index
    })
  },
  scroll: function (e) {
    //  console.log(e) ;
    var that = this,scrollTop=that.data.scrollTop;
    that.setData({
      scrollTop:e.detail.scrollTop
    })
    // console.log('e.detail.scrollTop:'+e.detail.scrollTop) ;
    // console.log('scrollTop:'+scrollTop)
  },
  onLoad: function () {
    console.log('Venues onLoad');
    var that = this;
    wx.setNavigationBarTitle({
      title: "GymMaster Venues"
    });
    /*
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      //更新数据
      that.setData({
        userInfo:userInfo
      })
    })
    */
    wx.request({
      url: that.data.server + 'until/venuesSlides',
      success: function(res) {
        if (res.data.code == 1) {
          that.setData({
            slides: res.data.data,
          });
          console.log(res.data.data);
        }
      }
    })
    wx.request({
      url: that.data.server + 'venue/getAvailableVenues',
      method: 'GET',
      success: function(res) {
        var categories = [];
        for (var i = 0; i < res.data.data.length; i++) {
            categories.push(res.data.data[i].date);
        }
        that.setData({
            everyday: categories,
        });
        // set the date for the next 7 days
        var date = [];
        for(var i = 0; i<7;i++){
          var onedate = {};
          onedate.id = i;
          onedate.datestr = that.fun_date(i);
          date.push({id:i, name:onedate.datestr});
        }
        that.setData({
          categories: date,
        });
      }
    })
    this.getGoodsList(0);
    that.showZanTopTips("Tap to book venues !");
  },
  getGoodsList: function (categoryId) {
    if (categoryId == 0) {
      categoryId = "";
    }
    var that = this;
    wx.request({
      url: that.data.server + 'venue/getAvailableVenues',
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
        for (var date in res.data.data) {
          // only get the month and date of the date
          var date1 = date.substring(5, 10);
          var venues = [];
          if ( date1 == that.fun_date(categoryId) ) {
            for (var i = 0; i < res.data.data[date].length; i++) {
              // check if all eight slots are available
              var flag = 0;
              for (var j = 0; j < 8; j++) {
                if (res.data.data[date][i].cap[j] == 0) {
                  flag ++;
                }
              }
              if (flag != 8) {
                venues.push(res.data.data[date][i].venue);
              }
            }
            that.setData({
              today_venues: venues,
            });
            console.log(that.data.today_venues);
          }
        }
        }

    })
  },

  fun_date: function(a){
    var date1 = new Date();
    var date2 = new Date(date1);
    date2.setDate(date1.getDate()+a);
    var mon = date2.getMonth() + 1;
    var day = date2.getDate();
    var time = (mon < 10 ? ('0' + mon) : mon) + "-" + (day < 10 ? ('0' + day) : day);
    if(a === 0){
      time += '(Today)';
    }
    else if (a === 1) {
      time += '(Tomorrow)';
    }
    return time;
  },
  //toptips
  showZanTopTips:function(content, options = {}) {
    let zanTopTips = this.data.zanTopTips || {};
    // clear existing timer
    if (zanTopTips.timer) {
      clearTimeout(zanTopTips.timer);
      zanTopTips.timer = undefined;
    }

    if (typeof options === 'number') {
      options = {
        duration: options
      };
    }

    options = Object.assign({
      duration: 5000
    }, options);

    let timer = setTimeout(() => {
      this.setData({
        'zanTopTips.show': false,
        'zanTopTips.timer': undefined
      });
    }, options.duration);

    // show topTips
    this.setData({
      zanTopTips: {
        show: true,
        content,
        options,
        timer
      }
    });
  },
})
