import request from '@/utils/request'

// 查询会员卡管理列表
export function listVip(query) {
  return request({
    url: '/gym/vip/list',
    method: 'get',
    params: query
  })
}

// 查询会员卡管理详细
export function getVip(vipId) {
  return request({
    url: '/gym/vip/' + vipId,
    method: 'get'
  })
}
// 查询会员卡管理详细
export function signIn(vipId) {
  return request({
    url: '/gym/vip/sign/in/' + vipId,
    method: 'get'
  })
}

// 新增会员卡管理
export function addVip(data) {
  return request({
    url: '/gym/vip',
    method: 'post',
    data: data
  })
}

// 修改会员卡管理
export function updateVip(data) {
  return request({
    url: '/gym/vip',
    method: 'put',
    data: data
  })
}

// 删除会员卡管理
export function delVip(vipId) {
  return request({
    url: '/gym/vip/' + vipId,
    method: 'delete'
  })
}

export function renewal(data){
  return request({
    url: '/gym/vip/renewal',
    method: 'post',
    data: data
  })
}
