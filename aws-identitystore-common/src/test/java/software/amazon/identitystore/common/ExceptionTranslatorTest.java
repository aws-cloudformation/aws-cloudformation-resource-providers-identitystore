package software.amazon.identitystore.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.identitystore.model.AccessDeniedException;
import software.amazon.awssdk.services.identitystore.model.ConflictException;
import software.amazon.awssdk.services.identitystore.model.IdentitystoreException;
import software.amazon.awssdk.services.identitystore.model.InternalServerException;
import software.amazon.awssdk.services.identitystore.model.ResourceNotFoundException;
import software.amazon.awssdk.services.identitystore.model.ServiceQuotaExceededException;
import software.amazon.awssdk.services.identitystore.model.ThrottlingException;
import software.amazon.awssdk.services.identitystore.model.ValidationException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.CfnResourceConflictException;
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException;
import software.amazon.cloudformation.exceptions.CfnServiceLimitExceededException;
import software.amazon.cloudformation.exceptions.CfnThrottlingException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTranslatorTest {

    @Test
    public void testHandleCommonExceptions_conflictException() {
        Exception e = ConflictException.builder().build();
        assertThrows(CfnResourceConflictException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_validationException() {
        Exception e = ValidationException.builder().build();
        assertThrows(CfnInvalidRequestException.class,  () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_ServiceQuotaExceededException() {
        Exception e = ServiceQuotaExceededException.builder().build();
        assertThrows(CfnServiceLimitExceededException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_ResourceNotFoundException() {
        Exception e = ResourceNotFoundException.builder().build();
        assertThrows(CfnNotFoundException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_AccessDeniedException() {
        Exception e = AccessDeniedException.builder().build();
        assertThrows(CfnAccessDeniedException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_ThrottlingException() {
        Exception e = ThrottlingException.builder().build();
        assertThrows(CfnThrottlingException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_InternalServerException() {
        Exception e = InternalServerException.builder().build();
        assertThrows(CfnServiceInternalErrorException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }

    @Test
    public void testHandleCommonExceptions_IdentityStoreException() {
        Exception e = IdentitystoreException.builder()
                .awsErrorDetails(AwsErrorDetails.builder()
                    .errorCode("IdentitystoreException")
                    .build())
                .statusCode(403)
                .build();
        assertThrows(CfnGeneralServiceException.class, () ->
                ExceptionTranslator.translateToCfnException(e, "TestResource"));
    }
}
