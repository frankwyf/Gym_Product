<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$tr('profile.oldPassword')" prop="oldPassword">
      <el-input v-model="user.oldPassword" :placeholder="$tr('profile.oldPasswordRequired')" type="password" show-password />
    </el-form-item>
    <el-form-item :label="$tr('profile.newPassword')" prop="newPassword">
      <el-input v-model="user.newPassword" :placeholder="$tr('profile.newPasswordRequired')" type="password" show-password />
    </el-form-item>
    <el-form-item :label="$tr('profile.confirmPassword')" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" :placeholder="$tr('profile.confirmPassword')" type="password" show-password />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $tr('common.save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $tr('common.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from "@/api/system/user";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error(this.$tr('profile.passwordNotMatch')));
      } else {
        callback();
      }
    };
    return {
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: this.$tr('profile.oldPasswordRequired'), trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: this.$tr('profile.newPasswordRequired'), trigger: "blur" },
          { min: 6, max: 20, message: this.$tr('profile.passwordLength'), trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: this.$tr('profile.newPasswordRequired'), trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserPwd(this.user.oldPassword, this.user.newPassword).then(response => {
            this.$modal.msgSuccess(this.$tr('profile.updateSuccess'));
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>
