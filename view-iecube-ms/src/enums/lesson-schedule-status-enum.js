import framework from '@/enums/enum-framework'

const enums = [
  {
    value: 'NOT_START',
    label: '未开始',
    colorClass: 'info-color'
  },
  {
    value: 'CURRENT',
    label: '进行中',
    colorClass: 'success-color'
  },
  {
    value: 'END',
    label: '已结束',
    colorClass: 'danger-color'
  }
]

export default framework.enum(enums)
