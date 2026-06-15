<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>{{ $tr('dashboard.title') }}</h2>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <div
          id="t1"
          class="text item"
          :style="{ width: '600px', height: '500px' }"
        />
      </el-col>
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <div
          id="t2"
          class="text item"
          :style="{ width: '600px', height: '500px' }"
        />
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>

<script>
import echarts from "echarts";
import { getStudentNumber } from "@/api/gym/studentAssignment";
import { getCharts } from "@/api/operation/commodity";
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.0.0",
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    initCharts() {
      const legendKeys = [
        'dashboard.legend.basketball',
        'dashboard.legend.football',
        'dashboard.legend.volleyball',
        'dashboard.legend.tennis',
        'dashboard.legend.badminton'
      ];
      const legendLabels = legendKeys.map((key) => this.$tr(key));
      const weekdayLabels = [
        this.$tr('dashboard.weekday.mon'),
        this.$tr('dashboard.weekday.tue'),
        this.$tr('dashboard.weekday.wed'),
        this.$tr('dashboard.weekday.thu'),
        this.$tr('dashboard.weekday.fri'),
        this.$tr('dashboard.weekday.sat'),
        this.$tr('dashboard.weekday.sun')
      ];

      getCharts().then((response) => {
        console.log(response.c);
        // console.log(response.d);
        var myChart = echarts.init(document.getElementById("t2"));
        // 绘制图表
        myChart.setOption({
          title: {
            text: this.$tr('dashboard.lessonAnalysis'),
          },
          // legend: {
          //   data: response.c,
          //   top:"5%"
          // },
          xAxis: {
            type: "category",
            data: response.c,
          },
          yAxis: {
            type: "value",
          },
          series: [
            {
              data: response.d,
              type: "bar",
              showBackground: true,
              backgroundStyle: {
                color: "rgba(180, 180, 180, 0.2)",
              },
            },
          ],
        });
      });
      getStudentNumber().then((response) => {
        // console.log(response.a);
        // console.log(response.b);
        var myChart = echarts.init(document.getElementById("t1"));
        // 绘制图表
        myChart.setOption({
          title: {
            text: this.$tr('dashboard.studentAnalysis'),
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
              label: {
                backgroundColor: "#6a7985",
              },
            },
          },
          legend: {
            data: legendLabels,
            top:"5%",
          },
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true,
          },
          xAxis: [
            {
              type: "category",
              boundaryGap: false,
              data: weekdayLabels,
            },
          ],
          yAxis: [
            {
              type: "value",
            },
          ],
          series: [
            {
              name: legendLabels[0],
              type: "line",
              stack: "Total",
              areaStyle: {},
              emphasis: {
                focus: "series",
              },
              data: [12, 13, 10, 13, 9, 23, 21],
            },
            {
              name: legendLabels[1],
              type: "line",
              stack: "Total",
              areaStyle: {},
              emphasis: {
                focus: "series",
              },
              data: [22, 18, 19, 23, 29, 33, 31],
            },
            {
              name: legendLabels[2],
              type: "line",
              stack: "Total",
              areaStyle: {},
              emphasis: {
                focus: "series",
              },
              data: [15, 23, 20, 15, 19, 33, 41],
            },
            {
              name: legendLabels[3],
              type: "line",
              stack: "Total",
              areaStyle: {},
              emphasis: {
                focus: "series",
              },
              data: [32, 33, 30, 33, 39, 33, 32],
            },
            {
              name: legendLabels[4],
              type: "line",
              stack: "Total",
              label: {
                show: true,
                position: "top",
              },
              areaStyle: {},
              emphasis: {
                focus: "series",
              },
              data: [82, 93, 90, 93, 129, 133, 132],
            },
          ],
        });
      });
    },
  },
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
