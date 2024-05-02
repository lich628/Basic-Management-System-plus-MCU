const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  userAccount: state => state.user.userAccount,
  userName: state => state.user.userName,
  role: state => state.user.role,
  password: state => state.user.password
}
export default getters
