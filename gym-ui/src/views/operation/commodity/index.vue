<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$tr('commodity.name')" prop="commodityName">
        <el-input
          v-model="queryParams.commodityName"
          :placeholder="$tr('commodity.placeholderName')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$tr('commodity.price')" prop="commodityPrice">
        <el-input
          v-model="queryParams.commodityPrice"
          :placeholder="$tr('commodity.placeholderPrice')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$tr('commodity.quantity')" prop="commodityNumber">
        <el-input
          v-model="queryParams.commodityNumber"
          :placeholder="$tr('commodity.placeholderQuantity')"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $tr('commodity.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $tr('commodity.reset') }}</el-button>
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
          v-hasPermi="['operation:commodity:add']"
        >{{ $tr('commodity.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['operation:commodity:edit']"
        >{{ $tr('commodity.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['operation:commodity:remove']"
        >{{ $tr('commodity.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['operation:commodity:export']"
        >{{ $tr('commodity.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commodityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$tr('commodity.name')" align="center" prop="commodityName" />
      <el-table-column :label="$tr('commodity.price')" align="center" prop="commodityPrice" />
      <el-table-column :label="$tr('commodity.quantity')" align="center" prop="commodityNumber" />
      <el-table-column :label="$tr('commodity.action')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleInputOrOutput(scope.row,true)"
            v-hasPermi="['operation:commodity:edit']"
          >{{ $tr('commodity.inputStock') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-minus"
            @click="handleInputOrOutput(scope.row,false)"
            v-hasPermi="['operation:commodity:edit']"
          >{{ $tr('commodity.outputStock') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['operation:commodity:edit']"
          >{{ $tr('commodity.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['operation:commodity:remove']"
          >{{ $tr('commodity.delete') }}</el-button>
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
    <!-- 出库对话框 -->
    <el-dialog :title="$tr('commodity.outputDialogTitle')" :visible.sync="out" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-input-number v-model="form.inputOrOutput" :min="1" :max="form.commodityNumber" :label="$tr('commodity.outputQuantity')"></el-input-number>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitNumber(false)">{{ $tr('common.confirm') }}</el-button>
        <el-button @click="cancel">{{ $tr('common.cancel') }}</el-button>
      </div>
    </el-dialog>
    <!-- 入库对话框 -->
    <el-dialog :title="$tr('commodity.inputDialogTitle')" :visible.sync="input" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-input-number v-model="form.inputOrOutput" :min="1" :label="$tr('commodity.inputQuantity')"></el-input-number>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitNumber(true)">{{ $tr('common.confirm') }}</el-button>
        <el-button @click="cancel">{{ $tr('common.cancel') }}</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$tr('commodity.name')" required prop="commodityName">
          <el-input v-model="form.commodityName" :placeholder="$tr('commodity.placeholderName')" />
        </el-form-item>
        <el-form-item :label="$tr('commodity.price')" required prop="commodityPrice">
          <price-input :form.sync = "form" :width = "150" prop = "commodityPrice" :rules = "rules"></price-input>
        </el-form-item>
        <el-form-item :label="$tr('commodity.quantity')" prop="commodityNumber">
          <el-input-number v-model="form.commodityNumber" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item :label="$tr('commodity.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :placeholder="$tr('commodity.placeholderRemark')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $tr('common.confirm') }}</el-button>
        <el-button @click="cancel">{{ $tr('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCommodity, getCommodity, delCommodity, addCommodity, updateCommodity,addNumber,reduceNumber } from "@/api/operation/commodity";
import priceInput from '../../components/collection/priceInput'

export default {
  name: "Commodity",
  components: {
    priceInput,
  },
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
      // 商品表格数据
      commodityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //出库弹窗
      input:false,
      //入库弹窗
      out:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        commodityName: null,
        commodityPrice: null,
        commodityNumber: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        commodityPrice: [
          {
            pattern: /^1000000000$|^1000000000.0$|^1000000000.00$|^[+]{0,1}(\d{0,9})$|^[+]{0,1}(\d{0,9}\.\d{1,2})$/,
            message: this.$tr('commodity.priceRule'),
            trigger: 'blur',
          },
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询商品列表 */
    getList() {
      this.loading = true;
      listCommodity(this.queryParams).then(response => {
        this.commodityList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.input =false;
      this.out = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        commodityId: null,
        commodityName: null,
        commodityPrice: null,
        commodityNumber: null,
        inputOrOutput:null,
        remark: null
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
      this.ids = selection.map(item => item.commodityId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$tr('commodity.addTitle');
    },
    handleInputOrOutput(row,status){
      this.reset();
      const commodityId = row.commodityId || this.ids
      getCommodity(commodityId).then(response => {
        this.form = response.data;
        if(status==true){
          this.input =true;
        }else{
          this.out =true;
        }
        this.title = this.$tr('commodity.editTitle');
      });
    },
    submitNumber(status){
      if(status==true){
        addNumber(this.form).then(response => {
          this.input = false;
          this.getList();
          this.$modal.msgSuccess(this.$tr('commodity.inputSuccess'));
        });
      }else{
        reduceNumber(this.form).then(response => {
          this.out = false;
          this.getList();
          this.$modal.msgSuccess(this.$tr('commodity.outputSuccess'));
        });
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const commodityId = row.commodityId || this.ids
      getCommodity(commodityId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$tr('commodity.editTitle');
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.commodityId != null) {
            updateCommodity(this.form).then(response => {
              this.$modal.msgSuccess(this.$tr('commodity.updateSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addCommodity(this.form).then(response => {
              this.$modal.msgSuccess(this.$tr('commodity.addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const commodityIds = row.commodityId || this.ids;
      this.$modal.confirm(this.$tr('commodity.confirmDelete')).then(function() {
        return delCommodity(commodityIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$tr('commodity.deleteSuccess'));
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('operation/commodity/export', {
        ...this.queryParams
      }, `commodity_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
