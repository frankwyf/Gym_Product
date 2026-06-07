// 查询私教列表
import request from "@/utils/request";

export function listTeacher(query) {
  return request({
    url: '/gym/studentAssignment/list',
    method: 'get',
    params: query
  })
}

export function getStudentNumber() {
  return request({
    url: '/gym/studentAssignment/charts',
    method: 'get',
  })
}

// 查询私教的学生
export function getStudent(teacherId) {
  return request({
    url: '/gym/studentAssignment/student/list/' + teacherId,
    method: 'get'
  })
}

// 查询未分配私教的学生
export function getStudentNoTeacher() {
  return request({
    url: '/gym/studentAssignment/student/list/',
    method: 'get'
  })
}

// 分配学员
export function updateTeacher(data) {
  return request({
    url: '/gym/studentAssignment/update',
    method: 'put',
    params: data
  })
}


// 删除学员信息
export function deleteTeacher(studentId) {
  return request({
    url: '/gym/studentAssignment/delete/'+ studentId,
    method: 'get',
  })
}

// 删除学员信息
export function getTeacher(memberId) {
  return request({
    url: '/gym/studentAssignment/teacher/list/'+ memberId,
    method: 'get',
  })
}
