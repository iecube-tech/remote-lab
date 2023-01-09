export default {
  convert2Map(enums) {
    const allocationTypeMap = {}
    for (let i = 0; i < enums.length; i++) {
      allocationTypeMap[enums[i].value] = enums[i].label
    }
    return allocationTypeMap
  },
  convert2Filter(map) {
    return (value) => {
      return map[value]
    }
  },
  enum(enums) {
    const map = this.convert2Map(enums)
    const filter = this.convert2Filter(map)
    const obj = {}
    obj['enum'] = enums
    obj['map'] = map
    obj['filter'] = filter
    return obj
  }
}
