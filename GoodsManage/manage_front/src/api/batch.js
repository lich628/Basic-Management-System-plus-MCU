import request from '@/utils/request'

export function batchList() {
    return request({
        url: '/batches/list',
        method: 'get'
    })
}

export function historyList() {
  return request({
    url: '/batches/historyList',
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

export function historyListSelect(batchInfoInput, batchType, batchStatus) {
  return request({
    url: '/batches/historyListLike',
    method: 'get',
    params: { batchInfoInput, batchType, batchStatus }
  })
}

export function userBatch(userId) {
  return request({
    url: '/batches/userBatch',
    method: 'get',
    params: { userId }
  })
}

export function userBatchSelect(userId,batchInfoInput, batchType, batchStatus) {
  return request({
    url: '/batches/userBatchListLike',
    method: 'get',
    params: { userId, batchInfoInput, batchType, batchStatus}
  })
}

export function batchStatistics() {
  return request({
    url: '/batches/statistics',
    method: 'get'
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

export function newBatch(batch) {
  return request({
    url: '/batches/list',
    method: 'post',
    data: batch,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function selectBatchByUid(cardUid) {
  return request({
    url: '/batches/list/selectByUid',
    method: 'get',
    params: { cardUid }
  })
}

export function closeBatchByBatchId(batchId) {
  return request({
    url: '/batches/closeBatch',
    method: 'put',
    params: { batchId }
  })
}

export function closeBatchInfo(batchId) {
  return request({
    url: '/batches/closeBatchInfo',
    method: 'put',
    params: { batchId }
  })
}


