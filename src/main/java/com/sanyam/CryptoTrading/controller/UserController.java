package com.sanyam.CryptoTrading.controller;


import com.sanyam.CryptoTrading.request.ForgotPasswordTokenRequest;
import com.sanyam.CryptoTrading.domain.VerificationType;
import com.sanyam.CryptoTrading.model.ForgotPasswordToken;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.VerificationCode;
import com.sanyam.CryptoTrading.request.ResetPasswordRequest;
import com.sanyam.CryptoTrading.response.ApiResponse;
import com.sanyam.CryptoTrading.response.AuthResponse;
import com.sanyam.CryptoTrading.service.EmailService;
import com.sanyam.CryptoTrading.service.ForgotPasswordService;
import com.sanyam.CryptoTrading.service.UserService;
import com.sanyam.CryptoTrading.service.VerificationCodeService;
import com.sanyam.CryptoTrading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    private String jwt;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);


        return new ResponseEntity<User>(user , HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType
    ) throws Exception {
        User user = userService.findUserByJwt(jwt);

        VerificationCode verificationCode =
                verificationCodeService
                        .getVerificationCodeByUser(user.getId());

        if(verificationCode == null) {
            verificationCode = verificationCodeService
                    .sendVerificationCode(user,verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpMail
                    (user.getEmail(), verificationCode.getOtp());
        }


        return new ResponseEntity<>("Verification Otp sent successfully", HttpStatus.OK);
    }




    //Verifying Otp
    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp ,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);


        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo =  verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();
        boolean isVerified = verificationCode.getOtp().equals(otp);

        if(isVerified) {
            User udpatedUser = userService.enableTwoFactorAuth
                    (verificationCode.getVerificationType(),
                            sendTo,user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(udpatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong Otp ");
    }


    //SENDING OTP IF FORGOT PASSWORD
    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPasswordTokenRequest req
    ) throws Exception {


        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService
                .findByUser(user.getId());

        if(token == null)
        {
            token  = forgotPasswordService.createToken(user,id,otp,
                    req.getVerificationType(),req.getSendTo());

        }
        if(req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpMail(
                    user.getEmail(), token.getOtp());
        }
        AuthResponse response  = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password Reset Otp sent successfully");

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    //VERIFYING OTP IF FORGOT PASSWORD
    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id ,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {


        ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp);

        if(isVerified) {
            userService.updatePassword(
                    forgotPasswordToken.getUser(),
                    req.getPassword());
            ApiResponse res= new ApiResponse();
            res.setMsg("Password updated successfully");
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

        throw new Exception("Wrong Otp ");
    }



}
