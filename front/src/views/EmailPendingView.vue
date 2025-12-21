<template>
  <div class="email-pending-page">
    <div class="email-pending-container">
      <div class="email-pending-card">
        <div class="email-icon">📧</div>
        <h1 class="title">Подтвердите вашу почту</h1>
        <p class="message">
          Вам отправлено сообщение на почту для подтверждения,
          проверьте почту и подтвердите ее.
        </p>
        <p class="email-info" v-if="email">
          Письмо отправлено на: <strong>{{ email }}</strong>
        </p>
        <div class="actions">
          <router-link to="/login" class="btn-primary">
            Перейти ко входу
          </router-link>
          <router-link to="/" class="btn-secondary">
            На главную
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const email = computed(() => route.query.email || '')
</script>

<style scoped>
.email-pending-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0d47a1 0%, #1565c0 100%);
  padding: 20px;
}

.email-pending-container {
  width: 100%;
  max-width: 500px;
}

.email-pending-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 48px 40px;
  text-align: center;
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.email-icon {
  font-size: 64px;
  margin-bottom: 24px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 16px 0;
}

.message {
  font-size: 16px;
  color: #4a5568;
  line-height: 1.6;
  margin: 0 0 16px 0;
}

.email-info {
  font-size: 14px;
  color: #718096;
  margin: 0 0 32px 0;
}

.email-info strong {
  color: #1565c0;
}

.actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.btn-primary {
  display: block;
  padding: 14px 24px;
  background: linear-gradient(135deg, #0d47a1 0%, #1565c0 100%);
  color: white;
  text-decoration: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  transition: transform 0.2s, box-shadow 0.2s;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.btn-secondary {
  display: block;
  padding: 14px 24px;
  background: transparent;
  color: #1565c0;
  text-decoration: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  border: 2px solid #1565c0;
  transition: background 0.2s, color 0.2s;
}

.btn-secondary:hover {
  background: #1565c0;
  color: white;
}
</style>

