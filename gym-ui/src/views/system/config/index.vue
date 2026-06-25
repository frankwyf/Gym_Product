<template>
  <div>
    <el-table :data="coaches" style="width: 100%">
      <el-table-column width="20"></el-table-column>
      <el-table-column prop="coaid" :label="$tr('config.coachId')"></el-table-column>
      <el-table-column prop="username" :label="$tr('config.username')"></el-table-column>
      <el-table-column prop="firstName" :label="$tr('config.firstName')"></el-table-column>
      <el-table-column prop="lastName" :label="$tr('config.lastName')"></el-table-column>
      <el-table-column prop="certifications" :label="$tr('config.certifications')"></el-table-column>
      <el-table-column prop="salary" :label="$tr('config.salary')"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from 'axios'
const GYM_API = process.env.VUE_APP_GYMMASTER_API || ''

export default {
  data() {
    return {
      coaches: [],
    };
  },
  mounted() {
    axios.get(`${GYM_API}/until/coaches`).then((response) => {
      this.coaches = response.data.data;
    });
  },
};
</script>
