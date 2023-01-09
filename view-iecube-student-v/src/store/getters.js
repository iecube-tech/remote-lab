const getters = {
  route: state => state.app.route,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  schoolLogoUrl: state => state.user.schoolLogoUrl
}
export default getters
