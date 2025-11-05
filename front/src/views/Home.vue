<template>
  <div class="homepage">
    <!-- 导航栏 -->
    <el-header class="navbar">
      <div class="nav-container">
        <div class="logo">
          <el-image src="/static/logo.png" alt="BabyMonitor" style="height: 50px" />
          <span class="logo-text">BabyMonitor</span>
        </div>
        <el-menu
            mode="horizontal"
            :default-active="activeNav"
            class="nav-menu"
            @select="handleNavSelect"
        >
          <el-menu-item index="home">首页</el-menu-item>
          <el-menu-item index="features">功能特色</el-menu-item>
          <el-menu-item index="pricing">价格方案</el-menu-item>
          <el-menu-item index="about">关于我们</el-menu-item>
          <el-menu-item index="contact">联系我们</el-menu-item>
        </el-menu>
        <div class="nav-actions">
          <el-button @click="handleLogin">平台管理</el-button>
          <el-button type="primary" @click="handleRegister">免费试用</el-button>
        </div>
      </div>
    </el-header>

    <!-- 英雄区块 -->
    <section class="hero-section">
      <div class="hero-container">
        <div class="hero-content">
          <h1 class="hero-title">
            智能婴儿监控<br>
            <span class="highlight">守护宝宝安全</span>
          </h1>
          <p class="hero-description">
            全天候智能监控，实时掌握宝宝状态。高清视频、环境监测、智能报警，<br>
            让您安心工作，宝宝健康成长。
          </p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="handleTryFree">
              立即免费试用
            </el-button>
            <el-button size="large" @click="handleWatchDemo">
              <el-icon><VideoPlay /></el-icon>
              观看演示
            </el-button>
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <div class="stat-number">10,000+</div>
              <div class="stat-label">幸福家庭</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">24/7</div>
              <div class="stat-label">全天守护</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">99.9%</div>
              <div class="stat-label">稳定运行</div>
            </div>
          </div>
        </div>
        <div class="hero-image">
          <el-image src="/static/page1.png" alt="系统演示" />
        </div>
      </div>
    </section>

    <!-- 功能特色 -->
    <section class="features-section" id="features">
      <div class="container">
        <div class="section-header">
          <h2>核心功能特色</h2>
          <p>全方位的婴儿监护解决方案，为您提供专业的安全保障</p>
        </div>
        <el-row :gutter="30">
          <el-col :xs="24" :sm="12" :md="8" v-for="feature in features" :key="feature.id">
            <el-card class="feature-card" shadow="hover">
              <div class="feature-icon">
                <el-icon :size="48" :color="feature.color">
                  <component :is="feature.icon" />
                </el-icon>
              </div>
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.description }}</p>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </section>

    <!-- 产品演示 -->
    <section class="demo-section">
      <div class="container">
        <div class="demo-content">
          <div class="demo-text">
            <h2>随时随地，尽在掌握</h2>
            <p>通过手机App或网页，实时查看宝宝状态，接收智能报警通知</p>
            <ul class="demo-features">
              <li><el-icon><Check /></el-icon> 高清实时视频流</li>
              <li><el-icon><Check /></el-icon> 双向语音对讲</li>
              <li><el-icon><Check /></el-icon> 历史录像回放</li>
              <li><el-icon><Check /></el-icon> 多设备同时观看</li>
            </ul>
          </div>
          <div class="demo-image">
            <el-image src="/static/app-demo.png" alt="App演示" />
          </div>
        </div>
      </div>
    </section>

    <!-- 价格方案 -->
    <section class="pricing-section" id="pricing">
      <div class="container">
        <div class="section-header">
          <h2>选择适合您的方案</h2>
          <p>灵活的价格方案，满足不同家庭的需求</p>
        </div>
        <el-row :gutter="30" justify="center">
          <el-col :xs="24" :sm="12" :md="8" v-for="plan in pricingPlans" :key="plan.id">
            <el-card class="pricing-card" :class="{ 'recommended': plan.recommended }">
              <div class="pricing-header" v-if="plan.recommended">
                <el-tag type="success" effect="dark">推荐</el-tag>
              </div>
              <div class="plan-name">{{ plan.name }}</div>
              <div class="plan-price">
                <span class="currency">¥</span>
                <span class="amount">{{ plan.price }}</span>
                <span class="period">/{{ plan.period }}</span>
              </div>
              <p class="plan-description">{{ plan.description }}</p>
              <ul class="plan-features">
                <li v-for="feature in plan.features" :key="feature">
                  <el-icon><Check /></el-icon>
                  {{ feature }}
                </li>
              </ul>
              <el-button
                  :type="plan.recommended ? 'primary' : ''"
                  size="large"
                  class="plan-button"
                  @click="handleSelectPlan(plan)"
              >
                {{ plan.buttonText }}
              </el-button>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </section>

    <!-- 客户评价 -->
    <section class="testimonials-section">
      <div class="container">
        <div class="section-header">
          <h2>用户真实评价</h2>
          <p>听听其他父母的使用体验</p>
        </div>
        <el-carousel :interval="5000" height="200px">
          <el-carousel-item v-for="testimonial in testimonials" :key="testimonial.id">
            <div class="testimonial-item">
              <el-avatar :size="60" :src="testimonial.avatar" />
              <div class="testimonial-content">
                <p>"{{ testimonial.content }}"</p>
                <div class="testimonial-author">
                  <strong>{{ testimonial.author }}</strong>
                  <span>{{ testimonial.role }}</span>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </section>

    <!-- CTA区块 -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-content">
          <h2>立即开始保护您的宝宝</h2>
          <p>注册即可享受14天免费试用，无需信用卡</p>
          <el-button type="primary" size="large" @click="handleTryFree">
            开始免费试用
          </el-button>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <el-footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <div class="footer-logo">
              <el-image src="/static/logo.png" alt="BabyMonitor" />
              <span>BabyMonitor</span>
            </div>
            <p>专业的婴儿智能监控解决方案提供商</p>
            <div class="social-links">
              <el-icon><ChatDotRound /></el-icon>
              <el-icon><Promotion /></el-icon>
              <el-icon><Link /></el-icon>
            </div>
          </div>
          <div class="footer-section">
            <h4>产品</h4>
            <ul>
              <li><a href="#features">功能特色</a></li>
              <li><a href="#pricing">价格方案</a></li>
              <li><a href="#">下载App</a></li>
              <li><a href="#">更新日志</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>支持</h4>
            <ul>
              <li><a href="#">帮助中心</a></li>
              <li><a href="#">使用文档</a></li>
              <li><a href="#">视频教程</a></li>
              <li><a href="#">联系我们</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>公司</h4>
            <ul>
              <li><a href="#">关于我们</a></li>
              <li><a href="#">加入我们</a></li>
              <li><a href="#">隐私政策</a></li>
              <li><a href="#">服务条款</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>联系我们</h4>
            <div class="contact-info">
              <p><el-icon><Phone /></el-icon> 400-123-4567</p>
              <p><el-icon><Message /></el-icon> support@babymonitor.com</p>
              <p><el-icon><Location /></el-icon> 北京市朝阳区科技园</p>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2024 BabyMonitor. 保留所有权利.</p>
        </div>
      </div>
    </el-footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  VideoCamera,
  Microphone,
  Bell,
  TrendCharts,
  Iphone,
  Lock,
  Check,
  VideoPlay,
  ChatDotRound,
  Promotion,
  Link,
  Phone,
  Message,
  Location
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const activeNav = ref('home')

