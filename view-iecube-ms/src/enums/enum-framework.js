export default {
  convert2Map(enums) {
    const allocationTypeMap = {}
    for (let i = 0; i < enums.length; i++) {
      allocationTypeMap[enums[i].value] = enums[i]
    }
    return allocationTypeMap
  },
  convert2LabelMap(enums) {
    const allocationTypeMap = {}
    for (let i = 0; i < enums.length; i++) {
      allocationTypeMap[enums[i].value] = enums[i].label
    }
    return allocationTypeMap
  },
  convert2Filter(labelMap) {
    return (value) => {
      return labelMap[value]
    }
  },
  enum(enums) {
    const map = this.convert2Map(enums)
    const labelMap = this.convert2LabelMap(enums)
    const filter = this.convert2Filter(labelMap)
    const obj = {}
    obj['enum'] = enums
    obj['map'] = map
    obj['labelMap'] = labelMap
    obj['filter'] = filter
    return obj
  }
}
