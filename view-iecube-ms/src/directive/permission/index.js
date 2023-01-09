import { hasPermission } from '@/permission'

function checkPermission(el, binding, vNode) {
  if (!hasPermission(binding.value)) {
    el.parentNode && el.parentNode.removeChild(el)
  }
}

export default {
  inserted: checkPermission,
  update: checkPermission
}