// 功能特色数据
const features = ref([
  {
    id: 1,
    icon: VideoCamera,
    title: '高清视频监控',
    description: '1080P高清画质，夜视功能，随时随地查看宝宝状态',
    color: '#409EFF'
  },
  {
    id: 2,
    icon: Microphone,
    title: '双向语音对讲',
    description: '实时双向语音，远程安抚宝宝，支持声音检测报警',
    color: '#67C23A'
  },
  {
    id: 3,
    icon: Bell,
    title: '智能报警系统',
    description: '移动检测、哭声识别、温度异常等多维度智能报警',
    color: '#E6A23C'
  },
  {
    id: 4,
    icon: TrendCharts,
    title: '环境监测',
    description: '实时监测温度、湿度、空气质量，创造舒适环境',
    color: '#F56C6C'
  },
  {
    id: 5,
    icon: Iphone,
    title: '多端支持',
    description: '支持手机App、平板、电脑多设备同时观看',
    color: '#909399'
  },
  {
    id: 6,
    icon: Lock,
    title: '数据安全',
    description: '端到端加密传输，确保您的隐私数据安全',
    color: '#8E44AD'
  }
])

// 价格方案
const pricingPlans = ref([
  {
    id: 1,
    name: '基础版',
    price: '0',
    period: '永久免费',
    description: '适合单个宝宝的基础监控',
    features: [
      '1个摄像头设备',
      '720P视频画质',
      '基础移动检测',
      '7天历史记录',
      '基础客服支持'
    ],
    buttonText: '免费使用',
    recommended: false
  },
  {
    id: 2,
    name: '专业版',
    price: '29',
    period: '月',
    description: '适合对监控有更高要求的家庭',
    features: [
      '3个摄像头设备',
      '1080P高清画质',
      '智能哭声识别',
      '30天历史记录',
      '温度湿度监测',
      '优先客服支持'
    ],
    buttonText: '免费试用',
    recommended: true
  },
  {
    id: 3,
    name: '企业版',
    price: '99',
    period: '月',
    description: '适合托儿所、月子中心等机构',
    features: [
      '10个摄像头设备',
      '2K超清画质',
      '所有智能功能',
      '90天历史记录',
      '多用户管理',
      '专属技术支持',
      '定制化开发'
    ],
    buttonText: '联系销售',
    recommended: false
  }
])

