const app = getApp()
Page({
  data: {
    server: app.globalData.server,
    cloudImgList:[],
    inputValue:'',
    titleValue:'',
    tishi:'Enter content of the post',
    addresssInfo:{},
  },



  onLoad: function (options) {
    // console.log(app.globalData.userInfo)
  },
  getValue(e){
    console.log(e.detail.value)
    this.setData({
      inputValue: e.detail.value
    })
  },
  chooseImage(){
    var that = this;
    wx.chooseImage({
      count: 9 - that.data.cloudImgList.length,
      sizeType: ['original','compressed'],
      sourceType: ['album','camera'],
      success(res){
        console.log(res)
        console.log(res.tempFilePaths)
        that.setData({
          tempImgList: res.tempFilePaths
        })
        //上传图片
        that.data.tempImgList = res.tempFilePaths
      }
    })
  },
  uploadImages(callback) {
    var that = this;
    var index = 0;
    var successCount = 0;
    var failCount = 0;
    var length = that.data.tempImgList.length;
    function doUpload() {
      if (index >= length) {
        // All images uploaded, update the UI
        that.setData({
          cloudImgList: that.data.cloudImgList.concat(that.data.tempImgList)
        });
        wx.hideLoading();
        callback();
        return;
      }

      var filePath = that.data.tempImgList[index];
      wx.showLoading({
        title: 'Uploading (' + (index + 1) + '/' + length + ')'
      });
      wx.uploadFile({
        url: that.data.server + 'file/upload/posts',
        filePath: filePath,
        name: 'file',
        header: {
          'content-type': 'multipart/form-data',
          'token': app.globalData.userInfo
        },
        success: function(res) {
          successCount++;
          var filename = res.data.substring(29,69);
          that.setData({
            media: filename
          })
          console.log('success', that.data.media);
          // Add the uploaded image to the cloudImgList
          that.data.cloudImgList.push(res.data);
        },
        fail: function(res) {
          failCount++;
          console.log('fail', res);
        },
        complete: function() {
          index++;
          doUpload();
        }
      });
    }
    doUpload();
  },

  submitData(){
    var that = this;
    wx.showLoading({
      title: 'Posting...',
      mask:'true'
    })
    that.uploadImages(function() {
      console.log(that.data.media);
      console.log("uploading images");
      // create a post in the database
      wx.request({
        url: that.data.server + 'posts/add',
        header: {
          'content-type': 'application/json',
          'token': app.globalData.userInfo
        },
        data: {
          content: that.data.inputValue,
          media: that.data.media,
        },
        success: function (res) {
          console.log(res.data.data);
        }
      })
    });
    wx.redirectTo({
      url: '../post-index/post-index',
  })
  },



})
