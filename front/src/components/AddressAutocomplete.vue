<template>
  <div class="address-autocomplete">
    <label v-if="label" :for="id" class="autocomplete-label">
      {{ label }}
      <span v-if="required" class="required">*</span>
    </label>

    <div class="autocomplete-container">
      <input
        :id="id"
        v-model="searchQuery"
        type="text"
        class="autocomplete-input"
        :placeholder="placeholder"
        :required="required"
        @input="handleInput"
        @focus="showSuggestions = true"
        @blur="handleBlur"
      />

      <div v-if="loading" class="autocomplete-spinner"></div>

      <!-- Список подсказок -->
      <div v-if="showSuggestions && suggestions.length > 0" class="suggestions-dropdown">
        <div
          v-for="(suggestion, index) in suggestions"
          :key="suggestion.placeId || index"
          class="suggestion-item"
          @mousedown="selectSuggestion(suggestion)"
        >
          <div class="suggestion-icon">📍</div>
          <div class="suggestion-content">
            <div class="suggestion-name">{{ suggestion.displayName }}</div>
            <div class="suggestion-details">
              {{ formatDetails(suggestion) }}
            </div>
          </div>
        </div>
      </div>

      <!-- Пустое состояние -->
      <div v-if="showSuggestions && searchQuery && !loading && suggestions.length === 0" class="suggestions-dropdown">
        <div class="suggestion-empty">
          <div class="empty-icon">🔍</div>
          <p>Адрес не найден</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { searchAddress } from '@/api/geocoding'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: 'Начните вводить адрес...'
  },
  required: {
    type: Boolean,
    default: false
  },
  id: {
    type: String,
    default: 'address-input'
  }
})

const emit = defineEmits(['update:modelValue', 'select'])

const searchQuery = ref(props.modelValue)
const suggestions = ref([])
const showSuggestions = ref(false)
const loading = ref(false)
let debounceTimer = null

watch(() => props.modelValue, (newValue) => {
  searchQuery.value = newValue
})

const handleInput = () => {
  emit('update:modelValue', searchQuery.value)

  // Дебаунс для уменьшения количества запросов
  clearTimeout(debounceTimer)
  
  if (searchQuery.value.length < 3) {
    suggestions.value = []
    return
  }

  loading.value = true

  debounceTimer = setTimeout(async () => {
    try {
      const response = await searchAddress(searchQuery.value)
      suggestions.value = response.data || []
    } catch (error) {
      console.error('Error searching address:', error)
      suggestions.value = []
    } finally {
      loading.value = false
    }
  }, 500)
}

const selectSuggestion = (suggestion) => {
  searchQuery.value = suggestion.displayName
  emit('update:modelValue', suggestion.displayName)
  emit('select', suggestion)
  showSuggestions.value = false
  suggestions.value = []
}

const handleBlur = () => {
  // Небольшая задержка, чтобы клик по подсказке успел сработать
  setTimeout(() => {
    showSuggestions.value = false
  }, 200)
}

const formatDetails = (suggestion) => {
  const parts = []
  if (suggestion.city) parts.push(suggestion.city)
  if (suggestion.country) parts.push(suggestion.country)
  return parts.join(', ') || 'Дополнительная информация отсутствует'
}
</script>

<style scoped>
.address-autocomplete {
  width: 100%;
}

.autocomplete-label {
  display: block;
  font-size: 0.9rem;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 0.5rem;
}

.required {
  color: #e53e3e;
}

.autocomplete-container {
  position: relative;
}

.autocomplete-input {
  width: 100%;
  padding: 0.75rem;
  padding-right: 2.5rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.2s;
  box-sizing: border-box;
}

.autocomplete-input:focus {
  outline: none;
  border-color: #1565c0;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.autocomplete-spinner {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  width: 18px;
  height: 18px;
  border: 2px solid #e2e8f0;
  border-top-color: #1565c0;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.suggestions-dropdown {
  position: absolute;
  top: calc(100% + 0.25rem);
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  max-height: 300px;
  overflow-y: auto;
  z-index: 1000;
}

.suggestion-item {
  display: flex;
  align-items: start;
  gap: 0.75rem;
  padding: 0.75rem;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid #f7fafc;
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-item:hover {
  background: #f7fafc;
}

.suggestion-icon {
  font-size: 1.25rem;
  flex-shrink: 0;
  margin-top: 0.1rem;
}

.suggestion-content {
  flex: 1;
  min-width: 0;
}

.suggestion-name {
  font-size: 0.9rem;
  font-weight: 500;
  color: #1a202c;
  margin-bottom: 0.25rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.suggestion-details {
  font-size: 0.8rem;
  color: #718096;
}

.suggestion-empty {
  text-align: center;
  padding: 2rem 1rem;
  color: #718096;
}

.empty-icon {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  opacity: 0.5;
}

.suggestion-empty p {
  margin: 0;
  font-size: 0.9rem;
}

@keyframes spin {
  to {
    transform: translateY(-50%) rotate(360deg);
  }
}

/* Скроллбар */
.suggestions-dropdown::-webkit-scrollbar {
  width: 6px;
}

.suggestions-dropdown::-webkit-scrollbar-track {
  background: #f7fafc;
  border-radius: 8px;
}

.suggestions-dropdown::-webkit-scrollbar-thumb {
  background: #cbd5e0;
  border-radius: 8px;
}

.suggestions-dropdown::-webkit-scrollbar-thumb:hover {
  background: #a0aec0;
}
</style>
