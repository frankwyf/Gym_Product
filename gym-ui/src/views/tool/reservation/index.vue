<template>
  <div>
    <div class="search-container" style="margin-left:20px">
      <strong>rid: </strong>
      <el-input
        v-model="queryParams.rid"
        placeholder="Please enter reservation id"
        clearable
        size="small"
        style="width: 160px; margin: 10px"
        @keyup.enter.native="handleRid($event)"
      />
      <strong>vid: </strong>
      <el-input
        v-model="queryParams.vid"
        placeholder="Please enter venue id"
        clearable
        size="small"
        style="width: 160px; margin: 10px"
        @keyup.enter.native="handleVid($event)"
      />
    </div>
    <div class="button-container" style="margin-bottom: 10px;margin-left: 10px">
      <el-button @click="editTable" style="width: 60px;margin:10px" size="small">{{ validTable ? 'valid' : 'unpaid' }}</el-button>
      <el-button @click="addReservation" size="small">add</el-button>
      <el-button icon="el-icon-refresh" size="small" @click="resetQuery" style="margin-left: 20px">Reset</el-button>
      <el-button @click="showQR" size="small" style="margin-left: 20px">QRcode</el-button>
    </div>
    <el-table :data="reservations">
      <el-table-column :width="30"></el-table-column>
      <el-table-column prop="rid" label="rid"></el-table-column>
      <el-table-column prop="rdate" label="Date"></el-table-column>
      <el-table-column prop="facility" label="Facility"></el-table-column>
      <el-table-column prop="venue" label="Venue"></el-table-column>
      <el-table-column prop="amount" label="Amount"></el-table-column>
      <el-table-column :label="'Time'">
        <template slot-scope="scope">{{ getPeriodTimes(scope.row.period) }}</template>
      </el-table-column>
      <el-table-column prop="payment" label="Payment"></el-table-column>
      <el-table-column prop="status" label="Status"></el-table-column>
      <el-table-column prop="ruid" label="User ID"></el-table-column>
      <el-table-column label="Actions">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">Edit</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="dialogTitle" :visible.sync="dialogQRVisible" width="500px" append-to-body>
      <StreamBarcodeReader id="barcodeReader" @decode="onDecode"></StreamBarcodeReader>
      <h2>The decoded value in QR/barcode is</h2>
      <h2>{{ decodedText }}</h2>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeQRDialog">Close</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="Rid" prop="rid">
          <el-input v-model="form.rid" placeholder="Enter reservation id" disabled></el-input>
        </el-form-item>
        <el-form-item label="Date" prop="rdate">
          <el-date-picker v-model="form.rdate" type="date" placeholder="Select reservation date"
                          :picker-options="{ disabledDate: time => time.getTime() < Date.now() || time.getTime() > (Date.now() + 7 * 24 * 60 * 60 * 1000) }"
                          @change="handleDateChange"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Facility" prop="facility">
          <el-input v-model="form.facility" placeholder="Enter facility"></el-input>
        </el-form-item>
        <el-form-item label="Venue" prop="venue">
          <el-input v-model="form.venue" placeholder="Enter venue"></el-input>
        </el-form-item>
        <el-form-item label="Amount" prop="amount">
          <el-input v-model="form.amount" placeholder="Enter amount"></el-input>
        </el-form-item>
        <el-form-item label="Payment" prop="payment">
          <el-select v-model="form.payment" placeholder="Select payment">
            <el-option label="Cash" value="cash"></el-option>
            <el-option label="Card" value="card"></el-option>
            <el-option label="Other" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="form.status" placeholder="Select status">
            <el-option label="Valid" value="valid"></el-option>
            <el-option label="Unpaid" value="unpaid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="User id" prop="ruid">
          <el-input v-model="form.ruid" placeholder="Enter user id"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">Save</el-button>
        <el-button @click="closeDialog">Cancel</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="dialogTitle" :visible.sync="addDialogVisible" width="500px" append-to-body>
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="90px">
        <el-form-item label="Date" prop="rdate">
          <el-date-picker v-model="addForm.rdate" type="date" placeholder="Select reservation date"
                          :picker-options="{ disabledDate: time => time.getTime() < Date.now() || time.getTime() > (Date.now() + 7 * 24 * 60 * 60 * 1000) }"
                          @change="handleDateChange"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Facility" prop="facility">
          <el-input v-model.number="addForm.facility" placeholder="Enter facility"></el-input>
        </el-form-item>
        <el-form-item label="Venue" prop="venue">
          <el-input v-model.number="addForm.venue" placeholder="Enter venue"></el-input>
        </el-form-item>
        <el-form-item label="Amount" prop="amount">
          <el-input v-model.number="addForm.amount" placeholder="Enter amount"></el-input>
        </el-form-item>
        <el-form-item label="Period" prop="period">
          <el-checkbox-group v-model="addForm.period">
            <el-checkbox v-for="p in periods" :key="p" :label="p">{{ p }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="Payment" prop="payment">
          <el-select v-model="addForm.payment" placeholder="Select payment">
            <el-option label="Cash" value="cash"></el-option>
            <el-option label="Card" value="card"></el-option>
            <el-option label="Other" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="addForm.status" placeholder="Select status">
            <el-option label="Valid" value="valid"></el-option>
            <el-option label="Unpaid" value="unpaid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="User id" prop="ruid">
          <el-input v-model.number="addForm.ruid" placeholder="Enter user id"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddForm">Save</el-button>
        <el-button @click="closeAddDialog">Cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
const GYM_API = process.env.VUE_APP_GYMMASTER_API || ''
import { StreamBarcodeReader } from "vue-barcode-reader";
export default {
  name: "ReservationDeal",
  components: {
    StreamBarcodeReader
  },
  data() {
    return {
      decodedText: "",
      dialogQRVisible:false,
      reservations: [],
      queryParams: {
        rid:undefined,
        vid:undefined,
      },
      validReservations: [],
      invalidReservations: [],
      fullReservations: [],
      dialogVisible: false,
      addDialogVisible: false,
      dialogTitle:'',
      form:{},
      addForm:{period:["9:00","10:00"]},
      periods:["9:00","10:00","11:00","15:00","16:00","17:00","18:00","19:00"],
      validTable: true,
      rules:{
        rid: [
          { required: true, message: 'Please enter reservation id', trigger: 'blur' },
        ],
        rdate: [
          { required: true, message: 'Please enter reservation date', trigger: 'blur' },
        ],
        facility: [
          { required: true, message: 'Please enter facility', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        period: [{required:true,message:'Please at least choose one', trigger: 'change'}],
        venue: [
          { required: true, message: 'Please enter venue', trigger: 'blur' },
          { type: 'number', message: 'Venue must be a number', trigger: 'blur' },
        ],
        amount: [
          { required: true, message: 'Please enter amount', trigger: 'blur' },
          { type: 'number', message: 'Amount must be a number', trigger: 'blur' },
        ],
        payment: [
          { required: true, message: 'Please enter payment', trigger: 'blur' },
        ],
        status: [
          { required: true, message: 'Please enter status', trigger: 'blur' },
        ],
        ruid: [
          { required: true, message: 'Please enter user id', trigger: 'blur' },
          { type: 'number', message: 'User id must be a number', trigger: 'blur' },
        ],
      },
      addRules:{
        rdate: [
          { required: true, message: 'Please enter reservation date', trigger: 'blur' },
        ],
        facility: [
          { required: true, message: 'Please enter facility', trigger: 'blur' },
          { type: 'number', message: 'Facility must be a number', trigger: 'blur' },
        ],
        venue: [
          { required: true, message: 'Please enter venue', trigger: 'blur' },
          { type: 'number', message: 'Venue must be a number', trigger: 'blur' },
        ],
        amount: [
          { required: true, message: 'Please enter amount', trigger: 'blur' },
          { type: 'number', message: 'Amount must be a number', trigger: 'blur' },
        ],
        payment: [
          { required: true, message: 'Please enter payment', trigger: 'blur' },
        ],
        status: [
          { required: true, message: 'Please enter status', trigger: 'blur' },
        ],
        ruid: [
          { required: true, message: 'Please enter user id', trigger: 'blur' },
          { type: 'number', message: 'User id must be a number', trigger: 'blur' },
        ],
      },
    };
  },
  methods: {
    trail() {
      console.log(this.addForm.period);
    },
    getPeriodTimes(periodStr) {
      const periodArr = periodStr.split(',').map(Number);
      const periodTimes = ['9:00', '10:00', '11:00', '15:00', '16:00', '17:00', '18:00', '19:00'];
      return periodArr.map(period => periodTimes[period - 1]).join(', ');
    },
    handleEdit(row) {
      this.dialogTitle = 'Edit Reservation';
      this.form = { ...row };
      this.dialogVisible = true;
    },
    handleDelete(row) {
      this.dialogTitle = 'Edit Reservation';
      this.form = { ...row };
      this.form.status = "invalid";
      const index = this.reservations.findIndex(r => r.rid === this.form.rid);
      this.reservations[index].status = this.form.status;
      const editReservation = {
        rdate: this.reservations[index].rdate,
        facility: this.reservations[index].facility,
        venue: this.reservations[index].venue,
        amount: this.reservations[index].amount,
        payment: this.reservations[index].payment,
        status: this.reservations[index].status,
        ruid: this.reservations[index].ruid,
        period: this.reservations[index].period,
        rid: this.reservations[index].rid,
      };
      axios.put(`${GYM_API}/reservation/update`,editReservation)
        .then(response => {
          console.log(response.data);
        })
        .catch(error => {
          console.error(error);
        })
      this.fetchValidData();
      this.fetchInvalidData();
      this.fetchFullData();
      this.dataChange();
    },
    onDecode(text) {
      this.decodedText = text;
      this.$refs.barcodeReader.stop();
    },
    showQR(){
      this.dialogTitle = "scanning the QR code";
      this.dialogQRVisible=!this.dialogQRVisible;
    },
    closeQRDialog(){
      this.dialogQRVisible = false;
      this.decodedText = '';
    },
    submitAddForm(){
      this.$refs.addForm.validate(valid => {
        if (valid) {
          const selectedPeriodIndices = this.addForm.period.map(period => this.periods.indexOf(period)+1);
          const selectedPeriodsString = selectedPeriodIndices.join(',');
          // console.log(selectedPeriodsString+'this');
          const year = this.addForm.rdate.getFullYear();
          const month = ("0" + (this.addForm.rdate.getMonth() + 1)).slice(-2);
          const day = ("0" + this.addForm.rdate.getDate()).slice(-2);
          const formattedDate = `${year}-${month}-${day}`;
          const editAddReservation = {
            Ruid: this.addForm.ruid,
            rdate: formattedDate,
            facility: this.addForm.facility,
            venue: this.addForm.venue,
            amount: this.addForm.amount,
            period: selectedPeriodsString,
            payment: this.addForm.payment,
            status: this.addForm.status,
          };
          console.log(editAddReservation);
          axios.post(`${GYM_API}/reservation/add/management`,editAddReservation)
            .then(response => {
              console.log(response.data);
            })
            .catch(error => {
              console.error(error);
            })
          this.fetchValidData();
          this.fetchInvalidData();
          this.fetchFullData();
          this.dataChange();
          console.log(this.reservations);
          this.closeAddDialog();
        } else {
          return false;
        }
      })
    },
    handleRid(event){
      this.loading = true;
      // console.log(this.queryParams.vid);
      const ridValue = this.queryParams.rid;
      axios.get(`${GYM_API}/reservation/findId`, { params: { id: ridValue } })
        .then((response) => {
          this.reservations = [];
          this.reservations.push(response.data.data);
        })
        .catch((error) => {
          console.error(error);
        });
      if(event){
        event.target.blur();
      }
    },
    handleVid(event){
      this.loading = true;
      const vidValue = this.queryParams.vid;
      axios.get(`${GYM_API}/reservation/findVid`, { params: { id: vidValue } })
        .then((response) => {
          console.log(response.data.data);
          // this.reservations = [];
          // this.reservations.push(response.data.data);
          this.reservations = response.data.data;
        })
        .catch((error) => {
          console.error(error);
        });
      if(event){
        event.target.blur();
      }
    },
    // handleVname(event){
    //   this.loading = true;
    //   const vnameValue = this.queryParams.vname;
    //   axios.get(`${GYM_API}/reservation/findvname`, { params: { name1: vnameValue } })
    //     .then((response) => {
    //       this.reservations = [];
    //       this.reservations.push(response.data.data);
    //     })
    //     .catch((error) => {
    //       console.error(error);
    //     });
    //   if(event){
    //     event.target.blur();
    //   }
    // },
    submitForm(){
      this.$refs.form.validate(valid => {
        if (valid) {
          const index = this.reservations.findIndex(r => r.rid === this.form.rid);
          this.reservations[index].rdate = this.form.rdate;
          this.reservations[index].facility = this.form.facility;
          this.reservations[index].venue = this.form.venue;
          this.reservations[index].amount = this.form.amount;
          this.reservations[index].payment = this.form.payment;
          this.reservations[index].status = this.form.status;
          this.reservations[index].ruid = this.form.ruid;
          const editReservation = {
            rdate: this.reservations[index].rdate,
            facility: this.reservations[index].facility,
            venue: this.reservations[index].venue,
            amount: this.reservations[index].amount,
            payment: this.reservations[index].payment,
            status: this.reservations[index].status,
            ruid: this.reservations[index].ruid,
            period: this.reservations[index].period,
            rid: this.reservations[index].rid,
          };
          axios.put(`${GYM_API}/reservation/update`,editReservation)
            .then(response => {
              console.log(response.data);
            })
            .catch(error => {
              console.error(error);
            })
          this.fetchValidData();
          this.fetchInvalidData();
          this.fetchFullData();
          this.dataChange();
          console.log(this.reservations);
          this.closeDialog();
        } else {
          return false;
        }
      })
    },
    dataChange(){
      if (this.validTable) {
        this.reservations = this.validReservations;
      } else {
        this.reservations = this.invalidReservations;
      }
      console.log(1);
    },
    addReservation() {
      this.dialogTitle = 'Add Reservation';
      this.form = {};
      this.addDialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    closeAddDialog(){
      this.addDialogVisible = false;
    },
    fetchValidData() {
      axios.get(`${GYM_API}/reservation/page?page=1&pageSize=10`)
        .then(response => {
          this.validReservations = response.data.data.records.filter(record => record.status === 'valid');
          // console.log(this.validReservations);
          this.dataChange();
        })
        .catch(error => {
          console.log(error);
        });
    },
    fetchInvalidData() {
      axios.get(`${GYM_API}/reservation/page?page=1&pageSize=10`)
        .then(response => {
          this.invalidReservations = response.data.data.records.filter(record => record.status === 'unpaid');
          // console.log(this.invalidReservations);
        })
        .catch(error => {
          console.log(error);
        });
    },
    fetchFullData() {
      axios.get(`${GYM_API}/reservation/page?page=1&pageSize=10`)
        .then(response => {
          this.fullReservations = response.data.data.records;
          // console.log(this.fullReservations);
        })
        .catch(error => {
          console.log(error);
        });
    },
    handleDateChange() {
      // 获取当前选择皁E��朁E
      const selectedDate = new Date(this.form.rdate);

      // 封E��期对象转换为“yyyy-MM-dd”格式的字符串
      const formattedDate = selectedDate.getFullYear() + '-' +
        ('0' + (selectedDate.getMonth() + 1)).slice(-2) + '-' +
        ('0' + selectedDate.getDate()).slice(-2);

      // 封E��式化后的日期设置回表单对象
      this.form.rdate = formattedDate;
    },
    editTable(){
      this.validTable = !this.validTable;
      if (this.validTable) {
        this.reservations = this.validReservations;
      } else {
        this.reservations = this.invalidReservations;
      }
    },
    resetQuery(){
      this.queryParams.vid = undefined;
      this.queryParams.rid = undefined;
      if (this.validTable) {
        this.reservations = this.validReservations;
      } else {
        this.reservations = this.invalidReservations;
      }
    },
  },
  mounted() {
    this.fetchInvalidData();
    this.fetchFullData();
    this.fetchValidData();
    console.log(this.validReservations);
  },
}
</script>

<style scoped>

</style>
