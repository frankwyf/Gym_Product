<template>
  <div class="app-container">
    <h4 class="form-header h4">Coach information</h4>
    <el-table v-loading="loading" :row-key="getRowKey" @row-click="clickRow" ref="table" @selection-change="handleSelectionChange" :data="teachers.slice((pageNum-1)*pageSize,pageNum*pageSize)">
      <el-table-column label="Name" align="center" prop="nickName" />
      <el-table-column label="Phone" align="center" prop="phonenumber" />
      <el-table-column label="Email" align="center" prop="email" />
    </el-table>
  </div>
</template>

<script>
import { getStudent,getStudentNoTeacher,updateTeacher,deleteTeache,getTeacher} from "@/api/gym/studentAssignment";

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
      teachers: [],
      selectDialog:false,
      selectStudents:[],
      selectStudentIds:[],
      // 用户信息
      form: {}
    };
  },
  created() {
    const memberId = this.$route.params && this.$route.params.userId;
    if (memberId) {
      this.loading = true;
      getTeacher(memberId).then((response) => {
        this.teachers = response.teachers;
        this.total = this.teachers.length;
        this.$nextTick(() => {
          this.teachers.forEach((row) => {
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
