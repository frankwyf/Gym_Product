<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="绑定会员姓名" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="请输入会员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="绑定会员手机号" prop="memberPhone">
        <el-input
          v-model="queryParams.memberPhone"
          placeholder="请输入会员手机号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会员卡号" prop="vipNo">
        <el-input
          v-model="queryParams.vipNo"
          placeholder="请输入会员卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会员卡有效日期" prop="effective">
        <el-date-picker clearable size="small"
          v-model="queryParams.effective"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择会员卡有效日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['gym:vip:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vipList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="绑定会员姓名" align="center" prop="memberName" />
      <el-table-column label="绑定会员手机号" align="center" prop="memberPhone" />
      <el-table-column label="会员卡号" align="center" prop="vipNo" />
      <el-table-column label="会员卡有效日期" align="center" prop="effective" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.effective, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会员卡状态" align="center" width="180">
        <template slot-scope="scope">
          <el-tag v-if="checkTime(scope.row)" type="success">有效</el-tag>
          <el-tag v-else type="danger">过期</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleSignIn(scope.row)"
            v-hasPermi="['gym:vip:edit']"
            v-if="checkTime(scope.row)"
          >到店签到</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            v-else
            @click="handleRenewal(scope.row)"
            v-hasPermi="['gym:vip:edit']"
          >续卡</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleRenewal(scope.row)"
            v-hasPermi="['gym:vip:remove']">
            修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['gym:vip:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会员卡管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item v-if="!checkFormTime(form.effective)" label="续费时间" prop="effective">
          <el-input-number v-model="form.renewal" :min="1" :max="100" />
        </el-form-item>
        <el-form-item v-else label="到期日期" prop="effective">
          <el-date-picker clearable size="small"
                          v-model="changeTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择会员生日">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listVip, getVip, delVip, addVip, updateVip, signIn, renewal} from "@/api/gym/vip";
import {getUsage} from "@/api/gym/usage";

export default {
  name: "Vip",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 会员卡管理表格数据
      vipList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      changeTime:"0000-00-00",

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vipNo: null,
        effective: null
      },
      // 表单参数
      form: {
        effective:"0000-00-00",
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询会员卡管理列表 */
    getList() {
      this.loading = true;
      listVip(this.queryParams).then(response => {
        this.vipList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        vipId: null,
        vipNo: null,
        effective: null,
        memberName:null,
        memberPhone:null
      };
      this.resetForm("form");
    },
    //检查会员卡是否过期
    checkTime(row){
      var effective  = row.effective.replace("-", "/"); //替换字符，变成标准格式
      var today =  new Date();
      var date = new Date(Date.parse(effective));
      if(today<date)return true;
      else return false;
    },
    checkFormTime(time){
      console.log(time)
      var effective  = time.replace("-", "/"); //替换字符，变成标准格式
      var today =  new Date();
      var date = new Date(Date.parse(effective));
      if(today<date)return true;
      else return false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.vipId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会员卡管理";
    },
    /** 续费按钮 **/
    handleRenewal(row){
      this.reset();
      this.changeTime = row.effective;
      const vipId = row.vipId || this.ids
      getVip(vipId).then(response => {
        this.form = response.data;
        console.log(this.form);
        this.open = true;
        this.title = "续卡";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const vipId = row.vipId || this.ids
      getVip(vipId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会员卡管理";
      });
    },
    handleSignIn(row){
      const vipId = row.vipId || this.ids
      signIn(vipId).then(response => {
        this.$modal.msgSuccess("签到成功");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.effective = this.changeTime;
          if (this.form.vipId != null) {
            renewal(this.form).then(response => {
              this.$modal.msgSuccess("续费成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVip(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const vipIds = row.vipId || this.ids;
      this.$modal.confirm('是否确认删除会员卡管理编号为"' + vipIds + '"的数据项？').then(function() {
        return delVip(vipIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gym/vip/export', {
        ...this.queryParams
      }, `vip_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
