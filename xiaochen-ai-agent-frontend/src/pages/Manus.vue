<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { ArrowLeft, Send, Loader2, Bot, User } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { Message, sendManusMessage } from '@/api'

const router = useRouter()
const chatContainer = ref<HTMLElement | null>(null)
const inputMessage = ref('')
const isLoading = ref(false)
const messages = ref<Message[]>([])

function goBack() {
  router.push('/')
}

function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

function addMessage(content: string, isUser: boolean) {
  messages.value.push({
    id: Date.now().toString() + Math.random().toString(36).substring(2),
    content,
    isUser,
    timestamp: Date.now()
  })
  scrollToBottom()
}

function handleSend() {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return

  inputMessage.value = ''
  addMessage(message, true)
  isLoading.value = true

  const aiMessage: Message = {
    id: Date.now().toString() + Math.random().toString(36).substring(2),
    content: '',
    isUser: false,
    timestamp: Date.now()
  }
  messages.value.push(aiMessage)
  scrollToBottom()

  sendManusMessage(
    message,
    (token) => {
      // 后端每完成一步才响应一次，每次响应前加空行分隔步骤
      if (aiMessage.content.length > 0) {
        aiMessage.content += '\n'
      }
      aiMessage.content += token
      scrollToBottom()
    },
    (error) => {
      console.error('SSE error:', error)
      aiMessage.content = `抱歉，执行出错了：${error.message}`
      isLoading.value = false
    },
    () => {
      isLoading.value = false
    }
  )
}

function handleKeydown(event: KeyboardEvent) {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    handleSend()
  }
}

onMounted(() => {
  addMessage('您好！我是 YuManus，您的全能 AI 助手 🤖\n我可以调用搜索、文件、PDF生成等多种工具，帮您完成各类复杂任务。请告诉我您的需求吧！', false)
})
</script>

