import request from '@/utils/request'

export function batchList() {
    return request({
        url: '/batches/list',
        method: 'get'
    })
}
export function batchListSelect(batchInfoInput, batchType, batchStatus) {
  return request({
    url: '/batches/listLike',
    method: 'get',
    params: { batchInfoInput, batchType, batchStatus }
  })
}

export function updateBatch(batch) {
  return request({
    url: '/batches/list',
    method: 'put',
    data: batch,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function getOccupiedUids() {
  return request({
    url: '/batches/occupiedUids',
    method: 'get'
  })
}

