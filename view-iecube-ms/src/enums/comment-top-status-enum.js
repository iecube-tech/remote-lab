import framework from '@/enums/enum-framework'

const enums = [
  {
    value: true,
    label: '是'
  },
  {
    value: false,
    label: '否'
  }
]

const val = framework.enum(enums)
val.filter = (value) => {
  if (value === undefined) {
    return '否'
  }
  return val.labelMap[value]
}
export default val
