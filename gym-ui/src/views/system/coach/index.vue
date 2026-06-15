<template>
  <div>
    <el-table :data="coaches" style="width: 100%">
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
import axios from 'axios';

export default {
  data() {
    return {
      coaches: [],
    };
  },
  mounted() {
    axios.get('http://localhost:8087/until/coaches').then((response) => {
      this.coaches = response.data.data;
    });
  },
};
</script>
