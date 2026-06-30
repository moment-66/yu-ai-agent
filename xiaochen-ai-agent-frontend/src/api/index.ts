import axios from 'axios'

// 开发环境用 Vite proxy（相对路径），生产环境用完整后端地址
const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'

const api = axios.create({
  baseURL,
  timeout: 300000
})

export interface Message {
  id: string
  content: string
  isUser: boolean
  timestamp: number
}

export function generateChatId(): string {
  return Date.now().toString(36) + Math.random().toString(36).substring(2)
}

export async function sendLoveAppMessage(
  message: string,
  chatId: string,
  onToken: (token: string) => void,
  onError: (error: Error) => void,
  onComplete: () => void
): Promise<void> {
  const url = `${baseURL}/ai/love_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`
  
  try {
    const response = await fetch(url)
    if (!response.ok) {
      let errorMessage = `HTTP error! status: ${response.status}`
      try {
        const errorText = await response.text()
        if (errorText) {
          errorMessage += ` - ${errorText.substring(0, 200)}`
        }
      } catch (_) {
        // ignore
      }
      throw new Error(errorMessage)
    }
    
    const reader = response.body?.getReader()
    if (!reader) {
      throw new Error('No readable stream')
    }
    
    const decoder = new TextDecoder('utf-8')
    let buffer = ''
    
    while (true) {
      const { done, value } = await reader.read()
      
      if (done) {
        const remainingData = parseSseData(buffer)
        if (remainingData) {
          onToken(remainingData)
        }
        onComplete()
        break
      }
      
      buffer += decoder.decode(value, { stream: true })
      
      const result = parseSseTokens(buffer)
      if (result.tokens.length > 0) {
        result.tokens.forEach(token => onToken(token))
        buffer = buffer.substring(result.totalConsumed)
      }
    }
  } catch (error) {
    onError(error as Error)
  }
}

interface SseTokensResult {
  tokens: string[]
  totalConsumed: number
}

function parseSseTokens(buffer: string): SseTokensResult {
  const tokens: string[] = []
  let consumed = 0
  
  while (true) {
    const newlineIndex = buffer.indexOf('\n')
    if (newlineIndex === -1) break
    
    const line = buffer.substring(0, newlineIndex)
    buffer = buffer.substring(newlineIndex + 1)
    consumed += newlineIndex + 1
    
    if (line.startsWith('data:')) {
      const data = line.substring(5).trim()
      if (data) {
        tokens.push(data)
      }
    } else if (line === '') {
      continue
    }
  }
  
  return { tokens, totalConsumed: consumed }
}

function parseSseData(buffer: string): string {
  const lines = buffer.split('\n')
  const dataParts: string[] = []
  
  for (const line of lines) {
    if (line.startsWith('data:')) {
      const data = line.substring(5).trim()
      if (data) {
        dataParts.push(data)
      }
    }
  }
  
  return dataParts.join('')
}

export async function sendManusMessage(
  message: string,
  onToken: (token: string) => void,
  onError: (error: Error) => void,
  onComplete: () => void
): Promise<void> {
  const url = `${baseURL}/ai/manus/chat?message=${encodeURIComponent(message)}`
  
  try {
    const response = await fetch(url)
    if (!response.ok) {
      let errorMessage = `HTTP error! status: ${response.status}`
      try {
        const errorText = await response.text()
        if (errorText) {
          errorMessage += ` - ${errorText.substring(0, 200)}`
        }
      } catch (_) {
        // ignore
      }
      throw new Error(errorMessage)
    }
    
    const reader = response.body?.getReader()
    if (!reader) {
      throw new Error('No readable stream')
    }
    
    const decoder = new TextDecoder('utf-8')
    let buffer = ''
    
    while (true) {
      const { done, value } = await reader.read()
      
      if (done) {
        const remainingData = parseSseData(buffer)
        if (remainingData) {
          onToken(remainingData)
        }
        onComplete()
        break
      }
      
      buffer += decoder.decode(value, { stream: true })
      
      const result = parseSseTokens(buffer)
      if (result.tokens.length > 0) {
        result.tokens.forEach(token => onToken(token))
        buffer = buffer.substring(result.totalConsumed)
      }
    }
  } catch (error) {
    onError(error as Error)
  }
}

export default api
