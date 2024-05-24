import request from "@/utils/request";

export function IOList() {
  return request({
    url: '/ioRecords/list',
    method: 'get',
  })
}

export function addIORecords(io_Records) {
  return request({
    url: '/ioRecords/list',
    method: 'post',
    data: io_Records,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function deleteIORecords(ioId) {
  return request({
    url: `/ioRecords/list/${ioId}`,
    method: 'delete',
  })
}
