package me.project.humanstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import me.project.humanstarter.annotations.KafkaAudit;
import me.project.humanstarter.annotations.WeylandWatchingYou;
import me.project.humanstarter.data.AuditMode;
import me.project.humanstarter.exceptions.KafkaPropertiesException;
import me.project.humanstarter.service.KafkaProducerService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class WeylandAnnotationAspect {

    @Autowired
    private KafkaProducerService service;

    @Around("@annotation(weyland)")
    public Object around(ProceedingJoinPoint pjp, WeylandWatchingYou weyland) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        AuditMode mode = weyland.value();

        log.info("AuditMode: {}", mode);

        if (mode == AuditMode.KAFKA) {
            if (!method.isAnnotationPresent(KafkaAudit.class)) {
                String className = method.getDeclaringClass().getSimpleName();
                String methodName = method.getName();
                throw new KafkaPropertiesException(
                        "Method " + className + "." + methodName + " uses KAFKA audit but is missing @KafkaAudit annotation"
                );
            } else {
                KafkaAudit kafkaAudit = method.getAnnotation(KafkaAudit.class);
                log.info("Kafka topic: {}", kafkaAudit.topic());

            }
        }

        else {
            if (!method.isAnnotationPresent(KafkaAudit.class)) {
                log.info("Method name: {}" +
                        "\nMethod arguments: {}", pjp.getSignature(), pjp.getArgs());
            }
            else throw new KafkaPropertiesException("cannot use @KafkaAudit annotation with AuditMode.CONSOLE");

        }

        return pjp.proceed();
    }

    @AfterReturning("@annotation(weyland)")
    public void afterReturning(JoinPoint jp, WeylandWatchingYou weyland) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();

        if (!method.isAnnotationPresent(KafkaAudit.class)) {
            log.info("Method: {} returned value of class {} ", method.getName(), method.getReturnType());
        }
        else {
            KafkaAudit kafkaAudit = method.getAnnotation(KafkaAudit.class);
            service.send(kafkaAudit.topic(), "Method name: "+ method.getName() +
                    " returned value of class: " + method.getReturnType() +
                    "\nMethod parameters: " + Arrays.toString(method.getParameterTypes()));

        }



    }

    @AfterThrowing(pointcut = "@annotation(weyland)", throwing = "e")
    public void afterThrowing(JoinPoint jp, WeylandWatchingYou weyland, Exception e) {

        Method method = ((MethodSignature) jp.getSignature()).getMethod();

        if (!method.isAnnotationPresent(KafkaAudit.class)) {
            log.info("Method: {} thrown exception: {}", method, e.getMessage());
        }
        else {
            KafkaAudit kafkaAudit = method.getAnnotation(KafkaAudit.class);
            service.send(kafkaAudit.topic(),"Method name: " + method.getName() +
                    "throwed exception" + e.getMessage() +
                    "\nMethod parameters: " + Arrays.toString(method.getParameterTypes()));


        }
    }
}
