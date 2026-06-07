// pages/wallet.js
var app = getApp();
Page({
  // Add your page data here
  data: {
    server: app.globalData.server,
    // Add your accounts data here
    accounts: [],
    // Add your modal data here
    showChargeModal: false,
    chargeAmount: 0,
    chargingAccountId: null
  },
  onLoad(query) {
    var that = this;
    // load the accounts data from the server
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
  },
  // Handle the form submit event
  onSubmit(event) {
    const formData = event.detail.value;
    formData.balance = parseFloat(formData.balance);
    console.log(formData);
    wx.request({
      url: this.data.server + 'account/add',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      data: {
        balance: formData.balance,
        method: formData.method,
        isActive: formData.isActive,
      },
      success: function (res) {
        console.log("Add account response:")
        console.log(res.data);
        if (res.data.code == 1){
          wx.showToast({
            title: 'Account created!',
            icon: 'success',
          });
          wx.redirectTo({
            url: '../wallet/wallet'
          })
        }
        else {
          wx.showModal({
            title: 'Error',
            content: res.data.msg,
            showCancel: false,
          })
        }
      }
    })
  },


  // Handle delete the  account event
  onEditAccount(event) {
    const accountId = event.currentTarget.dataset.id;
    this.setData({
      showChargeModal: true,
      chargeAmount: 0,
      chargingAccountId: accountId
    });
  },

  onChargeInput(event) {
    const amount = event.detail.value;
    this.setData({ chargeAmount: amount });
  },

  onChargeCancel() {
    this.setData({ showChargeModal: false });
  },

  onChargeConfirm() {
    const chargingAccountId = this.data.chargingAccountId;
    const chargeAmount = parseFloat(this.data.chargeAmount);
    if (chargeAmount > 0) {
      // Call a function to update the account balance with the given amount
      this.updateAccountBalance(chargingAccountId, chargeAmount);
    }
    this.setData({ showChargeModal: false });
  },

  updateAccountBalance(accountId, amount) {
    wx.request({
      url: this.data.server + 'account/edit',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      data: {
        aid: accountId,
        balance: amount
      },
      success(res) {
        console.log(res.data);
        // Update the account balance in the UI
        wx.redirectTo({
            url: '../wallet/wallet'
        })
      },
      fail(err) {
        console.error(err);
      }
    });
  },


  // Handle delete the  account event
  onDeleteAccount(event) {
    const accountId = event.currentTarget.dataset.id;
    console.log(accountId);
    wx.request({
        url: this.data.server + 'account/delete',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      data: {
          aid : accountId,
      },
      success: function (res) {
        console.log("Delete account response:")
        console.log(res.data);
        if (res.data.code == 1) {
          wx.showToast({
            title: 'Account deleted!',
            icon: 'success',
          })
          wx.redirectTo({
            url: '../wallet/wallet'
          })
        }
      }
    })
  },
});
