package software.amazon.identitystore.common;

import software.amazon.awssdk.services.identitystore.model.AccessDeniedException;
import software.amazon.awssdk.services.identitystore.model.ConflictException;
import software.amazon.awssdk.services.identitystore.model.InternalServerException;
import software.amazon.awssdk.services.identitystore.model.ResourceNotFoundException;
import software.amazon.awssdk.services.identitystore.model.ServiceQuotaExceededException;
import software.amazon.awssdk.services.identitystore.model.ThrottlingException;
import software.amazon.awssdk.services.identitystore.model.ValidationException;
import software.amazon.cloudformation.exceptions.BaseHandlerException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.CfnResourceConflictException;
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException;
import software.amazon.cloudformation.exceptions.CfnServiceLimitExceededException;
import software.amazon.cloudformation.exceptions.CfnThrottlingException;

public class ExceptionTranslator {

    /**
     * Method that handles Service Exceptions and convert them to CloudFormation exceptions
     * @param resourceType resourceType
     * @param e Exception
     */
    public static void translateToCfnException(final Exception e, final String resourceType) {
        if (e instanceof ConflictException) {
            throw new CfnResourceConflictException(e);
        } else if (e instanceof ValidationException) {
            throw new CfnInvalidRequestException(e);
        } else if (e instanceof ServiceQuotaExceededException) {
            throw new CfnServiceLimitExceededException(resourceType, e.getMessage());
        } else if (e instanceof ResourceNotFoundException) {
            throw new CfnNotFoundException(e);
        } else if (e instanceof AccessDeniedException) {
            throw new CfnAccessDeniedException(resourceType, e);
        } else if (e instanceof ThrottlingException) {
            throw new CfnThrottlingException(resourceType, e);
        } else if (e instanceof InternalServerException) {
            throw new CfnServiceInternalErrorException(resourceType, e);
        }
        throw new CfnGeneralServiceException(e);
    }
}
