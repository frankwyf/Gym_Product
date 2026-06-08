var util = require("../../utils/util.js");
var app = getApp();
Page({
  data:{
    server: app.globalData.server,
    registBtnTxt:"Register",
    registBtnBgBgColor:"#1CBCB4",
    getSmsCodeBtnTxt:"Get code",
    getSmsCodeBtnColor:"#1CBCB4",
    // getSmsCodeBtnTime:60,
    btnLoading:false,
    registDisabled:false,
    smsCodeDisabled:false,
    inputUserName: '',
    inputPassword: '',
    Email: '',
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
  formSubmit:function(e){
    var param = e.detail.value;
    console.log(param);
    this.mysubmit(param);
  },
  mysubmit:function (param){
    var flag = this.checkEmail(param.Email)&&this.checkPassword(param)&&this.checkSmsCode(param)
    var that = this;
    if(flag){
        this.setregistData1();
       if(this.regist(param)){
         that.redirectTo();
       }
       else {
         wx.redirectTo({
           url: '../regist/index'
         })
       }
        setTimeout(function(){
          wx.showToast({
            title: 'Register Success !',
            icon: 'success',
            duration: 1500
          });
        },2000);
    }
    else {
      that.setregistData2();
    }
  },
  getEmail:function(e){
   var value  = e.detail.value;
   this.setData({
    Email: value
   });
  },
  setregistData1:function(){
    this.setData({
      registBtnTxt:"Registering...",
      registDisabled: !this.data.registDisabled,
      registBtnBgBgColor:"#999",
      btnLoading:!this.data.btnLoading
    });
  },
  setregistData2:function(){
    this.setData({
      registBtnTxt:"Register",
      registDisabled: !this.data.registDisabled,
      registBtnBgBgColor:"#ff9900",
      btnLoading:!this.data.btnLoading
    });
  },
  checkEmail:function(param){
    var email = util.regexConfig().email;
    var inputUserName = param.trim();
    if(email.test(inputUserName)){
      return true;
    }else{
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'Email format is incorrect !',
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
        content: 'Password cannot be empty !',
        confirmText:'OK'
      });
      return false;
    }else if(password.length<6||password.length>20){
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'Password length is 6-20 bits !',
        confirmText:'OK'
      });
      return false;
    }else{
      return true;
    }
  },
  getSmsCode:function(){
    var email = this.data.Email;
    var that = this;
    var count = 90;
    if(this.checkEmail(email)){
      // get sms code from server
      wx.request({
        url: this.data.server + 'getCaptcha',
        data: {
            email: email
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
      })
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

    if(smsCode!=tempSmsCode){
      wx.showModal({
        title: 'Tips',
        showCancel:false,
        content: 'Enter the right Sms code!',
        confirmText:'OK'
      });
      return false;
    }else{
      console.log('Sms code is right !')
      return true;
    }
  },

  regist:function(param){
    // register logic
    var that = this;
    wx.request({
        url: that.data.server + 'customer/register',
      method: 'POST',
        data: {
          firstName: param.firstname,
          lastName: param.lastname,
          username: param.username,
          email: param.Email,
          password: param.password,
        },
        success: function (res) {
          console.log(res.data);
            if (res.data.code == 1){
                wx.showToast({
                    title: 'Register Success !',
                    icon: 'success',
                    duration: 1500
                });
                setTimeout(function () {
                    that.redirectTo()
                }, 2000);
            }
            else {
                wx.showModal({
                    title: 'Error',
                    showCancel: false,
                    content: res.data.data,
                    confirmText: 'OK',
                })
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
