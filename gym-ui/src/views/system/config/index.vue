<template>
  <div>
    <el-table :data="coaches" style="width: 100%">
      <el-table-column width="20"></el-table-column>
      <el-table-column prop="coaid" label="Coach ID"></el-table-column>
      <el-table-column prop="username" label="Username"></el-table-column>
      <el-table-column prop="firstName" label="First Name"></el-table-column>
      <el-table-column prop="lastName" label="Last Name"></el-table-column>
      <el-table-column prop="certifications" label="Certifications"></el-table-column>
      <el-table-column prop="salary" label="Salary"></el-table-column>
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
