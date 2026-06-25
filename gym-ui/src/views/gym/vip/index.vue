<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="扈大ｮ壻ｼ壼遭蟋灘錐" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="隸ｷ霎灘・莨壼遭蟋灘錐"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="扈大ｮ壻ｼ壼遭謇区惻蜿ｷ" prop="memberPhone">
        <el-input
          v-model="queryParams.memberPhone"
          placeholder="隸ｷ霎灘・莨壼遭謇区惻蜿ｷ"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="莨壼遭蜊｡蜿ｷ" prop="vipNo">
        <el-input
          v-model="queryParams.vipNo"
          placeholder="隸ｷ霎灘・莨壼遭蜊｡蜿ｷ"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="莨壼遭蜊｡譛画譜譌･譛・" prop="effective">
        <el-date-picker clearable size="small"
          v-model="queryParams.effective"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="騾画叫莨壼遭蜊｡譛画譜譌･譛・">
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['gym:vip:export']"
        >蟇ｼ蜃ｺ</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vipList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="扈大ｮ壻ｼ壼遭蟋灘錐" align="center" prop="memberName" />
      <el-table-column label="扈大ｮ壻ｼ壼遭謇区惻蜿ｷ" align="center" prop="memberPhone" />
      <el-table-column label="莨壼遭蜊｡蜿ｷ" align="center" prop="vipNo" />
      <el-table-column label="莨壼遭蜊｡譛画譜譌･譛・" align="center" prop="effective" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.effective, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="莨壼遭蜊｡迥ｶ諤・" align="center" width="180">
        <template slot-scope="scope">
          <el-tag v-if="checkTime(scope.row)" type="success">譛画譜</el-tag>
          <el-tag v-else type="danger">霑・悄</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="謫堺ｽ・" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleSignIn(scope.row)"
            v-hasPermi="['gym:vip:edit']"
            v-if="checkTime(scope.row)"
          >蛻ｰ蠎礼ｭｾ蛻ｰ</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            v-else
            @click="handleRenewal(scope.row)"
            v-hasPermi="['gym:vip:edit']"
          >扈ｭ蜊｡</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleRenewal(scope.row)"
            v-hasPermi="['gym:vip:remove']">
            菫ｮ謾ｹ
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['gym:vip:remove']"
          >蛻髯､</el-button>
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

    <!-- 豺ｻ蜉謌紋ｿｮ謾ｹ莨壼遭蜊｡邂｡逅・ｯｹ隸晄｡・-->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item v-if="!checkFormTime(form.effective)" label="扈ｭ雍ｹ譌ｶ髣ｴ" prop="effective">
          <el-input-number v-model="form.renewal" :min="1" :max="100" />
        </el-form-item>
        <el-form-item v-else label="蛻ｰ譛滓律譛・" prop="effective">
          <el-date-picker clearable size="small"
                          v-model="changeTime"
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
  </div>
</template>

<script>
import {listVip, getVip, delVip, addVip, updateVip, signIn, renewal} from "@/api/gym/vip";
import {getUsage} from "@/api/gym/usage";

export default {
  name: "Vip",
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
      // 莨壼遭蜊｡邂｡逅・｡ｨ譬ｼ謨ｰ謐ｮ
      vipList: [],
      // 蠑ｹ蜃ｺ螻よ・｢・
      title: "",
      // 譏ｯ蜷ｦ譏ｾ遉ｺ蠑ｹ蜃ｺ螻・
      open: false,

      changeTime:"0000-00-00",

      // 譟･隸｢蜿よ焚
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vipNo: null,
        effective: null
      },
      // 陦ｨ蜊募盾謨ｰ
      form: {
        effective:"0000-00-00",
      },
      // 陦ｨ蜊墓｡鬪・
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 譟･隸｢莨壼遭蜊｡邂｡逅・・陦ｨ */
    getList() {
      this.loading = true;
      listVip(this.queryParams).then(response => {
        this.vipList = response.rows;
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
        vipId: null,
        vipNo: null,
        effective: null,
        memberName:null,
        memberPhone:null
      };
      this.resetForm("form");
    },
    //譽譟･莨壼遭蜊｡譏ｯ蜷ｦ霑・悄
    checkTime(row){
      var effective  = row.effective.replace("-", "/"); //譖ｿ謐｢蟄礼ｬｦ・悟序謌先・㊥譬ｼ蠑・
      var today =  new Date();
      var date = new Date(Date.parse(effective));
      if(today<date)return true;
      else return false;
    },
    checkFormTime(time){
      var effective  = time.replace("-", "/"); //譖ｿ謐｢蟄礼ｬｦ・悟序謌先・㊥譬ｼ蠑・
      var today =  new Date();
      var date = new Date(Date.parse(effective));
      if(today<date)return true;
      else return false;
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
    // 螟夐画｡・我ｸｭ謨ｰ謐ｮ
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.vipId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 譁ｰ蠅樊潔髓ｮ謫堺ｽ・*/
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "豺ｻ蜉莨壼遭蜊｡邂｡逅・";
    },
    /** 扈ｭ雍ｹ謖蛾聴 **/
    handleRenewal(row){
      this.reset();
      this.changeTime = row.effective;
      const vipId = row.vipId || this.ids
      getVip(vipId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "扈ｭ蜊｡";
      });
    },
    /** 菫ｮ謾ｹ謖蛾聴謫堺ｽ・*/
    handleUpdate(row) {
      this.reset();
      const vipId = row.vipId || this.ids
      getVip(vipId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "菫ｮ謾ｹ莨壼遭蜊｡邂｡逅・";
      });
    },
    handleSignIn(row){
      const vipId = row.vipId || this.ids
      signIn(vipId).then(response => {
        this.$modal.msgSuccess("遲ｾ蛻ｰ謌仙粥");
      });
    },
    /** 謠蝉ｺ､謖蛾聴 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.effective = this.changeTime;
          if (this.form.vipId != null) {
            renewal(this.form).then(response => {
              this.$modal.msgSuccess("扈ｭ雍ｹ謌仙粥");
              this.open = false;
              this.getList();
            });
          } else {
            addVip(this.form).then(response => {
              this.$modal.msgSuccess("譁ｰ蠅樊・蜉・");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 蛻髯､謖蛾聴謫堺ｽ・*/
    handleDelete(row) {
      const vipIds = row.vipId || this.ids;
      this.$modal.confirm('譏ｯ蜷ｦ遑ｮ隶､蛻髯､莨壼遭蜊｡邂｡逅・ｼ門捷荳ｺ"' + vipIds + '"逧・焚謐ｮ鬘ｹ・・').then(function() {
        return delVip(vipIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("蛻髯､謌仙粥");
      }).catch(() => {});
    },
    /** 蟇ｼ蜃ｺ謖蛾聴謫堺ｽ・*/
    handleExport() {
      this.download('gym/vip/export', {
        ...this.queryParams
      }, `vip_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
