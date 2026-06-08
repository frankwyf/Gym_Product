var util = require("../../utils/util.js");
var app = getApp();
Page({
  data:{
    server: app.globalData.server,
    registBtnTxt:"Submit",
    registBtnBgBgColor:"#1CBCB4",
    getSmsCodeBtnTxt:"Get Code",
    getSmsCodeBtnColor:"#1CBCB4",
    // getSmsCodeBtnTime:60,
    btnLoading:false,
    registDisabled:false,
    smsCodeDisabled:false,
    phoneNum: '',
    Username: '',
    smsCode: -100,

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
  getPhoneNum:function(e){
   var value  = e.detail.value;
   this.setData({
    phoneNum: value
   });
  },
  getUsername:function(e){
    var value  = e.detail.value;
    this.setData({
      Username: value
    });
  },
  formSubmit:function(e){
    var param = e.detail.value;
    this.mysubmit(param);
  },
  mysubmit:function (param){
    var userName = param.username.trim();
    var password = param.password.trim();
    var num = param.email.trim();
    var flag = this.checkUserName(num)&&this.checkPassword(param)&&this.checkSmsCode(param)
    var that = this;
    if(flag){
      // send the new password to server
      wx.request({
        url: this.data.server + 'loginCus/resetPassword',
        data: {
          name : userName,
          newPassword: password
        },
        success: function(res) {
          console.log(res.data);
          if (res.data.code == 1){
            wx.showToast({
              title: 'Reset Success !',
              icon: 'success',
              duration: 1500
            })
          }
          else {
            // show the message from server
            wx.showModal({
              title: 'Error',
              showCancel:false,
              content: res.data.msg,
              confirmText:'OK',
            });
          }
        }
      })
        this.setregistData1();
        setTimeout(function(){
          wx.showToast({
            title: 'Reset Success !',
            icon: 'success',
            duration: 1500
          });
          that.setregistData2();
          that.redirectTo(param);
        },2000);
    }
  },
  setregistData1:function(){
    this.setData({
      registBtnTxt:"Submitting...",
      registDisabled: !this.data.registDisabled,
      registBtnBgBgColor:"#999",
      btnLoading:!this.data.btnLoading
    });
  },
  setregistData2:function(){
    this.setData({
      registBtnTxt:"Submit",
      registDisabled: !this.data.registDisabled,
      registBtnBgBgColor:"#ff9900",
      btnLoading:!this.data.btnLoading
    });
  },
  checkUserName:function(num){
    var phone = util.regexConfig().email;
    // var inputUserName = param.username.trim();
    if(phone.test(num)){
      return true;
    }else{
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'Enter a valid email address',
        confirmText:'OK',
      });
      return false;
    }
  },
  checkPhoneIsRegist: function(phoneNum) {
    return new Promise((resolve, reject) => {
      // get all registered emails from server
      wx.request({
        url: this.data.server + 'loginCus/getEmails',
        success: function(res) {
          var emails = res.data;
          for (var i = 0; i < emails.length; i++) {
            if (emails[i] == phoneNum) {
              resolve(true); // phone number is registered, resolve with true
              return;
            }
          }
          // phone number is not registered, resolve with false
          resolve(false);
        },
        fail: function(error) {
          reject(error); // reject with error message
        }
      })
    });
  },

  checkPassword:function(param){
    var userName = param.username.trim();
    var password = param.password.trim();
    var confirmPwd = param.confirmPwd.trim();
    if (password != confirmPwd) {
        wx.showModal({
            title: 'Tips',
            showCancel:false,
            content: 'The two passwords are different',
            confirmText:'OK',
        });
        return false;
    }
    if(password.length<=0){
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'Set new password',
        confirmText:'OK',
      });
      return false;
    }else if(password.length<6||password.length>20){
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'Password length is 6-20',
        confirmText:'OK',
      });
      return false;
    }else{
      return true;
    }
  },
  getSmsCode:function(){
    var phoneNum = this.data.phoneNum;
    var Username = this.data.Username;
    var that = this;
    var count = 90;
    if(this.checkUserName(phoneNum)&&this.checkPhoneIsRegist(phoneNum)){
      // send a request to server to get sms code
      wx.request({
        url: that.data.server + 'getCaptchaReset',
        data: {
          email: phoneNum,
          Username: Username
        },
        success: function(res) {
          console.log(res.data);
          if (res.data.code == 1){
            that.setData({
              smsCode: res.data.data
            })
          }
          else {
            // show the message from server
            wx.showModal({
              title: 'Error',
              showCancel:false,
              content: res.data.msg,
              confirmText:'OK',
            });
          }
        }
      });
      var si = setInterval(function(){
        if(count > 0){
          count--;
          that.setData({
            getSmsCodeBtnTxt:count+' s',
            getSmsCodeBtnColor:"#999",
            smsCodeDisabled: true
          });
        }else{
          that.setData({
            getSmsCodeBtnTxt:"Get Code",
            getSmsCodeBtnColor:"#ff9900",
            smsCodeDisabled: false
          });
          count = 60;
          clearInterval(si);
        }
      },1000);
    }
  },
  checkSmsCode:function(param){
    var smsCode = param.smsCode.trim();
    var tempSmsCode = this.data.smsCode;
    console.log(smsCode);
    console.log(tempSmsCode);
    if(smsCode!=tempSmsCode){
      wx.showModal({
        title: 'Error',
        showCancel:false,
        content: 'Enter the correct verification code',
        confirmText:'OK',
      });
      return false;
    }else{
      console.log('Sms code is right !')
      return true;
    }
  },
  redirectTo:function(){
    wx.redirectTo({
      url: '../login/index'
    })
  }

})
