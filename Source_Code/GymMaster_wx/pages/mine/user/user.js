// pages/user/user.js
var app = getApp();
Page({
  data: {
    server: app.globalData.server,
    profilePictureUrl: '',
    height: 170,
    weight: 60,
    goalWeight: 70,
    weekGoal: 3,
    targetDescription: "",
    heightOptions: Array.from({ length: 121 }, (v, k) => k + 130),
    weightOptions: Array.from({ length: 121 }, (v, k) => k + 30),
    weekGoalOptions: Array.from({ length: 8 }, (v, k) => k),
    heightIndex: 40,
    weightIndex: 30,
    goalWeightIndex: 40,
    weekGoalIndex: 3,
  },
  onShow() {
    var that = this;
    that.onLoad();
  },
  onLoad: function (options) {
    var that = this;
    // get the user info from backend
    wx.request({
      url: this.data.server + 'customer/CheckInformation',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data.data.customer);
        that.setData({
          profilePictureUrl: res.data.data.customer.profile,
          username: res.data.data.customer.username,
          firstName: res.data.data.customer.firstName,
            lastName: res.data.data.customer.lastName,
            email: res.data.data.customer.email,
          gender: res.data.data.customer.gender
        })
      }
    })
    // get the goal info from backend
    wx.request({
      url: this.data.server + 'customer/goal',
      header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
      },
      success: function (res) {
        console.log(res.data.data);
        that.setData({
          height: res.data.data.height,
          weight: res.data.data.weight,
          goalWeight: res.data.data.goalWeight,
          weekGoal: res.data.data.weekGoal,
          targetDescription: res.data.data.target,
        })
      }

    })
  },
  uploadProfilePicture: function () {
    var that = this;
    wx.chooseImage({
      success: function (res) {
        console.log("choose image success");
        const tempFilePaths = res.tempFilePaths;
        wx.uploadFile({
          url: that.data.server + 'file/upload/customer',
          method: 'POST',
          filePath: tempFilePaths[0],
          header: {
            'token': app.globalData.userInfo
          },
          name: 'file',
          success: function (res) {
            console.log("file upload success");
            const object = JSON.parse(res.data);
            console.log(object.data);
            // update the profile picture url
            wx.request({
              url: that.data.server + 'file/download',
              header: {
                'content-type': 'application/json',
                'token': app.globalData.userInfo
              },
              data: {
                name: object.data
              },
              success: function (res) {
                console.log(res.data.data);
                wx.showToast({
                  title: 'Profile updated',
                  icon: 'success',
                  duration:1500,
                })
              }
            })
          }
        });
      }
    });
  },
  onWeightPickerChange: function (event) {
    const { value } = event.detail;
    this.setData({
      weight: this.data.weightOptions[value],
      weightIndex: value,
    });
  },
  onGoalWeightPickerChange: function (event) {
    const { value } = event.detail;
    this.setData({
      goalWeight: this.data.weightOptions[value],
      goalWeightIndex: value,
    });
  },
  onWeekGoalPickerChange: function (event) {
    const { value } = event.detail;
    this.setData({
      weekGoal: this.data.weekGoalOptions[value],
      weekGoalIndex: value,
    });
  },
  onTargetDescriptionInput: function (event) {
    const { value } = event.detail;
    this.setData({
      targetDescription: value,
    });
  },
  onHeightPickerChange: function (event) {
    const { value } = event.detail;
    this.setData({
      height: this.data.heightOptions[value],
      heightIndex: value,
    });
  },
  onGenderChange: function (event) {
    const { value } = event.detail;
    this.setData({
      gender: value,
    });
  },
  onFormSubmitcus: function (event) {

  // Get the form data from the event object
  const formData = event.detail.value;

  // Submit the form data to the server for processing
  wx.request({
    url: this.data.server + 'customer/update',
    method: 'POST',
    header: {
        'content-type': 'application/json',
        'token': app.globalData.userInfo
    },
    data: formData,
    success: function (res) {
      // Handle the response from the server, such as displaying a success message
      console.log(res.data);
    },
    fail: function (res) {
      // Handle errors, such as displaying an error message
      console.log(res);
    }
  });
  // Optional: Redirect the user to a different page or display a success message
    wx.showToast({
        title: 'Account Updated',
        icon: 'success',
        duration: 2000
    })
},
  onFormSubmitgoal: function (event) {
    console.log(event.detail.value);
  // Get the form data from the event object
  const formData1 = event.detail.value;

  // Submit the form data to the server for processing
  wx.request({
    url: this.data.server + 'customer/updateGoal',
    method: 'POST',
    header: {
      'content-type': 'application/json',
      'token': app.globalData.userInfo
    },
    data: formData1,
    success: function (res) {
      // Handle the response from the server, such as displaying a success message
      console.log(res.data);
    },
    fail: function (res) {
      // Handle errors, such as displaying an error message
      console.log(res);
    }
  });

  // Optional: Redirect the user to a different page or display a success message
    wx.showToast({
      title: 'Goal Updated',
      icon: 'success',
      duration: 2000
    })
}

});
