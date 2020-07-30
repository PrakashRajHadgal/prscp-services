package org.egov.prscp.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class ApplicationAspectLogs {

	@Autowired
	private ObjectMapper mapper;

	@Before("execution(* org.egov.prscp.*.*.*.*(..))")
	public void before(JoinPoint joinPoint) throws JsonProcessingException {
		log.info("Prscp-Services::Before::Method Name::{}, Executed Time::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis());
		log.debug("Prscp-Services::Before::Method Name::{}, Executed Time::{}, Request Parameters::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis(),
				joinPoint.getArgs().length > 0 ? mapper.writeValueAsString(joinPoint.getArgs()[0])
						: "No Request Parameters");
	}

	@After("execution(* org.egov.prscp.*.*.*.*(..))")
	public void after(JoinPoint joinPoint) throws JsonProcessingException {
		log.info("Prscp-Services::After::Method Name::{}, Executed Time::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis());
		log.debug("Prscp-Services::After::Method Name::{}, Executed Time::{}, Request Parameters::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis(),
				joinPoint.getArgs().length > 0 ? mapper.writeValueAsString(joinPoint.getArgs()[0])
						: "No Request Parameters");
	}

	@AfterReturning(value = "execution(* org.egov.prscp.web.controllers.*.*(..))", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) throws JsonProcessingException {
		log.info("Prscp-Services::AfterReturning::Method Name::{}, Executed Time::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis());
		log.debug(
				"Prscp-Services::AfterReturning::Method Name::{}, Executed Time::{}, Request Parameters::{}, Response::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis(),
				joinPoint.getArgs().length > 0 ? mapper.writeValueAsString(joinPoint.getArgs()[0])
						: "No Request Parameters",
				result);

	}

	@AfterThrowing(value = "execution(* org.egov.prscp.*.*.*.*(..))", throwing = "exception")
	public void afterThrowing(JoinPoint joinPoint, Throwable exception) throws JsonProcessingException {
		log.error(
				"Prscp-Services::AfterFailed::Method Name::{}, Executed Time::{}, Request Parameters::{}, Error Response::{}, Exception Details::{}",
				joinPoint.getSignature().getDeclaringTypeName(), System.currentTimeMillis(),
				joinPoint.getArgs().length > 0 ? mapper.writeValueAsString(joinPoint.getArgs()[0])
						: "No Request Parameters",
				exception.getMessage(), exception.getStackTrace());
	}

}
