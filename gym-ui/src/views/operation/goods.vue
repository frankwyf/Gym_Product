<template>
  <div>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <div id="t2"  class="text item" :style="{width: '600px', height: '300px'}" />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {getCharts} from '@/api/operation/commodity'
import echarts from 'echarts'

export default {
  mounted() {
    this.initCharts()
  },
  methods: {
    initCharts() {
      getCharts().then(response => {
        var myChart = echarts.init(document.getElementById("t2"))
        // 绘制图表
        myChart.setOption({
          title: {
            text: "商品统计",
          },
          xAxis: {
            type: 'category',
            data: response.c
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: response.d,
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
              }
            }
          ]
        })
      })
    },
  }
}
</script>
