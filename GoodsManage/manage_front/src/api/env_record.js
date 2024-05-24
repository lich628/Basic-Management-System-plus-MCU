import request from "@/utils/request";

export function getData() {
  return request({
    url: '/data/list',
    method: 'get',
  })
}

export function addData(data) {
  return request({
    url: '/data/list',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function deleteData(id) {
  return request({
    url: `/data/list/${id}`,
    method: 'delete',
  })
}

export function buzz(turnOn) {
  return request({
    url: `/data/buzzer`,
    method: 'post',
    params: {turnOn}
  })
}