<template>
  <div class="h-dvh flex flex-col bg-gradient-to-br from-slate-50 via-white to-blue-50/30">
    <!-- ====== 顶部导航 ====== -->
    <header class="sticky top-0 z-10 backdrop-blur-md bg-white/85 border-b border-gray-100/60">
      <div class="max-w-4xl mx-auto px-3 sm:px-5 lg:px-6 py-2.5 sm:py-3">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-2 sm:gap-3">
            <button
              @click="goBack"
              class="p-1.5 sm:p-2 rounded-xl hover:bg-gray-100 transition-colors active:scale-95"
              aria-label="返回首页"
            >
              <ArrowLeft class="w-5 h-5 text-gray-500" />
            </button>

            <!-- AI 头像 -->
            <div class="w-8 h-8 sm:w-10 sm:h-10 bg-gradient-to-br from-blue-400 via-cyan-500 to-teal-500 rounded-xl flex items-center justify-center shadow-md shadow-blue-500/20 flex-shrink-0">
              <Bot class="w-4 h-4 sm:w-5 sm:h-5 text-white" />
            </div>

            <div class="min-w-0">
              <h1 class="font-semibold text-sm sm:text-base text-gray-800 truncate">AI 超级智能体</h1>
              <p class="text-[10px] sm:text-xs text-gray-400 hidden sm:block">YuManus · 全能助手</p>
            </div>
          </div>

          <!-- 状态 -->
          <div class="flex items-center gap-2">
            <span :class="[
              'text-[10px] sm:text-xs px-1.5 sm:px-2 py-0.5 sm:py-1 rounded-lg flex items-center gap-1',
              isLoading ? 'bg-blue-100 text-blue-600' : 'bg-gray-100 text-gray-400'
            ]">
              <Loader2 v-if="isLoading" class="w-3 h-3 animate-spin" />
              <span class="hidden sm:inline">{{ isLoading ? '执行中' : '就绪' }}</span>
            </span>
          </div>
        </div>
      </div>
    </header>

    <!-- ====== 聊天区域 ====== -->
    <div ref="chatContainer" class="flex-1 overflow-y-auto scrollbar-thin px-3 sm:px-5 lg:px-6 py-4 sm:py-5">
      <div class="max-w-3xl mx-auto space-y-4 sm:space-y-5">
        <div
          v-for="message in messages"
          :key="message.id"
          :class="[
            'flex gap-2 sm:gap-3 fade-in',
            message.isUser ? 'flex-row-reverse' : 'flex-row'
          ]"
        >
          <!-- 头像 -->
          <div :class="[
            'w-8 h-8 sm:w-9 sm:h-9 rounded-xl flex items-center justify-center flex-shrink-0 shadow-sm',
            message.isUser
              ? 'bg-gradient-to-br from-violet-400 to-purple-600 shadow-violet-500/15'
              : 'bg-gradient-to-br from-blue-400 via-cyan-500 to-teal-500 shadow-blue-500/15'
          ]">
            <User v-if="message.isUser" class="w-4 h-4 sm:w-4.5 sm:h-4.5 text-white" />
            <Bot v-else class="w-4 h-4 sm:w-4.5 sm:h-4.5 text-white" />
          </div>

          <!-- 消息气泡 -->
          <div :class="[
            'max-w-[80%] sm:max-w-[70%] lg:max-w-[65%] tablet-message-bubble mobile-message-bubble',
            message.isUser ? 'text-right' : 'text-left'
          ]">
            <div :class="[
              'px-3 sm:px-4 py-2 sm:py-2.5 rounded-2xl message-bubble',
              message.isUser
                ? 'bg-gradient-to-br from-violet-500 to-purple-600 text-white rounded-tr-sm shadow-sm shadow-violet-500/15'
                : 'bg-white text-gray-700 rounded-tl-sm shadow-sm border border-gray-100/80 ai-message'
            ]">
              <p class="message-content text-sm sm:text-[15px] leading-relaxed">{{ message.content }}</p>
            </div>
            <p :class="[
              'text-[10px] sm:text-xs text-gray-300 mt-1 px-1',
              message.isUser ? 'text-right' : 'text-left'
            ]">
              {{ new Date(message.timestamp).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }) }}
            </p>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-if="isLoading && messages[messages.length - 1]?.content === ''" class="flex gap-2 sm:gap-3 fade-in">
          <div class="w-8 h-8 sm:w-9 sm:h-9 rounded-xl bg-gradient-to-br from-blue-400 via-cyan-500 to-teal-500 flex items-center justify-center flex-shrink-0 shadow-sm shadow-blue-500/15">
            <Bot class="w-4 h-4 text-white" />
          </div>
          <div class="bg-white px-4 py-3 rounded-2xl rounded-tl-sm shadow-sm border border-gray-100/80">
            <div class="flex items-center gap-1.5">
              <span class="typing-dot w-2 h-2 bg-blue-400 rounded-full inline-block"></span>
              <span class="typing-dot w-2 h-2 bg-blue-400 rounded-full inline-block"></span>
              <span class="typing-dot w-2 h-2 bg-blue-400 rounded-full inline-block"></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ====== 输入区域 ====== -->
    <footer class="sticky bottom-0 bg-white/90 backdrop-blur-md border-t border-gray-100/60">
      <div class="max-w-3xl mx-auto px-3 sm:px-5 lg:px-6 py-2.5 sm:py-3">
        <div class="flex items-end gap-2 sm:gap-3">
          <div class="flex-1 bg-gray-100/80 rounded-2xl px-3 sm:px-4 py-2 sm:py-2.5 focus-within:bg-white focus-within:ring-2 focus-within:ring-blue-400/20 focus-within:shadow-sm transition-all duration-200">
            <textarea
              v-model="inputMessage"
              @keydown="handleKeydown"
              placeholder="输入您的需求，我来调用工具帮您完成..."
              rows="1"
              class="chat-input w-full bg-transparent border-none outline-none resize-none text-sm sm:text-[15px] text-gray-700 placeholder-gray-400"
            ></textarea>
          </div>
          <button
            @click="handleSend"
            :disabled="!inputMessage.trim() || isLoading"
            :class="[
              'p-2.5 sm:p-3 rounded-xl transition-all duration-200 active:scale-95 flex-shrink-0',
              inputMessage.trim() && !isLoading
                ? 'bg-gradient-to-br from-blue-500 via-cyan-500 to-teal-500 text-white shadow-md shadow-blue-500/20 hover:shadow-lg'
                : 'bg-gray-200 text-gray-400 cursor-not-allowed'
            ]"
            aria-label="发送消息"
          >
            <Send class="w-5 h-5" />
          </button>
        </div>
        <p class="text-[10px] sm:text-xs text-gray-300 text-center mt-1.5 hidden sm:block">
          按 Enter 发送 · Shift + Enter 换行
        </p>
      </div>
    </footer>
  </div>
</template>
