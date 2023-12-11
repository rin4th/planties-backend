package com.planties.plantiesbackend.configuration;

public class CustomException {
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }
    public static class InvalidTokenException extends RuntimeException{
        public InvalidTokenException(String message){
            super(message);
        }
    }

    public static class UsernameTakenException extends RuntimeException{
        public UsernameTakenException(String message){
            super(message);
        }
    }

    public static class EmailTakenException extends RuntimeException{
        public EmailTakenException(String message){
            super(message);
        }
    }

    public static class AccessDeniedException extends RuntimeException{
        public AccessDeniedException(String message){
            super(message);
        }
    }

    public static class InvalidIdException extends RuntimeException{
        public InvalidIdException(String message){
            super(message);
        }
    }

    public static class NameOrTypeTakenException extends RuntimeException{
        public NameOrTypeTakenException(String message){
            super(message);
        }
    }

    public static class WrongCredentialException extends RuntimeException{
        public WrongCredentialException(String message){
            super(message);
        }
    }

    public static class UsernameNotFoundException extends RuntimeException{
        public UsernameNotFoundException(String message){
            super(message);
        }
    }

}
