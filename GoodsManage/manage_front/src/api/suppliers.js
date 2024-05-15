import request from "@/utils/request";

export function getSuppliers() {
  return request({
    url: '/suppliers/getAllSuppliers',
    method: 'get'
  })
}

export function addSupplier(Supplier) {
  return request({
    url: '/suppliers/addSupplier',
    method: 'post',
    data: Supplier,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function updateSupplier(Supplier) {
  return request({
    url: '/suppliers/updateSupplier',
    method: 'put',
    data: Supplier,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function deleteSupplier(id) {
  return request({
    url: `/suppliers/deleteSupplier/${id}`,
    method: 'delete',
  })
}
