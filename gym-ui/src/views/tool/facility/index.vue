<template>
  <div class="app-container">
    <div class="search-container">
      <strong>vid: </strong>
      <el-input
        v-model="queryParams.vid"
        placeholder="Please enter venue id"
        clearable
        size="small"
        style="width: 160px; margin: 10px"
        @keyup.enter.native="handleVid($event)"
      />
      <strong>vname: </strong><el-input
      v-model="queryParams.vname"
      placeholder="Please enter venue name"
      clearable
      size="small"
      style="width: 160px; margin: 10px"
      @keyup.enter.native="handleVname($event)"
    />
      <strong>fid: </strong><el-input
      v-model="queryParams.fid"
      placeholder="Please enter facility id"
      clearable
      size="small"
      style="width: 160px; margin: 10px"
      @keyup.enter.native="handleFid($event)"
    />
      <strong>Date: </strong>
      <el-date-picker
      v-model="queryParams.date"
      :picker-options="pickerOptions"
      type="date"
      @change="handleDate"
      style="margin: 10px;width:180px"
      placeholder="Reservation date">
      </el-date-picker>
    </div>
    <div class="button-container" style="margin-bottom: 10px">
      <el-button
        type="warning"
        plain
        icon="el-icon-download"
        size="mini"
        @click="printPDF"
      >Download</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">Reset</el-button>
    </div>
    <el-table :data="activeVenues">
      <el-table-column :width="30"></el-table-column>
      <el-table-column prop="fid" label="fid"></el-table-column>
      <el-table-column prop="vid" label="vid"></el-table-column>
      <el-table-column prop="vname" label="Venue Name"></el-table-column>
      <el-table-column prop="date" label="Date"></el-table-column>
      <el-table-column prop="price" label="Price"></el-table-column>
      <el-table-column label="Early">
        <template slot-scope="scope">
          <div v-for="(cap, index) in scope.row.capacity.slice(0, 4)" :key="index">
        <span>
          {{ ['09:00', '10:00', '11:00', '12:00'][index] }}
        </span>
            <el-checkbox v-model="scope.row.capacity[index]"></el-checkbox>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Late">
        <template slot-scope="scope">
          <div v-for="(cap, index) in scope.row.capacity.slice(4)" :key="index">
        <span>
          {{ ['15:00', '16:00', '17:00', '18:00', '19:00'][index] }}
        </span>
            <el-checkbox v-model="scope.row.capacity[index + 4]"></el-checkbox>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Actions">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">Edit</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="fid" prop="fid">
          <el-input v-model="form.fid" placeholder="Enter facility id" disabled></el-input>
        </el-form-item>
        <el-form-item label="Venue" prop="vname">
          <el-input v-model="form.vname" placeholder="Enter venue name" disabled></el-input>
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input v-model="form.price" placeholder="Enter price"></el-input>
        </el-form-item>
        <el-form-item label="Desc" prop="description">
          <el-input type="textarea" :rows="3" :style="{ height: '100px' }" v-model="form.description" placeholder="Enter description"></el-input>
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
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
export default {
  name: 'VenueTable',
  data() {
    return {
      venues: [],
      loading: true,
      activeVenues: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      pickerOptions: {
        disabledDate: (time) => {
          const today = new Date();
          const nextWeek = new Date();
          nextWeek.setDate(today.getDate() + 7); // 获取今天皁E��期并加丁E天
          return time.getTime() < today.getTime() || time.getTime() > nextWeek.getTime(); // 设置禁用日期范围
        }
      },
      rules: {
        price: [
          { required: true, message: 'Please enter price', trigger: 'blur' },
        ],
        description: [
          { required: true, message: 'Please enter description', trigger: 'blur' },
        ],
      },
      showSearch: true,
      queryParams: {
        vid:undefined,
        vname:undefined,
        fid:undefined,
        date:undefined,
      },
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      axios.get(`${GYM_API}/venue/getAvailableVenues`)
        .then((response) => {
          const venues = [];
          const activeVenues = [];
          for (const date in response.data.data) {
            if (Object.prototype.hasOwnProperty.call(response.data.data, date)) {
              response.data.data[date].forEach((item) => {
                const venue = { ...item.venue };
                venue.date = date;
                venue.capacity = item.cap.slice(); // 复制原始容量值
                venues.push(venue);
                activeVenues.push({
                  ...venue,
                  capacity: item.cap.map(cap => cap !== 0) // 封E��量值转换为币E��值
                });
              });
            }
          }
          this.venues = venues;
          this.activeVenues = activeVenues;
          console.log(this.venues);
          // console.log(this.activeVenues);
        })
        .catch((error) => {
          console.error(error);
        });
    },
    handleEdit(row) {
      this.dialogTitle = 'Edit Venue';
      this.form = { ...row };
      this.dialogVisible = true;
    },
    handleVid(event){
      this.loading = true;
      // console.log(this.queryParams.vid);
      const vidValue = this.queryParams.vid;
      axios.get(`${GYM_API}/venue/getById`, { params: { vid: vidValue } })
        .then((response) => {
          // console.log(response.data.data);
          const activeVenues = [];
          const today = new Date();
          today.setDate(today.getDate() + 1);
          const anotherday = new Date();
          for (const date in response.data.data) {

            if (Object.prototype.hasOwnProperty.call(response.data.data, date)) {
              const item = response.data.data[date];
              anotherday.setDate(today.getDate() + Number(date));
              const year = anotherday.getFullYear();
              const month = anotherday.getMonth() + 1;
              const day = anotherday.getDate();
              const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
              const activeVenue = { ...item.venue };
              activeVenue.date = formattedDate;
              console.log(date);
              console.log(formattedDate);
              console.log(activeVenue.date);
              activeVenue.capacity = item.cap.map(cap => cap !== 0);
              activeVenues.push(activeVenue);
            }
          }

          this.activeVenues = activeVenues;
          // console.log(this.activeVenues);
        })
        .catch((error) => {
          console.error(error);
        });
      this.loading=false;
      if(event){
        event.target.blur();
      }
    },
    handleFid(event){
      this.loading = true;
      console.log(this.queryParams.fid);
      const vidValue = this.queryParams.fid;
      axios.get(`${GYM_API}/venue/getFid`, { params: { fids: vidValue } })
        .then((response) => {
          const activeVenues = [];
          console.log(response.data.data);
          const today = new Date();
          today.setDate(today.getDate() + 1);
          const anotherday = new Date();
          for (const date in response.data.data) {
            // response.data.data[date].forEach((item) => {
            //   const activeVenue = { ...item.venue };
            //   activeVenue.date = date;
            //   activeVenue.capacity = item.cap.map(cap => cap !== 0);
            //   activeVenues.push(activeVenue);
            // });
            if (Object.prototype.hasOwnProperty.call(response.data.data, date)) {
              const item = response.data.data[date];
              anotherday.setDate(today.getDate() + Number(date));
              const year = anotherday.getFullYear();
              const month = anotherday.getMonth() + 1;
              const day = anotherday.getDate();
              const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
              const activeVenue = { ...item.venue };
              activeVenue.date = formattedDate;
              console.log(date);
              console.log(formattedDate);
              console.log(activeVenue.date);
              activeVenue.capacity = item.cap.map(cap => cap !== 0);
              activeVenues.push(activeVenue);
            }
          }
          this.activeVenues = activeVenues;
          // console.log(this.activeVenues);
        })
        .catch((error) => {
          console.error(error);
        });
      this.loading=false;
      if(event){
        event.target.blur();
      }
    },
    handleVname(event){
      this.loading = true;
      // console.log(this.queryParams.vid);
      const vnameValue = this.queryParams.vname;
      console.log(vnameValue);
      axios.get(`${GYM_API}/venue/getByName`, { params: { vid: vnameValue } })
        .then((response) => {
          const activeVenues = [];
          const today = new Date();
          today.setDate(today.getDate() + 1);
          const anotherday = new Date();
          for (const date in response.data.data) {
            // response.data.data[date].forEach((item) => {
            //   const activeVenue = { ...item.venue };
            //   activeVenue.date = date;
            //   activeVenue.capacity = item.cap.map(cap => cap !== 0);
            //   activeVenues.push(activeVenue);
            // });
            if (Object.prototype.hasOwnProperty.call(response.data.data, date)) {
              const item = response.data.data[date];
              anotherday.setDate(today.getDate() + Number(date));
              const year = anotherday.getFullYear();
              const month = anotherday.getMonth() + 1;
              const day = anotherday.getDate();
              const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
              const activeVenue = { ...item.venue };
              activeVenue.date = formattedDate;
              // console.log(date);
              // console.log(formattedDate);
              // console.log(activeVenue.date);
              activeVenue.capacity = item.cap.map(cap => cap !== 0);
              activeVenues.push(activeVenue);
            }
          }

          this.activeVenues = activeVenues;
          // console.log(this.activeVenues);
        })
        .catch((error) => {
          console.error(error);
        });
      this.loading=false;
      if(event){
        event.target.blur();
      }
    },
    handleDate(){
      // console.log(this.queryParams.date);
      const year = this.queryParams.date.getFullYear();
      const month = this.queryParams.date.getMonth() + 1;
      const day = this.queryParams.date.getDate();
      const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
      console.log(formattedDate); // 输�E�E�E023-05-09
      this.loading = true;
      // console.log(this.queryParams.vid);
      axios.get(`${GYM_API}/venue/getDate`, { params: { Date: formattedDate } })
        .then((response) => {
          const activeVenues = [];
          // const today = new Date();
          // today.setDate(today.getDate() + 1);
          // const anotherday = new Date();
          for (const date in response.data.data) {
            // response.data.data[date].forEach((item) => {
            //   const activeVenue = { ...item.venue };
            //   activeVenue.date = date;
            //   activeVenue.capacity = item.cap.map(cap => cap !== 0);
            //   activeVenues.push(activeVenue);
            // });
            if (Object.prototype.hasOwnProperty.call(response.data.data, date)) {
              console.log(date);
              const item = response.data.data[date];
              // anotherday.setDate(today.getDate() + Number(date));
              // const year = anotherday.getFullYear();
              // const month = anotherday.getMonth() + 1;
              // const day = anotherday.getDate();
              // const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
              const activeVenue = { ...item.venue };
              activeVenue.date = formattedDate;
              // console.log(date);
              // console.log(formattedDate);
              // console.log(activeVenue.date);
              activeVenue.capacity = item.cap.map(cap => cap !== 0);
              activeVenues.push(activeVenue);
            }
          }

          this.activeVenues = activeVenues;
          console.log(this.activeVenues);
        })
        .catch((error) => {
          console.error(error);
        });
      this.loading=false;
    },
    resetQuery(){
      // console.log(this.activeVenues);
      // console.log(this.venues.capacity);
      this.activeVenues = this.venues.map(venue => ({
        ...venue,
        capacity: venue.capacity.map(cap => cap !== 0)
      }));
      this.queryParams.vid = undefined;
      this.queryParams.vname = undefined;
      this.queryParams.date = undefined;
      this.queryParams.fid = undefined;
      // console.log(this.activeVenues);
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // Handle form submission
          // console.log(this.form);
          const index = this.venues.findIndex(v => v.vid === this.form.vid);
          // 更新该 venue 对象皁E��性值
          this.venues[index].price = this.form.price;
          this.venues[index].description = this.form.description;
          this.activeVenues[index].price = this.form.price;
          this.activeVenues[index].description = this.form.description;
          const editVenue = {
            vid: this.venues[index].vid,
            fid: this.venues[index].fid,
            vname: this.venues[index].vname,
            price: this.form.price,
            description: this.form.description,
            profile: this.venues[index].profile,
            status: this.venues[index].status,
            capacity: this.venues[index].capacity[0]
          };
          // 使用Axios封E��地对象发送到后端服务器
          axios.put(`${GYM_API}/venue/edit`, editVenue)
            .then(response => {
              console.log(response.data);
            })
            .catch(error => {
              console.error(error);
            });
          this.dialogVisible = false;
        } else {
          return false;
        }
      });
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    printPDF() {
      const table = document.querySelector('.el-table__body-wrapper');
      html2canvas(table).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF('p', 'mm', 'a4');
        const width = pdf.internal.pageSize.getWidth();
        const height = (canvas.height * width) / canvas.width;
        pdf.addImage(imgData, 'PNG', 0, 0, width, height);
        pdf.save('venues.pdf');
      });
    },
  },
};
</script>
