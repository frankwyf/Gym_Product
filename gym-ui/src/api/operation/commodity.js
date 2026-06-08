import request from '@/utils/request'

// 查询商品列表
export function listCommodity(query) {
  return request({
    url: '/operation/commodity/list',
    method: 'get',
    params: query
  })
}

// 查询商品详细
export function getCommodity(commodityId) {
  return request({
    url: '/operation/commodity/' + commodityId,
    method: 'get'
  })
}

//商品入库
export function addNumber(data){
  return request({
    url: '/operation/commodity/modify/in',
    method: 'post',
    data: data
  })
}
//商品出库
export function reduceNumber(data){
  return request({
    url: '/operation/commodity/modify/out',
    method: 'post',
    data: data
  })
}

// 新增商品
export function addCommodity(data) {
  return request({
    url: '/operation/commodity',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateCommodity(data) {
  return request({
    url: '/operation/commodity',
    method: 'put',
    data: data
  })
}

// 删除商品
export function delCommodity(commodityId) {
  return request({
    url: '/operation/commodity/' + commodityId,
    method: 'delete'
  })
}

// 查询商品详细
export function getCharts() {
  return request({
    url: '/operation/commodity/charts',
    method: 'get'
  })
}
