<template>
  <div>
    <div class="search-container" style="margin-left: 20px">
      <strong>coach: </strong><el-input
      v-model="queryParams.coachId"
      placeholder="coach id"
      clearable
      size="small"
      style="width: 160px; margin: 10px"
      @keyup.enter.native="handleCoachId($event)"
    />
      <strong>type: </strong><el-input
      v-model="queryParams.courseType"
      placeholder="course type"
      clearable
      size="small"
      style="width: 160px; margin: 10px"
      @keyup.enter.native="handleCourseType($event)"
    />
      <strong>venue: </strong><el-input
      v-model="queryParams.courseVenue"
      placeholder="course venue"
      clearable
      size="small"
      style="width: 160px; margin: 10px"
      @keyup.enter.native="handleCourseVenue($event)"
    />
      <strong>price: </strong>
      <el-input-number v-model="queryParams.minPrice" placeholder="min" size="small" style="width: 100px" @change="handleCoursePrice()"></el-input-number>
      <el-input-number v-model="queryParams.maxPrice" placeholder="max" size="small" style="width: 100px; margin-left: 10px" @change="handleCoursePrice()"></el-input-number>
    </div>
    <div class="button-container" style="margin-bottom: 10px;margin-left: 20px">
      <el-button icon="el-icon-plus" size="mini" type="primary" @click="handleAdd">add</el-button>
      <el-button icon="el-icon-refresh" size="mini" type="warning" @click="resetQuery">Reset</el-button>
      <el-button icon="el-icon-s-data" size="mini" type="success" @click="showFigure">Visual</el-button>
      <el-button
        type="warning"
        plain
        icon="el-icon-download"
        size="mini"
        @click="printPDF"
      >Download</el-button>
    </div>
    <el-table :data="activeCourses">
      <el-table-column width="20"></el-table-column>
      <el-table-column label="Course ID" prop="couid"></el-table-column>
      <el-table-column label="Coach ID" prop="coaid"></el-table-column>
      <el-table-column label="Price" prop="price"></el-table-column>
      <el-table-column label="Type" prop="type"></el-table-column>
      <el-table-column label="Course Facility" prop="courseFacility"></el-table-column>
      <el-table-column label="Time" prop="time">
        <template slot-scope="scope">{{ formatDate(scope.row.time) }}</template>
      </el-table-column>
      <el-table-column label="Capability" prop="capability"></el-table-column>
      <el-table-column label="Course Venue" prop="courseVenue"></el-table-column>
      <el-table-column label="Actions">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">Edit</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="dialogTitle" :visible.sync="dialogChartVisible" width="800px" append-to-body>
      <div id="chart" style="height: 300px;"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeChartDialog">Close</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="dialogTitle" :visible.sync="dialogEditVisible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="Course" prop="course">
          <el-input v-model="form.couid" placeholder="Enter course id" disabled></el-input>
        </el-form-item>
        <el-form-item label="Coach" prop="coaid">
          <el-input v-model="form.coaid" placeholder="Enter coach id" disabled></el-input>
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input v-model="form.price" placeholder="Enter price"></el-input>
        </el-form-item>
        <el-form-item label="Type" prop="type">
          <el-input v-model="form.type" placeholder="Enter type"></el-input>
        </el-form-item>
        <el-form-item label="Capa" prop="capability">
          <el-input v-model="form.capability" placeholder="Enter capability"></el-input>
        </el-form-item>
        <el-form-item label="Desc" prop="description">
          <el-input type="textarea" :rows="3" :style="{ height: '100px' }" v-model="form.description" placeholder="Enter description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm">Save</el-button>
        <el-button @click="closeEditDialog">Cancel</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="80px">
        <el-form-item label="Coach" prop="coaid">
          <el-input v-model="addForm.coaid" placeholder="Enter coach id"></el-input>
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input v-model="addForm.price" placeholder="Enter price"></el-input>
        </el-form-item>
        <el-form-item label="Type" prop="type">
          <el-input v-model="addForm.type" placeholder="Enter type"></el-input>
        </el-form-item>
        <el-form-item label="Facility" prop="courseFacility">
          <el-input v-model="addForm.courseFacility" placeholder="Enter facility"></el-input>
        </el-form-item>
        <el-form-item label="Venue" prop="courseVenue">
          <el-input v-model="addForm.courseVenue" placeholder="Enter venue"></el-input>
        </el-form-item>
        <el-form-item label="Cover" prop="cover">
          <el-input v-model="addForm.cover" placeholder="Enter cover"></el-input>
        </el-form-item>
        <el-form-item label="Time" prop="time">
