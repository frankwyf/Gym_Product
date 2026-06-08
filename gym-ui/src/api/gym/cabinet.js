import request from '@/utils/request'

// 查询租柜列表
export function listCabinet(query) {
  return request({
    url: '/gym/cabinet/list',
    method: 'get',
    params: query
  })
}

// 查询租柜详细
export function getCabinet(cabinetId) {
  return request({
    url: '/gym/cabinet/' + cabinetId,
    method: 'get'
  })
}

// 新增租柜
export function addCabinet(data) {
  return request({
    url: '/gym/cabinet',
    method: 'post',
    data: data
  })
}

// 修改租柜
export function updateCabinet(data) {
  return request({
    url: '/gym/cabinet',
    method: 'put',
    data: data
  })
}

// 删除租柜
export function delCabinet(cabinetId) {
  return request({
    url: '/gym/cabinet/' + cabinetId,
    method: 'delete'
  })
}

export function renewal(data){
  return request({
    url: '/gym/cabinet/renewal',
    method: 'post',
    data: data
  })
}

export function distribute(){
  return request({
    url: '/gym/cabinet/member/list',
    method: 'get',
  })
}