// 用户评价
const testimonials = ref([
  {
    id: 1,
    content: '使用BabyMonitor后，我终于可以安心工作了。智能报警功能非常准确，宝宝一哭手机就会立即提醒。',
    author: '张妈妈',
    role: '职场妈妈',
    avatar: ''
  },
  {
    id: 2,
    content: '画面清晰，操作简单，爷爷奶奶也能轻松使用。双向语音功能很棒，可以远程安抚宝宝。',
    author: '李先生',
    role: '二宝爸爸',
    avatar: ''
  },
  {
    id: 3,
    content: '作为托儿所负责人，BabyMonitor的企业版完美满足了我们的需求，多摄像头管理和权限设置非常方便。',
    author: '王园长',
    role: '幼儿园园长',
    avatar: ''
  }
])

// 导航选择
const handleNavSelect = (index: string) => {
  const element = document.getElementById(index)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}

// 登录
const handleLogin = () => {
  ElMessage.info('跳转到登录页面')
  // 实际项目中这里应该跳转到登录页面
}

// 注册
const handleRegister = () => {
  ElMessage.info('跳转到注册页面')
}

// 免费试用
const handleTryFree = () => {
  ElMessage.success('开始14天免费试用！')
}

// 观看演示
const handleWatchDemo = () => {
  ElMessage.info('打开演示视频')
}

// 选择方案
const handleSelectPlan = (plan: any) => {
  if (plan.id === 1) {
    handleTryFree()
  } else if (plan.id === 2) {
    ElMessage.success(`开始${plan.name}免费试用`)
  } else {
    ElMessage.info('请联系销售: 400-123-4567')
  }
}
</script>

<style scoped>
.homepage {
  font-family: 'Helvetica Neue', Arial, sans-serif;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  z-index: 1000;
  border-bottom: 1px solid #f0f0f0;
}

.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
}

.logo img {
  height: 32px;
}

.nav-menu {
  border-bottom: none;
}

.nav-actions {
  display: flex;
  gap: 10px;
}

