package kazurego7.junit5demo.controller;

public record GetUserResponse(
        String userId,
        String userName,
        String mailAddress,
        String userType) {
}


