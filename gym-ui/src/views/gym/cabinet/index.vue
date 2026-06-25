<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="GymNo" prop="cabinetNo">
        <el-input
          v-model="queryParams.cabinetNo"
          placeholder="Please enter Gym number"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="User" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="Please enter member name"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Date" prop="cabinetDate">
        <el-date-picker clearable size="small"
          v-model="queryParams.cabinetDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="Select due date">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
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
          v-hasPermi="['gym:cabinet:add']"
        >Add</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['gym:cabinet:edit']"
        >Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['gym:cabinet:remove']"
        >Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['gym:cabinet:export']"
        >Download</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cabinetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="GymNo" align="center" prop="cabinetNo" />
      <el-table-column label="User" align="center" prop="memberName" />
      <el-table-column label="Due date" align="center" prop="cabinetDate">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.cabinetDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Status" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.memberId==null" type="success">Available</el-tag>
          <el-tag v-else-if="checkTime(scope.row)">Used</el-tag>
          <el-tag v-else type="danger">Expired</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['gym:cabinet:edit']"
          >Edit</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            v-if="scope.row.memberId==null||!checkTime(scope.row)"
            @click="handleDistribute(scope.row)"
            v-hasPermi="['gym:cabinet:edit']"
          >Distribute</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            v-if="!checkTime(scope.row)"
            @click="handleRenewal(scope.row)"
            v-hasPermi="['gym:cabinet:edit']"
          >Renewal</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['gym:cabinet:remove']"
          >Delete</el-button>
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

    <!-- 添加或修改租柜对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="GymNo" prop="cabinetNo">
          <el-input v-model="form.cabinetNo" placeholder="Please enter Gym number" />
        </el-form-item>
        <el-form-item label="Due date" prop="cabinetNo">
          <el-date-picker clearable size="small"
                          v-model="form.cabinetDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="Select due date">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">Submit</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>

    <!-- 租柜续期对话框 -->
    <el-dialog :title="title" :visible.sync="renewal" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-input-number v-model="form.renewal" :min="1" :max="10" label="Desc" />(per month)
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">Submit</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>

    <!-- 分配用户 -->
    <el-dialog :title="title" :visible.sync="distribute" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="User">
          <el-select v-model="form.memberId" filterable placeholder="Please select user">
            <el-option
              v-for="item in memberList"
              :key="item.memberId"
              :label="item.memberName"
              :value="item.memberId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Due date" prop="cabinetDate">
          <el-date-picker clearable size="small"
                          v-model="form.cabinetDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="Select due date">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">Submit</el-button>
        <el-button @click="cancel">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCabinet, getCabinet, delCabinet, addCabinet, updateCabinet,renewal,distribute } from "@/api/gym/cabinet";
export default {
  name: "Cabinet",
  data() {
    return {
      value:'',
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
      // 租柜表格数据
      cabinetList: [],
      //会员数据
      memberList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      renewal:false,
      distribute:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cabinetNo: null,
        cabinetDate: null,
        memberName:null,
        renewal:null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询租柜列表 */
    getList() {
      this.loading = true;
      listCabinet(this.queryParams).then(response => {
        this.cabinetList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.renewal =false;
      this.distribute =false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        cabinetId: null,
        cabinetNo: null,
        memberId: null,
        cabinetDate: null,
        memberName:null,
        renewal:null
      };
      this.memberList = [];
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
      this.ids = selection.map(item => item.cabinetId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "Add gym";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const cabinetId = row.cabinetId || this.ids
      getCabinet(cabinetId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "Edit gym";
      });
    },
    /** 续费按钮 **/
    handleRenewal(row){
      this.reset();
       const cabinetId = row.cabinetId || this.ids
       getCabinet(cabinetId).then(response => {
         this.form = response.data;
         this.renewal = true;
         this.title = "Renewal";
       });
    },
    handleDistribute(row){
      this.reset();
      const cabinetId = row.cabinetId || this.ids
      getCabinet(cabinetId).then(response => {
        this.form = response.data;
        distribute().then(response=>{
          this.memberList = response.data;
        });
        this.distribute = true;
        this.title = "Distribute";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.cabinetId != null) {
            if(this.open == true||this.distribute == true){
              updateCabinet(this.form).then(response => {
                this.$modal.msgSuccess("Edit successfully");
                this.open = false;
                this.distribute = false;
                this.getList();
              });
            }else {
              renewal(this.form).then(response => {
                this.$modal.msgSuccess("Renew successfully");
                this.renewal = false;
                this.getList();
              });
            }
          } else {
            addCabinet(this.form).then(response => {
              this.$modal.msgSuccess("Add successfully");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    checkTime(row){
      if(row.cabinetDate == null) return true;
      var effective  = row.cabinetDate.replace("-", "/"); //替换字符，变成标准格式
      var today =  new Date();
      var date = new Date(Date.parse(effective));
      if(today<date)return true;
      else return false;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const cabinetIds = row.cabinetId || this.ids;
      this.$modal.confirm('Confirm to delete the data item with the rental cabinet number "' + cabinetIds + '"? ').then(function() {
        return delCabinet(cabinetIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("Delete successfully");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gym/cabinet/export', {
        ...this.queryParams
      }, `cabinet_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
