import dayjs from 'dayjs'

export function datetime (value) {
  if (value) {
    return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
  }
}

export function date (value) {
  if (value) {
    return dayjs(value).format('YYYY-MM-DD')
  }
}

export function time (value) {
  if (value) {
    return dayjs(value).format('HH:mm:ss')
  }
}
