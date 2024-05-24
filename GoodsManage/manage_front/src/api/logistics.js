import request from "@/utils/request";

export function getLogistics() {
  return request({
    url: '/logistics/getAllLogistics',
    method: 'get'
  })
}

export function addLogistics(logistics) {
  return request({
    url: '/logistics/addLogistics',
    method: 'post',
    data: logistics,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function updateLogistics(logistics) {
  return request({
    url: '/logistics/updateLogistics',
    method: 'put',
    data: logistics,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function deleteLogistics(id) {
  return request({
    url: `/logistics/deleteLogistics/${id}`,
    method: 'delete',
  })
}
