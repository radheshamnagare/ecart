package com.radhesham.ecart.process;

import com.radhesham.ecart.repository.ProductsRepository;
import com.radhesham.ecart.repository.SystemParameterRepository;
import com.radhesham.ecart.repository.UserRepository;
import com.radhesham.ecart.service.ForgetPasswordService;
import com.radhesham.ecart.service.SendEmailService;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class ThreadRunner {

    private static final Logger logger = LoggerFactory.getLogger(ThreadRunner.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Future<?>> tasks = new ConcurrentHashMap<>();
    private final ForgetPasswordProcess forgetPasswordProcess;
    private final ProductNotifyProcess productNotifyProcess;
    private final SystemParameterRepository systemParameterRepository;
    private final ForgetPasswordService forgetPasswordService;
    private final SendEmailService sendEmailService;
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public ThreadRunner(ForgetPasswordProcess forgetPasswordProcess,
                        ProductNotifyProcess productNotifyProcess,
                        ForgetPasswordService forgetPasswordService,
                        SendEmailService sendEmailService,
                        UserRepository userRepository,
                        SystemParameterRepository systemParameterRepository,
                        ProductsRepository productsRepository) {
        this.forgetPasswordProcess = forgetPasswordProcess;
        this.productNotifyProcess=productNotifyProcess;
        this.systemParameterRepository = systemParameterRepository;
        this.forgetPasswordService = forgetPasswordService;
        this.sendEmailService = sendEmailService;
        this.userRepository = userRepository;
        this.productsRepository=productsRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startTasks() {
        try {
            logger.info("startTasks::Application ready. Submitting tasks to executor...");
          //  executeTask("forgetPasswordWorker",forgetPasswordProcess);
        } catch (Exception e) {
            logger.error("Exception in startTasks()", e);
        }
    }

    /**
     * Execute a task with a name
     */
    public void executeTask(String name, Runnable task) {
        if (tasks.containsKey(name) && !tasks.get(name).isDone()) {
            logger.info("executeTask()::Task {} is already running.", name);
            return;
        }
        Future<?> future = executorService.submit(task);
        tasks.put(name, future);
        logger.info("executeTask()::Started task: {}", name);
    }

    /**
     * Stop a running task
     */
    public void stopTask(String name) {
        Future<?> future = tasks.get(name);
        if (future != null && !future.isDone()) {
            future.cancel(true);
            logger.info("Stopping task: {}", name);
        } else {
            logger.info("executeTask()::Task {} is not running.", name);
        }
    }

    /**
     * Restart a task
     */
    public void restartTask(String name, Runnable task) {
        stopTask(name);
        executeTask(name, task);
    }

    /**
     * Stop all tasks gracefully
     */
    @PreDestroy
    public void shutdown() {
        logger.info("shutdown()::Shutting down all background tasks...");
        tasks.keySet().forEach(this::stopTask);
        executorService.shutdownNow();
    }
}
