<template>
  <div>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <div id="t1"  class="text item" :style="{width: '600px', height: '300px'}" />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getStudentNumber } from '@/api/gym/studentAssignment'
import echarts from 'echarts'

export default {
  mounted() {
    this.initCharts()
  },
  methods: {
    initCharts() {
      getStudentNumber().then(response => {
        console.log(response.a);
        console.log(response.b);
        var myChart = echarts.init(document.getElementById("t1"))
        // 绘制图表
        myChart.setOption({
          title: {
            text: this.$tr('operation.studentAnalysis'),
          },
          xAxis: {
            type: 'category',
            data: response.a
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: response.b,
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
              }
            }
          ]
        })
      });
    },
  }
}
</script>
