import request from '@/utils/request'

// 查询会员卡使用记录列表
export function listUsage(query) {
  return request({
    url: '/gym/usage/list',
    method: 'get',
    params: query
  })
}

// 查询会员卡使用记录详细
export function getUsage(usageId) {
  return request({
    url: '/gym/usage/' + usageId,
    method: 'get'
  })
}

// 新增会员卡使用记录
export function addUsage(data) {
  return request({
    url: '/gym/usage',
    method: 'post',
    data: data
  })
}

// 修改会员卡使用记录
export function updateUsage(data) {
  return request({
    url: '/gym/usage',
    method: 'put',
    data: data
  })
}

// 删除会员卡使用记录
export function delUsage(usageId) {
  return request({
    url: '/gym/usage/' + usageId,
    method: 'delete'
  })
}
