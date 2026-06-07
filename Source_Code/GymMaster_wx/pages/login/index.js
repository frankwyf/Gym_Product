var util = require("../../utils/util.js");
var app = getApp();
Page({
  data:{
    server: app.globalData.server,
    loginBtnTxt:"Login",
    loginBtnBgBgColor:"#1CBCB4",
    btnLoading:false,
    disabled:false,
    inputUserName: '',
    inputPassword: '',
  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数

  },
  onReady:function(){
    // 页面渲染完成

  },
  onShow:function(){
    // 页面显示

  },
  onHide:function(){
    // 页面隐藏

  },
  onUnload:function(){
    // 页面关闭

  },
  formSubmit:function(e){
    var param = e.detail.value;
    this.mysubmit(param);
  },
  mysubmit:function (param){
    var flag = this.checkUserName(param)&&this.checkPassword(param)
    console.log(flag);
    if(flag){
        this.setLoginData1();
        this.checkUserInfo(param);
    }
  },
  setLoginData1:function(){
    this.setData({
      loginBtnTxt:"Login...",
      disabled: !this.data.disabled,
      loginBtnBgBgColor:"#999",
      btnLoading:!this.data.btnLoading
    });
  },
  setLoginData2:function(){
    this.setData({
      loginBtnTxt:"Login",
      disabled: !this.data.disabled,
      loginBtnBgBgColor:"#ff9900",
      btnLoading:!this.data.btnLoading
    });
  },
  checkUserName:function(param){
    // var email = util.regexConfig().email;
    // var phone = util.regexConfig().phone;

    var inputUserName = param.username.trim();
    if(inputUserName.length != 0){
      return true;
    }else{
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'please input username !',
        confirmText:'OK'
      });
      return false;
    }
  },
  checkPassword:function(param){
    var userName = param.username.trim();
    var password = param.password.trim();
    if(password.length<=0){
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'please input password !',
        confirmText:'OK'
      });
      return false;
    }else{
      return true;
    }
  },
  checkUserInfo:function(param){
    var username = param.username.trim();
    var password = param.password.trim();
    var that = this;
    console.log(username);
    console.log(password);
    // query backend to check username and password
    wx.request({
        url: that.data.server + 'loginCus/login',
        method: 'POST',
      data: {
          username: username,
        password : password
      },
      success: function (res) {
        console.log(res.data);
        if (res.data.code == 1){
          // set global data for token
            app.globalData.userInfo = res.data.data.token;
          // login success
          wx.showToast({
            title: 'Login Success',
            icon: 'success',
            duration: 1500
          });
          wx.redirectTo({
            url: '../mine/mine'
          })
        }
        else {
          wx.showModal({
            title: 'Tips',
            showCancel:false,
            content: 'Username or password is wrong, Please try again !',
            confirmText:'OK'
          });
          that.setLoginData2();
        }
      }
    })
  },
  redirectTo:function(){
    wx.redirectTo({
      url: '../mine/mine'
    })
  }
})
