<script setup lang="ts">
import { Heart, Bot, Sparkles, ArrowRight, Monitor, Tablet, Smartphone } from 'lucide-vue-next'
import { useRouter } from 'vue-router'

const router = useRouter()

const apps = [
  {
    id: 'love-app',
    name: 'AI 恋爱大师',
    description: '专业的恋爱心理专家，为您解答单身、恋爱、婚姻中的各种困惑，提供专属情感建议',
    icon: Heart,
    gradient: 'from-pink-400 via-rose-500 to-red-500',
    shadow: 'shadow-pink-500/25',
    ring: 'focus-within:ring-pink-500/20',
    bgGradient: 'from-slate-50 via-white to-pink-50'
  },
  {
    id: 'manus',
    name: 'AI 超级智能体',
    description: '全能型 AI 助手，具备自主规划能力，可调用搜索、文件、PDF等多种工具完成复杂任务',
    icon: Bot,
    gradient: 'from-blue-400 via-cyan-500 to-teal-500',
    shadow: 'shadow-blue-500/25',
    ring: 'focus-within:ring-blue-500/20',
    bgGradient: 'from-slate-50 via-white to-blue-50'
  }
]

function navigateTo(appId: string) {
  router.push(`/${appId}`)
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-white to-slate-100">
    <!-- 顶部导航 -->
    <header class="sticky top-0 z-10 backdrop-blur-md bg-white/80 border-b border-gray-100/50">
      <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-3 sm:py-4">
        <div class="flex items-center justify-between">
          <!-- Logo -->
          <div class="flex items-center gap-2.5 sm:gap-3">
            <div class="w-9 h-9 sm:w-11 sm:h-11 bg-gradient-to-br from-violet-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg shadow-violet-500/25">
              <Sparkles class="w-4.5 h-4.5 sm:w-5.5 sm:h-5.5 text-white" />
            </div>
            <div>
              <h1 class="text-base sm:text-lg font-bold text-gray-800 leading-tight">AI Agent 控制台</h1>
              <p class="text-[11px] sm:text-xs text-gray-400 hidden sm:block">Powered by Spring AI</p>
            </div>
          </div>
          <!-- 响应式标识 -->
          <div class="hidden sm:flex items-center gap-3 text-[11px] text-gray-400">
            <span class="flex items-center gap-1"><Monitor class="w-3 h-3" /> PC</span>
            <span class="flex items-center gap-1"><Tablet class="w-3 h-3" /> 平板</span>
            <span class="flex items-center gap-1"><Smartphone class="w-3 h-3" /> 手机</span>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容 -->
    <main class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12 lg:py-16">
      <!-- 标题 -->
      <div class="text-center mb-8 sm:mb-12 lg:mb-14">
        <h2 class="text-2xl sm:text-3xl lg:text-4xl font-bold text-gray-800 mb-3 sm:mb-4">
          选择您的 AI 智能助手
        </h2>
        <p class="text-sm sm:text-base text-gray-500 max-w-xl mx-auto leading-relaxed">
          多种专业 AI 助手，覆盖情感咨询与智能规划，总有一款适合您
        </p>
      </div>

      <!-- 应用卡片 -->
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-6 lg:gap-8 max-w-3xl mx-auto">
        <div
          v-for="app in apps"
          :key="app.id"
          @click="navigateTo(app.id)"
          class="card-hover group relative cursor-pointer bg-white rounded-2xl sm:rounded-3xl p-5 sm:p-6 lg:p-8 shadow-sm hover:shadow-xl border border-gray-100/80 overflow-hidden"
        >
          <!-- 背景光晕 -->
          <div :class="[
            'absolute -top-6 -right-6 w-28 h-28 sm:w-36 sm:h-36 rounded-full blur-3xl opacity-[0.07] transition-opacity duration-500 group-hover:opacity-[0.14]',
            app.id === 'love-app' ? 'bg-pink-400' : 'bg-blue-400'
          ]"></div>

          <div class="relative flex flex-col sm:flex-row items-start gap-4 sm:gap-5">
            <!-- 图标 -->
            <div :class="[
              'w-12 h-12 sm:w-14 sm:h-14 lg:w-16 lg:h-16 rounded-xl sm:rounded-2xl flex items-center justify-center flex-shrink-0 bg-gradient-to-br shadow-lg transition-transform duration-300 group-hover:scale-105',
              app.gradient, app.shadow
            ]">
              <component :is="app.icon" class="w-6 h-6 sm:w-7 sm:h-7 lg:w-8 lg:h-8 text-white" />
            </div>

            <!-- 文字 -->
            <div class="flex-1 min-w-0">
              <h3 class="text-lg sm:text-xl lg:text-2xl font-semibold text-gray-800 mb-1.5 sm:mb-2 group-hover:text-violet-600 transition-colors">
                {{ app.name }}
              </h3>
              <p class="text-sm sm:text-base text-gray-500 leading-relaxed mb-4 sm:mb-5">
                {{ app.description }}
              </p>
              <!-- 按钮 -->
              <div class="inline-flex items-center gap-2 px-4 sm:px-5 py-2 sm:py-2.5 rounded-full bg-gradient-to-r from-violet-500 to-purple-600 text-white text-sm font-medium shadow-md shadow-violet-500/20 group-hover:shadow-lg group-hover:gap-3 transition-all duration-300">
                <span>开始对话</span>
                <ArrowRight class="w-4 h-4 transition-transform duration-300 group-hover:translate-x-1" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 底部 -->
      <footer class="mt-10 sm:mt-14 lg:mt-16 text-center">
        <div class="flex flex-wrap items-center justify-center gap-x-3 gap-y-1 text-[11px] sm:text-xs text-gray-400">
          <span>Vue 3 + Vite + Tailwind CSS</span>
          <span class="hidden sm:inline">·</span>
          <span>响应式设计</span>
          <span class="hidden sm:inline">·</span>
          <span>流式对话体验</span>
          <span class="hidden sm:inline">·</span>
          <span>Spring AI 驱动</span>
        </div>
      </footer>
    </main>
  </div>
</template>
