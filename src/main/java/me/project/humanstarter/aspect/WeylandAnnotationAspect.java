package me.project.humanstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import me.project.humanstarter.annotations.KafkaAudit;
import me.project.humanstarter.annotations.WeylandWatchingYou;
import me.project.humanstarter.data.AuditMode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class WeylandAnnotationAspect {

    @Around("@annotation(weyland)")
    public Object around(ProceedingJoinPoint pjp, WeylandWatchingYou weyland) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        AuditMode mode = weyland.value();

        log.info("Weyland is watching! AuditMode: {}", mode);

        if (mode == AuditMode.KAFKA) {
            if (!method.isAnnotationPresent(KafkaAudit.class)) {
                String className = method.getDeclaringClass().getSimpleName();
                String methodName = method.getName();
                throw new IllegalStateException(
                        "Method " + className + "." + methodName + " uses KAFKA audit but is missing @KafkaAudit annotation!"
                );
            } else {
                KafkaAudit kafkaAudit = method.getAnnotation(KafkaAudit.class);
                log.info("Kafka topic: {}", kafkaAudit.topic());
            }
        }

        return pjp.proceed();
    }
}
