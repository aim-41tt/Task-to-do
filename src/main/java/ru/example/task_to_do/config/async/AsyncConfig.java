package ru.example.task_to_do.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    @Primary
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Настройка параметров пула
        int corePoolSize = 16;  // Количество потоков, равное количеству логических ядер
        int maxPoolSize = 32;   // Максимальное количество потоков (можно увеличить при необходимости)
        int queueCapacity = 5000;  // Размер очереди задач
        int keepAliveSeconds = 60;  // Время ожидания неактивного потока до завершения

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.initialize();

        return executor;
    }
}
