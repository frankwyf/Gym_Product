<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品名称" prop="commodityName">
        <el-input
          v-model="queryParams.commodityName"
          placeholder="请输入商品名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品价格" prop="commodityPrice">
        <el-input
          v-model="queryParams.commodityPrice"
          placeholder="请输入商品价格"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品数量" prop="commodityNumber">
        <el-input
          v-model="queryParams.commodityNumber"
          placeholder="请输入商品数量"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['operation:commodity:add']"
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
          v-hasPermi="['operation:commodity:edit']"
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
          v-hasPermi="['operation:commodity:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['operation:commodity:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commodityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商品名称" align="center" prop="commodityName" />
      <el-table-column label="商品价格" align="center" prop="commodityPrice" />
      <el-table-column label="商品数量" align="center" prop="commodityNumber" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleInputOrOutput(scope.row,true)"
            v-hasPermi="['operation:commodity:edit']"
          >入库</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-minus"
            @click="handleInputOrOutput(scope.row,false)"
            v-hasPermi="['operation:commodity:edit']"
          >出库</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['operation:commodity:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['operation:commodity:remove']"
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
    <!-- 出库对话框 -->
    <el-dialog title="出库" :visible.sync="out" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-input-number v-model="form.inputOrOutput" :min="1" :max="form.commodityNumber" label="出库数量"></el-input-number>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitNumber(false)">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 入库对话框 -->
    <el-dialog title="出库" :visible.sync="input" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-input-number v-model="form.inputOrOutput" :min="1" label="出库数量"></el-input-number>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitNumber(true)">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品名称" required prop="commodityName">
          <el-input v-model="form.commodityName"  placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品价格" required prop="commodityPrice">
          <price-input :form.sync = "form" :width = "150" prop = "commodityPrice" :rules = "rules"></price-input>
        </el-form-item>
        <el-form-item label="商品数量" prop="commodityNumber">
          <el-input-number v-model="form.commodityNumber" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
            message: ' 请输入 0-10亿 的正数，可保留两位小数',
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
      this.title = "添加商品";
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
        this.title = "修改商品";
      });
    },
    submitNumber(status){
      if(status==true){
        addNumber(this.form).then(response => {
          this.input = false;
          this.getList();
          this.$modal.msgSuccess("入库成功");
        });
      }else{
        reduceNumber(this.form).then(response => {
          this.out = false;
          this.getList();
          this.$modal.msgSuccess("出库成功");
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
        this.title = "修改商品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.commodityId != null) {
            updateCommodity(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCommodity(this.form).then(response => {
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
      const commodityIds = row.commodityId || this.ids;
      this.$modal.confirm('是否确认删除商品编号为"' + commodityIds + '"的数据项？').then(function() {
        return delCommodity(commodityIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
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
