// pages/post-details.js
var app = getApp();
Page({
  data: {
    post: [],
    comment: [],
    new_comment: '',
    server: app.globalData.server,
  },

  onLoad: function (options) {
    console.log(options.postID);
    var that = this;
    // Retrieve post data from server
    wx.request({
      url: this.data.server +'until/specificPost?postID=' + options.postID,
      success: function (res) {
        console.log(res.data.data);
        that.setData({ post: res.data.data });
      },
      fail: err => {
        console.error(err);
      }
    });
    // Retrieve comment data from server
    wx.request({
        url: this.data.server +'until/postComment?postID=' + options.postID,
        success: function (res) {
            console.log(res.data.data);
            that.setData({ comment: res.data.data });
            console.log(that.data.comment);
        }

    })
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
        })
    }
  },
  onCommentInput: function (event) {
    this.setData({ new_comment: event.detail.value });
    console.log(this.data.new_comment);
    console.log(this.data.post.pid);
  },
  onSendComment: function (event) {
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
    wx.request({
      url: this.data.server +'posts/postComment',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo,
        'content': this.data.new_comment,
        'PostID': this.data.post.pid
      },
      success: function (res) {
        console.log(res.data);
        if (res.data.data == "success") {
          wx.showToast({
            title: 'Comment successfully!',
            icon: 'success',
            duration: 2000
          })
          //refresh the page
            wx.redirectTo({
                url: '/pages/post-details/post-details?postID=' + this.data.post.pid
            })
        }
        else {
          wx.showToast({
            title: 'Comment failed!',
            icon: 'error',
            duration: 2000
          })
        }
      }
    })
  },

  filter: {
    // Date formatter function
    format: function (date, format) {
      var dt = new Date(date);
      var o = {
        "M+": dt.getMonth() + 1,
        "d+": dt.getDate(),
        "h+": dt.getHours(),
        "m+": dt.getMinutes(),
        "s+": dt.getSeconds(),
        "q+": Math.floor((dt.getMonth() + 3) / 3),
        "S": dt.getMilliseconds()
      };
      if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (dt.getFullYear() + "").substr(4 - RegExp.$1.length));
      }
      for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
          format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
      }
      return format;
    }
  }
});
