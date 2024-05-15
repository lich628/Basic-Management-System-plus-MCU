import request from '@/utils/request'

export function goodsList(pageNum, pageSize) {
    return request({
        url: '/goods/list',
        method: 'get',
        params: {pageNum, pageSize}
    })
}

export function goodsListAll() {
  return request({
    url: '/goods/listAll',
    method: 'get',
  })
}

export function goodsType() {
  return request({
    url: '/goods/list/type',
    method: 'get'
  })
}

export function goodsSearch(goodsName, goodsType, pageNum, pageSize) {
  return request({
    url: '/goods/listLike',
    method: 'get',
    params: {goodsName, goodsType, pageNum, pageSize}
  })
}

export function deleteGoodsById(Goods) {
  return request({
    url: '/goods/list',
    method: 'delete',
    data: Goods
  })
}

export function updateGoods(Goods) {
  return request({
    url: '/goods/list',
    method: 'put',
    data: Goods,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