/* 英雄区块样式 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 120px 0 80px;
  margin-top: 60px;
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 20px;
}

.highlight {
  background: linear-gradient(45deg, #FFD700, #FF6B6B);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-description {
  font-size: 1.2rem;
  line-height: 1.6;
  margin-bottom: 30px;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 40px;
}

.hero-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  opacity: 0.8;
}

.hero-image img {
  width: 100%;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

/* 通用容器 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 区块标题 */
.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-header h2 {
  font-size: 2.5rem;
  margin-bottom: 15px;
  color: #333;
}

.section-header p {
  font-size: 1.1rem;
  color: #666;
}

/* 功能特色样式 */
.features-section {
  padding: 80px 0;
  background: #f8f9fa;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  margin-bottom: 30px;
  border: none;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 1.3rem;
  margin-bottom: 15px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

/* 产品演示样式 */
.demo-section {
  padding: 80px 0;
  background: white;
}

.demo-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.demo-text h2 {
  font-size: 2.2rem;
  margin-bottom: 20px;
  color: #333;
}

.demo-text p {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 30px;
  line-height: 1.6;
}

.demo-features {
  list-style: none;
  padding: 0;
}

.demo-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  color: #333;
}

.demo-features .el-icon {
  color: #67C23A;
}

.demo-image img {
  width: 100%;
  border-radius: 15px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

/* 价格方案样式 */
.pricing-section {
  padding: 80px 0;
  background: #f8f9fa;
}

.pricing-card {
  text-align: center;
  padding: 40px 30px;
  position: relative;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.pricing-card.recommended {
  border-color: #409EFF;
  transform: scale(1.05);
}

.pricing-header {
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
}

.plan-name {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.plan-price {
  margin-bottom: 20px;
}

.currency {
  font-size: 1.5rem;
  vertical-align: super;
}

.amount {
  font-size: 3rem;
  font-weight: bold;
  color: #409EFF;
}

.period {
  color: #666;
}

.plan-description {
  color: #666;
  margin-bottom: 30px;
}

.plan-features {
  list-style: none;
  padding: 0;
  margin-bottom: 30px;
  text-align: left;
}

.plan-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  color: #333;
}

.plan-features .el-icon {
  color: #67C23A;
}

.plan-button {
  width: 100%;
}

/* 用户评价样式 */
.testimonials-section {
  padding: 80px 0;
  background: white;
}

.testimonial-item {
  display: flex;
  align-items: center;
  gap: 30px;
  max-width: 800px;
  margin: 0 auto;
  padding: 40px;
}

.testimonial-content {
  flex: 1;
  text-align: left;
}

.testimonial-content p {
  font-size: 1.1rem;
  line-height: 1.6;
  color: #333;
  margin-bottom: 15px;
  font-style: italic;
}

.testimonial-author strong {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.testimonial-author span {
  color: #666;
}

/* CTA区块样式 */
.cta-section {
  padding: 80px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-align: center;
}

.cta-content h2 {
  font-size: 2.5rem;
  margin-bottom: 20px;
}

.cta-content p {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0.9;
}

/* 页脚样式 */
.footer {
  background: #2c3e50;
  color: white;
  padding: 60px 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1.5fr;
  gap: 40px;
  margin-bottom: 40px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 15px;
}

.footer-logo img {
  height: 32px;
}

.footer-section h4 {
  margin-bottom: 20px;
  color: #ecf0f1;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section ul li a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #3498db;
}

.contact-info p {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  color: #bdc3c7;
}

.social-links {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.social-links .el-icon {
  font-size: 1.5rem;
  cursor: pointer;
  transition: color 0.3s;
}

.social-links .el-icon:hover {
  color: #3498db;
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #34495e;
  color: #95a5a6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-container,
  .demo-content,
  .footer-content {
    grid-template-columns: 1fr;
  }

  .hero-title {
    font-size: 2rem;
  }

  .nav-menu {
    display: none;
  }

  .pricing-card.recommended {
    transform: none;
  }

  .testimonial-item {
    flex-direction: column;
    text-align: center;
  }
}
</style>