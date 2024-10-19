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
      return handleException(joinPoint.getSignature().getName(), e);
    }
  }

  private ResponseEntity<Response> handleException(String methodName, Exception e) {
    log.error("Exception in method: {} - {}", methodName, e.getMessage());

    // Map specific methods to their custom handlers
    return switch (methodName) {
      case "createTables" -> handleCreateTablesException(e);
      case "insertRecords" -> handleInsertRecordsException(e);
      case "createTableRelationship" -> handleTableRelationshipException(e);
      case "getAllFinancialRecords" -> handleNotFoundException(e);
      case "getForeignKeyRelationship" -> handleForeignKeyRelationshipException(e);
      default -> handleGeneralException(methodName, e);
    };
  }

  private ResponseEntity<Response> handleCreateTablesException(Exception e) {
    if (e instanceof BadSqlGrammarException) {
      return buildBadRequestResponse("Unable to create tables, tables already exist");
    }
    return buildInternalErrorResponse(e.getMessage());
  }

  private ResponseEntity<Response> handleInsertRecordsException(Exception e) {
    if (e instanceof RuntimeException) {
      return buildBadRequestResponse(e.getMessage());
    }
    return buildInternalErrorResponse(e.getMessage());
  }

  private ResponseEntity<Response> handleTableRelationshipException(Exception e) {
    if (e instanceof EmptyResultDataAccessException
        || e instanceof ParentTransactionNotFoundException) {
      return buildBadRequestResponse("No parent request id found for filtered search");
    }
    return buildInternalErrorResponse(e.getMessage());
  }

  private ResponseEntity<Response> handleNotFoundException(Exception e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            Response.builder()
                .status(Status.FAILED.getValue())
                .errorMessage(e.getMessage())
                .build());
  }

  private ResponseEntity<Response> handleForeignKeyRelationshipException(Exception e) {
    if (e instanceof ForeignKeyRelationshipNotFoundException) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
              Response.builder()
                  .status(Status.FAILED.getValue())
                  .errorMessage(e.getMessage())
                  .build());
    }
    return buildInternalErrorResponse(e.getMessage());
  }

  private ResponseEntity<Response> handleGeneralException(String methodName, Exception e) {
    return buildInternalErrorResponse(
        String.format("Unhandled exception in method: %s :: %s", methodName, e.getMessage()));
  }

  private ResponseEntity<Response> buildBadRequestResponse(String message) {
    return ResponseEntity.badRequest()
        .body(Response.builder().status(Status.FAILED.getValue()).errorMessage(message).build());
  }

  private ResponseEntity<Response> buildInternalErrorResponse(String message) {
    return ResponseEntity.internalServerError()
        .body(Response.builder().status(Status.FAILED.getValue()).errorMessage(message).build());
  }
}
