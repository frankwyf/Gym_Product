<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="会员姓名" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="请输入会员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会员年龄" prop="memberAge">
        <el-input
          v-model="queryParams.memberAge"
          placeholder="请输入会员年龄"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会员手机号" prop="memberPhone">
        <el-input
          v-model="queryParams.memberPhone"
          placeholder="请输入会员手机号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会员邮箱" prop="memberEmail">
        <el-input
          v-model="queryParams.memberEmail"
          placeholder="请输入会员邮箱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会员生日" prop="memberBirthday">
        <el-date-picker clearable size="small"
          v-model="queryParams.memberBirthday"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择会员生日">
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['gym:member:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['gym:member:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['gym:member:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['gym:member:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会员姓名" align="center" prop="memberName" />
      <el-table-column label="会员年龄" align="center" prop="memberAge" />
      <el-table-column label="会员性别" align="center" prop="memberSex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.memberSex" />
        </template>
      </el-table-column>
      <el-table-column label="会员手机号" align="center" prop="memberPhone" />
      <el-table-column label="会员邮箱" align="center" prop="memberEmail" />
      <el-table-column label="会员生日" align="center" prop="memberBirthday" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.memberBirthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['gym:member:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['gym:member:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-checked"
            v-if="scope.row.vipId==null"
            @click="openApplyCard(scope.row)"
          >会员开卡
          </el-button>
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

    <!-- 添加或修改会员管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="会员姓名" prop="memberName">
          <el-input v-model="form.memberName" placeholder="请输入会员姓名" />
        </el-form-item>
        <el-form-item label="会员性别">
          <el-select v-model="form.memberSex" placeholder="请选择">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="会员手机号" prop="memberPhone">
          <el-input v-model="form.memberPhone" placeholder="请输入会员手机号" />
        </el-form-item>
        <el-form-item label="会员邮箱" prop="memberEmail">
          <el-input v-model="form.memberEmail" placeholder="请输入会员邮箱" />
        </el-form-item>
        <el-form-item label="会员生日" prop="memberBirthday">
          <el-date-picker clearable size="small"
            v-model="form.memberBirthday"
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
    <el-dialog title="请选择开卡时间" :visible.sync="apply" width="500px" append-to-body>
      <el-input-number v-model="applyTime" :min="1" :max="100" label="描述文字" /> （月）
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleApplyCard">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMember, getMember, delMember, addMember, updateMember,applyCard } from "@/api/gym/member";

export default {
  name: "Member",
  dicts: ['sys_user_sex'],
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
      // 会员管理表格数据
      memberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      apply:false,
      applyRow:{},
      applyTime:12,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        memberName: null,
        memberAge: null,
        memberSex: null,
        memberPhone: null,
        memberEmail: null,
        memberBirthday: null,
        vipId: null,
        teacherId: null,
        userId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        memberName: [
          { required: true, message: "会员姓名不能为空", trigger: "blur" }
        ],
        memberPhone:[
          {required: true, message: "会员电话不能为空", trigger: "blur" },
          { min: 11, max: 11, message: '请输入正确的电话', trigger: ['blur', 'change'] }
        ],
        memberEmail:[
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询会员管理列表 */
    getList() {
      this.loading = true;
      //var datas = [[${@dict.getType('sys_user_sex')}]];
      console.log();
      listMember(this.queryParams).then(response => {
        this.memberList = response.rows;
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
        memberId: null,
        memberName: null,
        memberAge: null,
        memberSex: null,
        memberPhone: null,
        memberEmail: null,
        memberBirthday: null,
        vipId: null,
        teacherId: null,
        userId: null
      };
      this.resetForm("form");
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
      this.ids = selection.map(item => item.memberId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会员管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const memberId = row.memberId || this.ids
      getMember(memberId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会员管理";
      });
    },
    openApplyCard(row){
      this.reset();
      this.applyRow = row;
      this.apply = true;
    },
    handleApplyCard(){
      var row = this.applyRow;
      const memberId = row.memberId;
      applyCard(memberId,this.applyTime).then(response => {
        this.getList();
      });
      this.apply = false;

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.memberId != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMember(this.form).then(response => {
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
      const memberIds = row.memberId || this.ids;
      this.$modal.confirm('是否确认删除会员管理编号为"' + memberIds + '"的数据项？').then(function() {
        return delMember(memberIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gym/member/export', {
        ...this.queryParams
      }, `member_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
