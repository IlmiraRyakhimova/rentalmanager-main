<template>
  <div class="home-page">
    <div class="home-container">
      <div class="welcome-card">
        <h1 class="welcome-title">RENTAL MANAGER</h1>
        <p class="welcome-subtitle">Ваш персональный менеджер аренды</p>
        <p class="welcome-description">
          Управляйте недвижимостью, бронированиями и клиентами в одном месте
        </p>

        <div class="action-buttons">
          <router-link to="/register" class="btn btn-primary">
            Зарегистрироваться
          </router-link>
          <router-link to="/login" class="btn btn-secondary"> Войти как агент </router-link>
        </div>

        <div class="features">
          <div class="feature-item">
            <div class="feature-icon">🏢</div>
            <h3 class="feature-title">Управление апартаментами</h3>
            <p class="feature-text">Добавляйте и управляйте объектами недвижимости</p>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📅</div>
            <h3 class="feature-title">Календарь бронирований</h3>
            <p class="feature-text">Отслеживайте все брони и платежи</p>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📊</div>
            <h3 class="feature-title">Списки и отчеты</h3>
            <p class="feature-text">Получайте полную статистику и аналитику</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.home-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.home-container {
  width: 100%;
  max-width: 900px;
}

.welcome-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 60px 40px;
  text-align: center;
  animation: fadeIn 0.6s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.welcome-title {
  font-size: 48px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 16px 0;
  letter-spacing: 2px;
}

.welcome-subtitle {
  font-size: 24px;
  color: #667eea;
  margin: 0 0 12px 0;
  font-weight: 600;
}

.welcome-description {
  font-size: 16px;
  color: #718096;
  margin: 0 0 40px 0;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 60px;
  flex-wrap: wrap;
}

.btn {
  padding: 14px 32px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s;
  cursor: pointer;
  border: none;
  display: inline-block;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.5);
}

.btn-secondary {
  background: white;
  color: #667eea;
  border: 2px solid #667eea;
}

.btn-secondary:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 32px;
  margin-top: 40px;
}

.feature-item {
  padding: 24px;
  border-radius: 12px;
  background: #f7fafc;
  transition: all 0.3s;
}

.feature-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: #2d3748;
  margin: 0 0 8px 0;
}

.feature-text {
  font-size: 14px;
  color: #718096;
  margin: 0;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .welcome-card {
    padding: 40px 28px;
  }

  .welcome-title {
    font-size: 36px;
  }

  .welcome-subtitle {
    font-size: 20px;
  }

  .action-buttons {
    flex-direction: column;
    align-items: stretch;
  }

  .btn {
    width: 100%;
  }

  .features {
    grid-template-columns: 1fr;
  }
}
</style>
