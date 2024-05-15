import request from "@/utils/request";

export function getOrders() {
  return request({
    url: '/orders/getAllOrders',
    method: 'get'
  })
}

export function addOrder(order) {
  return request({
    url: '/orders/addOrder',
    method: 'post',
    data: order,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function updateOrder(order) {
  return request({
    url: '/orders/updateOrder',
    method: 'put',
    data: order,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function deleteOrder(orderId) {
  return request({
    url: `/orders/deleteOrder/${orderId}`,
    method: 'delete',
  })
}
