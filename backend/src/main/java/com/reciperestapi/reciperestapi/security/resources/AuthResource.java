package com.reciperestapi.reciperestapi.security.resources;

import com.reciperestapi.reciperestapi.recipe.dto.response.LinkServiceBuilder;
import com.reciperestapi.reciperestapi.recipe.dto.response.ResponseDTO;
import com.reciperestapi.reciperestapi.security.dto.*;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.service.AuthenticationService;
import com.reciperestapi.reciperestapi.security.service.LogoutService;
import com.reciperestapi.reciperestapi.security.service.otp.OtpResendService;
import com.reciperestapi.reciperestapi.security.service.cookie.CookieServiceProvider;
import com.reciperestapi.reciperestapi.security.service.request.RequestServiceValidator;
import com.reciperestapi.reciperestapi.user.dto.UserDTO;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import com.reciperestapi.reciperestapi.common.settings.exceptions.AuthenticationException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.logging.Logger;


@Path("/auth")
@RequestScoped
public class AuthResource {

    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private LogoutService logoutService;
    @Inject
    private OtpResendService otpResendService;
    @Context
    private UriInfo uriInfo;
    @Inject
    private CookieServiceProvider cookieServiceProvider;
    @Inject
    private LinkServiceBuilder linkServiceBuilder;
    @Inject
    private RequestServiceValidator requestServiceValidator;
    private static final Logger LOGGER = Logger.getLogger(AuthResource.class.getName());


    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(AuthenticationRequest credentials) throws AuthenticationException, MessagingException {
        OtpToken otpToken = authenticationService.requestLogin(credentials);
        Map<String, String> links = linkServiceBuilder.generateLinks(uriInfo, "/auth/validate-otp/login", "http://localhost:3000/account/confirmation");
        return Response.ok().entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .date(ZonedDateTime.now())
                        .message("One Time Password (OTP) sent to "
                                + credentials.getEmail() +
                                ". Please enter it below to complete verification.")
                        .uuid(otpToken.getOtpUUId())
                        .rememberMe(credentials.isRememberMe())
                        .links(links)
                        .build())
                .build();
    }
    @POST
    @Path("/validate-otp/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response proceedOtpLogin(@RequestBody OtpValidationRequest otpValidationRequest) {
       ResponseTokenDetails responseTokenDetails = authenticationService.proceedOtpLogin(otpValidationRequest);
       LOGGER.info("responseTokenDetails --> " + responseTokenDetails);
       NewCookie newAccessTokenCookie = cookieServiceProvider.buildAccessTokenCookie(responseTokenDetails);
       NewCookie newRefreshTokenCookie = cookieServiceProvider.buildRefreshTokenCookie(responseTokenDetails);
       return Response.ok()
               .cookie(newAccessTokenCookie, newRefreshTokenCookie)
               .entity(responseTokenDetails)
               .build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register(AuthenticationRequest authenticationRequest) throws MessagingException {
        OtpToken otpToken = authenticationService.requestRegister(authenticationRequest);
        Map<String, String> links = linkServiceBuilder.generateLinks(uriInfo, "/auth/activate-account", "http://localhost:3000/account/confirmation");
        return Response.ok().entity(ResponseDTO
                        .builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .date(ZonedDateTime.now())
                        .message("Registration Successful." +
                                " One Time Password (OTP) sent to "
                                + authenticationRequest.getEmail() +
                                ". Please enter it below to complete verification.")
                        .uuid(otpToken.getOtpUUId())
                        .links(links)
                        .build())
                .build();
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response logout(@Context SecurityContext securityContext) {
        logoutService.logoutUser(securityContext);
        NewCookie accessCookie = cookieServiceProvider.clearAccessTokenCookie();
        NewCookie refreshCookie = cookieServiceProvider.clearRefreshTokenCookie();
        return Response.status(Response.Status.OK)
                .cookie(accessCookie, refreshCookie)
                .entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .message("Logged out successfully.")
                        .date(ZonedDateTime.now())
                        .build())
                .build();
    }

    @POST
    @Path("/refresh-token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public Response refreshToken(@Context SecurityContext securityContext) {
       ResponseTokenDetails responseTokenDetails = authenticationService.refreshToken(securityContext);
       NewCookie newAccessTokenCookie = cookieServiceProvider.buildAccessTokenCookie(responseTokenDetails);
       return Response.ok()
               .cookie(newAccessTokenCookie)
               .entity(responseTokenDetails)
               .build();
    }

    @POST
    @Path("/activate-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response confirm(OtpValidationRequest otpValidationRequest) {
        ResponseTokenDetails responseTokenDetails = authenticationService.activateAccount(otpValidationRequest);
        LOGGER.info("responseTokenDetails --> " + responseTokenDetails);
        NewCookie newAccessTokenCookie = cookieServiceProvider.buildAccessTokenCookie(responseTokenDetails);
        NewCookie newRefreshTokenCookie = cookieServiceProvider.buildRefreshTokenCookie(responseTokenDetails);
        return Response.ok()
                .cookie(newAccessTokenCookie, newRefreshTokenCookie)
                .entity(responseTokenDetails)
                .build();
    }

    @POST
    @Path("/request-password-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response requestPasswordReset(@Valid @RequestBody SendToEmailResetOtp toEmailResetOtp) throws MessagingException {
        OtpToken otpToken = authenticationService.requestPasswordReset(toEmailResetOtp.getEmail());
        Map<String, String> links = linkServiceBuilder.generateLinks(uriInfo, "/auth/validate-otp/reset-password", "http://localhost:3000/account/confirmation");
        return Response.ok().entity(ResponseDTO.builder()
                .status(Response.Status.OK.getReasonPhrase())
                        .date(ZonedDateTime.now())
                        .message("One Time Password (OTP) sent to "
                                + toEmailResetOtp.getEmail() +
                                ". Please enter it below to complete verification.")
                        .uuid(otpToken.getOtpUUId())
                        .links(links)
                        .build())
                .build();
    }

    @POST
    @Path("/validate-otp/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response proceedOtpResetPassword(@RequestBody OtpValidationRequest otpValidationRequest) {
        authenticationService.proceedOtpResetPassword(otpValidationRequest);
        Map<String, String> links = linkServiceBuilder.generateLinks(uriInfo, "/auth/reset-password", "http://localhost:3000/account/reset-password");
        return Response.accepted().entity(ResponseDTO.builder()
                        .status(Response.Status.ACCEPTED.getReasonPhrase())
                        .message("One Time Password (OTP) is valid.")
                        .uuid(otpValidationRequest.getUuid())
                        .links(links)
                        .date(ZonedDateTime.now())
                        .build())
                .build();
    }

    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.passwordReset(request);
        Map<String, String> links = linkServiceBuilder.generateLinks(uriInfo, "/auth/login", "");
        return Response.ok().entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .message("You have successfully change your password. Now you can login")
                        .links(links)
                        .build())
                .build();

    }

    @POST
    @Path("/resend")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response resendOtp(OtpValidationRequest otpValidationRequest) throws MessagingException {
        OtpToken otpToken = otpResendService.resendOtp(otpValidationRequest);
        return Response.ok().entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .uuid(otpToken.getOtpUUId())
                        .message("OTP resent successfully.")
                        .date(ZonedDateTime.now())
                        .build())
                .build();

    }

    @GET
    @Path("/user-info")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public Response getUserInfo(@Context SecurityContext securityContext) {
        User user = authenticationService.getAuthUserDetails(securityContext);
        UserDTO userDTO = UserDTO.builder().userUUId(user.getUserUUId()).username(user.getUsername()).roles(user.getRole()).build();
        return Response.ok().entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .date(ZonedDateTime.now())
                        .data(userDTO)
                        .build())
                .build();
    }

    @POST
    @Path("/validate-request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response isValidRequest(OtpValidationRequest otpValidationRequest){
        String message = requestServiceValidator.validateRequestId(otpValidationRequest);
        return Response.ok().entity(ResponseDTO.builder()
                        .status(Response.Status.OK.getReasonPhrase())
                        .message(message)
                        .date(ZonedDateTime.now())
                        .build())
                .build();

    }

}

