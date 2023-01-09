const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  routes: state => state.permission.routes,
  permissions: state => state.user.permissions,
  schoolLogoUrl: state => state.user.schoolLogoUrl,
  schoolAdminLogoUrl: state => state.user.schoolAdminLogoUrl
}
export default getters
