package com.kosmin.finance.finance_tracker.aspect;

import com.kosmin.finance.finance_tracker.exception.ForeignKeyRelationshipNotFoundException;
import com.kosmin.finance.finance_tracker.exception.ParentTransactionNotFoundException;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class FinanceTrackerServiceHandler {

  @Around("execution(* com.kosmin.finance.finance_tracker.service.FinanceTrackerService.*(..))")
  public Object handleServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception e) {
      return handleException(
          joinPoint.getSignature().getName(),
          e,
          Response.builder().status(Status.FAILED.getValue()).build());
    }
  }

  private ResponseEntity<Response> handleException(
      String methodName, Exception e, Response response) {
    switch (methodName) {
      case "createTables" -> {
        log.error(e.getMessage());
        if (e instanceof BadSqlGrammarException) {
          return ResponseEntity.badRequest()
              .body(
                  response.toBuilder()
                      .errorMessage("Unable to create tables, tables already exist")
                      .build());
        }
        return ResponseEntity.internalServerError()
            .body(response.toBuilder().errorMessage(e.getMessage()).build());
      }
      case "insertRecords" -> {
        log.error(e.getMessage());
        if (e instanceof RuntimeException) {
          return ResponseEntity.badRequest()
              .body(response.toBuilder().errorMessage(e.getMessage()).build());
        }
        return ResponseEntity.internalServerError()
            .body(response.toBuilder().errorMessage(e.getMessage()).build());
      }
      case "createTableRelationship" -> {
        log.error(e.getMessage());
        if (e instanceof EmptyResultDataAccessException
            || e instanceof ParentTransactionNotFoundException) {
          return ResponseEntity.badRequest()
              .body(
                  response.toBuilder()
                      .errorMessage("No parent request id found for filtered search")
                      .build());
        }
        return ResponseEntity.internalServerError()
            .body(response.toBuilder().errorMessage(e.getMessage()).build());
      }
      case "getAllFinancialRecords" -> {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(response.toBuilder().errorMessage(e.getMessage()).build());
      }
      case "getForeignKeyRelationship" -> {
        log.error(e.getMessage());
        if (e instanceof ForeignKeyRelationshipNotFoundException) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(response.toBuilder().errorMessage(e.getMessage()).build());
        }
        return ResponseEntity.internalServerError()
            .body(response.toBuilder().errorMessage(e.getMessage()).build());
      }
      default -> {
        log.error("Unhandled exception in method: {}", methodName);
        return ResponseEntity.internalServerError()
            .body(
                response.toBuilder()
                    .errorMessage(
                        String.format(
                            "Unhandled exception in method: %s :: %s", methodName, e.getMessage()))
                    .build());
      }
    }
  }
}
