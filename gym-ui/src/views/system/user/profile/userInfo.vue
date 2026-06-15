<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$tr('profile.nickname')" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item> 
    <el-form-item :label="$tr('profile.phone')" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item :label="$tr('profile.email')" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item :label="$tr('profile.gender')">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">{{ $tr('profile.male') }}</el-radio>
        <el-radio label="1">{{ $tr('profile.female') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $tr('common.save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $tr('common.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: this.$tr('profile.nicknameRequired'), trigger: "blur" }
        ],
        email: [
          { required: true, message: this.$tr('profile.emailRequired'), trigger: "blur" },
          {
            type: "email",
            message: this.$tr('profile.emailInvalid'),
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: this.$tr('profile.phoneRequired'), trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: this.$tr('profile.phoneInvalid'),
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
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
