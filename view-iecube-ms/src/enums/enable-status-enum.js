import framework from '@/enums/enum-framework'

const enums = [
  {
    value: 'ENABLE',
    label: '有效'
  },
  {
    value: 'DISABLE',
    label: '失效'
  }
]

export default framework.enum(enums)
