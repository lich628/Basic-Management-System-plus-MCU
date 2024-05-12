import request from "@/utils/request";

export function IOList() {
  return request({
    url: '/ioRecords/list',
    method: 'get',
  })
}

export function getIoRecords(batchIdOrOperatorName, pageNum, pageSize) {
  return request({
    url: '/ioRecords/listPage',
    method: 'get',
    params: {batchIdOrOperatorName, pageNum, pageSize}
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
    url: '/ioRecords/list',
    method: 'delete',
    ioId
  })
}
