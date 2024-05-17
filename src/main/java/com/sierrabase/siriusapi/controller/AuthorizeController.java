package com.sierrabase.siriusapi.controller;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseCodeDTO;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.AuthorizeModel;
import com.sierrabase.siriusapi.model.LoginAttemptModel;
import com.sierrabase.siriusapi.model.params.UserModel;
import com.sierrabase.siriusapi.service.AuthorizeService;
import com.sierrabase.siriusapi.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/authorization")
public class AuthorizeController {
    static private final String apiTag = "/api/authorization";

    @Autowired
    private AuthorizeService authorizeService;
    @Autowired
    private LoginAttemptService loginAttemptService;

    static private final String uri_users = "/users";
    static private final String uri_verification = uri_users + "/verification";
    static private final String uri_user_id = uri_users + "/{user_id}";
    static private final String uri_drones = uri_user_id + "/drones";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    // GET - users
    @GetMapping(uri_users)
    public ResponseEntity<?> getUsers() {
        ArrayList<AuthorizeModel> authorizeModel = authorizeService.getAllEntities();
        log.info("model: " + authorizeModel);
        ResponseDTO<ArrayList<AuthorizeModel>> response = ResponseDTO.<ArrayList<AuthorizeModel>>builder()
                .uri(getUri(uri_users))
                .success(authorizeModel != null)
                .result(authorizeModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - verification
    @PostMapping(uri_verification)
    public ResponseEntity<?> postVerificationByUserId(
            @RequestBody AuthorizeModel model) {
//        LoginParams sParams = LoginParams.builder().build();
        boolean getEntityByUserIdResult = authorizeService.checkForDuplicateUserId(model.getUserId());
        if (!getEntityByUserIdResult) {
            loginAttemptService.createEntity(new LoginAttemptModel(model, false));
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false,HttpStatus.NOT_FOUND.value(),"Account dose not exist"));
        }

        AuthorizeModel authorizeModel = authorizeService.verify(model);
        if (authorizeModel == null) {
            loginAttemptService.createEntity(new LoginAttemptModel(model, false));
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false,HttpStatus.UNAUTHORIZED.value(),"Password is not valid"));
        }
        loginAttemptService.createEntity(new LoginAttemptModel(model, true));
//        log.info("model: " + authorizeModel);
        ResponseDTO<AuthorizeModel> response = ResponseDTO.<AuthorizeModel>builder()
                .uri(getUri(uri_verification))
                .success(authorizeModel != null)
                .result(authorizeModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new user
    @PostMapping(uri_users)
    public ResponseEntity<?> createUser(
            @RequestBody UserModel userModel) {
//        LoginParams sParams = LoginParams.builder().build();

        // check for duplicate user loginId
        Boolean duplicated = authorizeService.checkForDuplicateUserId(userModel.getUserId());
        if (duplicated) {
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
        }

        AuthorizeModel authorizeModel = authorizeService.createEntity(userModel);
        log.info("model: " + authorizeModel);
        ResponseDTO<AuthorizeModel> response = ResponseDTO.<AuthorizeModel>builder()
                .uri(getUri(uri_users))
                .success(authorizeModel != null)
                .result(authorizeModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    private int u_id;
    private boolean parsePathVariablesOfUser(String userId) {
        u_id = URIParser.parseStringToIntegerId(userId);
        if(u_id < 0)
            return false;
        return true;
    }

    // GET - user
    @GetMapping(uri_user_id)
    public ResponseEntity<?> getUsersById(
            @PathVariable String user_id) {

        if(!parsePathVariablesOfUser(user_id))
            return ResponseEntity.badRequest().build();

        AuthorizeModel authorizeModel = authorizeService.getEntityById(u_id);
        log.info("model: " + authorizeModel);
        ResponseDTO<AuthorizeModel> response = ResponseDTO.<AuthorizeModel>builder()
                .uri(getUri(uri_user_id))
                .success(authorizeModel != null)
                .result(authorizeModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - user
    @PutMapping(uri_user_id)
    public ResponseEntity<?> updateUser(
            @PathVariable String user_id,
            @RequestBody AuthorizeModel model) {

        if(!parsePathVariablesOfUser(user_id))
            return ResponseEntity.badRequest().build();

        AuthorizeModel check = authorizeService.getEntityById(u_id);
        if (check == null) {
            return ResponseEntity.badRequest().build();
        }

        AuthorizeModel authorizeModel = authorizeService.updateEntity(u_id, model);
        log.info("model: " + authorizeModel);
        ResponseDTO<AuthorizeModel> response = ResponseDTO.<AuthorizeModel>builder()
                .uri(getUri(uri_user_id))
                .success(authorizeModel != null)
                .result(authorizeModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // Delete - user
    @DeleteMapping(uri_user_id)
    public ResponseEntity<?> deleteUser(
            @PathVariable String user_id) {

        if(!parsePathVariablesOfUser(user_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = authorizeService.deleteEntity(u_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_user_id))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - drones of a user
    @PostMapping(uri_drones)
    public ResponseEntity<?> getDronesByUserId(
            @PathVariable(required = true)String user_id) {

        if(!parsePathVariablesOfUser(user_id))
            return ResponseEntity.badRequest().build();

        AuthorizeModel authorizeModel = authorizeService.getDronesByUserId(u_id);
        log.info("model: " + authorizeModel);
        ResponseDTO<AuthorizeModel> response = ResponseDTO.<AuthorizeModel>builder()
                .uri(getUri(uri_drones))
                .success(authorizeModel != null)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
