import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasPermission = store.getters.permissions
      if (hasPermission && hasPermission.length > 0) {
        next()
      } else {
        try {
          // get user info
          const { permissionCodeList } = await store.dispatch('user/getInfo')

          if (permissionCodeList && permissionCodeList.length > 0) {
            const accessRoutes = await store.dispatch('permission/generateRoutes', permissionCodeList || [])
            router.addRoutes(accessRoutes)

            next({ ...to, replace: true })
          } else {
            next()
          }
        } catch (error) {
          console.log(error)
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})

export const hasPermission = permission => {
  if (!permission) {
    return true
  }
  const permissionCodeList = store.getters.permissions
  if ((!permissionCodeList) || permissionCodeList.length < 1) {
    return false
  }
  if (typeof permission === 'string') {
    return permissionCodeList.includes(permission)
  } else if (permission instanceof Array) {
    if (permission.length < 1) {
      return true
    }
    for (let i = 0; i < permission.length; i++) {
      if (permissionCodeList.includes(permission[i])) {
        return true
      }
    }
  }
  return false
}
