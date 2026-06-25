<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="莨壼遭蟋灘錐" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="隸ｷ霎灘・莨壼遭蟋灘錐"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="莨壼遭蟷ｴ鮴・ prop="memberAge">
        <el-input
          v-model="queryParams.memberAge"
          placeholder="隸ｷ霎灘・莨壼遭蟷ｴ鮴・
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="莨壼遭謇区惻蜿ｷ" prop="memberPhone">
        <el-input
          v-model="queryParams.memberPhone"
          placeholder="隸ｷ霎灘・莨壼遭謇区惻蜿ｷ"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="莨壼遭驍ｮ邂ｱ" prop="memberEmail">
        <el-input
          v-model="queryParams.memberEmail"
          placeholder="隸ｷ霎灘・莨壼遭驍ｮ邂ｱ"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="莨壼遭逕滓律" prop="memberBirthday">
        <el-date-picker clearable size="small"
          v-model="queryParams.memberBirthday"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="騾画叫莨壼遭逕滓律">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">謳懃ｴ｢</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">驥咲ｽｮ</el-button>
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
        >譁ｰ蠅・/el-button>
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
        >菫ｮ謾ｹ</el-button>
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
        >蛻髯､</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['gym:member:export']"
        >蟇ｼ蜃ｺ</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="莨壼遭蟋灘錐" align="center" prop="memberName" />
      <el-table-column label="莨壼遭蟷ｴ鮴・ align="center" prop="memberAge" />
      <el-table-column label="莨壼遭諤ｧ蛻ｫ" align="center" prop="memberSex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.memberSex" />
        </template>
      </el-table-column>
      <el-table-column label="莨壼遭謇区惻蜿ｷ" align="center" prop="memberPhone" />
      <el-table-column label="莨壼遭驍ｮ邂ｱ" align="center" prop="memberEmail" />
      <el-table-column label="莨壼遭逕滓律" align="center" prop="memberBirthday" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.memberBirthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="謫堺ｽ・ align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['gym:member:edit']"
          >菫ｮ謾ｹ</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['gym:member:remove']"
          >蛻髯､</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-checked"
            v-if="scope.row.vipId==null"
            @click="openApplyCard(scope.row)"
          >莨壼遭蠑蜊｡
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

    <!-- 豺ｻ蜉謌紋ｿｮ謾ｹ莨壼遭邂｡逅・ｯｹ隸晄｡・-->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="莨壼遭蟋灘錐" prop="memberName">
          <el-input v-model="form.memberName" placeholder="隸ｷ霎灘・莨壼遭蟋灘錐" />
        </el-form-item>
        <el-form-item label="莨壼遭諤ｧ蛻ｫ">
          <el-select v-model="form.memberSex" placeholder="隸ｷ騾画叫">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="莨壼遭謇区惻蜿ｷ" prop="memberPhone">
          <el-input v-model="form.memberPhone" placeholder="隸ｷ霎灘・莨壼遭謇区惻蜿ｷ" />
        </el-form-item>
        <el-form-item label="莨壼遭驍ｮ邂ｱ" prop="memberEmail">
          <el-input v-model="form.memberEmail" placeholder="隸ｷ霎灘・莨壼遭驍ｮ邂ｱ" />
        </el-form-item>
        <el-form-item label="莨壼遭逕滓律" prop="memberBirthday">
          <el-date-picker clearable size="small"
            v-model="form.memberBirthday"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="騾画叫莨壼遭逕滓律">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">遑ｮ 螳・/el-button>
        <el-button @click="cancel">蜿・豸・/el-button>
      </div>
    </el-dialog>
    <el-dialog title="隸ｷ騾画叫蠑蜊｡譌ｶ髣ｴ" :visible.sync="apply" width="500px" append-to-body>
      <el-input-number v-model="applyTime" :min="1" :max="100" label="謠剰ｿｰ譁・ｭ・ /> ・域怦・・
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleApplyCard">遑ｮ 螳・/el-button>
        <el-button @click="cancel">蜿・豸・/el-button>
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
      // 驕ｮ鄂ｩ螻・
      loading: true,
      // 騾我ｸｭ謨ｰ扈・
      ids: [],
      // 髱槫黒荳ｪ遖∫畑
      single: true,
      // 髱槫､壻ｸｪ遖∫畑
      multiple: true,
      // 譏ｾ遉ｺ謳懃ｴ｢譚｡莉ｶ
      showSearch: true,
      // 諤ｻ譚｡謨ｰ
      total: 0,
      // 莨壼遭邂｡逅・｡ｨ譬ｼ謨ｰ謐ｮ
      memberList: [],
      // 蠑ｹ蜃ｺ螻よ・｢・
      title: "",
      // 譏ｯ蜷ｦ譏ｾ遉ｺ蠑ｹ蜃ｺ螻・
      open: false,
      apply:false,
      applyRow:{},
      applyTime:12,
      // 譟･隸｢蜿よ焚
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
      // 陦ｨ蜊募盾謨ｰ
      form: {},
      // 陦ｨ蜊墓｡鬪・
      rules: {
        memberName: [
          { required: true, message: "莨壼遭蟋灘錐荳崎・荳ｺ遨ｺ", trigger: "blur" }
        ],
        memberPhone:[
          {required: true, message: "莨壼遭逕ｵ隸昜ｸ崎・荳ｺ遨ｺ", trigger: "blur" },
          { min: 11, max: 11, message: '隸ｷ霎灘・豁｣遑ｮ逧・鳩隸・, trigger: ['blur', 'change'] }
        ],
        memberEmail:[
          { required: true, message: '隸ｷ霎灘・驍ｮ邂ｱ蝨ｰ蝮', trigger: 'blur' },
          { type: 'email', message: '隸ｷ霎灘・豁｣遑ｮ逧・ぐ邂ｱ蝨ｰ蝮', trigger: ['blur', 'change'] }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 譟･隸｢莨壼遭邂｡逅・・陦ｨ */
    getList() {
      this.loading = true;
      //var datas = [[${@dict.getType('sys_user_sex')}]];
      listMember(this.queryParams).then(response => {
        this.memberList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 蜿匁ｶ域潔髓ｮ
    cancel() {
      this.open = false;
      this.reset();
    },
    // 陦ｨ蜊暮㍾鄂ｮ
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
    /** 謳懃ｴ｢謖蛾聴謫堺ｽ・*/
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 驥咲ｽｮ謖蛾聴謫堺ｽ・*/
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 螟夐画｡・我ｸｭ謨ｰ謐ｮ
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.memberId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 譁ｰ蠅樊潔髓ｮ謫堺ｽ・*/
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "豺ｻ蜉莨壼遭邂｡逅・;
    },
    /** 菫ｮ謾ｹ謖蛾聴謫堺ｽ・*/
    handleUpdate(row) {
      this.reset();
      const memberId = row.memberId || this.ids
      getMember(memberId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "菫ｮ謾ｹ莨壼遭邂｡逅・;
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
    /** 謠蝉ｺ､謖蛾聴 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.memberId != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess("菫ｮ謾ｹ謌仙粥");
              this.open = false;
              this.getList();
            });
          } else {
            addMember(this.form).then(response => {
              this.$modal.msgSuccess("譁ｰ蠅樊・蜉・);
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 蛻髯､謖蛾聴謫堺ｽ・*/
    handleDelete(row) {
      const memberIds = row.memberId || this.ids;
      this.$modal.confirm('譏ｯ蜷ｦ遑ｮ隶､蛻髯､莨壼遭邂｡逅・ｼ門捷荳ｺ"' + memberIds + '"逧・焚謐ｮ鬘ｹ・・).then(function() {
        return delMember(memberIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("蛻髯､謌仙粥");
      }).catch(() => {});
    },
    /** 蟇ｼ蜃ｺ謖蛾聴謫堺ｽ・*/
    handleExport() {
      this.download('gym/member/export', {
        ...this.queryParams
      }, `member_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
