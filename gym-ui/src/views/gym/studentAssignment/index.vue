
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--逕ｨ謌ｷ謨ｰ謐ｮ-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="遘∵蕗蜷咲ｧｰ" prop="nickName">
            <el-input
              v-model="queryParams.nickName"
              placeholder="隸ｷ霎灘・遘∵蕗蜷咲ｧｰ"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="謇区惻蜿ｷ遐・" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="隸ｷ霎灘・謇区惻蜿ｷ遐・"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">謳懃ｴ｢</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">驥咲ｽｮ</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="遘∵蕗郛門捷" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
          <el-table-column label="遘∵蕗逕ｨ謌ｷ蜷・" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="遘∵蕗蟋灘錐" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
          <el-table-column label="謇区惻蜿ｷ遐・" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible" />
          <el-table-column label="蛻帛ｻｺ譌ｶ髣ｴ" align="center" prop="createTime" v-if="columns[6].visible">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="謫堺ｽ・"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleManageStudent(scope.row)"
              >蛻・・</el-button>
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
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus } from "@/api/system/user";
import { listTeacher } from '@/api/gym/studentAssignment'
import { getToken } from "@/utils/auth";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: { Treeselect },
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
      // 逕ｨ謌ｷ陦ｨ譬ｼ謨ｰ謐ｮ
      userList: null,
      // 蠑ｹ蜃ｺ螻よ・｢・
      title: "",
      // 驛ｨ髣ｨ譬鷹蛾｡ｹ
      deptOptions: undefined,
      // 譏ｯ蜷ｦ譏ｾ遉ｺ蠑ｹ蜃ｺ螻・
      open: false,
      // 驛ｨ髣ｨ蜷咲ｧｰ
      deptName: undefined,
      // 鮟倩ｮ､蟇・・
      initPassword: undefined,
      // 譌･譛溯激蝗ｴ
      dateRange: [],
      // 蟯嶺ｽ埼蛾｡ｹ
      postOptions: [],
      // 隗定牡騾蛾｡ｹ
      roleOptions: [],
      // 陦ｨ蜊募盾謨ｰ
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 逕ｨ謌ｷ蟇ｼ蜈･蜿よ焚
      upload: {
        // 譏ｯ蜷ｦ譏ｾ遉ｺ蠑ｹ蜃ｺ螻ゑｼ育畑謌ｷ蟇ｼ蜈･・・
        open: false,
        // 蠑ｹ蜃ｺ螻よ・｢假ｼ育畑謌ｷ蟇ｼ蜈･・・
        title: "",
        // 譏ｯ蜷ｦ遖∫畑荳贋ｼ
        isUploading: false,
        // 譏ｯ蜷ｦ譖ｴ譁ｰ蟾ｲ扈丞ｭ伜惠逧・畑謌ｷ謨ｰ謐ｮ
        updateSupport: 0,
        // 隶ｾ鄂ｮ荳贋ｼ逧・ｯｷ豎ょ､ｴ驛ｨ
        headers: { Authorization: "Bearer " + getToken() },
        // 荳贋ｼ逧・慍蝮
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 譟･隸｢蜿よ焚
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 蛻嶺ｿ｡諱ｯ
      columns: [
        { key: 0, label: `逕ｨ謌ｷ郛門捷`, visible: true },
        { key: 1, label: `逕ｨ謌ｷ蜷咲ｧｰ`, visible: true },
        { key: 2, label: `逕ｨ謌ｷ譏ｵ遘ｰ`, visible: true },
        { key: 3, label: `驛ｨ髣ｨ`, visible: true },
        { key: 4, label: `謇区惻蜿ｷ遐`, visible: true },
        { key: 5, label: `迥ｶ諤`, visible: true },
        { key: 6, label: `蛻帛ｻｺ譌ｶ髣ｴ`, visible: true }
      ],
      // 陦ｨ蜊墓｡鬪・
      rules: {
        userName: [
          { required: true, message: "逕ｨ謌ｷ蜷咲ｧｰ荳崎・荳ｺ遨ｺ", trigger: "blur" },
          { min: 2, max: 20, message: '逕ｨ謌ｷ蜷咲ｧｰ髟ｿ蠎ｦ蠢・｡ｻ莉倶ｺ・2 蜥・20 荵矩龍', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: "逕ｨ謌ｷ譏ｵ遘ｰ荳崎・荳ｺ遨ｺ", trigger: "blur" }
        ],
        password: [
          { required: true, message: "逕ｨ謌ｷ蟇・∽ｸ崎・荳ｺ遨ｺ", trigger: "blur" },
          { min: 5, max: 20, message: '逕ｨ謌ｷ蟇・・柄蠎ｦ蠢・｡ｻ莉倶ｺ・5 蜥・20 荵矩龍', trigger: 'blur' }
        ],
        email: [
          {
            type: "email",
            message: "'隸ｷ霎灘・豁｣遑ｮ逧・ぐ邂ｱ蝨ｰ蝮",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "隸ｷ霎灘・豁｣遑ｮ逧・焔譛ｺ蜿ｷ遐・",
            trigger: "blur"
          }
        ]
      }
    };
  },
  watch: {
    // 譬ｹ謐ｮ蜷咲ｧｰ遲幃蛾Κ髣ｨ譬・
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 譟･隸｢逕ｨ謌ｷ蛻苓｡ｨ */
    getList() {
      this.loading = true;
      listTeacher(this.queryParams).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 譟･隸｢驛ｨ髣ｨ荳区級譬醍ｻ捺桷 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    // 遲幃芽鰍轤ｹ
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 闃らせ蜊募・莠倶ｻｶ
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 逕ｨ謌ｷ迥ｶ諤∽ｿｮ謾ｹ
    handleStatusChange(row) {
      let text = row.status === "0" ? "Able" : "Unable";
      this.$modal.confirm('遑ｮ隶､隕・' + text + '""' + row.userName + '"逕ｨ謌ｷ蜷暦ｼ・').then(function() {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "謌仙粥");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
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
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: []
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 螟夐画｡・我ｸｭ謨ｰ謐ｮ
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 蛻・・蟄ｦ逕・*/
    handleManageStudent: function(row) {
      const userId = row.userId;
      this.$router.push("/operation/assignment/student/" + userId);
    },
    /** 謠蝉ｺ､謖蛾聴 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("菫ｮ謾ｹ謌仙粥");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
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
      const userIds = row.userId || this.ids;
      this.$modal.confirm('譏ｯ蜷ｦ遑ｮ隶､蛻髯､逕ｨ謌ｷ郛門捷荳ｺ"' + userIds + '"逧・焚謐ｮ鬘ｹ・・').then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("蛻髯､謌仙粥");
      }).catch(() => {});
    },
    /** 蟇ｼ蜃ｺ謖蛾聴謫堺ｽ・*/
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 蟇ｼ蜈･謖蛾聴謫堺ｽ・*/
    handleImport() {
      this.upload.title = "逕ｨ謌ｷ蟇ｼ蜈･";
      this.upload.open = true;
    },
    /** 荳玖ｽｽ讓｡譚ｿ謫堺ｽ・*/
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 譁・ｻｶ荳贋ｼ荳ｭ螟・炊
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 譁・ｻｶ荳贋ｼ謌仙粥螟・炊
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "蟇ｼ蜈･扈捺棡", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 謠蝉ｺ､荳贋ｼ譁・ｻｶ
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
