// pages/post-index.js
const app = getApp();
Page({
  data: {
    server: app.globalData.server,
    contentHeight: 753,
    themes: [{ id: 0, name: 'ALL' },
             { id: 1, name: 'Customer'},
             { id: 2, name: 'Coach' },
             { id: 4, name: 'Employee' },
             { id: 5, name: 'Manager' }],

    posts: [[], [], [], [], []],
    post_now:[],
    activeCategoryId: 0,
    scrollTop:"0",
    selectCurrent:0,
    loadingMoreHidden:true
  },
onShow() {
    this.onLoad();
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
  },
  onLoad: function (options) {
    console.log("onLoad posts");
    var that = this;
    wx.request({
      url: that.data.server + 'until/allPosts',
      method: 'GET',
      success: function (res) {
        // put all the posts into the matrix 0
        var all = []
        for (var i = 0; i < res.data.data.length; i++) {
            all.push(res.data.data[i]);
        }
        that.setData({
          post_now: all,
        })
        console.log(that.data.post_now);
        for (var i = 0; i < res.data.data.length; i++) {
          that.data.posts[0].push(res.data.data[i]);
        }
        var tags = ["Customer", "Coach", "Employee", "Manager"];
        for (var j = 0; j < res.data.data.length; j++) {
          if (res.data.data[j].type == tags[j]) {
            that.data.posts[j + 1].push(res.data.data[j]);
          }
        }
      },
    })
  },
  // tab choosing function
  tabClick: function (e) {
    this.setData({
      activeCategoryId: e.currentTarget.id
    });
    this.getPostsList(this.data.activeCategoryId);
  },
  toDetailsTap:function(e){
    wx.navigateTo({
      url:"/pages/post-details/post-details?postID="+e.currentTarget.dataset.id
    })
  },
  getPostsList: function (categoryId) {
    this.setData({ post_now: this.data.posts[categoryId] })
  },
  onSendPost: function () {
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
    else {
      wx.navigateTo({
        url: '/pages/send/send'
      });
    }
  },
})
