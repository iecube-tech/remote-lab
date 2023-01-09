import framework from '@/enums/enum-framework'

const enums = [
  {
    value: 'IN_SCHOOL',
    label: '在校'
  },
  {
    value: 'LEAVE_SCHOOL',
    label: '离校'
  }
]

export default framework.enum(enums)
