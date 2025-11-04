import {createApp} from 'vue'
//如果你对打包后的文件大小不是很在乎，那么使用完整导入会更方便。
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './style.css'
import App from './App.vue'

const app = createApp(App)

app.use(ElementPlus)
app.mount('#app')