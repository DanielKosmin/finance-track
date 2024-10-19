package com.kosmin.finance.finance_tracker.aspect;

import com.kosmin.finance.finance_tracker.exception.ForeignKeyRelationshipNotFoundException;
import com.kosmin.finance.finance_tracker.exception.ParentTransactionNotFoundException;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.service.responseBuilder.ResponseBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class FinanceTrackerServiceHandler {

  private final ResponseBuilderService responseBuilderService;

  @Around("execution(* com.kosmin.finance.finance_tracker.service.FinanceTrackerService.*(..))")
  public Object handleServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception e) {
      return handleException(joinPoint.getSignature().getName(), e);
    }
  }

  private ResponseEntity<Response> handleException(String methodName, Exception e) {
    Response response = Response.builder().status(Status.FAILED.getValue()).build();
    log.error("Exception in method: {} - {}", methodName, e.getMessage());

    return switch (methodName) {
      case "createTables" -> handleCreateTablesException(e, response);
      case "insertRecords" -> handleInsertRecordsException(e, response);
      case "createTableRelationship" -> handleTableRelationshipException(e, response);
      case "getAllFinancialRecords" -> handleAllRecords(e, response);
      case "getForeignKeyRelationship" -> handleForeignKeyRelationshipException(e, response);
      default -> handleGeneralException(methodName, e, response);
    };
  }

  private ResponseEntity<Response> handleCreateTablesException(Exception e, Response response) {
    if (e instanceof BadSqlGrammarException) {
      return responseBuilderService.buildBadRequestResponse(
          "Unable to create tables, tables already exist", response);
    }
    return responseBuilderService.buildInternalErrorResponse(e.getMessage(), response);
  }

  private ResponseEntity<Response> handleInsertRecordsException(Exception e, Response response) {
    if (e instanceof RuntimeException) {
      return responseBuilderService.buildBadRequestResponse(e.getMessage(), response);
    }
    return responseBuilderService.buildInternalErrorResponse(e.getMessage(), response);
  }

  private ResponseEntity<Response> handleTableRelationshipException(
      Exception e, Response response) {
    if (e instanceof EmptyResultDataAccessException
        || e instanceof ParentTransactionNotFoundException) {
      return responseBuilderService.buildBadRequestResponse(
          "No parent request id found for filtered search", response);
    }
    return responseBuilderService.buildInternalErrorResponse(e.getMessage(), response);
  }

  private ResponseEntity<Response> handleAllRecords(Exception e, Response response) {
    return responseBuilderService.buildNotFoundResponse(e.getMessage(), response);
  }

  private ResponseEntity<Response> handleForeignKeyRelationshipException(
      Exception e, Response response) {
    if (e instanceof ForeignKeyRelationshipNotFoundException) {
      return responseBuilderService.buildNotFoundResponse(e.getMessage(), response);
    }
    return responseBuilderService.buildInternalErrorResponse(e.getMessage(), response);
  }

  private ResponseEntity<Response> handleGeneralException(
      String methodName, Exception e, Response response) {
    return responseBuilderService.buildInternalErrorResponse(
        String.format("Unhandled exception in method: %s :: %s", methodName, e.getMessage()),
        response);
  }
}
