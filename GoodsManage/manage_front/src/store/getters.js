const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  userId: state => state.user.userId,
  userAccount: state => state.user.userAccount,
  userName: state => state.user.userName,
  role: state => state.user.role,
  password: state => state.user.password,
  temperature: state => state.thresholds.temperature,
  humidity: state => state.thresholds.humidity,
  smokeSensor: state => state.thresholds.smokeSensor
}
export default getters
