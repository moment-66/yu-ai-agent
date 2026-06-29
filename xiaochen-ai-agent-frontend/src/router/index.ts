import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/pages/Home.vue')
  },
  {
    path: '/love-app',
    name: 'LoveApp',
    component: () => import('@/pages/LoveApp.vue')
  },
  {
    path: '/manus',
    name: 'Manus',
    component: () => import('@/pages/Manus.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
