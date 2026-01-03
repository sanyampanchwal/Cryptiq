package com.sanyam.CryptoTrading.controller;


import com.sanyam.CryptoTrading.config.JwtProvider;
import com.sanyam.CryptoTrading.model.TwoFactorOtp;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.repository.UserRepository;
import com.sanyam.CryptoTrading.response.AuthResponse;
import com.sanyam.CryptoTrading.service.CustomUserDetailsService;
import com.sanyam.CryptoTrading.service.EmailService;
import com.sanyam.CryptoTrading.service.TwoFactorOtpService;
import com.sanyam.CryptoTrading.service.WatchlistService;
import com.sanyam.CryptoTrading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WatchlistService watchlistService;

    //REGISTRATION OF ACCT IF DOESNT EXIST
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        //checking if email alr existssss
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if(isEmailExist != null) {
            throw new Exception("Email already used");
        }

        //else create new user
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(newUser);

        watchlistService.createWatchlist(savedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        //JWT TOKEN CREATION :
        String jwt = JwtProvider.generateToken(auth);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Registration successfull");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    //Signing in to already existing acct fwaeyyy
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String username = user.getEmail();
        String password = user.getPassword();
        Authentication auth = authenticate(username , password);

        //JWT TOKEN CREATION :
        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(username);

        //if 2 factor on do if block
        if(authUser.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();

            TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());
            //if old otp -> delete it
            if(oldTwoFactorOtp != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            //creating new otp
            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser, otp , jwt);


            emailService.sendVerificationOtpMail(username, otp);


            res.setSession(newTwoFactorOtp.getId());
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED );
        }


        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login successfull");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        //check if user exist and if password is correct
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username ");
        }
        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }


    @PostMapping("two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOpt(
            @PathVariable String otp ,
            @RequestParam String id) throws Exception {
        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp ,otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOtp.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);


        }
        throw new Exception("Invalid Otp");
    }

}
