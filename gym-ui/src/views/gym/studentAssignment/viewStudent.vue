<template>
  <div class="app-container">
    <h4 class="form-header h4">Coach information</h4>
    <el-form ref="form" :model="form" label-width="80px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item label="Name" prop="nickName">
            <el-input v-model="form.nickName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item label="Account" prop="userName">
            <el-input v-model="form.userName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <h4 class="form-header h4">Student information</h4>
    <el-table v-loading="loading" :row-key="getRowKey" @row-click="clickRow" ref="table" @selection-change="handleSelectionChange" :data="students.slice((pageNum-1)*pageSize,pageNum*pageSize)">
      <el-table-column label="ID" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Name" align="center" prop="memberName" />
      <el-table-column label="Gender" align="center" prop="memberSex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.memberSex" />
        </template>
      </el-table-column>
      <el-table-column label="Phone" align="center" prop="memberPhone" />
      <el-table-column label="Email" align="center" prop="memberEmail" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />

    <el-dialog title="Choose student" :visible.sync="selectDialog">
      <el-table :data="selectStudents" @selection-change="handleStudentSelectionChange">
        <el-table-column label="ID" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="Name" align="center" prop="memberName" />
        <el-table-column label="Gender" align="center" prop="memberSex">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.memberSex" />
          </template>
        </el-table-column>
        <el-table-column label="Phone" align="center" prop="memberPhone" />
        <el-table-column label="Email" align="center" prop="memberEmail" />
      </el-table>
      <el-form label-width="100px">
        <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
          <el-button type="primary" @click="submitForm()">Submit</el-button>
          <el-button @click="close()">Return</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { getStudent,getStudentNoTeacher,updateTeacher,deleteTeacher} from "@/api/gym/studentAssignment";

export default {
  name: "AuthRole",
  dicts: ['sys_user_sex'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 分页信息
      total: 0,
      pageNum: 1,
      pageSize: 10,
      // 选中角色编号
      studentIds:[],
      // 角色信息
      students: [],
      selectDialog:false,
      selectStudents:[],
      selectStudentIds:[],
      // 用户信息
      form: {}
    };
  },
  created() {
    const teacherId = this.$route.params && this.$route.params.userId;
    if (teacherId) {
      this.loading = true;
      getStudent(teacherId).then((response) => {
        this.form = response.user;
        this.students = response.students;
        this.total = this.students.length;
        this.$nextTick(() => {
          this.students.forEach((row) => {
            if (row.flag) {
              this.$refs.table.toggleRowSelection(row);
            }
          });
        });
        this.loading = false;
      });
    }
  },
  methods: {
    /** 单击选中行数据 */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.studentIds = selection.map((item) => item.memberId);
    },
    // 多选框选中数据
    handleStudentSelectionChange(selection) {
      this.selectStudentIds = selection.map((item) => item.memberId);
    },
    // 保存选中的数据编号
    getRowKey(row) {
      return row.roleId;
    },
    getStudentList(){
      this.loading = true;
      getStudent(this.form.userId).then((response) => {
        this.students = response.students;
        this.total = this.students.length;
        this.$nextTick(() => {
          this.students.forEach((row) => {
            if (row.flag) {
              this.$refs.table.toggleRowSelection(row);
            }
          });
        });
        this.loading = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      const userId = this.form.userId;
      const selectStudentIds = this.selectStudentIds.join(",");
      updateTeacher({ teacherId: userId, students: selectStudentIds }).then((response) => {
        this.$modal.msgSuccess("Distribute successfully");
        this.selectDialog= false;
        this.getStudentList();
      });
    },
    /** 关闭按钮 */
    close() {
      this.selectDialog =false;
    },
    handleDelete(row){
      deleteTeacher(row.memberId).then((response) => {
        this.$modal.msgSuccess("Delete successfully");
        this.getStudentList();
      });
    },
    handleAdd(){
      this.selectDialog = true;
      getStudentNoTeacher().then((response) => {
        this.selectStudents = response.students;
      });
    }
  },
};
</script>
