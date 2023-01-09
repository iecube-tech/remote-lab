<template>
  <div>
    <div
         style="width: 100%">
      <div :ref="'div' + i"
           v-for="i in totalPageCount"
           :key="i">
        <canvas :ref="'canvas' + i"
                @click="handleClickCanvas"></canvas>
      </div>
    </div>
  </div>
</template>

<script>
import { getDocument, GlobalWorkerOptions } from 'pdfjs-dist'
import workerSrc from 'pdfjs-dist/build/pdf.worker.entry'
GlobalWorkerOptions.workerSrc = workerSrc

export default {
  name: 'pdf-preview',
  components: {},
  props: {
    url: {
      type: String,
      default: undefined
    },
    password: {
      default: 'Zailairen19'
    }
  },
  data () {
    return {
      totalPageCount: 0,
      showModal: false,
      updateCallback: undefined,
      loading: false,
      timeOutName: '',
      imageSrc: '',
      debounce: ''
    }
  },
  watch: {
    url (newValue) {
      console.log(newValue)
      if (!newValue) {
        return false
      }
      this.renderPdf()
    }
  },
  mounted () {
    this.$nextTick(() => {
      if (!this.url) return false
      this.renderPdf()
    })
  },
  methods: {
    renderPdf () {
      this.loading = true
      const dom = this.$refs
      const loadingTask = getDocument(this.url)
      loadingTask.onPassword = (updateCallback, reason) => {
        updateCallback(this.password)
      }
      loadingTask.promise.then(pdf => {
        this.totalPageCount = pdf.numPages
        for (let i = 1; i <= pdf.numPages; i++) {
          pdf.getPage(i).then(function (page) {
            const desiredWidth = 960
            const scale = desiredWidth / page.getViewport({ scale: 1 }).width
            const viewport = page.getViewport({ scale: scale })
            if (!dom['canvas' + i] || !dom['canvas' + i][0]) return false
            const canvas = dom['canvas' + i][0]
            const context = canvas.getContext('2d')
            canvas.height = viewport.height
            canvas.width = viewport.width
            const renderContext = {
              canvasContext: context,
              viewport: viewport
            }
            page.render(renderContext)
          })
        }
        this.loading = false
      })
    },
    handleClickCanvas (canvas) {
      this.imageSrc = canvas.target.toDataURL('image/png')
    },
    handleScale () {
      const viewer = this.$el.querySelector('.images').$viewer
      viewer.show()
    }
  }
}
</script>

<style scoped>
.images {
  display: none;
}
</style>
