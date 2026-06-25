<template lang="pug">
el-form-item.price-parent(:label="label" :prop="prop" )
  el-input(:style="`width: ${width}px`" ref="input" v-model="form.commodityPrice" :placeholder="placeholder" @blur="blurInput" @focus="focusInput")
  .price-mask(:style="`width: ${width -30}px`" v-show="showFormatPrice" @click="focusInput") {{formaterPrice}}
</template>

<script>
export default {
  name: 'PriceInput',
  props: {
    label: {
      type: String,
      default: '',
    },
    prop: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: '请输入',
    },
    width: {
      type: Number,
      default: 140,
    },
    form: {
      type: Object,
      default: () => ({
        commodityPrice: '',
      }),
    },
    rules: {
      type: Object,
      default: () => { },
    },
  },
  data() {
    return {
      showFormatPrice: false, // 是否显示遮罩
    }
  },
  computed: {
    formaterPrice() {
      if (
        this.form.commodityPrice !== '' &&
        this.form.commodityPrice !== null
      ) {
        // 去掉前面的0
        const integer = this.form.commodityPrice.toString().split('.')[0]
        const decimal = this.form.commodityPrice.toString().split('.')[1]
          ? `.${this.form.commodityPrice.toString().split('.')[1]}`
          : ''
        return `${integer
          .toString()
          .replace(/(?=(?!^)(\d{3})+$)/g, ',')}${decimal}`
      } else {
        return ''
      }
    },
  },
  methods: {
    // 聚焦金额输入框
    focusInput() {
      this.showFormatPrice = false
      this.$refs.input.focus()
    },
    // 失焦金额输入框
    blurInput() {
      if (this.form.commodityPrice !== '') {
        // 去掉前面的0
        const integer = Number(this.form.commodityPrice.toString().split('.')[0])
        const decimal = this.form.commodityPrice.toString().split('.')[1]
          ? `.${this.form.commodityPrice.toString().split('.')[1]}`
          : ''
        this.form.commodityPrice = isNaN(`${integer}${decimal}`)
          ? this.form.commodityPrice
          : `${integer}${decimal}`
        if (typeof this.rules[this.prop][0].pattern !== 'object') {
          throw new Error(`请确保 rules[${this.prop}][0].pattern 为正则表达式`)
        }
        this.showFormatPrice = this.rules[this.prop][0].pattern.test(
          this.form.commodityPrice,
        )
      }
    },
  },
}
</script>

<style lang="less" scoped>
.price-mask {
  position: absolute;
  z-index: 2;
  top: 1px;
  left: 125px;
  background: white;
  width: 110px;
  overflow: auto;
  font-size: 13px;
}
</style>
