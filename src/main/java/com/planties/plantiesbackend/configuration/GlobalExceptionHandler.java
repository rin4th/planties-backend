package com.planties.plantiesbackend.configuration;


import com.planties.plantiesbackend.model.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(CustomException.NameOrTypeTakenException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomException.UsernameTakenException.class)
    public ResponseEntity<Object> handleUsernameTakenException(CustomException.UsernameTakenException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.EmailTakenException.class)
    public ResponseEntity<Object> handleEmailTakenException(CustomException.EmailTakenException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(CustomException.BadRequestException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.InvalidIdException.class)
    public ResponseEntity<Object> handleInvalidIdException(CustomException.InvalidIdException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.NameOrTypeTakenException.class)
    public ResponseEntity<Object> handleNameOrTypeTakenException(CustomException.NameOrTypeTakenException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(CustomException.AccessDeniedException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomException.WrongCredentialException.class)
    public ResponseEntity<Object> handleWrongCredentialException(CustomException.WrongCredentialException e){
        return ResponseHandler.generateResponse("failed", e.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }


}
