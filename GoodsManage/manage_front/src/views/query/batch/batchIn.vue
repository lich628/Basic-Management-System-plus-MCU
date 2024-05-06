<template>
  <div>
    <div>
      <el-button @click="startData">接收串口信息</el-button>
      <el-button @click="stopData" type="danger">停止</el-button>
    </div>
    <div>
      <li v-for="msg in messages" :key="msg.id" style="color: #2c3e50">{{ msg.content }}</li>
    </div>
  </div>
</template>

<script>
export default {
  name: "Batch",
  data() {
    return {
      messages: []
    }
  },
  created() {
    this.$socket.addEventListener('open', this.onOpen)
    this.$socket.addEventListener('message', this.onMessage)
    this.$socket.addEventListener('close', this.onClose)
  },
  methods : {
    onOpen(event) {
      console.log('连接成功')
    },
    onMessage(event) {
      console.log('接收到的消息为：', event.data)
      this.messages.push({content: event.data})
    },
    onClose(event) {
      console.log('连接关闭')
    },
    sendMessage(message) {
      this.$socket.send(this.message)
    },
    startData(){
      this.$socket.send('start')
    },
    stopData(){
      this.$socket.send('stop')
    }
  },
  beforeMount() {
  },
  destroyed() {
    this.$socket.removeEventListener('open', this.onOpen)
    this.$socket.removeEventListener('message', this.onMessage)
    this.$socket.removeEventListener('close', this.onClose)
  }
}
</script>

<style>

</style>

