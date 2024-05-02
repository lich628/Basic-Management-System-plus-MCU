import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/users/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/users/logout',
    method: 'post'
  })
}

export function update(data) {
  return request({
    url: '/users/list',
    headers: { 'Content-Type': 'multipart/form-data' },
    method: 'put',
    data
  })
}

export function userList(pageNum, pageSize) {
  return request({
    url: '/users/list',
    method: 'get',
    params: {pageNum, pageSize}
  })
}

export function userListLike(userNameOrAccount, sex, role, pageNum, pageSize) {
  return request({
    url: `/users/listLike`,
    method: 'get',
    params: {userNameOrAccount, sex, role, pageNum, pageSize}
  })
}

export function deleteUserById(Users) {
  return request({
    url: '/users/list',
    method: 'delete',
    data: Users
  })
}

export function addUser(data) {
  return request({
    url: '/users/list/add',
    method: 'post',
    data
  })
}