<!--          <el-date-picker-->
<!--            v-model="addForm.time"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-ddTHH:mm:ss.SSSZ"-->
<!--            placeholder="Select time"-->
<!--            :picker-options="{ start: '09:00', step: '00:30', end: '18:00' }"-->
<!--            @change="handleDateChange"-->
<!--          ></el-date-picker>-->
          <el-input v-model="addForm.time" placeholder="Enter time"></el-input>
        </el-form-item>
        <el-form-item label="Capa" prop="capability">
          <el-input v-model="addForm.capability" placeholder="Enter capability"></el-input>
        </el-form-item>
        <el-form-item label="Desc" prop="description">
          <el-input type="textarea" :rows="3" :style="{ height: '100px' }" v-model="addForm.description" placeholder="Enter description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">Save</el-button>
        <el-button @click="closeDialog">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
const GYM_API = process.env.VUE_APP_GYMMASTER_API || ''
import * as echarts from 'echarts';
import html2canvas from 'html2canvas'
import jsPDF from 'jspdf'

export default {
  data() {
    return {
      courses: [],
      activeCourses: [],
      dialogChartVisible:false,
      dialogTitle:'',
      queryParams: {
        courseVenue:undefined,
        courseType:undefined,
        coachId:undefined,
        minPrice:0,
        maxPrice:99,
      },
      rules: {
        price: [
          { required: true, message: 'Please enter price', trigger: 'blur' },
        ],
        type: [
          { required: true, message: 'Please enter type', trigger: 'blur' },
        ],
        capability: [
          { required: true, message: 'Please enter capability', trigger: 'blur' },
        ],
        description: [
          { required: true, message: 'Please enter description', trigger: 'blur' },
        ],
      },
      addRules: {
        coaid: [
          { required: true, message: 'Please enter price', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        price: [
          { required: true, message: 'Please enter price', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        type: [
          { required: true, message: 'Please enter type', trigger: 'blur' },
        ],
        capability: [
          { required: true, message: 'Please enter capability', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        description: [
          { required: true, message: 'Please enter description', trigger: 'blur' },
        ],
        courseFacility: [
          { required: true, message: 'Please enter courseFacility', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        courseVenue: [
          { required: true, message: 'Please enter courseVenue', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        cover: [
          { required: true, message: 'Please enter cover', trigger: 'blur' },
        ],
        time: [
          { required: true, message: 'Please enter time', trigger: 'blur' },
        ],
      },
      form:[],
      addForm:{
        capability:10,
        couid: 8,
        coaid:5,
        courseFacility: 6,
        courseVenue: 1,
        cover:"1.jpg",
        description:"default",
        price:30,
        time:"2023-04-26T07:00:00.000+00:00",
        type:"tennis",
      },
      // addForm:{},
      dialogEditVisible:false,
      dialogVisible:false,
      unwantedCourse:{},
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    handleEdit(row) {
      this.dialogTitle = 'Edit Course';
      this.form = { ...row };
      this.dialogEditVisible = true;
      console.log(this.form);
    },
    fetchData() {
      axios.get(`${GYM_API}/until/allCourses`)
        .then((response) => {
          console.log(response.data.data);
          this.courses = response.data.data;
          this.activeCourses = this.courses;
          // console.log(typeof (this.courses[0].time));
        })
        .catch((error) => {
          console.error(error);
        });
    },
    handleCoachId(event){
      this.activeCourses = [];
      axios.get(`${GYM_API}/until/Course/coaid`, { params: { coAid: this.queryParams.coachId } })
        .then((response) => {
          console.log(response.data.data);
          this.activeCourses=response.data.data;
        })
        .catch((error) => {
          console.error(error);
        });
      if(event){
        event.target.blur();
      }
    },
    handleCourseType(event){
      this.activeCourses = [];
      axios.get(`${GYM_API}/until/Course/Type`, { params: { courseType: this.queryParams.courseType } })
        .then((response) => {
          console.log(response.data.data);
          this.activeCourses=response.data.data;
        })
        .catch((error) => {
          console.error(error);
        });
      if(event){
        event.target.blur();
      }
    },
    handleDelete(row){
      this.unwantedCourse = { ...row };
      axios.delete(`${GYM_API}/until/Course/Delete/${this.unwantedCourse.couid}`)
        .then(response => {
          console.log(this.courses);
          this.courses = this.courses.filter(course => course.couid !=this.unwantedCourse.couid);
          location.reload();
        })
        .catch(error => {
          console.error(error);
        });
    },
    handleCourseVenue(event){
      this.activeCourses = [];
      axios.get(`${GYM_API}/until/Course/VenueCourse`, { params: { courseVenue: this.queryParams.courseVenue } })
        .then((response) => {
          console.log(response.data.data);
          this.activeCourses=response.data.data;
        })
        .catch((error) => {
          console.error(error);
        });
      if(event){
        event.target.blur();
      }
    },
    handleCoursePrice(){
      this.activeCourses = [];
      axios.get(`${GYM_API}/until/Course/Price`, { params: { max: this.queryParams.maxPrice,min: this.queryParams.minPrice } })
        .then((response) => {
          console.log(response.data.data);
          this.activeCourses=response.data.data;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    resetQuery() {
      this.activeCourses = this.courses;
      this.queryParams.minPrice = 0;
      this.queryParams.maxPrice = 99;
      this.queryParams.coachId = undefined;
      this.queryParams.courseType = undefined;
      this.queryParams.courseVenue = undefined;
    },
    printPDF() {
      const table = document.querySelector('.el-table__body-wrapper');
      html2canvas(table).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF('p', 'mm', 'a4');
        const width = pdf.internal.pageSize.getWidth();
        const height = (canvas.height * width) / canvas.width;
        pdf.addImage(imgData, 'PNG', 0, 0, width, height);
        pdf.save('coaches.pdf');
      });
    },
    renderChart() {
      // Calculate the count of each course type
      const typeCounts = {};
      console.log(this.courses);
      this.courses.forEach(course => {
        if (typeCounts[course.type]) {
          typeCounts[course.type]++;
        } else {
          typeCounts[course.type] = 1;
        }
      });

      // Create a data series for the chart
      const data = [];
      Object.keys(typeCounts).forEach(type => {
        data.push({
          name: type,
          value: typeCounts[type],
        });
      });

      // Define the chart options
      const options = {
        theme:'dark',
        animation: true,
        animationDuration: 1000,
        animationEasing: 'cubicInOut',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        xAxis: {
          type: 'category',
          data: Object.keys(typeCounts),
          axisLabel: {
            fontSize: 12,
          },
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data,
            type: 'bar',
            // 设置柱状图颜色
            itemStyle: {
              color: '#0077be',
            },
            // 设置柱状图边桁E��弁E
            emphasis: {
              itemStyle: {
                borderColor: '#0077be',
                borderWidth: 2,
              },
            },
          },
        ],
      };

      // Set the chart options and render the chart
      this.chart.setOption(options);
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    showFigure(){
      console.log(1);
      this.dialogChartVisible=true;
      this.dialogTitle='course data visualization';
      if (this.chart) {
        this.chart.dispose();
      }
      this.$nextTick(() =>{
        // Create a new instance of the ECharts object
        this.chart = echarts.init(document.getElementById('chart'));
        // Call the method to render the chart
        this.renderChart();
      })

    },
    closeChartDialog(){
      this.dialogChartVisible=false;
      if (this.chart) {
        this.chart.dispose();
      }
    },
    handleAdd(){
      // this.addForm = this.courses[0];
      this.dialogTitle = 'Add Course';
      this.dialogVisible = true;
    },
    closeEditDialog() {
      this.dialogEditVisible = false;
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    submitEditForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // Handle form submission
          // console.log(this.form);
          const index = this.courses.findIndex(c => c.couid === this.form.couid);
          // 更新该 venue 对象皁E��性值
          this.courses[index].price = this.form.price;
          this.courses[index].description = this.form.description;
          this.courses[index].type = this.form.type;
          this.courses[index].capability = this.form.capability;
          const editCourse = this.courses[index];
          // 使用Axios封E��地对象发送到后端服务器
          axios.put(`${GYM_API}/until/updateCourse`, editCourse)
            .then(response => {
              console.log(response.data);
            })
            .catch(error => {
              console.error(error);
            });
          this.dialogEditVisible = false;
        } else {
          return false;
        }
      });
    },
    // handleDateChange(date) {
    //   if (date instanceof Date) {
    //     // 如果传入皁E��日期对象�E�封E�E转换为字符串
    //     this.addForm.time = date.toISOString()
    //   } else {
    //     // 否则，直接赋值
    //     this.addForm.time = date
    //   }
    // },
    submitForm() {
      this.$refs.addForm.validate(valid => {
        if (valid) {
          // Handle form submission
          // 使用Axios封E��地对象发送到后端服务器
          // axios.get(${GYM_API}/until/addCourse', this.addForm, {
          //   headers: {
          //     'Content-Type': 'application/json'
          //   }
          // } )
          //   .then(response => {
          //     console.log(response.data);
          //   })
          //   .catch(error => {
          //     console.error(error);
          //   });
          console.log(this.addForm);
          const addCourse = {
            capability: this.addForm.capability,
            coaid: this.addForm.coaid,
            couid: this.addForm.couid,
            courseFacility: this.addForm.courseFacility,
            courseVenue: this.addForm.courseVenue,
            cover: this.addForm.cover,
            desciption: this.addForm.description,
            price: this.addForm.price,
            time: this.addForm.time,
            type: this.addForm.type,
          }
          this.courses.push(addCourse);
          this.dialogVisible = false;
          this.addForm.couid = this.addForm.couid + 1;
        } else {
          console.log("this is not valid");
          return false;
        }
      });
    },
  },
};
</script>
